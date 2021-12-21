package rest_server

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/kenjishioya/app/grpc_client"
)

type JsonRequest struct {
	Call_list []string `json:call_list`
}

func Serve() {
	r := gin.Default()

	r.GET("/ping", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "pong",
		})
	})

	r.POST("/greet", func(c *gin.Context) {
		var callList JsonRequest
		if err := c.ShouldBindJSON(&callList); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		answerList := grpc_client.GreetAll(callList.Call_list)
		c.JSON(200, gin.H{
			"answer_list": answerList,
		})
	})
	r.Run()
}
