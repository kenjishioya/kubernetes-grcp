package com.kenjishioya.kubernetes_grpc.grpc_server;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.ProtocolStringList;
import com.kenjishioya.kubernetes_grpc.grpc_client.AppGrpcClient;
import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc.GreetServiceImplBase;

import io.grpc.stub.StreamObserver;

public class GreetServiceImpl extends GreetServiceImplBase {

    private List<String> callOptions = new ArrayList<String>(){
        {
            add("node");
            add("python");
        }
    };

    private String validateTargetCall(String targetCall, List<String> requestParam, List<String> answerList) {
        if (this.callOptions.contains(targetCall)) {
            return targetCall;
        }
        else {
            answerList.add(String.format("There is no %s in the list. Choose from %s!", targetCall,  String.join(", ", callOptions)));
            if (!requestParam.isEmpty()) {
                String firstCall = requestParam.remove(0);
                return validateTargetCall(firstCall, requestParam, answerList);
            }
        }
        return "";
    }

    @Override
    public void greet(GreetRequest request, StreamObserver<GreetResponse> responseObserver) {
        // super.greet(request, responseObserver);
        
        List<String> answerList = new ArrayList<String>(){
            {
                add("Hello from Java");
            }
        };
        
        ProtocolStringList callList = request.getCallListList();

        if (!callList.isEmpty()) {
            String targetCall = callList.get(0);
            List<String> requestParam = new ArrayList<String>(callList.subList(1, callList.size()));
            targetCall = validateTargetCall(targetCall, requestParam, answerList);

            if (!targetCall.isEmpty()) {
                System.out.println("Do something.");
                AppGrpcClient client = new AppGrpcClient();
                List<String> list = client.call(targetCall, requestParam);
                answerList.addAll(list);
            }
        }
        GreetResponse response = GreetResponse.newBuilder().addAllAnswerList(answerList).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
