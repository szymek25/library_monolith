This is my old monolithic app which was developed for thesis. Now idea is to learn about new tools and migrate this app into microservices architecture


## Installation
Execute recreatecontainer.sh to generate local container. After that docker-compose up should setup local env. You can also use this script to recompile app after applying changes, then simply re-run docker-compose up.

## Deployment into local k8s cluster
For training purposes I have created local k8s cluster using minikube. To deploy app into local k8s cluster execute following steps:

1. Push docker image into docker hub: docker push szymek25/library-system:latest - where szymek25 is my docker hub username
2. Execute kubectl apply -f kube
3. In second terminal execute sudo minikube tunnel

App should be accessible in browser under http://localhost

Additionally, you could check kafka queues by accessing control center under http://localhost:9021/

## Migration steps
Here you can find steps which I have taken to migrate monolithic app into microservices architecture:
1. Introduced docker and kubernetes configs for easier development and deployment
2. Installed keycloak as new authorization server
3. Migrated authentication from monolithic app into keycloak
4. Started migrating other functionalities related with user management. After some time turned out that it requires introducing some additional logic in monolithic app, so I decided to create new microservice for user management 
5. Introduced api-gateway component
6. Introduced kafka for async communication
7. Introduced and implemented user-service for user management


## Other repositories in project
1. library-api-gateway - https://github.com/szymek25/library-api-gateway
2. user-service - https://github.com/szymek25/user-service

## Next steps
1. Introduce service-discovery
2. Introduce service for depertments and book
3. Introduce service for orders
4. Contract testing
5. Monitoring on ELK stack or some other
6. Deploy into cloud

## Brief overview of user-management
All user data is kept in keycloak server. Keycloak server is also authorization source in the system. However, user-service also keeps user data for faster access. Once library monolith app query for user data, then user-service serves data from its own DB. If some updates are done directly in keycloak system, then keycloak generates events into queue and user-service updates data. user-service is intermediate layer between keycloak and library app. So, for each user specific operations user-service exposes endpoints or listen for events in kafka. Once user-service receives event/request then sends http request into keycloak. Once operation completed by keycloak side, then keycloak emits proper event and user-service updates its own DB state. 