all: clean

clean:
	gradle clean

compile:
	gradle build

test:
	gradle test

jar: 
	gradle build -x test
	gradle bootJar

run: jar
	echo Starting Spring at:  http://localhost:8084
	java -jar build/libs/backoffice-1.0.jar

# Docker

docker-build: jar
	docker build -t backoffice .
	docker images

docker-run: docker-build
	docker run --name backoffice -td -p 80:8080 backoffice	
	docker ps

docker-clean:
	docker stop backoffice
	docker rm backoffice
	docker rmi backoffice

docker-shell:
	docker exec -it backoffice bash 

docker-push:
	gradle build
	docker login
	docker build -t rheadash/backoffice:v1 .
	docker push rheadash/backoffice:v1 

# Compose

network-ls:
	docker network ls 

network-create:
	docker network create --driver bridge backoffice-v1_network

network-prune:
	docker network prune

compose-up:
	docker-compose up --scale books=2 -d

compose-down:
	docker-compose down 	

lb-stats:
	echo "user = admin | password = admin"
	open http://localhost:1936

lb-test:
	open http://localhost


# Pod

pod-run:
	kubectl apply -f pod.yaml

pod-list:
	kubectl get pods

pod-desc:
	kubectl describe pods backoffice

pod-delete:
	kubectl delete -f pod.yaml

pod-shell:
	kubectl exec -it backoffice -- /bin/bash

pod-logs:
	kubectl logs -f backoffice

# Deployment

deployment-create:
	kubectl create -f deployment.yaml --save-config 

deployment-get:
	kubectl get deployments

deployment-get-pods:
	kubectl get pods -l name=backoffice

deployment-pod-shell:
	kubectl exec -it $(pod) -- /bin/bash

deployment-upgrade:
	kubectl apply  -f deployment.yaml

deployment-delete:
	kubectl delete deployment backoffice-deployment

# Service

service-create:
	kubectl create -f service.yaml

service-get:
	kubectl get services

service-get-ip:
	kubectl get service backoffice -o wide

service-delete:
	kubectl delete service backoffice

# Ingress

ingress-apply:
	kubectl apply -f ingress.yaml

ingress-ip:
	kubectl get ingress backoffice-ingress
