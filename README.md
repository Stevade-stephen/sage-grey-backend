**User Account Management API - README**

This README provides instructions on how to set up and run the User Account Management API. The API allows users to create accounts, fund their accounts, transfer funds, withdraw funds, and fetch all users with their corresponding accounts.

**Prerequisites:**
- Java JDK 11 or later installed
- Apache Maven installed
- MySQL or another compatible database installed and running

**1. Clone the Repository:**
```
git clone https://github.com/yourusername/user-account-api.git
cd user-account-api
```

**2. Database Configuration:**

Create a MySQL database and update the application properties accordingly:

- Open `src/main/resources/application.properties`.
- Set the database URL, username, and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/user_account_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

**3. Build the Application:**
```
mvn clean install
```

**4. Run the Application:**
```
mvn spring-boot:run
```

The API will start running on `http://localhost:8080`.

**5. Endpoints:**

- **Create User:**
    - Endpoint: `POST /api/v1/create-user`
    - Request Body:
      ```json
      {
        "firstName": "John",
        "lastName": "Doe",
        "username": "johndoe",
        "email": "johndoe@example.com",
        "password": "yourpassword"
      }
      ```

- **Create Account for User:**
    - Endpoint: `POST /api/users/{userId}/accounts`
    - Request Body:
      ```json
      {
        "accountType": "SAVINGS"
      }
      ```

- **Fund Account:**
    - Endpoint: `POST /api/v1/users/fund-account`
    - Request Parameters:
        - `amount`: The amount to fund the account with.
        - `accountId`: The id of the account to be funded.

- **Transfer Funds:**
    - Endpoint: `POST /api/v1/users/transfers`
    - Request Parameters:
        - `fromAccountId`: Source account ID.
        - `toAccountId`: Destination account ID.
        - `amount`: The amount to transfer.

- **Withdraw Funds:**
    - Endpoint: `POST /api/v1/users/withdrawals`
    - Request Parameters:
        - `accountId`: Account ID.
        - `amount`: The amount to withdraw.

- **Fetch All Users with Accounts:**
    - Endpoint: `GET /api/users`

**6. Testing the Endpoints:**

You can use tools like cURL, Postman, or a web browser to test the endpoints.

For example, to create a new user, you can use the following cURL command:
```
curl -X POST -H "Content-Type: application/json" -d '{"firstName":"John", "lastName":"Doe", "username":"johndoe", "email":"johndoe@example.com"}' http://localhost:8080/api/users
```

For other endpoints, follow a similar approach by specifying the necessary request body and parameters as required.

**7. Security Considerations:**

This API is secured with Spring Security using Json web tokens JWT.
The open endpoints are that for the create-user and the login.
The other endpoints are authenticated using the JWT token gotten from logging in.

**8. Conclusion:**

You have now set up and run the User Account Management API successfully. You can use the provided endpoints to manage user accounts, fund accounts, transfer funds, withdraw funds, and fetch users with their associated accounts.