
### Spring users

* Ping
  * curl --location --request GET 'http://users:8082/ping'

* Create user
  * curl -X POST -H "Content-Type: application/json" -d '{"firstName": "Joanna", "lastName": "Jones", "email": "joanna.jones@sjsu.edu","password": "password"}' "http://users:8082/users"
* Login with user
  * curl "http://users:8082/users?email=joanna.jones@sjsu.edu&password=password"

### Spring books

### Spring payments

* Ping 
  * curl --location --request GET 'http://localhost:8081/ping'
  
* Get creditcards request
  * curl --location --request GET 'http://localhost:8081/creditcards

* Post: add a card
  * curl --location --request POST 'http://localhost:8081/command' \
> --header 'Content-Type: application/json' \
> --data-raw '{
>     "id": null,
>     "action": "Save",
>     "firstname": "Kim",
>     "lastname": "Daisy",
>     "address": "789 Rose st",
>     "city": "Fremont",
>     "state": "CA",
>     "zip": "95111",
>     "phone": "(408)-123-4569",
>     "cardnumber": "4622-9431-2701-3747",
>     "expmonth": "December",
>     "expyear": "2022",
>     "cvv": "370",
>     "email": "daisy.brown@sjsu.edu",
>     "cartId": " null",
>     "subtotal": 0.0,
>     "transactionAmount": 0.0,
>     "transactionCurrency": null,
>     "authId": null,
>     "authStatus": null,
>     "captureId": null,
>     "captureStatus": null
> }'


* Post: Place an order
  * curl --location --request POST 'http://localhost:8081/placeorder?email=mary@gmail.com&placeorder=null'