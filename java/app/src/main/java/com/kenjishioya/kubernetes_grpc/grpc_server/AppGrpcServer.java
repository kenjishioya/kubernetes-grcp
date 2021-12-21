package com.kenjishioya.kubernetes_grpc.grpc_server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class AppGrpcServer {
    public void serve() throws IOException, InterruptedException {
        System.out.println("server start...");
        Server server = ServerBuilder.forPort(50051).addService(new GreetServiceImpl()).build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            System.out.println("Recieved Shutdown Request.");
            server.shutdown();
            System.out.println("Successfully Shutdown");
        } ));
        server.awaitTermination();
    }
} 
