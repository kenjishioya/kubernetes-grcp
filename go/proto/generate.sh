#!/bin/bash
# if protoc does not exist.
# apt-get install -y protobuf-compiler
protoc --go_out=./greetpd --go-grpc_out=./greetpd proto/greet.proto
