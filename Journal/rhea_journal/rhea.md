# Rhea's Journal

## Week 1

### November 8

Today, I pushed starter code for our project, copied from Labs 6 and 7, to make setting up the object models and Cybersource Payment Gateway simpler later on. I also created some bare-bone templates for our front end pages, particularly the home, registration, and login pages. Right now they do not have any functionality, but they are ready to be expanded on.

My task:

![image](images/rhea-nov-8.PNG)

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/ce91cf02daecc39a9424f7f29f1c6e757c287939)

### November 9

I added another template for the catalog front-end page, and created starter files for handling the Book object.

My task:

![image](images/rhea-nov-8.PNG)

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/0f5394a36cc3597018581f0fe3f0ee9d2772ab38)

## Week 2

### November 16

I added two more templates, for the password reset page and the refund page. 

My next task will be adding the back end support for resetting passwords.

My changes:

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-16-a.JPG)
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-16-b.JPG)

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/67c1d610a932215575b247d63144d5debc5e3997)

My task:
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-16-board.JPG)

### November 20

I have created my own branch to do some test implementation. Currently my team and I are trying to decide whether to split our source code into 3 folders or 1, and I am testing whether implementing in one folder is feasible. 

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/1d2ef67a0420178ea9e42120a8b78bf7dd775231)

(later)

I have also implemented the registration & login backend. Now users can create an account and login. 

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/357db383d9f201f86ee577356b77c53c0ed0baf8)

My task:
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-20-board.JPG)

## Week 3

### November 24

In my own branch, I separated the html templates into their own frontend folder, and edited the docker-compose.yaml files so that I can deploy the frontend and my own spring-users microservice on separate ports. I also added REST API calls for the frontend to communicate with the backend. I now have the frontend and the login/registration backend running on separate ports, and able to communicate. 

[My task](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-24-tasks.JPG)

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/e3f84627b80654e5d5cba07b20793e60cb6bdcd8)

[My commit #2](https://github.com/nguyensjsu/fa21-172-dmg/commit/71fbbfa7734a7dc6c8171b1bda5977f05dfade92)

[My commit #3](https://github.com/nguyensjsu/fa21-172-dmg/commit/d32dac2ba9e19ce4c3970fbb617dac9371d9abf2)

Docker deployment below
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-24-b.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-24-c.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-24-d.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-24-a.JPG)

### November 25

I implemented the login function to connect to the backend, and the app can now detect when a user correctly logs in, when the user enters an incorrect password, and whent he user enters an incorrect email (i.e. an email address not in the database).

[My commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/31aca83dd43508ca850e7b404b8ee7dd3daf2c64)

Login functionality below

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-25-a.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-25-b.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/nov-25-c.JPG)

### November 26

For the login, registration, and password reset functions, I added response pages so that when the user enters an incorrect email or an incorrect password, the website does not simply display an HTTP 404 or 500 error. 

[Login commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/42b2561e811a8e4a50999e9f36cfd57c81dc9b4e)

[Registration commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/0d0589ccf0a9c572b6b9113e8ca0506a593a71eb)

[Pswd reset commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/578ac1db2e25a3627a22b4a1cdcb7455df67b457)

[ADD PICTURES LATER]

### November 27

I deployed the frontend, database, and users (login/registration/password management) services to Kubernetes.

<b>Images</b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/gke-a.JPG)
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/gke-b.JPG)

<b>Task board</b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/gke-board.JPG)

<b>Commits</b>

[Commit 1](https://github.com/nguyensjsu/fa21-172-dmg/commit/26380c4285179fb46c62042bf83a145a88260508)

[Commit 2](https://github.com/nguyensjsu/fa21-172-dmg/commit/b6e7c98eaec40c6aa8832ab39d8b009789f83dab)

## Week 4

### November 28

I edited the docker-compose.yaml file and each service's application.properties files so that each service uses its own database. Previously, all services were creating tables in the same database. This ensures the separation of the microservices. 

<b>Images </b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/separate-a.JPG)

<b>Task board</b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/separate-board.JPG)

<b>Commits</b>

[Separate db commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/dded9efaedbe887932c3d78bf93eec140b3b5623)

I connected the spring-users service to Kong API Gateway in GKE. 

<b>Commits</b>

[Kong commit 1](https://github.com/nguyensjsu/fa21-172-dmg/commit/0e216e914e7bdb6575c53e50ca53c8aa2e652b23)

[Kong commit 2](https://github.com/nguyensjsu/fa21-172-dmg/commit/28aa4cdc1816a7fd59b816df65418e62e7cf312e)

<b>Task board </b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/kong-board.JPG)

<b>Images</b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/kong-a.JPG)
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/kong-b.JPG)
![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/kong-c.JPG)

### December 1

I created a new service for the back office, which will handle employee-only functions such as password reset and refunds. I moved the password reset function from the frontend to the backoffice, and removed it from the frontend. I also updated each service's properties so that each one uses a different database and a different username+password to access the database, as well as the mysql Kubernetes deployment file.

<b>Images </b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/dec-1-a.JPG)

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/dec-1-b.JPG)

<b>Task board</b>

![image](https://github.com/nguyensjsu/fa21-172-dmg/blob/main/Journal/rhea_journal/images/dec-1-board.JPG)

<b>Commits</b>

[Commit 1](https://github.com/nguyensjsu/fa21-172-dmg/commit/530b959b6ba2b1166299e437348932cae19fe801)

[Commit 2](https://github.com/nguyensjsu/fa21-172-dmg/commit/6f33ace8bf0967d01f0817430aeedc229d97569f)

[Commit 3](https://github.com/nguyensjsu/fa21-172-dmg/commit/515613c1042b450a108aa642ef575a6cf1a99969)
