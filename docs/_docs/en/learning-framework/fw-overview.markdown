---
title: Overview
permalink: en/learning-framework/fw-overview
abstract: >- # this means to ignore newlines until "baseurl:"
  This project allows for the deployment of Java based microservices on the Spring-Boot platform. Microservices are a modern approach to software whereby application 
  code is delivered in small, manageable pieces, independent of others. 
  Their small scale and relative isolation can lead to many additional benefits, such as easier maintenance, improved productivity, greater fault tolerance, better business alignment, and more.
---

# Framework Objective

It used to be that data existed in a box and when one system needed data from another, the data was physically copied or accessed through some form of distributed processing or federation.  However, the replication of application functionality and data using point to point connections was not a sustainable paradigm.  As the number of enterprise applications proliferated, there needed to be more direct communications between applications, which introduced another layer of interface, which was eventually labeled **Enterprise Application Integration**, or **EAI**.  The idea was that if these systems could be integrated, existing functionality could be reused instead of replicated across multiple systems.  The reduced functional redundancy meant that functionality only needed to be maintained and enhanced within a single system saving time and money in development, testing, and deployments.

Application integration began with the **application programming interface (API)** to expose system functionality to external processes.  Where this worked okay it still required application to be written in the same language and running on the same platform.  This led to language and platform specific adaptors, implementing the mediator pattern, in order to integrate systems.  However, these adaptors were difficult to maintain and therefore not widely adopted. 

The next phase of the integration evolution attempted to expose system function and data through platform independent means.  Over time, the EAI industry started widely promoting the use of a **Services Oriented Architecture (SOA)** using the **SOAP (Simple Object Access Protocol)** specification, which used existing EAI methodology of integrating applications through the use of messaging.  Web Services, which facilitated a SOA, exposed the system’s functions in a language and platform independent manner.

**Data as a Service**, or **DaaS**, is the next logical extension of SOA.  The same benefits realized by exposing system functionality through services within a SOA, rather than replicating it, can be realized by exposing data through services.  If data is needed, it is requested and delivered in a message based, language and platform independent method; without requiring an extract, transformation, and load (ETL) process or reporting packages.

The concept of data-as-a-service advocates accessing data "where it lives" -- the actual platform on which the data resides doesn't matter.  With DaaS, any business process can access data wherever it resides. It is built on the notion that data quality can happen in a centralized place, cleansing and enriching data, and then be offered to different systems, applications or users, irrespective of where they were in the organization or on the network.

This framework, built using a **microservice architecture** delivers:

1. Data acquisition services from ANY relational source
1. Data acquisition services from Excel or CSV files
1. Data Insert, Update, and Delete services into ANY relational source
1. Services to identify Insert, Update, and Delete records based on consecutive data snapshots (that can be acquired from a data acquisition service and loaded with the insert, update, and delete services)
1. Data quality services to check data against standard taxonomies and/or allowed value lists
1. Utility services to upload and download files, generate artifacts such as Java beans for intermediate storage and database DDLs, and data marshalling between Excel, CSVs, and relational database result sets to and from Java Beans.



# What is a microservice architecture?

An architecture that structures the application as a set of loosely coupled, collaborating services. Each service is 

- Highly maintainable and testable, enabling rapid and frequent development and deployment
- Loosely coupled with other services; enables a team to work independently the majority of time on their service(s) without being impacted by changes to other services and without affecting other services
- Independently deployable, enabling a team to deploy their service without having to coordinate with other teams
- Capable of being developed by a small team - essential for high productivity by avoiding the high communication head of large teams

Services communicate using either synchronous protocols such as HTTP/REST or asynchronous protocols such as AMQP. Services can be developed and deployed independently of one another. Each service has its own database in order to be decoupled from other services. Data consistency between services is maintained using the [Saga pattern](https://microservices.io/patterns/data/saga.html).

# Why use microservices?

A traditional “monolithic” application houses code in a single location for all of the services needed to fulfill its functions. To start with, that usually works well. But what happens if a specific service needs to be scaled to meet demand? Generally, a new instance of the entire server-side component will need to be created, and work will need to be done across the codebase to ensure the newly-scaled service fits into the wider application ecosystem. This is hardly a good use of resources.

Alternatively, if an application grows over time, it’s likely that independent teams of programmers will be responsible for working on different functions. Even with the best of intentions, it’s difficult for these teams to work on the same codebase at the same time while maintaining strong communication. Even if perfect communication is possible, a lot of time and agility is inevitably lost.

These are the problems microservices architecture is designed to solve. Because each service has its own code base, microservices can be modified or scaled individually by independent teams without causing wider compatibility issues or requiring heavy inter-team communication.

# How do microservices communicate?
There are several different ways for microservices to communicate. Two important methods are:

1. Synchronous HTTP communication
1. Asynchronous Message-Driven communication

Typically, implementation starts with synchronous HTTP REST-like communication between services. There are plenty of libraries supporting this method of communication and it’s straightforward to bootstrap this approach. However, a synchronous call will force the calling side to wait until the execution on the receiver side is done, leading to unnecessary blocking. Further, the calling service must “know” the receiving service exists in order to make a call, which leads to tight coupling. HTTP communication is commonly used in small, simple systems, and in instances where the caller needs to instantaneously find out the state of the system (i.e., by performing a GET call).

To achieve better location transparency and looser coupling, an alternative approach is asynchronous Message-Driven communication between services. This approach requires an additional component – a Message Broker – which increases the system’s complexity. In large scale systems, message-driven communication is usually chosen over synchronous HTTP communication.