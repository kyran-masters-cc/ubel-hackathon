Create a controller which should take following fields as input:

[]: # - provider
[]: # - payment_rails
[]: # - No of messages
[]: # 
[]: # The controller should do the following:
[]: # 1) based on provider and payment_rails get the currency and van from config file
[]: # 2)Generate N of transactions and the transaction should have following fields in below format:
{
"id": "{id}",
"version": 2,
"credit_debit": "credit",
"amount": 100,
"currency": "{currency}",
"value_date": "2022-10-30",
"tracking_id": "{id}",
"reference": "{reference}",
"payment_rail": "{payment_rail}",
"provider": "{provider}",
"origin_account": {
"account_number": "FR26TCCL20786956994877",
"bic": "CMBRFR2BARK",
"iban": "FR26TCCL20786956994877"
},
"destination_account": {
"account_number": "{destination_acc_no}"
},
"sender": {
"free_text": "unstructuredOne, unstructuredTwo",
"name": "{sender_name}",
"ultimate_sender_name": "ultimateDebtorName",
"address": "address line1, address line2, address line3, address line4",
"country": "{country}"
},
"transaction_content": null }
[]:The message should be published on rabbit mq topic 'new_transaction_message'
Response to controlelr should be list of ids of the generated transactions
[]: # 
[]: # ## Running tests
[]: # Tests can be run locally or in a CI/CD pipeline.
[]: # 
[]: # ### Running tests locally
[]: # 
[]: # 1. Ensure you have Java 21 or higher installed.
[]: # 2. Ensure you have Maven installed.
[]: # 3. Clone the repository.
[]: # 4. Navigate to the project directory.
[]: # 5. Run the tests using the command:
[]: # 
[]: #    ```bash
[]: #    mvn clean test
[]: #    ```
[]: # 
[]: # ### Running tests in a CI/CD pipeline
[]: # 
[]: # 
[]: # The controller can be called from a step definition file. 
[]: # 
[]: # ## Reporting issues
[]: # Raise an issue in this repo with details of the problem.
[]: # 
[]: # ## Contributing
[]: # Raise a pull request in this repo with details of the changes made.
[]: # 
[]: # ## Contact
[]: # For any questions or suggestions, please open an issue or contact the maintainers of this repository.
[]: #