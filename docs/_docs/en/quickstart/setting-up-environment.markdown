---
title: Setting up envioronment
permalink: /en/quickstart/setting-up-environment
redirect_from: /docs/home/
layout: docs
abstract: >- # this means to ignore newlines until "baseurl:"
  To get things up and running, we'll start with GitHub. At the heart of GitHub is an open source version control system (VCS) called Git. Git is responsible for everything GitHub-related that happens locally on your computer and will be the starting point for developing with the framework.
---

# Overivew

A framework is a set of cooperating classes that make up a reusable design for a specific class of software. A framework provides architectural guidance by partitioning the design into abstract classes and defining their responsibilities and collaborations. A developer customizes a framework to a particular application through configuration and/or by subclassing and composing instances of framework classes.

Microservice architectures are the ‘new normal’. Microservices are a modern approach to software whereby application code is delivered in small, manageable pieces, independent of others. Building small, self-contained, ready to run applications can bring great flexibility and added resilience to your code. Spring Boot’s many purpose-built features make it easy to build and run your microservices in production at scale. 

# Forking the Code

Most commonly, forks are used to either propose changes to someone else's project or to use someone else's project as a starting point for your own idea. You can fork a repository to create a copy of the repository and make changes without affecting the upstream repository.

For co-creation within this framework we'll use forks. For users they will:

1. Fork the repository
1. Add your services or make a fix
1. Submit a pull request to the project owner

![Forking the code]({{ site.baseurl }}/assets/images/docs/forking-the-code.png)

_details still being worked out_

# Getting the Spring framework to run in Eclipse

Install the m2e connector for mavenarchiver plugin 0.17.3 from [https://download.eclipse.org/m2e-wtp/releases/1.4/](https://download.eclipse.org/m2e-wtp/releases/1.4/).

There are incompatibilities between the version of M2E and the version 3.1.2 of the maven-jar-plugin.  Until the problem is solved by the development teams of M2E or maven-jar-plugins, you have to add to the `<build>` element in your `pom.xml` file the following line in order to solve this issue:

<pre name="code" class="xml">
<pluginManagement>
	<plugins>
		<plugin>
			<groupId>org.apache.maven.plugins</groupId>
			<artifactId>maven-jar-plugin</artifactId>
			<version>3.1.1</version>
		</plugin>
	</plugins>
</pluginManagement>

</pre>



# Building the application

![Maven logo]({{ site.baseurl }}/assets/images/docs/Maven_logo.svg){: #custom-id style="width:7%;"} [Apache Maven](https://maven.apache.org/) is a popular build tool, that takes your project’s Java source code, compiles it, tests it and converts it into an executable Java program: either a .jar or a .war file.  Maven is one of the most popular build tools in the Java universe (others being [Gradle](https://gradle.org/) or old-school [Ant](https://ant.apache.org/)). You can not only build Java projects with it, but pretty much every project written in a JVM language like Kotlin or Scala, as well as other languages like C# and Ruby.

For this site we'll use the traditional `mvn clean install` command to build the project.

1. This command starts with the `mvn` executable, which means you need Maven installed on your machine or image.
1. Next the `clean` command, which will delete all previously compiled Java `.class` files and resources (like `.properties`) in your project. Your build will start from a clean slate.
1. Finally `install` will then compile, test & package your Java project and even install/copy your built .jar/.war file into your local Maven repository.

Maven has the concept of a build lifecycle, which is made up of different phases. These phases are sequential and depend on each other.  

![Maven Phases]({{ site.baseurl }}/assets/images/docs/mavenphasesv3.png)

For examoke, when you call `mvn deploy`, mvn will also execute every lifecycle phase before deploy, in order: validate, compile, test, package, verify, install.

Unlike other tools, where project dependencies are stored in your project’s directory, Maven has the concept of repositories.

There are local repositories (in your user’s home directory: `~/.m2/`) and remote repositories. Remote repositories could be internal, company-wide repositories.


# Invoking Functions

- Link to [Swagger Document](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/swagger-ui.html)
- Link to [Swagger JSON api-document](https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/v2/api-docs)

## Invoking Limits Function

```

curl --location --request GET 'https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/limits/' 

```

Returns

<pre name="code" class="js">
{
   "startTime":"2021-02-05T00:50:14.326+00:00",
   "taxUploadStartTime":"2021-02-05T00:50:14.326+00:00",
   "dataUploadStartTime":"2021-02-05T00:50:14.328+00:00",
   "evaluationStartTime":"2021-02-05T00:50:14.872+00:00",
   "statisticsStartTime":"2021-02-05T00:50:35.162+00:00",
   "completionTime":"2021-02-05T00:50:37.244+00:00",
   "taxonomyLevels":6,
   "totalRecords":128063,
   "validRecords":111379,
   "percentValidRecords":86.97203720044041,
   "invalidBrachRecords":11859,
   "percentInvalidBrachRecords":9.260285953007504,
   "invalidNodeRecords":4825,
   "percentInvalidNodeRecords":3.767676846552088,
   "tieOut":true,
   "outputDatasetUrl":"https://ms-spring-server.dal1a.ciocloud.nonprod.intranet.ibm.com/api/v1/downloadFile/taxonomyEvaluationResults.csv"
}
</pre>

