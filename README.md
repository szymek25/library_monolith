This is my old monolithic app which was developed for thesis. Now idea is to learn about new tools and migrate this app into microservices architecture


## Installation
Execute recreatecontainer.sh to generate local container. After that docker-compose up should setup local env. You can also use this script to recompile app after applying changes, then simply re-run docker-compose up.

## Deployment into local k8s cluster
For training purposes I have created local k8s cluster using minikube. To deploy app into local k8s cluster execute following steps:

1. Push docker image into docker hub: docker push szymek25/library-system:latest - where szymek25 is my docker hub username
2. Execute kubectl apply -f kube
3. In second terminal execute sudo minikube tunnel

App should be accessible in browser under http://localhost
