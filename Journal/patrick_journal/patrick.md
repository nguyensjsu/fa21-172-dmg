# Patrick's Journal

## Week 1

### November 12

Started on database design and implementation for books catalog.

![task](https://user-images.githubusercontent.com/34024255/142553904-f3cb07b3-b2df-4c99-a5d3-9ab2c9bbca91.png)


## Week 2

### November 17

Finished ERD for spring-books database

<img width="815" alt="spring-books-erd" src="https://user-images.githubusercontent.com/34024255/142553987-f475f84a-4a20-4dfa-b406-69eb1b1cf663.png">

## Week 3

### November 23
![Inventory](https://user-images.githubusercontent.com/34024255/143806110-cf54f3e2-6498-43fd-93fe-ea29b7f4727b.png)

Added entities and repositories for Book, ShoppingCart, and CartItem classes

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/bd26593bed39d332dae21c2153a62d209b3e3f2d)

Added `data.sql` file to initialize spring-books database

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/cf4a9b10e9271c33cb93b733551e464b8501ad70)

Added dockerfile/docker-compose.yaml and MySQL database initialization for spring-books

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/2fe5c9793fb43d9c5fad81c92f92a3e4183da244)

### November 24
![cart](https://user-images.githubusercontent.com/34024255/143806404-c9f5aadb-6ed0-4ff7-be13-eb9aa5ab4c9e.png)

Allow catalog to pass ISBN to controller in spring-books

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/c2a9accb0b55e0372003530fb5d531af8cb4542c)

![book_lookup](https://user-images.githubusercontent.com/34024255/143806727-e1d417eb-532c-4c79-997e-0d0976e248c9.png)


Got shopping cart backend working, allowing multiple items to be added to cart and stored in database
Shopping cart now functional with HTML page, updated HTML to have shopping cart link

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/6b4119103c0a6c892e2c92f204cd314f979f9cad)

### November 25
![unknown](https://user-images.githubusercontent.com/34024255/143996251-a6cfe44f-77ce-4750-9b9a-4f37e605e744.png)

Added ability to delete items from shopping cart 

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/e82560e74830ca0f6aa41a53294637989e51aec6)

![unknown](https://user-images.githubusercontent.com/34024255/143997169-22d9eedd-87e6-4d76-8eec-57fb08f36d9c.png)
![unknown](https://user-images.githubusercontent.com/34024255/143997180-e2f48d3c-8e90-4769-989f-748809e484c9.png)

Fixed POST request issue with shopping cart, allowed clearing of shopping cart

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/e62c96deef9f6568e9357fa81d09833332ec9163) 

### November 27

![unknown](https://user-images.githubusercontent.com/34024255/144019935-f822b00d-a711-436b-bbed-3b7db7eb2f1b.png)

Added quantity fields to catalog page that adds quantity of selected item

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/bf43c6d2137fc6ded5d7b836bcb27c1cdb55ae0e)

![image](https://user-images.githubusercontent.com/34024255/144019765-2e205da2-7949-4d9e-bc6d-423a0df8950e.png)

## Week 4

### Novemeber 28

![unkown](https://cdn.discordapp.com/attachments/825238290797953027/914456887678881802/unknown.png)

Separated frontend from backend of spring-books making `BookController.java` a rest controller in spring-books

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/d0a12d23619d02bf4d26c3595d8369158b5266ee)

![unknown](https://cdn.discordapp.com/attachments/825238290797953027/914457155711668284/unknown.png)

Pushed fix to allow deletion of cart items and clearing of shopping cart from separated frontend

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/f26f7621f51b68cd092441dc6930973291d56246)

![unknown](https://cdn.discordapp.com/attachments/825238290797953027/914782789390446613/unknown.png)


Implemented Kong API gateway with spring-books in Docker, tested using Postman

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/f5b914ba155f0b58ea1fe20fe68e0b033506c566)

![unknown](https://cdn.discordapp.com/attachments/825238290797953027/914776610840920074/unknown.png)
* Pinging spring-books

![unknown](https://cdn.discordapp.com/attachments/825238290797953027/914780674144231464/unknown.png)
* Post request to add item to cart

![unkown](https://cdn.discordapp.com/attachments/825238290797953027/914780751214542858/unknown.png)
* Getting shopping cart information

![unkown](https://cdn.discordapp.com/attachments/825238290797953027/914782245942865930/unknown.png)
* Clearing cart

### November 29

![unknown](https://cdn.discordapp.com/attachments/825238290797953027/915785109393801296/unknown.png)

Added initial API request to send cart information to spring-payments, need to continue work on spring-payments accepting values

[Commit](https://github.com/nguyensjsu/fa21-172-dmg/commit/526611b1fac1e2b90784f05b261e494e5584d924) 
