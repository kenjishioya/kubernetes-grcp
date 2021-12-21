from concurrent import futures
import logging

import grpc
import proto.greet_pb2 as pd
import proto.greet_pb2_grpc as pd_grpc

from grpc_client.client import call_next

logger = logging.getLogger(__name__)

class GreetService(pd_grpc.GreetServiceServicer):

    call_options = [
        "java",
        "node"
    ]

    def validateTargetCall(self, target_call, request_param, answer_list):
        if target_call in self.call_options:
            return target_call
        else:
            answer_list.append(f'There is no {target_call} in the list. Choose from {", ".join(self.call_options)}!')
            if len(request_param) > 0:
                first_call = request_param.pop(0)
                return self.validateTargetCall(first_call, request_param, answer_list)

    def Greet(self, request, context):
        call_list = request.call_list
        answer_list = ['Hello from Python']

        if len(call_list) > 0:
            target_call, request_param = call_list[0], call_list[1:]
            target_call = self.validateTargetCall(target_call, request_param, answer_list)

        if target_call is not None:
            _answer_list = call_next(target_call, request_param)
            answer_list.extend(_answer_list)

        return pd.GreetResponse(answer_list=answer_list)

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    pd_grpc.add_GreetServiceServicer_to_server(GreetService(), server)
    server.add_insecure_port('[::]:50051')
    logger.info("server starting...")
    server.start()
    server.wait_for_termination()