package grpc_client

import (
	"context"
	// "flag"
	"log"
	"time"
	"fmt"
	"strings"

	pd "github.com/kenjishioya/app/greetpd"
	"google.golang.org/grpc"
	"google.golang.org/grpc/keepalive"
)

var kacp = keepalive.ClientParameters{
	Time:                10 * time.Second,
	Timeout:             5 * time.Second,
	PermitWithoutStream: true,
}

var call_options = []string{
	"java",
	"node",
	"python",
}

func contains(s []string, str string) bool {
	for _, v := range s {
		if v == str {
			return true
		}
	}
	return false
}

func validateTargetCall(targetCall string, requestParam []string, answerList []string) (string, []string) {
	if contains(call_options, targetCall) {
		return targetCal, answerList
	} else {
		answerList = append(answerList, fmt.Sprintf("There is no %v in the list. Choose from %v!", targetCall, strings.Join(call_options, ", ")))
		if len(requestParam) > 0 {
			_targetCall, _requestParam := requestParam[0], requestParam[1:]
			return validateTargetCall(_targetCall, answerList)
		}
	}
	return "", answerList
}

func GreetAll(callList []string) []string {
	// Set up a connection to the server.
	answerList := []string{
		"Hallo from go",
	}

	if len(callList) > 0 {
		targetCall, requestParam := callList[0], callList[1:]
		targetCall, answerList = validateTargetCall(targetCall, requestParam, answerList)
		
		if len(targetCall) > 0 {
			conn, err := grpc.Dial(
				"kubernetes-grpc-"+targetCall+":50051",
				grpc.WithInsecure(),
				grpc.WithKeepaliveParams(kacp),
			)
			if err != nil {
				log.Print("did not connect: %v", err)
				return append(answerList, fmt.Sprintf("Connection failed to %v", targetCall))
			}
			defer conn.Close()
			c := pd.NewGreetServiceClient(conn)
			// Contact the server and print out its response.
			ctx, cancel := context.WithTimeout(context.Background(), time.Second)
			defer cancel()
			req := &pd.GreetRequest{
				CallList: requestParam,
			}
			res, err := c.Greet(ctx, req)
			if err != nil {
				log.Print("could not greet: %v", err)
				return append(answerList, fmt.Sprintf("Error occured when calling %v", targetCall))
			}
			answerList = append(answerList, res.GetAnswerList()...)
		}
	}
	return answerList
}
