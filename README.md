### How to run locally

- Download the project

- Add the connection string to application.properties

- Run InstaCloneApplication.java
- Use Postman to interact with endpoints

### Description

Graded group assignment; we chose to create a Java Spring Boot API with Postgres where
users can register, login, upload, download and delete images.

### Features

#### 1. User registration

```
Method: POST
Endpoint: /register
Body (JSON): 
{
    "username":"EXAMPLE_USER",
    "password": "EXAMPLE_PASSWORD"
}

```

#### 2. User Login

```
Method: POST
Endpoint: /login
Body (JSON): 
{
    "username":"EXAMPLE_USER",
    "password": "EXAMPLE_PASSWORD"
}
```

#### 3. Image upload

```
Method: POST
Endpoint: /image/upload
Body (form-data): key = image, value = an image file, e.g example.png
Header: key = Authorization, value = JWT token string
```

#### 4. Image download

```
Method: GET
Endpoint: /image/download/{imageId}
Path-variable: e.g imageId = 13
Header: key = Authorization, value = JWT token string
```

#### 5. Image deletion

```
Method: DELETE
Endpoint: /image/delete/{imageId}
Path-variable: e.g imageId = 13
Header: key = Authorization, value = JWT token string
```

#### 6. Show a user's profile page

```
Method: GET
Endpoint: /profilepage/{username}
Path-variable: e.g username = "REGISTERED_USER"
Header: key = Authorization, value = JWT token string
```

#### 7. Edit a user's profile page description

```
Method: PUT
Endpoint: /profilepage/edit/description
Body (JSON): 
{
    "description":"NEW_DESCRIPTION"
}
Header: key = Authorization, value = JWT token string
```


