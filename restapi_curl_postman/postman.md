
### Spring users

### Spring books

### Spring payments

* Ping
  * GET: http://localhost:8081/creditcards?email=mary@gmail.com
  
  
* Get creditcards request
  GET: http://localhost:8081/creditcards
 
* Post: add a card
  * POST: http://localhost:8081/command
  * Body: raw -> JSON
    * {
      "id": null,
      "action": "Save",
      "firstname": "Kim",
      "lastname": "Daisy",
      "address": "789 Rose st",
      "city": "Fremont",
      "state": "CA",
      "zip": "95111",
      "phone": "(408)-123-4569",
      "cardnumber": "4622-9431-2701-3747",
      "expmonth": "December",
      "expyear": "2022",
      "cvv": "370",
      "email": "daisy.brown@sjsu.edu",
      "cartId": " null",
      "subtotal": 0.0,
      "transactionAmount": 0.0,
      "transactionCurrency": null,
      "authId": null,
      "authStatus": null,
      "captureId": null,
      "captureStatus": null
      }
  

* Place an order
  * POST: http://localhost:8081/placeorder?email=mary@gmail.com&placeorder=null
  * Params: key=email ; value = mary@gmail.com
    * key = placeorder; value = null
 