Steps to build and run:
----------------------------

Prerequisite installations:
-------------------------------
Install jre 1.8.0

Install mysql 8.0

MySql DB is assumed to be installed and running on localhost. Else, update the db host name in application.properties

Run the DDLs (contents of Flag-DDLs.sql) in sequence against the mysql DB as a root user or as an user with privileges to create DB and users:

src/main/resources/Flag-DDLs.sql

This creates DB, tables, user and grants permissions to the user

Run the application:
------------------------- 
1. From the root folder boot-country-flags run:

	./gradlew clean build
	
	java -Xms1024m -Xmx1024m  -jar build/libs/boot-country-flags-0.1.0.jar 

2. Swagger:
------------
	Access swagger ui at : http://localhost:8080/swagger-ui.html
	Access api-docs swagger at : http://localhost:8080/v2/api-docs

3. Dataload: 
------------
	Load the continents json to mysql table. This step is required before retrieving.
	
	POST http://localhost:8080/triggerETL
	body: {"fullRefresh":true}

	curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/triggerETL --data '{"fullRefresh":true}'

	triggerETL will load the contents of the json into MYSQL flags DB.

4. Architectural consideration: 
---------------------------------

Dataload should be trigerred by a separate process. Ideally, a well orchestrated ETL job pipeline should be created to load large amounts of data. This will help to refresh/replace the data completely or append it periodically in batch or real time.
For simplicity /triggerETL endpoint is provided.  

REST API end points: 
-------------------------------
(curl command also provided)

5. /flag - Retrieve all flags :

	GET http://localhost:8080/flag
	
	curl -X GET -i http://localhost:8080/flag

6. /fag/country/<countryname> - To retrieve a country flag.

	GET http://localhost:8080/flag/country/Fiji
	
	curl -X GET -i http://localhost:8080/flag/country/Fiji

7. /flag/continent/<continentname> - To retrieve flags of all countries in a continent

	GET http://localhost:8080/flag/continent/Africa
	
	curl -X GET -i http://localhost:8080/flag/continent/Africa

----------

8. Health check endpoint: 

	GET http://localhost:8080/actuator/health

9. Performance:
---------------------
	metrics endpoint :
	
	http://localhost:8080/actuator/metrics
	
	curl -X GET -i http://localhost:8080/actuator/metrics

10. Number of times country flags are accessed(use path parameter flag.search.country.<countryname>):
	
	http://localhost:8080/actuator/metrics/flag.search.country.Fiji
	
	curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.search.country.Fiji'
	
11. Number of times continent flags are accessed (use path parameter flag.search.continent.<continentname>
	
	http://localhost:8080/actuator/metrics/flag.search.continent.Africa
	
	curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.search.continent.Africa'

12. Timer statistics for accessing get country api

	http://localhost:8080/actuator/metrics/flag.country.timer
	
	curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.country.timer'

13. Timer statistics for accessing get continent api

	http://localhost:8080/actuator/metrics/flag.continent.timer
	
	curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.continent.timer'

14. jvm metrics available at http://localhost:8080/actuator/metrics/jvm...

	Example: curl -X GET -i 'http://localhost:8080/actuator/metrics/jvm.memory.committed'

