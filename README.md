# Spring SOAP Web Client

SOAP is a messaging protocol. Messages (requests and responses) are XML documents over HTTP. The XML contract is defined by the WSDL (Web Services Description Language). It provides a set of rules to define the messages, bindings, operations, and location of the service.
There are two possible approaches when creating a web service: Contract-Last and Contract-First. When we use a contract-last approach, we start with the Java code, and generate the web service contract (WSDL) from the classes. When using contract-first, we start with the WSDL contract, from which we generate the Java classes.
Spring-WS only supports the contract-first development style. The aim of this project is to consume a soap web service with Spring Boot.

## Setup

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web-services</artifactId>
    </dependency>
</dependencies>
<build>
    <plugins>
        <plugin>
            <groupId>org.jvnet.jaxb2.maven2</groupId>
            <artifactId>maven-jaxb2-plugin</artifactId>
            <version>0.14.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <schemaLanguage>WSDL</schemaLanguage>
                <generatePackage>com.spring.training.model</generatePackage>
                <schemas>
                    <schema>
                        <url>http://localhost:8080/ws/countries.wsdl</url>
                    </schema>
                    <schema>
                        <url>http://localhost:8080/ws/persons.wsdl</url>
                    </schema>
                </schemas>
            </configuration>
        </plugin>
    </plugins>
</build>
```