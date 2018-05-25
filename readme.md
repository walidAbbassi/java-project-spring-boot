## Using MySQL in Spring Boot via Spring Data JPA , thymeleaf and Hibernate

See here for more informations:

- Spring Boot
- Spring Security
- JPA (Hibernate)
- Spring Data JPA
- MySQL
- Spring MVC
- Thymeleaf
- Bootstrap
- Java 8
- SSL certificate

### Build and run

#### Configurations

Open the `application.properties` file and set your own configurations.

#### Prerequisites

- Java 8
- Maven > 3.5.3\

#### Important Maven Commands
mvn –version -> Find the maven version
mvn compile -> compiles source files
mvn test-compile -> compiles test files - one thing to observe is this also compiles source files
mvn clean -> deletes target directory
mvn test -> run unit tests
mvn package -> creates a jar for the project
help:effective-settings -> Debug Maven Settings
help:effective-pom -> Look at the complete pom after all inheritances from parent poms are resolved
dependency:tree -> Look at all the dependencies and transitive dependencies
dependency:sources -> Download source code for all dependencies
–debug -> Debug flag. Can be used with all the above commands
mvn clean
mvn eclipse:clean
mvn eclipse:eclipse
mvn dependency:resolve -U
mvn spring-boot:run
mvn clean spring-boot:run

#### From terminal

Go on the project's root folder, then type:

    $ mvn clean spring-boot:run

#### From Eclipse (Spring Tool Suite)

Import as *Existing Maven Project* and run it as *Spring Boot App*.


### Usage

- Run the application and go on http://localhost:8080/
- with the database:
    * `create database with name 'db_scolarite_mvc'.
    
### Generate a self-signed SSL certificate

- Open your Terminal prompt and write the following command to create a JKS keystore:

`keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -keystore keystore.jks -validity 3650`

- If you want to create a PKCS12 keystore, and you should, the command is the following:

`keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650`

You then will be asked to enter a password for the keystore. It must have at least 6 characters.

- If you’re using a self-signed SSL certificate, your browser won’t trust your application and will warn the user that it’s not secure. And that’ll be the same no matter the client.

`keytool -export -keystore keystore.jks -alias tomcat -file walidCertificate.crt`

- To make a client trust your application by providing it with your certificate. Since you stored your certificate inside the keystore, you need to extract it. Again, keytool supports us very well: 
running the command prompt in administrator mode

`cd C:\Program Files\Java\jdk1.8.0_171\jre\lib\security`

`keytool -importcert -file yourpath/walidCertificate.crt -alias tomcat -keystore cacerts`
