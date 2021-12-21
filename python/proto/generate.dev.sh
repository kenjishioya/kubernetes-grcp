#!bin/bash/
python -m grpc_tools.protoc -I/src/python/proto --python_out=/src/python/proto --grpc_python_out=/src/python/proto /src/python/proto/greet.proto
