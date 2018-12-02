Steps to build and run:
----------------------------

Prerequisite installations:
-------------------------------
Install jre 1.8.0
Install mysql 8.0

mysql DB is assumed to be installed and running on localhost. Else, update the db host name in application.properties

Run the DDLs in sequence against the mysql DB with as a root user or user with privileges to create DB and users:
src/main/resources/Flag-DDLs.sql

This creates DB, tables, user and grants permissions to the user

Run the application:
------------------------- 
From the root folder boot-country-flags run:
./gradlew clean build
java -Xms1024m -Xmx1024m  -jar build/libs/boot-country-flags-0.1.0.jar 

Access swagger ui at : http://localhost:8080/swagger-ui.html
Access api-docs swagger at : http://localhost:8080/v2/api-docs

1. To load the data. That is, to load the continents json to mysql table
POST http://localhost:8080/triggerETL
body: {"fullRefresh":true}

curl -X POST -H 'Content-Type: application/json' -i http://localhost:8080/triggerETL --data '{"fullRefresh":true}'

triggerETL will load the contents of the json into MYSQL flags DB.

Architectural consideration: Dataload should be trigerred by a separate process ideally a well orchestrated ETL job pipeline to load large amounts of data. This will help to refresh/replace the data completely or append it periodically in batch or real time.
For simplicity /triggerETL endpoint is provided.  

2. To retrieve flags of all countries of all continents:

http://localhost:8080/flag
curl -X GET -i http://localhost:8080/flag

3. To retrieve flag of any country, REST GET call with country name.
http://localhost:8080/flag/country/Fiji
curl -X GET -i http://localhost:8080/flag/country/Fiji

4. To retrieve flag of all countries in a continent, REST GET call with continent name
http://localhost:8080/flag/continent/Africa
curl -X GET -i http://localhost:8080/flag/continent/Africa

----------

Access health at : http://localhost:8080/actuator/health

Performance metrics at:
http://localhost:8080/actuator/metrics
curl -X GET -i http://localhost:8080/actuator/metrics

Number of times country flags are accessed(use path parameter flag.search.country.<countryname>):
http://localhost:8080/actuator/metrics/flag.search.country.Fiji
curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.search.country.Fiji'
	
Number of times continent flags are accessed (use path parameter flag.search.continent.<continentname>
http://localhost:8080/actuator/metrics/flag.search.continent.Africa
curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.search.continent.Africa'

Timer statistics for accessing get country api
http://localhost:8080/actuator/metrics/flag.country.timer
curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.country.timer'

Timer statistics for accessing get continent api
http://localhost:8080/actuator/metrics/flag.continent.timer
curl -X GET -i 'http://localhost:8080/actuator/metrics/flag.continent.timer'

jvm metrics available at http://localhost:8080/actuator/metrics/jvm...
Example: curl -X GET -i 'http://localhost:8080/actuator/metrics/jvm.memory.committed'

Architectural Considerations TBD:
---------------------------------
1. Add authentication/authorization to the APIs
2. Front end the application instance with a load balancer like nginx for scaling.
3. Add throttling rules to prevent DOS
4. Add frequently accessed entries to a cache like Redis, with expiry. Fetch from DB only on a cache miss.
5. Push the metrics to external time series metrics DB like prometheus or graphite.
6. Push the logging to a centralized log cluster like splunk or ELK.
7. Consider a nosql MPP DB like Impala for aggregations, if data size increases tremendously. The countries and flags usecase does not warrant a big data infrastructure.

Code Review Comments - Self Review:
--------------------------------------

1. Handle different response types. Customize the API to return appropriate HTTP codes.
2. More unit tests needed and needs more coverage. Even though more functional tests are required in this application since the search service is only a CRUD wrapper.
3. Customize DB connection pooling. Use persistence api if data model grows.
4. More edge cases handling and exception handling required.

Review Comments from Self Review:
-------------------------------------

1. 

