
# JobRunr RBT Application

## Introduction

JobRunr RBT Application is a general-purpose application designed to make job management easier by providing a dashboard that allows users to search for jobs by method or class. The dashboard offers a complete overview of jobs, including their status, functionality, and ability to be edited, deleted, rescheduled, and requeued.

The application provides a complete set of functionality for managing different types of jobs, from Scheduled to Failed, enabling users to quickly identify and resolve issues.

Jobs status and functionality for jobs status.
|Status|Description|Functionality|
|--|--|--|
| Scheduled | Jobs that are waiting to be enqueued and processed.| They can be searched, edited and deleted. |
| Enqueued | Jobs that are waiting to be processed.|They can be searched and deleted.|
| Processing| Jobs that are currently being processed.|They can be searched and deleted.|
| Succeeded | Jobs that successfully finished processing.| They can be searched, edited and deleted. |
| Failed | Jobs that ran in to some sort of error and did not finish processing successfully.| They can be edited, reenqueued and deleted. |
| Deleted | Jobs that were automatically deleted by the system, or deleted by the user. |They can be searched.|

The dashboard also allows user preview of jobs that are in the database, as well as options to configure them. This includes changing scheduled times where applicable, as well as package, class, method and arguments.
There is also an option to batch reenqueue jobs by providing CSV with job id's.
## Installation and Configuration
The easiest way to install application and dashboard is with docker.

Command for start full application is
```bash
docker-compose up
```
    
### Application configuration

In `application.properties` must set configuration for JobRunr and database configuration:

```bash
spring.datasource.url=jdbc:postgresql://localhost:DATABASE_PORT/DATABASE_NAME
spring.datasource.username=DATABASE_USERNAME
spring.datasource.password=DATABASE_PASSWORD

```

### Dashboard configuration


In `package.json` change server name to where the application is hosted:

```bash
"proxy": "http://server:8000",
```
then in `jobs.spec.js` change server name where is hosted application:
```bash
cy.visit('http://server:8000/dashboard/jobs');
```

- server: The server name or IP

    
## Run Locally

Clone the project

```bash
git clone https://github.com/nemanjaandjelkovic/jobrunrrbt.git
```

### Dashboard

Before running the dashboard you need to run backend for full functionality.

- **Install node version 14+**

Go to the project directory

```bash
cd frontend
```

Install dependencies

```bash
npm install
```

Start the server

```bash
npm run start
```

Runs the app in the development mode.\
Open [http://server:3000/dashboard](http://server:3000/dashboard) to view it in your browser.

- server: The server name or IP

The page will reload when you make changes. \
You may also see any lint errors in the console.

### Application

In `application.properties` change properties:
```bash
spring.datasource.url=jdbc:postgresql://localhost:DATABASE_PORT/DATABASE_NAME
spring.datasource.username=DATABASE_USERNAME
spring.datasource.password=DATABASE_PASSWORD
```


## Documentation

If you want to see documentation, you need to build application first and use dokka plugin.

In documentation, you can see description of functions and models.

The Swagger UI page will then be available at `http://server:port/swagger-ui/index.html`  

The OpenAPI description will be available at the following url for json format: `http://server:port/v3/api-docs `

Documentation can be available in yaml format as well, on the following path : `http://server:port/v3/api-docs.yaml`

- server: The server name or IP
- port: The server port

