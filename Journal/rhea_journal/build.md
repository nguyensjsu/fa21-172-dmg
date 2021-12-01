# Build instructions

## Create Docker images
1. Run gradle build in frontend, spring-users, spring-books, and spring-payments.
2. Run make-docker push in all four services.

## GKE Deployment

4. Upload all files in folder "k8s modified" to Google Cloud.

### Deploy mysql
6. Run kubectl apply -f mysql-deployment.yaml in Kubernetes.
7. Run kubectl apply -f mysql-service.yaml in Kubernetes.

### Deploy frontend
9. Run kubectl apply -f frontend-deployment.yaml in Kubernetes.
10. Run kubectl apply -f frontend-service.yaml in Kubernetes.

### Deploy users service
12. Run kubectl apply -f users-deployment.yaml in Kubernetes.
13. Run kubectl apply -f users-service.yaml in Kubernetes.

### Deploy payments service
15. <Add deploying books, payments later>

### Deploy books service

### Attach Kong to 
17. Upload all files in spring-users/kong-kubernetes
18. Follow Lab 8 instructions. 
