package com.kenjishioya.kubernetes_grpc.grpc_client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.proto.greet.GreetRequest;
import com.proto.greet.GreetResponse;
import com.proto.greet.GreetServiceGrpc;

public class AppGrpcClient {
    public List<String> call(String targetCall, List<String> requestParam) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget(String.format("kubernetes-grpc-%s:50051", targetCall)).usePlaintext().build();
        try {

            GreetServiceGrpc.GreetServiceBlockingStub syncclient = GreetServiceGrpc.newBlockingStub(managedChannel);
    
            GreetRequest request = GreetRequest.newBuilder().addAllCallList(requestParam).build();
    
            GreetResponse response = syncclient.greet(request);

            return response.getAnswerListList();
        } catch(Exception e) {
            System.out.println("test");
            return Arrays.asList(String.format("Error occured when calling %s", targetCall));
        } finally {
            managedChannel.shutdown();
        }
    }
}
