# Team Project (Book Store )

### Team Journal
[Team Journal](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/team_journal/README.md)

### Individual Member Journals 
[Rhea's journal](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/rhea.md)

[Ha's journal](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/ha_journal/ha.md)

[Patrick's journal](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/patrick_journal/patrick.md)

### Project Demo

### Book Store App


### Project Notes

#### What is KONG API Gateway?

* KONG API Gateway is a simple way to configure public-facing endpoints to ensure efficient for microservices architecture
* Reference: https://konghq.com/learning-center/api-gateway/api-gateway-uses/
* A microservice-based system can consist of several of individual services.
* Direct client-to-microservices communication means exposing the APIs for each microservice.
* API gateway is an alternative to direct client-to-microservice communication.
* A **gateway acts as an abstraction layer** for your microservices and provides a **single point of entry** for consumers of your application.
* API gateway:
  * is the abstraction of the backend microservices.
  * acts as a proxy for application's microservices
  * exposing the public-facing API endpoints
  * routing incoming client requests to the relevant services
  * is the single point of entry to your system
    * restrict access to your microservices from the outside world
    * reducing the potential attack
  * has the ability to scale services independently according to load
  * provides load balancing to ensure even or weighted distribution of incoming requests across the available instances of service.

* If the system serves multiple types of clients, it may be appropriate to provide multiple API gateways based on those types

#### RabbitMQ
* Reference:https://springframework.guru/spring-boot-messaging-with-rabbitmq/
* RabbitMQ is a common messaging broker which allows applications to connect and communicate
* RabbitMQ is a common services in microservices-based systems to communicate asynchronously through messaging.
  * to send and receive message for inter-service communication.
* A message sender sends a message to the message broker.
* The broker stores the message until a message receiver application connects and consume the message.
* This image shows how messages are communicated in RabbitMQ

![Project DMG](images/RabbitMQ.png)
<p>&nbsp;</p>