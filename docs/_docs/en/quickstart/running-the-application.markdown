---
title: Running the Application
permalink: /en/quickstart/running-the-application
abstract: >- # this means to ignore newlines until "baseurl:"
  Running locally and/or using Docker Descktop.
---

# Running the application       
For deploying applications on Cirrus, there is a progression of deployments used:
1.	(when possible) Run the application locally.  This ensures that the code itself works (we don’t want to be investigating a container or Cirrus issue only to find that the code itself doesn’t work).
2.	Build and run application on Docker Desktop.  This will ensure that the Dockerfile/image creation process works.
3.	Build and run the application in Cirrus/OpenShift.

## Running the application locally     

To perform a **Maven** build and run, perform the following:
1.	Maven build.  
    1. From the “Package Explorer” panel, select the pom.xml file, right click, and select “Run as…” -> “Maven Build…” from the context menu.  
    1. Under “Goal” specify **`clean verify`**
    1. Click “Run”

    This will perform the build and populate the “**target**” folder, building the ***.jar** file based on `<name>` and `<version>` element values specified in the `pom.xml` file (`ms-spring-server-0.0.1-SNAPSHOT.jar`).

2.	Run the application
    1.	Go to the directory with the jar file and execute:

<pre name="code" class="php">
java -jar ms-spring-server-0.0.1-SNAPSHOT.jar
</pre>

You can test with **CURL**, browser (for GET methods), or **POSTMAN**, e.g.:

```
 curl --location --request GET 'http://localhost:8080/api/v1/limits/' 

```
Which returns `{"minimum":5,"maximum":995}`

or
```

curl --location --request POST 'http://localhost:8080/api/v1/taxonomyEvaluator/' \
--form 'tax=@"/Users/steve/$WFM/wf360/data/jrs_taxononomy.csv"' \
--form 'data=@"/Users/steve/$WFM/wf360/data/rah_people_data.csv"' \
--form 'keyStr="0,2,16,18,20,15"'

```

Which returns: 

<pre name="code" class="js">
{
   "startTime":"2021-04-04T21:30:37.749+00:00",
   "taxUploadStartTime":"2021-04-04T21:30:37.749+00:00",
   "dataUploadStartTime":"2021-04-04T21:30:37.757+00:00",
   "evaluationStartTime":"2021-04-04T21:30:38.136+00:00",
   "statisticsStartTime":"2021-04-04T21:30:45.103+00:00",
   "completionTime":"2021-04-04T21:30:45.550+00:00",
   "taxonomyLevels":6,
   "totalRecords":128063,
   "validRecords":111379,
   "percentValidRecords":86.97203720044041,
   "invalidBrachRecords":11859,
   "percentInvalidBrachRecords":9.260285953007504,
   "invalidNodeRecords":4825,
   "percentInvalidNodeRecords":3.767676846552088,
   "tieOut":true,
   "outputDatasetUrl":"http://localhost:8080/api/v1/downloadFile/taxonomyEvaluationResults.csv"
}
</pre>

## Running Simple Local Docker File
The initial Dockerfile is simple.  It assumes that the Maven build has already been performed and just copies over the `*.jar` file.  This is however not a good option, since the compilation and build will be based on the local machine’s operating system and dependencies.  A better approach would be to perform the build within the container, which we’ll do next.

<pre name="code" class="js">
FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} apiServer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/apiServer-0.0.1-SNAPSHOT.jar"]
</pre>

The first thing we need is a **Java Runtime Environment (JRE)**. JRE is a software package, that has everything required to run a Java program. It includes an implementation of the Java Virtual Machine (JVM) with an implementation of the Java Class Library.  We are going to start with OpenJDK because it is licensed under GPL with Classpath Exception. The Classpath Exception part is important. This license allows using OpenJDK with any software license, not just the GPL. In particular, you can use OpenJDK in proprietary software without disclosing your code.  We’re also going to use the “alpine” flavor of this JDK, since they are the minimal images (smallest).

After we create the Dockerfile, we can perform the Docker image build and run the image.

1.	Build the Docker image and give it a tag.
```
docker image build --tag ms-spring-server .
```
1.	Start the server
```
docker container run --detach --publish 8080:8080 --name ms-spring-server ms-spring-server .
```
1.	Go to Docker Desktop and see that the container is running. Check the log (clicking on the container name)    
![Docker Desktop]({{ site.baseurl }}/assets/images/docs/docker-desktop.png)

## Running Multi-stage Local Docker File

The second version of the dockerfile builds/packages the Java source code, using Maven, within the container.

Multi-stage builds are a feature introduced in Docker 17.05 on the daemon and client. Multistage builds are useful to optimize Dockerfiles while keeping them easy to read and maintain.

Before multi-stage builds, when building images, it was challenging to keep the image size down. Each instruction in the Dockerfile adds a layer to the image, and you need to remember to clean up any artifacts you don’t need before moving on to the next layer. To write a really efficient Dockerfile, you have traditionally needed to employ shell tricks and other logic to keep the layers as small as possible and to ensure that each layer has the artifacts it needs from the previous layer and nothing else.


