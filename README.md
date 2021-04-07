# CLI Test Automation v1




CLI Test Automation v1 is a Selenium based Data Driven Test Autmation framework built in Java & designed to perform automated tests on Contentstack CLI.



# Features

  - Easy code management
  - Performs Automated testing of Contentstack plugins
  - Validates actual output against expected parameters by accessing generated logs
  - Generates interactive publishable HTML Test execution reports after  execution for realtime analytics 
  - OS independent
  - Uses external Excel (.xlsx) file as Test data source
  

You can also:
  - Control which github branch to use for automated testing
  - Control which plugins to consider for Testing cycle
  - Control how many Test cases to execute in one cycle


### Tech

This framework uses below tools :

* [Java] - core Java concepts
* [TestNG] - ability to write powerful test cases with the help of annotations, grouping and parametrizing which makes framework robust
* [Maven] - used to define project structure, dependencies, build, and test management
* [Apache POI] - set of library files that gives an API to manipulate Microsoft documents like . xls and . xlsx
* [Extent Reports] - Beautifully crafted reports and realtime analytics so you can look at your tests in a totally different way


### Installation

This framework requires [Java](https://www.oracle.com/java/technologies/oracle-java-archive-downloads.html) jdk 1.8+ to run.

 - Clone the framework
 - Add MAVEN_HOME env variable  (https://www.baeldung.com/install-maven-on-windows-linux-mac)

After completing above steps simply open ur cmd or terminal and run below command :
```sh
mvn test
```





[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [Java]: <https://www.oracle.com/java/technologies/oracle-java-archive-downloads.html>
   [TestNG]: <https://testng.org/doc/>
   [Maven]: <https://maven.apache.org/>
   [Apache POI]: <https://poi.apache.org/>
   [Extent Reports]: <http://www.extentreports.com/>
   

