---
title: Component Model
permalink: en/learning-framework/component-model
abstract: >- # this means to ignore newlines until "baseurl:"
  The component model emphasizes the separation of concerns with respect to the functionality available within the framework. It is a reuse-based approach to defining, implementing and composing loosely coupled independent components into the system. An individual software component is a package, a web service, a web resource, or a module that encapsulates a set of related functions (or data).  All system processes are placed into separate components so that all of the data and functions inside each component are semantically related (just as with the contents of classes). Because of this principle, it is often said that components are modular and cohesive.
---

![Spring-Boot Microservice Architecture]({{ site.baseurl }}/assets/images/docs/spring-boot-microservice-architecture.png)

# Application

This application uses Maven, so we used Spring Initializr to generate the initial project with the required dependencies (Spring Web), specified in the **Project Object Model** or **POM** file.  The POM is the fundamental unit of work in Maven. It is an XML file that contains information about the project and configuration details used by Maven to build the project. It contains default values for most projects. Examples for this is the build directory, which is target; the source directory, which is src/main/java; the test source directory, which is src/test/java; etc. This is because all POMs are inherit from a super-POM, that contain these default values (see: [https://maven.apache.org/ref/3.6.3/maven-model-builder/super-pom.html](https://maven.apache.org/ref/3.6.3/maven-model-builder/super-pom.html)). When executing a task or goal, Maven looks for the POM in the current directory. It reads the POM, gets the needed configuration information, then executes the goal.

Some of the configuration that can be specified in the POM are the project dependencies, the plugins or goals that can be executed, the build profiles, clean, verify, etc. Other information such as the project version, description, developers, mailing lists and such can also be specified.

The minimum requirement for a POM are the following:
- project root
- modelVersion - should be set to 4.0.0
- groupId - the id of the project's group.
- artifactId - the id of the artifact (project)
- version - the version of the artifact under the specified group


The following listing shows the pom.xml file that was created when you choose Maven:

<pre name="code" class="xml">
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.4</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.ibm.wfm.ms</groupId>
	<artifactId>ms-spring-server</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>ms-spring-server</name>
	<description>Spring Boot Centralized Configuration</description>
	<properties>
		<java.version>11</java.version>
		<spring-cloud.version>2020.0.0</spring-cloud.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

</project>
</pre>

We'll add to this all the dependencies that we'll need as we continue.  The Spring Initializr also creates a simple application class called `Application.java`.  We'll make some changes to this, primarily to enable `Swagger` documents to be generated automatically for us.

To enable the Swagger2 in Spring Boot application, you need to add the following dependencies in the `POM.xml` configuration file.

<pre name="code" class="xml">
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
  </dependency>		
  
  <dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
  </dependency>
</pre>

Thereafter, looking at **Application.java**:

<pre name="code" class="java">
@SpringBootApplication
@EnableSwagger2
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class Application {
	
	@Autowired
	private SwaggerProperties swaggerProperties;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public Docket produceApi() {
	    return new Docket(DocumentationType.SWAGGER_2)
	            .apiInfo(apiInfo())
	            .select()       
	            .paths(PathSelectors.regex("/api.*"))
	            .build();
	}

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .contact(new Contact(swaggerProperties.getContactName(), swaggerProperties.getContactUrl(), swaggerProperties.getContactEmail()))
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .version(swaggerProperties.getVersion())
                .build();
    }
}
</pre>

Line 1, `@SpringBootApplication` is a convenience annotation that adds all of the following:

- `@Configuration:` Tags the class as a source of bean definitions for the application context.
- `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
- `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the com/example package, letting it find the controllers.

Line 2, the `@EnableSwagger2` annotation is used to enable the Swagger2 for your Spring Boot application.  Swagger2 is an open source project used to generate the REST API documents for RESTful web services. It provides a user interface to access our RESTful web services via the web browser.

Next, we create a `Docket` Bean (lines 15-22) to configure Swagger2 for the Spring Boot application. We need to define the base package to configure REST API(s) for Swagger2 (`DocumentationType.SWAGGER_2`). 

This bean set the `apiInfo` property for the `Docket` Bean (line 18) by create a new instance of `springfox.documentation.service.ApiInfo` in the private `apiInfo()` method (lines 24-32). It also set `paths` using `regex` of `springfox.documentation.builders.PathSelectors` that evaluates the supplied regular expression and returns paths/URL that conform (i.e., all paths prefixed with `/api`).

Looking at `springfox.documentation.builders.ApiInfoBuilder` constuctor (line 25) returned from the private `apiInfo()` method, you'll notice all the properties are coming from the `swaggerProperties` instance of the `SwaggerProperties` class.  This class is constructed via the `@Autowired` annotation (lines 8-9) and enabled by the `@EnableConfigurationProperties` (line 3).  If you look at the `SwaggerProperties` class you'll see:

<pre name="code" class="java">
@Component
@ConfigurationProperties("swagger")
public class SwaggerProperties {
</pre>

Spring will automatically bind any property defined in our property file (`resources/application.properties`) that has the prefix `swagger` based on the `@ConfigurationProperties` annotation in the `SwaggerProperties` class.

Finally, the `main()` method (line 11) uses Spring Boot’s `SpringApplication.run()` method to launch an application. Did you notice that there was not a single line of XML? There is no `web.xml` file, either. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure.

# Controllers     
The controller layer is probably the most important part of a Spring Boot application. It binds everything together right from the moment a request is intercepted till the response is prepared and sent back. The controller layer is present in the controller package, the best practices suggest that we keep this layer versioned to support multiple versions of the application and the same practice is applied here.

Spring 4.0 introduced the **`@RestController`** annotation in order to simplify the creation of RESTful web services. It's a convenient annotation that combines `@Controller` and `@ResponseBody`, which eliminates the need to annotate every request handling method of the controller class with the `@ResponseBody` annotation.  We typically use `@Controller` and `@RestController` in combination with a `@RequestMapping` annotation for request handling methods.

`@GetMapping` is a specialized version of `@RequestMapping` annotation that acts as a shortcut for `@RequestMapping(method = RequestMethod.GET)`. `@PostMapping` is also a specialized version of `@RequestMapping` annotation that acts as a shortcut for `@RequestMapping(method = RequestMethod.POST)`.

In the Java example below a `POST` request of `/api/v1/taxonomyEvaluator` would invoke the `taxonomyEvaluator()` method.  The `@RequestParam` annotation  extracts query parameters, form parameters, and even files from the request.  Method parameters annotated with `@RequestParam` are required by default.  This means that if the parameter isn’t present in the request, we'll get an 400 Bad Request error.  We can configure any `@RequestParam` to be optional, though, with the `required=false` attribute.  You can also specify a default value using the `defaultValue` attribute.  Finally, to store the uploaded file we can use the **`MultipartFile`** variable.

Looking at the declaration of the  **TaxonomyEvaluatorController.java** class:

<pre name="code" class="java">
@RestController
@RequestMapping("/api/v1")
public class TaxonomyEvaluatorContoller {
	
	@Autowired
	private FileStorageService fss;
	@Autowired
	private FileStorageProperties fileStorageProperties;
	
	@PostMapping("/taxonomyEvaluator")
	public TaxonomyEvaluationResponse taxonomyEvaluator(@RequestParam("tax") MultipartFile taxFile
			, @RequestParam("data") MultipartFile dataFile
			, @RequestParam(defaultValue = ",") String delimiter
			, @RequestParam("keyStr") String keyStr
			, @RequestParam(required=false) String ofn) {
</pre>


Although not used in this application, **Multi-Value Parameters** can be mapped using a single @RequestParam using `@RequestParam List<String> id`. With this a comma-delimited `id` parameter can be mapped:

```

http://<server>/api/v1/foos?id=1,2,3
----
IDs are [1,2,3]

```

or a list of separate id parameters:

```
http://<server>/api/foos?id=1&id=2
----
IDs are [1,2]

```

Finally we can **Map All Parameters** without defining their names or count by just using a `Map`:

<pre name="code" class="java">
@PostMapping("/api/foos")
@ResponseBody
public String updateFoos(@RequestParam Map<String,String> allParams) {
    return "Parameters are " + allParams.entrySet();
}
</pre>

which will then reflect back any parameters sent:

```

curl -X POST -F 'name=abc' -F 'id=123' http://localhost:8080/api/foos
-----
Parameters are {[name=abc], [id=123]}

```
<p>&nbsp;</p>

# Services
_Put services stuff here_

# Intra-service communitcation

In the controller of one microservice you call your another service using the `RestTemplate.getForEntity(url,responseType,uriVariables)`. 

<pre name="code" class="java">

 ResponseEntity&lt;FbsFootballDim&gt; responseEntity = null;
 new RestTemplate().getForEntity(
        "http://localhost:8080/api/v1/sec-football", FbsFootballDim.class,  uriVariables);
</pre>

Where 
- `url`: the url of your first (micro)(rest)service. 
- `responseType`: the class/type of the object awaited as response. 
- `uriVariables`: is a map containing variables for the URI template.
  

# Utilities

## Object-relational Mapper
The framework contains a object-relational mapper (ORM) Java abstraction that automates the transfer of data stored in relational database tables into a list of objects and vice versa. This abstraction performs data marshaling to read, write, update, and/or delete to and from any relational data source where the source and target are not known until runtime. The mapper understands and handles surrogate and natural keys, views, SQL of arbitrary complexity, slow-changing dimensions, and allows the source and targets to have different sources, entity and/or attribute names, and have different schemas.

## Excel Mapper
The framework contains an excel-object mapper Java abstraction that automates the transfer of data stored in an excel spreadsheet tab into a list of objects. This list can then be used by the ORM to persist the data into relational databases. This abstraction performs data marshaling to read from an excel spreadsheet and tab and populate an object list for subsequent processing.

## Data Marshalling

The framework contains a base set of data marshalling functions using Java annotations and reflection. 

**Java reflection** allows you interrogate the properties of a class, such as the names and types of its instance variables and methods, at run time. It also enables classes to be created from their names, dynamically creating and invoking class constructors at runtime. It allows these base functions to do serialization and deserialization objects in a completely generic manner. This means that there is no need to write code for marshalling functions for each type of object. Any mapping required (e.g., source to target attribute name, attribute offsets) are captured using **Java annotations**.

There are marshalling functions for Excel, CSVs, JSON, XML, and relational databases.

## Delta Processing
The framework contains a data comparison microservice that can compare two sources and identify inserts, updates, and deletes between the two sources. Ladder comparison algorithm.

## JR/S Mapper
The framework contains a employee JR/S mapper function as a microservice within this framework.

## File Bursting
The framework contains a generic file bursting service. This service allows a single file to be split into an arbitrary number of files, retaining a common set of column headings, based on a configurable set of parameters.

## Taxonomy Evaluator
The framework contains a Taxonomy Evaluator. The taxonomy evaluator will evaluate any data file against any taxonomy based on a map of taxonomy keys within the data file being evaluated. For each data file record, the service identifies all invalid nodes and/or arcs (i.e., valid nodes with invalid parent-child relationships), the lowest valid node within a branch, and the valid leaf node (when it exists) and its full branch.