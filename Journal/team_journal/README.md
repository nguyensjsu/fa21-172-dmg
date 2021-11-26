# Team Project (Book Store)

## Week 1( 11/07.2021)  
* Team meeting: 2:00 pm - 2:30pm
### Topic Choosing
* Options:
  1. Starbuck
  2. Book Store
* Chosen topic: Book Store
* Tasks to do this week
  1.  Do more research on book store
  2.  Work on the frontend
  3.  Create team Task Board

* Project Board

![Project Board](images/project_board_week1_1.png)
<p>&nbsp;</p>

![Project Board](images/project_board_week1_2.png)
<p>&nbsp;</p>

## Week 2 : 11/14/2021
* Team meeting: 2:00 pm - 2:30pm
#### Team Discussion:
  * Using Kong gateway for API calls
  * Using RabbitMQ 
  * Create separate databases for user and catalog
  * RabbitMQ can be used between placing an order, calculating price, and processing a payment.
  * Updated project Board

#### Tasks division
* Team members pick up tasks

![Project Board](images/project_board_week2.png)
<p>&nbsp;</p>


## Week 3 : 11/22/2021

* Team members pick up tasks

![Project Board](images/project_board_week3.png)
<p>&nbsp;</p>

* Discussion on how to make the application as microservices
* Separating the project to three models:
  * Spring-user
  * Spring-books
  * Spring-payments
  
* Challenges
  * Cannot navigate to the views from different modules
### November 24

* On rhea branch, frontend (html pages & controllers) separated from backend code. Rhea's service (login/registration) can communicate with frontend via REST API calls. Frontend and spring-users services can be deployed to Docker on separate ports and communicate. 
* Main branch still has frontend and backend code together for now.
