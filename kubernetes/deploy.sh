#!bin/bash/

gcloud container clusters create kubernetes-grpc --zone asia-east1-a

gcloud container clusters delete kubernetes-grpc --zone asia-east1-a