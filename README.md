### About

This is frontend part of Web-application. Frontend responses for retrieving data from DB and representing to user. It can send REST-request to backend for updating data in DB.

### Task

Create a simple Web-application and CI/CD infrastructure and pipeline for it.

Using API https://covidtracker.bsg.ox.ac.uk/about-api get all data about “By country over time" for current year and store it into your DB:

```
date_value, country_code, confirmed, deaths, stringency_actual, stringency.
```
Output the data by country_code (the country_code is set) in the form of a table and sort them by deaths in ascending order.

### Building frontend

```bash
mvn clean compile package -DskipTests=true

docker build -t frontend:latest .

docker run --name frontend -e DB_HOST=<IP:PORT> \ 
                          -e DB_NAME=<DATABASE_NAME> \ 
                          -e DB_USERNAME=<DATABASE_USERNAME> \ 
                          -e DB_PASSWORD=<DATABASE_DB_PASSWORD> \
                          -e BACKEND_API=<BACKEND_URL>
                           frontend:latest
```
