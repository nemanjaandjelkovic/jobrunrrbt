# Installation and setup

 

 - Install node version 14+
 - Run `npm install` inside the ***project folder***
 - Inside the `package.json` and `jobs.spec.js` change servers ***ip adress*** where jobrunr dashboard is hosted and add `:PORT_WHERE_JOBRUNR_DASHBOARD_IS_HOSTED` to it.
 - Inside the `package.json` set `"proxy": "http://ipadress:PORT_WHERE_JOBRUNR_DASHBOARD_IS_HOSTED/"`
 - Inside the `jobs.spec.js` set `cy.visit('http://ipadress:PORT_WHERE_JOBRUNR_DASHBOARD_IS_HOSTED/dashboard/jobs');`
 - After completing the steps above and running the program, dashboard should be running on the port `3000`



# JobRunr RBT

Microservice over database with interactive dashboard


## Dashboard

The dashboard offers full overview over Jobs. They can be searched, requeued, deleted, rescheduled and edited from here.

### Scheduled
Jobs that are waiting to be enqueued and processed are found here. They can be searched, edited and deleted from here.
### Enqueued jobs
Jobs that are waiting to be processed are found here.
### Processing 
Jobs that are currently being processed are found here.
### Succeeded Jobs
Jobs that successfully finished processing are found here. They can be searched, edited and deleted.
### Failed Jobs
Jobs that ran in to some sort of error and did not finish processing successfully are cound here. They can be edited, reenqueued and deleted.
### Deleted Jobs
Jobs that were automatically deleted by the system, or deleted by the user are found here.
### Jobs re-enqueue
You can re-enqueue list of jobs by using the popup menu prompted by this option.

> Input format: 

`id/id/id...`
### Individual jobs
Each individual job can be explored.
#### Jobs code
The jobs code is displayed in the form of code block
>Example: 
````java
import java.lang.System;
System.out.print(Input text);
````
#### Edit Job ( If applicable )
Dropdown menu where the job details can be edited.
![Edit job menu](https://i.imgur.com/uf4iKq9.png)
#### Job history
Details of the jobs life cycle
![Job history](https://i.imgur.com/5h7GRCx.png)
