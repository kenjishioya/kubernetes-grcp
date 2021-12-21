"""The Python implementation of the GRPC helloworld.Greeter client."""

from __future__ import print_function

import grpc
import proto.greet_pb2 as pd
import proto.greet_pb2_grpc as pd_grpc

def call_next(target_call, request_param):
    try:
        with grpc.insecure_channel(f'kubernetes-grpc-{target_call}:50051') as channel:
            stub = pd_grpc.GreetServiceStub(channel)
            response = stub.Greet(pd.GreetRequest(call_list=request_param))
        return response.answer_list
    except Exception:
        return [f'Error occured when calling {target_call}']
