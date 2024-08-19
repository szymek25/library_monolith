This is my old monolithic app which was developed for thesis. Now idea is to learn about new tools and migrate this app into microservices architecture


## Installation
Execute recreatecontainer.sh to generate local container. After that docker-compose up should setup local env. You can also use this script to recompile app after applying changes, then simply re-run docker-compose up.

## Deployment into local k8s cluster
For training purposes I have created local k8s cluster using minikube. To deploy app into local k8s cluster execute following steps:

1. Push docker image into docker hub: docker push szymek25/library-system:latest - where szymek25 is my docker hub username
2. Execute kubectl apply -f kube
3. In second terminal execute sudo minikube tunnel

App should be accessible in browser under http://localhost

## Migration steps
Here you can find steps which I have taken to migrate monolithic app into microservices architecture:
1. Introduced docker and kubernetes configs for easier development and deployment
2. Installed keycloak as new authorization server
3. Migrated authentication from monolithic app into keycloak
4. Started migrating other functionalities related with user management. After some time turned out that it requires introducing some additional logic in monolithic app, so I decided to create new microservice for user management 
5. Introduced api-gateway component


## Other repositories in project
1. library-api-gateway - https://github.com/szymek25/library-api-gateway
2. 