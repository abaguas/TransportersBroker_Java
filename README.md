# Distributed Systems Project 2015-2016 #

Group 64 - Campus Alameda

André Águas - 78854 - andre.aguas@ist.utl.pt

Rui Sá - 78324 - rui.sa@ist.utl.pt

João Neto - 78745 - joao.b.neto@ist.utl.pt


Repository:
[abaguas/UpaTransporter](https://github.com/abaguas/UpaTransporter)

-------------------------------------------------------------------------------
## Description

Development of a sistemy based in SOAP Web Services, implemented in the J2EE plarform, to aggregate services of good transporters, similar to Uber's service with passengers.


### Architecture


The main components of the system are clients, a broker, transporters and a name server (UDDI). The transporters publish themselves in the name server, which is used by the broker to find them and negotiate the services requested by the clients. 
Here is a simple figure of the system's architecture: 

<img src="https://github.com/abaguas/UpaTransporter/blob/master/images/architecture.png" width ="800" height="400">


The broker is the most complex component. It is divided in 4 modules:
* *broker-ws* that communicates with the client
* *transporter-ws-cli* that communicates with the transporters
* *ca-ws-cli* that communicates with the CA
* *broker-ws-cli* that communicates with the backup broker


The client is just a *broker-ws-cli* module and the transporter a *transporter-ws*.


In addition, the system is composed by other components that guarantee reliability and security:
* A backup broker to provide primary
* A CA to generate public key certificates used in digital signatures. These signatures guarantee the authenticity, fresheness and integrity of the communication between the broker and the transporters.


### Operation


All the operations performed by the 3 parties are described in a WSDL contract.


The basic rules of the protocol are the following:

1) The client requests a transport from an *origin* to a *destination* for a *maximum price*
2) The broker asks a *budget* to all the transporters
3) The broker assigns the task to the transporter which offered the lowest value


All the states of a service are described bellow:


<img src="https://github.com/abaguas/UpaTransporter/blob/master/images/states.png" width ="600" height="600">



There are some rules concerning max and min price as well as areas of operation of transporters according to odd or even id (for further details refer to [enunciado (portuguese only)](https://github.com/abaguas/UpaTransporter/blob/master/enunciado.pdf))

-------------------------------------------------------------------------------
## Dependencies

Refer to [INSTALL.md](https://github.com/abaguas/UpaTransporter/blob/master/INSTALL.md)

-------------------------------------------------------------------------------
## Installation instructions


### Environment

[0] Boot the OS

Linux or Mac OS X


[1] Initiate the support servers

JUDDI:
```
./juddi-3.3.2_tomcat-7.0.64_9090/bin/startup.sh
```


[2] Create temporary folder

```
mkdir temp
cd temp/
```


[3] Obtain source code of the project

```
git clone https://github.com/abaguas/UpaTransporter.git (via https) 
git clone git@github.com:abaguas/UpaTransporter.git (via ssh)
```



[4] Install auxiliary libraries and modules

```
cd uddi-naming
mvn clean install
```


### Certificate Authority

[1] Build and execute the **server**

```
cd ca-ws
mvn clean install
mvn exec:java
```

[2] Build the **client**

```
cd ca-ws-cli
mvn clean install
```


### Transporter

[1] Build and execute the **server**

```
cd transporter-ws
mvn clean install
mvn exec:java
```

and in other tab (the tests that will be run on the broker client require 2 transporters executing)

```
cd transporter-ws
mvn exec:java -Dws.i=2
```

[2] Build the **client**

```
cd transporter-ws-cli
mvn clean install
```


### Broker

[1] Build and execute the **server**

```
cd broker-ws
mvn clean install
mvn exec:java
```


[2] Build the client **client**

```
cd broker-ws-cli
mvn clean install
```

-------------------------------------------------------------------------------
## Running instructions

You can keep the 2 Transporters and the CA running.
Then launch the Broker:

```
cd broker-ws
mvn exec:java
```

And launch the backup Broker:

```
cd broker-ws
mvn exec:java -Dws.br=2
```

Follow the instructions on both terminals (i.e. on the primal Broker press "enter" and, after, on the backup Broker press "enter")


Now launch the Client:
```
cd broker-ws-cli
mvn exec:java
```


You can (and should) kill the Broker's process to verify that the primary Backup is working correctly.


<br><br>


Disclaimer: It was not the aim of this project to provide a interface to the client, it only runs a small demonstration. The tests verify the correctness of all the operation implemented in the system.


PS: The prints are meant to better understand the operations that the system is performing.

-------------------------------------------------------------------------------
**END**
