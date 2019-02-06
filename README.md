
# **PABRE-WS**
This service was created as a result of the OpenReq project funded by the European Union Horizon 2020 Research and Innovation programme under grant agreement No. 732463.

Pabre Swagger file can be found at: [OpenReq Server](http://217.172.12.199:9408/pabre-ws/doc/)

The following technologies are used:

* Jersey REST framework (&rarr; [https://jersey.github.io/](https://jersey.github.io/) )
* Hibernate (&rarr; [http://hibernate.org/](http://hibernate.org/) )
* Derby embedded database (&rarr; [http://db.apache.org/derby/](https://db.apache.org/derby/papers/DerbyTut/embedded_intro.html) )
* Swagger (&rarr; [https://swagger.io/](https://swagger.io/) )
* Maven (&rarr; [https://maven.apache.org/](https://maven.apache.org/) )
* Apache Tomcat (&rarr; [http://tomcat.apache.org/](http://tomcat.apache.org/))
* DKPro (&rarr; [https://dkpro.github.io/](https://dkpro.github.io/) )
* Postman (&rarr; [https://www.getpostman.com/](https://www.getpostman.com/))


## Table of contents

* [Functionalities of the Web service](#functionalities-of-the-web-service)
* [Installing with Eclipse](#installing-with-eclipse)
* [Build the project with Maven](#build-the-project-with-maven)
* [Build the project with Maven plugin in Eclipse](#build-the-project-with-maven-plugin-in-eclipse)
* [Running the project with `pabre-ws.war`](README.md#running-the-project-with-pabre-ws.war)
* [Running the project with `pabre-ws.jar`](README.md#running-the-project-with-pabre-ws.jar)
* [Developers](#developers)
* [Switching database](#switching-database)

requirement-patterns
## Functionalities of the Web service

The services offers the following features:

* REST service to manage requirement patterns
* REST service to manage complete catalogue with many schemas having they own patterns
* Store all the information in the embedded database



## Installing with Eclipse

 1. Get the source code:

	 * Download the zip or clone the project with git:

        ```
	        https://mast-tuleap.informatik.uni-hamburg.de/plugins/git/openreq/upc/requirement-patterns.git
        ```

2.	Run Eclipse

3.	Goto File > Import > Maven > Existing Maven Projects

4.	Go to the folder where the project is located and press Finish

5.	To ensure that the libraries are downloaded: right click on the proyect > Maven > Update Project


## Build the project with Maven

1. Open command line in pabre-ws

2. Build the project:

	* Generate only `pabre-ws.war` with:
		```
		mvn clean install
		```

	* Generate `pabre-ws.war` and `pabre-ws.jar` (with embedded Tomcat7):
		```
		mvn clean install tomcat7:exec-war-only
		```

## Build the project with Maven plugin in Eclipse

1. Open the project

2.	Go to Run configuration:

	```
	Right click on project > Run As > Maven build...
	```

3. Set the configuration:
	Name: `pabre-ws derbyEm`
	Goals : `clean install tomcat7:exec-war-only`

4. Press `Run`

5. You can obtain the `pabre-ws.war` and `pabre-ws.jar` inside the `/target` folder


## Running the project with `pabre-ws.war`
> **Important**: by default pabre-ws uses a embedded derby database, derby uses the directory where it is been executed .
For example: running the `startup.sh` is been executed in `apache-tomcat-7.0.90/bin`, so the Derby database will be create a database with the route `apache-tomcat-7.0.90/bin/pabreDatabase` , if the user dosen't have permission to write, it will be created in the system temporary files, console will show you the used route

There are 2 ways to run with `pabre.ws`

* **Modifying the `pabre-ws.war` before deploy**

	1. Obtain the `pabre-ws.war`
	2.	Open the `pabre-ws.war` as a .zip/.rar folder
	3.	Open the `hibernate.cfg.xml` located in `WEB-INF/classes/config`
	4.	Modify the file with the follow items:
		* `hibernate.connection.username`: Username of the database  (Optional).
		* `hibernate.connection.password`: Password of the database  (Optional).
		* `connection.directory`: The directory where the database should be created or is located. By default: directory where the tomcat is been executed. (cannot end in `/`)
		* `connection.database.name`: Database name, by default is pabreDatabase (Optional).
	5. Save and accept the war update
	6.	Go to your own `Tomcat` folder
	7.	Copy the war inside  `webapps` folder
	8.	Start the `Tomcat`

* **Modifying after deploy**

	1.	Obtain the `pabre-ws.war`
	2.	Copy the `pabre-ws.war` into the Tomcat webapps folder
	3.	When the war is deployed (folder created), Stop the `Tomcat`
	4.	Go to `pabre-ws/WEB-INF/classes/config`
	5.	Open the `hibernate.cfg.xml` located in `WEB-INF/classes/config`
	6.	Modify the file with the follow items:
		* `hibernate.connection.username`: Username of the database  (Optional).
		* `hibernate.connection.password`: Password of the database  (Optional).
		* `connection.directory`: The directory where the database should be created or is located. By default: directory where the tomcat is been executed. (cannot end in `/`)
		* `connection.database.name`: Database name, by default is pabreDatabase (Optional).
	7. Save the file
	8.	Start the `Tomcat`

## Running the project with `pabre-ws.jar`

> **Important**: by default pabre-ws uses a embedded derby database, derby uses the directory where it is been executed . In this case, when you execute `pabre-ws.jar` in the same directory it will be created a folder with the name `pabreDatabase`.
In case that the user don't have permission to write in that folder, the database will be created in the system temp folder, The console will show you which route has been used.

> **Important**: The default port used by the Embedded tomcat is 9408.

1. Obtain the `pabre-ws.jar`
2. Execute: ``` java -jar pabre-ws.jar ```
3. Embedded Tomcat7 will be executed and you can now access  `http://localhost:9408/pabre-ws/api/`

## Developers

* **Database**:While developing is recomended to use the Client/Server derby configuration with the profile `derby`, otherwise embedded derby take some time while is shutting down. To change the profile of execution:

    1. Right click on the project
    2. Click on `Properties`
    3. Select `Maven`
    4. Write `derby` inside active maven profile


* **Tests**: To test all the WS Postman is being used.

    * The tests location: inside the `src/main/resources/tests` are 2 files:
       * **PABRE-WS.postman_collection.json**: This file there are all the tests to be executed by Postman
       * **PABRE-WS.postman_environment.json**: This file contains all the environment needed to execute the tests (the variable **baseUrl** should be updated with the server URL where service is being executed to test it)
    * How to execute the tests:
        1. Install the command Newman
        2. Go to the project root directory
        3. Execute the command: `newman run pabre-ws/src/main/resources/tests/PABRE-WS.postman_collection.json -e pabre-ws/src/main/resources/tests/PABRE-WS.postman_environment.json`




## Switching database
 Pabre use `Hibernate` library, so it can switch between different databases.
 In `hibernate.cfg.xml` we can write the configuration. Some examples of diferrents configurations:

### Embedded Derby (Default) :
``` xml
<!-- Database connection settings -->
<property name="hibernate.connection.driver_class">org.apache.derby.jdbc.EmbeddedDriver</property>
<property name="hibernate.connection.url"></property>
<property name="hibernate.connection.username"></property>
<property name="hibernate.connection.password"></property>
<property name="hibernate.connection.autocommit">false</property>

<!-- Pabre connection configuration -->
<property name="connection.directory"></property> <!-- Directory where to save the embedded database -->
<property name="connection.database.name">pabreTesting</property> <!-- Database name -->

<!-- SQL dialect -->
<property name="hibernate.dialect">org.hibernate.dialect.DerbyDialect</property>
```

### Client/Server Derby :

``` xml
<!-- Database connection settings -->
<property name="hibernate.connection.driver_class">org.apache.derby.jdbc.ClientDriver</property>
<property name="hibernate.connection.url">jdbc:derby://{URL}:{PORT}/{DATABASE NAME};create=true</property>
<property name="hibernate.connection.username">{USERNAME}</property>
<property name="hibernate.connection.password">{PASSWORD}</property>
<property name="hibernate.connection.autocommit">false</property>

<!-- Pabre connection configuration -->
<property name="connection.directory"></property> <!-- Directory where to save the embedded database -->
<property name="connection.database.name">pabreTesting</property> <!-- Database name -->

<!-- SQL dialect -->
<property name="hibernate.dialect">org.hibernate.dialect.DerbyDialect</property>
```

### MySQL:

``` xml
<property name="hibernate.connection.driver_class">com.mysql.jdbc.Driver</property>
<property name="hibernate.connection.url">jdbc:mysql://{URL}:{PORT}/{DATABASE NAME}</property>
<property name="hibernate.connection.username">{USERNAME}</property>
<property name="hibernate.connection.password">{PASSWORD}</property>
<property name="hibernate.connection.autocommit">false</property>

<!-- Pabre connection configuration -->
<property name="connection.directory"></property> <!-- Directory where to save the embedded database -->
<property name="connection.database.name">pabreTesting</property> <!-- Database name -->


<!-- SQL dialect -->
<property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
```

### PostgreSQL:

``` xml
<property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
<property name="hibernate.connection.url">jdbc:postgresql://{URL}:{PORT}/{Database name}</property>
<property name="hibernate.connection.username">{USERNAME}</property>
<property name="hibernate.connection.password">{PASSWORD}</property>
<property name="hibernate.connection.autocommit">false</property>

<!-- Pabre connection configuration -->
<property name="connection.directory"></property> <!-- Directory where to save the embedded database -->
<property name="connection.database.name">pabreTesting</property> <!-- Database name -->

<!-- SQL dialect -->
<property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>

```

### Sources

- https://www.upc.edu/gessi/PABRE/index.html
- https://re-magazine.ireb.org/articles/requirements-reuse
- https://www.iso.org/standard/22749.html

## How to contribute

See OpenReq project contribution [guidelines](https://github.com/OpenReqEU/OpenReq/blob/master/CONTRIBUTING.md)

## License

Free use of this software is granted under the terms of the [EPL version 2 (EPL2.0)](https://www.eclipse.org/legal/epl-2.0/)
