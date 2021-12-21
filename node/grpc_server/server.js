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

const { callNext } = require('../grpc_client/client')

const callOptions = [
  "java",
	"python"
]

const validateTargetCall = (targetCall, requestParam, answerList) => {
	if (callOptions.includes(targetCall)) {
		return targetCall
	} else {
		answerList.push(`There is no ${targetCall} in the list. Choose from ${callOptions.join(', ')}!`)
    if (requestParam.length > 0) {
      const firstCall = requestParam.shift()
			return validateTargetCall(firstCall, requestParam, answerList)
		}
	}
	return ""
}

const greet = async (call, callback) => {
  const callList = call.request.call_list;
  let answerList = ['Hello from Nodejs']

  if (callList.length > 0) {
    let targetCall = callList.shift()
    targetCall = validateTargetCall(targetCall, callList, answerList)

    if (targetCall.length > 0) {
      let list
      try {
        list = await callNext(targetCall, callList)
        answerList = answerList.concat(list)
      } catch (error) {
        answerList.push(`Error occured when calling ${targetCall}`)
      }
    }
  }
  
  callback(null, {answer_list: answerList});
}

const serve = () => {
  const server = new grpc.Server();
  server.addService(greet_proto.GreetService.service, {greet: greet});
  server.bindAsync('0.0.0.0:50051', grpc.ServerCredentials.createInsecure(), () => {
    console.log('start server')
    server.start();
  });
}

module.exports = {
  serve
}