# Introduction

> spring-boot-rest app
	is sample application for Spring boot rest api

## Functional Requirements
* load the unidirectional treenode on the server startup
* expose api the following API
    -   To query the descendants of the node
    -   Change the parent of the node
    
## Technology
* Java 8
* gradle
* Spring boot
* log4j2
* docker

## Get source code
Clone the repository 
```shell
git clone https://github.com/ravichandranrc/spring-boot-rest-app.git
```

## Build 
To build the jar with gradle:
```shell
./gradlew clean build
```

## Build docker Container
To build docker container:
```shell
docker build -t <name>:<tag> .
```

for e.g
```shell
docker build -t spring-boot-rest-app:latest .
```

## To Run docker Container
```shell
 docker run -d -p8080:8080 spring-boot-rest-app:latest
```