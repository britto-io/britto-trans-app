# britto-trans-app

This project is the first pass at the transaction layer service for the britto suite of services.

## Build and Run Locally
This section describes how to get the app running locally.

### Build
`sbt clean compile`

### Run
`sbt run`

## Google Container Service
This section describes how to get the application running in the Google Container Service

### Prerequisites
* Google cloud account
* A google project created

### Publish a Local Docker Image
`sbt docker:publishLocal`

### Tag the image for the google container service
`docker tag [image id] gcr.io/[project id]/britto-trans-app:1.0-SNAPSHOT`

## Push the image to google container registry
`gcloud docker push gcr.io/[project id]/britto-trans-app:1.0-SNAPSHOT`

###  Set the compute zone
`gcloud config set compute/zone us-central1-b`

### Configure credentials
`gcloud container clusters get-credentials [cluster name]`

### Create a pod
`kubectl run britto-trans-app --image=gcr.io/[project id]/britto-trans-app:1.0-SNAPSHOT --port=8080 `
``````
kubectl get deployments 
kubectl get pods 
kubectl logs <POD-NAME> 
kubectl cluster-info 
kubectl get events 
kubectl config view 
```

### Setup load balancer
`kubectl expose deployment britto-trans-app --type="LoadBalancer"`

```
kubectl get services britto-trans-app
NAME               CLUSTER-IP     EXTERNAL-IP       PORT(S)    AGE
britto-trans-app   10.35.247.33   104.197.218.107   8080/TCP   8m
```

### Clean up the service and deployment
`kubectl delete service,deployment britto-trans-app`




