# Internship-Backend

## Purpose:
This repo is for creating a REST-API for a demo project that is used for company management. It is mainly to learn certain mechanisms (which are discussed under Topics) that are used in backend development using Spring Boot 3 and Spring 6.

## Topics:
The main topics that this project tackles are:
1. S3 storage,
2. Multilanguage response
3. Soft delete mechanism.
4. Many to many relationships
5. One to many table relation
6. Signin and reset password through sending email.

## Dependencies:
They are defined in [pom.xml]

## Entities to create:
1. User entity.
2. Company entity.

## Code structure:
1. **config pkg**: Includes the configuration files for application and spring security.
2. **controllers pkg**: It includes the different REST controllers that are needed in this project, meaning any endpoint can be seen in here.
3. **jpa pkg**: It includes the different repositories (interfaces) that are needed to communicate with the database.
4. **jwt pkg**: It includes the jwt filter.
5. **models pkg**: It includes the entities with their respective attributes.
6. **payload pkg**: Consists of 2 subpackages which are:
  1. response pkg: defines the structure for the JSON response.
  2. request pkg: defines the structure for the JSON request.
7. **services pkg**: defines the service layer to be used as an intermediary between repositories and controllers.

## Link:
[To be added]
