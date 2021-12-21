const PROTO_PATH = __dirname + '../../proto/greet.proto';

const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const packageDefinition = protoLoader.loadSync(
    PROTO_PATH,
    {keepCase: true,
     longs: String,
     enums: String,
     defaults: true,
     oneofs: true
    });
const greet_proto = grpc.loadPackageDefinition(packageDefinition).greet;

const callNext = (targetCall, requestParam) => {
  var client = new greet_proto.GreetService(`kubernetes-grpc-${targetCall}:50051`, grpc.credentials.createInsecure())
  return new Promise((resolve, reject) => client.greet({call_list: requestParam}, function(err, response) {
    if(err) {
      return reject(err)
    }
    resolve(response.answer_list)        
  }))
}

module.exports = {
  callNext
}
