[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=15170889&assignment_repo_type=AssignmentRepo)

# Installation Manual

> Founder information
> @team w16bzzz
>
> Yuanyi Wang z5445322@ad.unsw.edu.au
> Siying Wu z5433930@ad.unsw.edu.au
> Zhaoyue Zhang z5447138@ad.unsw.edu.au
> Zuoming Yan z5486762@ad.unsw.edu.au
> Tianrui Wang z5407459@ad.unsw.edu.au
> Zelong Huang z5489331@ad.unsw.edu.au

## Configure environment variables

Ensure that the Docker service on your computer is running; you can check using the following command:

```
(base) yanzuoming@Johns-MacBook-Pro EazyinvoiceServer % docker --version
Docker version 20.10.22, build 3a2c30b63a
```

## How to deploy the project docker environment with one step

1. Obtain the final project artifacts from GitHub or other sources, unzip the package, and execute the relevant commands:

```
tar -zxvf eazyinvoice_final_release_pkg.tar.gz
```

2. After unzip the package, follow steps below:

```
cd EazyInvoice 
docker-compose up --build -d 
```

### The final output

If all previous commands were executed correctly, you should see the following output in the console:

```[+]
(base) yanzuoming@Johns-MacBook-Pro EazyInvoice % docker-compose up --build -d
[+] Building 1.5s (19/19) FINISHED                                                                                                                                   docker:desktop-linux
 => [platform internal] load build definition from Dockerfile                                                                                                                        0.0s
 => => transferring dockerfile: 1.06kB                                                                                                                                               0.0s
 => [platform internal] load .dockerignore                                                                                                                                           0.0s
 => => transferring context: 2B                                                                                                                                                      0.0s
 => [platform internal] load metadata for docker.io/library/openjdk:17-jdk-slim                                                                                                      0.9s
 => [platform stage-1 1/6] FROM docker.io/library/openjdk:17-jdk-slim@sha256:aaa3b3cb27e3e520b8f116863d0580c438ed55ecfa0bc126b41f68c3f62f9774                                        0.0s
 => [platform internal] load build context                                                                                                                                           0.0s
 => => transferring context: 184B                                                                                                                                                    0.0s
 => CACHED [platform stage-1 2/6] RUN apt-get update &&     apt-get install -y wget &&     wget https://archive.apache.org/dist/tomcat/tomcat-9/v9.0.65/bin/apache-tomcat-9.0.65.ta  0.0s
 => CACHED [platform stage-1 3/6] RUN rm -rf /usr/local/tomcat/webapps/*                                                                                                             0.0s
 => CACHED [platform stage-1 4/6] COPY ./*.war /usr/local/tomcat/webapps/ROOT.war                                                                                                    0.0s
 => CACHED [platform stage-1 5/6] COPY tomcat/conf/server.xml /usr/local/tomcat/conf/server.xml                                                                                      0.0s
 => CACHED [platform stage-1 6/6] COPY tomcat/conf/context.xml /usr/local/tomcat/conf/context.xml                                                                                    0.0s
 => [platform] exporting to image                                                                                                                                                    0.0s
 => => exporting layers                                                                                                                                                              0.0s
 => => writing image sha256:cb8dd4e0f36b141104de623e32a4c852e1cbc5078fd339493496907ffe832176                                                                                         0.0s
 => => naming to docker.io/library/eazyinvoice-platform                                                                                                                              0.0s
 => [frontend internal] load .dockerignore                                                                                                                                           0.0s
 => => transferring context: 2B                                                                                                                                                      0.0s
 => [frontend internal] load build definition from Dockerfile                                                                                                                        0.0s
 => => transferring dockerfile: 438B                                                                                                                                                 0.0s
 => [frontend internal] load metadata for docker.io/library/nginx:alpine                                                                                                             0.0s
 => [frontend stage-1 1/3] FROM docker.io/library/nginx:alpine                                                                                                                       0.0s
 => [frontend internal] load build context                                                                                                                                           0.0s
 => => transferring context: 792B                                                                                                                                                    0.0s
 => [frontend stage-1 2/3] COPY ./build /usr/share/nginx/html                                                                                                                        0.2s
 => [frontend stage-1 3/3] COPY nginx.conf /etc/nginx/nginx.conf                                                                                                                     0.0s
 => [frontend] exporting to image                                                                                                                                                    0.1s
 => => exporting layers                                                                                                                                                              0.1s
 => => writing image sha256:f6df33e27af39eed244dcd61b8d784f9b01b96b34ba7f36a54a4916ac9ab192f                                                                                         0.0s
 => => naming to docker.io/library/nginx:alpine                                                                                                                                      0.0s
[+] Running 4/4
 ✔ Network eazyinvoice_app-networks  Created                                                                                                                                         0.0s 
 ✔ Container eazyinvoice-db-1        Started                                                                                                                                         0.0s 
 ✔ Container eazyinvoice-platform-1  Started                                                                                                                                         0.0s 
 ✔ Container eazyinvoice-frontend-1  Started             

```

## Enjoy EazyInvoice!

Last but not least,

You can directly access the project's main page using your browser at [http://localhost:8080](http://localhost:8080).
