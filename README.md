# store
A retail store website (Backend)
This Spring Boot based for a retail store website that do discount operations.


# Description
This project provides an API for login, registration and calculate bill .

## UML:

![Store-UML](https://github.com/user-attachments/assets/1872a67e-9ef3-477f-a2f9-7c4ebbbe188b)


The server handles requests to calculate bills by applying this discount cratieria:
    
1. If the user is an employee of the store, he gets a 30% discount.
2. If the user is an affiliate of the store, he gets a 10% discount.
3. If the user has been a customer for over 2 years, he gets a 5% discount.
4. For every $100 on the bill, there would be a $ 5 discount (e.g. for $ 990, you get $ 45 as
a discount).
5. The percentage based discounts do not apply on groceries.
6. A user can get only one of the percentage based discounts on a bill.

# Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

## Prerequisites

You need the following installed and available in your $PATH:

 • Java 17 or newer
 
 • Maven

 • Docker

## Installation

### Clone the Repository

```bash
git clone https://github.com/anasdallah/store
```

###  Navigate to the directory

```bash
cd store
```

### Run build-script.sh to run and build the docker compose file and all the needed to be ready:

```bash
./build-script.sh
```

### SonarQube Report
You could find it inside sonar-report directory.


# Usage

Once the container is running, you can import the postman colletion called "store.postman_collection.json"

Please note that there's alrady 4 users in the mongo db, with all types of users (Customer, Employee, Affiliate)

And All the passwords are same: "pass12345"

Usernames:
affiliate
employee
customer_3_years
customer_1_year

The system will generate JWT, to be used in calculate bill.

### To login:
`POST localhost:8080/auth/login`
{
    "username": "employee",
    "password": "pass12345"
}

To register:
POST localhost:8080/auth/register
{
    "username": "anasdallah98",
    "password": "pass12345",
    "roles": [
        "ROLE_CUSTOMER"
    ]
}


To calculate bill, please take the JWT and pass it in with Bearer prefix.

Post localhost:8080/bill/calculate
{
  "item_ids": ["1","2"]
}


**Note**: There's mongo-express image if you want to see the data in mongo:
http://localhost:8081/


## Built With

#### • Spring Boot
#### • Maven
#### • MongoDB
#### • Docker
#### • SonarQube 
