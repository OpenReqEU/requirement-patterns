
# ***PABRE-WS***
This file contains all the things TODO in this project.

## Change fetching type from `Eager` to `Lazy`

Changing fetching from Eager to Lazy, improves the performance of the application, due to lack of overhead generated from fetching all the entities.


## Refactor dependencies of patternObject

Refactor dependencies between patternObjects. using the following example: <https://stackoverflow.com/questions/1831186/many-to-many-on-the-same-table-with-additional-columns>

## Create a .sql file creating schema

Currently PABRE is using the hibernate creation of schema from annotation, just to make more efficient and more stable it should make a .sql file, with all the creation statments.

## Remove Rawtype usages and Unchecked conversition

Inside the controllers and mediators, we have a `@SuppressWarnings({ "rawtypes", "unchecked" })`, the code should be improved so with don't need to supress warnings

## Pass the Postman test to JUnit

Currently when we have to execute the tests we have to open Postman tool, in the future we could execute the postman test inside the code using library like this: <https://github.com/poynt/postman-runner>

## Create versioning system

Search a good way to versioning, right now we don't have any rules about how to version, so it should be maked. 

## Configure C3P0

Currently the project already has c3p0 (managing pool connections to the database), but is not tested and checked if is working correctly or not.

## Improve search new ID to assign

Right now the project to check if a ID is assigned or is available do a `Select * from Object`, so access to many tables, in future it should improve the performance searching a new way to check if a ID is already used or not

## Check pabre-ws.jar errors

Right now when we execute the `pabre-ws.jar` a exeption are printed, but they are not afecting to the functionality because we don't have tomcat users, but it should be checked, The message:

	```
		GRAVE: Exception looking up UserDatabase under key UserDatabasejava.lang.NullPointerException
        at org.apache.catalina.realm.UserDatabaseRealm.startInternal(UserDatabaseRealm.java:255)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.realm.CombinedRealm.startInternal(CombinedRealm.java:201)
        at org.apache.catalina.realm.LockOutRealm.startInternal(LockOutRealm.java:120)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:1109)
        at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:302)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.StandardService.startInternal(StandardService.java:443)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:732)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.startup.Catalina.start(Catalina.java:684)
        at org.apache.tomcat.maven.runner.Tomcat7Runner.run(Tomcat7Runner.java:242)
        at org.apache.tomcat.maven.runner.Tomcat7RunnerCli.main(Tomcat7RunnerCli.java:204)
		nov 30, 2018 3:30:28 PM org.apache.catalina.realm.CombinedRealm startInternal
		GRAVE: Failed to start "org.apache.catalina.realm.UserDatabaseRealm/1.0" realmorg.apache.catalina.LifecycleException: Failed to start component [Realm[UserDatabaseRealm]]
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:154)
        at org.apache.catalina.realm.CombinedRealm.startInternal(CombinedRealm.java:201)
        at org.apache.catalina.realm.LockOutRealm.startInternal(LockOutRealm.java:120)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.ContainerBase.startInternal(ContainerBase.java:1109)
        at org.apache.catalina.core.StandardEngine.startInternal(StandardEngine.java:302)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.StandardService.startInternal(StandardService.java:443)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.core.StandardServer.startInternal(StandardServer.java:732)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        at org.apache.catalina.startup.Catalina.start(Catalina.java:684)
        at org.apache.tomcat.maven.runner.Tomcat7Runner.run(Tomcat7Runner.java:242)
        at org.apache.tomcat.maven.runner.Tomcat7RunnerCli.main(Tomcat7RunnerCli.java:204)
		Caused by: org.apache.catalina.LifecycleException: No UserDatabase component found under key UserDatabase
        at org.apache.catalina.realm.UserDatabaseRealm.startInternal(UserDatabaseRealm.java:264)
        at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:150)
        ... 13 more
	```