To address this, the “builder pattern” was created which had one Dockerfile (Dockerfile.build) to use for development (which contained everything needed to build your application), and a slimmed-down one (Dockerfile) to use for production, which only contained your application and exactly what was needed to run it.  This was less than ideal since it required the maintenance of two Dockerfiles.

With multi-stage builds, you use multiple `FROM` statements in your Dockerfile. Each `FROM` instruction can use a different base, and each of them begins a new stage of the build. You can selectively copy artifacts from one stage to another, leaving behind everything you don’t want in the final image.
By default, the stages are not named, and you refer to them by their integer number, starting with 0 for the first `FROM` instruction. However, you can name your stages, by adding an `AS <name>` to the `FROM` instruction.

The dockerfile used is below:

<pre name="code" class="js">
# Build stage
FROM maven:3.6.3-openjdk-11 as build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package 

# Package stage
FROM openjdk:11-jdk
COPY --from=build /home/app/target/ms-spring-server-0.0.1-SNAPSHOT.jar /ms-spring-server-0.0.1-SNAPSHOT.jar

# Make directories for upload and download files and grant required permissions
RUN mkdir /uploads
RUN mkdir /downloads

RUN chgrp -R 0 /uploads && \
    chmod -R g=u /uploads

RUN chmod -R 777 /uploads
RUN chmod -R 777 /downloads

EXPOSE 8080

# Run the app
ENTRYPOINT ["java","-jar","/ms-spring-server-0.0.1-SNAPSHOT.jar", "com.ibm.wfm.Application"]
</pre>

The file’s first stage uses the Maven/Java JDK 8 – alpine (minimal) image to build the application within the container.  This stage is named “build”.  The second stage uses Java OpenJDK 8 and copies the previously generated/compiled Java ARchive (JAR) file to the application’s root.  Thereafter, the application is started.

This Dockerfile was built and deployed locally using Docker Desktop and the same `docker build` and `docker run` commands that were used with the “Basic Dockerfile”. 

## Deploying the application in Cirrus
Deploying the application to Cirrus, 3 basic steps were followed:
1.	Create and commit the code to a GitHub Enterprise repository
2.	(If necessary) In Cirrus, create a new project or use an existing project
3.	In Cirrus, create a new pipeline with the project linked to the GitHub Enterprise repository (from step 1).  The pipeline will create the Docker image
4.	In Cirrus, create a new application with the project linked using the Docker image from the previous step (3).

### Create GitHub Enterprise Repository
For this sample application, I created the GitHub Enterprise repository:
- [https://github.ibm.com/IBM-Services-WFM/ms-spring-server/tree/master](https://github.ibm.com/IBM-Services-WFM/ms-spring-server/tree/master)
- git@github.ibm.com:IBM-Services-WFM/ms-spring-server.git

Thereafter, under the project, using the Team context menu and set the GitHub enterprise repository to the above.  Once connected and shared, you can use the “Git Staging” tab in the Eclipse GIT perspective to stage (add), commit, and push changes to the repo.

### Create a new project
Go to the [Cirrus Platform landing page](https://brokered.ciocloudservices.ibm.com/cirrus/projects), and, if required, create “New Project”.  

### Create a new pipeline
Open (click) the new or existing project and navigate to the “Pipelines” tab.
 
From the pipelines page, click the “New Pipeline” button.
In the “New Pipeline” dialog, specify the following:
- Pipeline type: GitHub Enterprise integrated (Source-to-Image or Dockerfile)
- Name: any unique name for the pipeline, `ms-spring-server`
- Container runtime: Dockerfile.  This indicates the runtime that should be used to compile the application.
- GitHub Enterprise repository: [https://github.ibm.com/IBM-Services-WFM/ms-spring-server.git](https://github.ibm.com/IBM-Services-WFM/ms-spring-server.git) (note, you must have administrator privileges on this repository)
   - GitHub Branch: master (note that the pipelines automatically rebuild with new commits to this branch)
   - Source Directory: . (dot, current directory)
   - Build Environment Values: leave empty.  These are key value pairs that the build process may require.
 
Use the “History” tab of the new pipeline to check on the status of the image build.  The log for the build should end with a “Build complete - …” message.

### Create a new application
Next, select the “Applications” tab and click the “New Application” button.
 
In the “New Application” dialog, specify the following:
- Environment: Non-production
- Pipeline: Specify the Docker image   just created (`ms-spring-server`)
- Target:
   - Cluster: The cluster where the app will be deployed (us-south1-nonprod)
   - Hostname (optional): This will become the prefix for the deployment URL (`ms-spring-server`)
   - EAL IMAP Number (optional): Internal applications must be in the Enterprise Application Library
- Runtime:
  - Container Port: Port that should be bound to application route (8080)
  - Environment from Secrets (optional): Allows you to select one or more secrets to be injected into your runtime container.  Secrets are bound to the specific cluster you select, and will give your application access to env vars at runtime.
  - Replicas (pods): Specify the number of initial replicas (pods) to be created.  The default is 3.
  - Memory tier: Limits the amount of memory for each replica on your application 

When your done click the “Deploy” button.  This will deploy your application built from the latest built image available in the specified Pipeline. 

