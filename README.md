# olacarro-proj

### Instructions

- Download the repository and unzip it
- `cd olacarro-proj`
  
##### Build and execute jar & Launch mongo

- Run the following commands:
  - `sudo service mongodb start` to start mongo (make sure port 27017 is free)
  - `mvn clean package` to generate the jar inside `target` folder (Optional as the jar is included)
  - `java -jar target/olacarro-0.0.1-SNAPSHOT.jar`
  - **OR _(same result)_**
  - `sh build_jar.sh`
  
##### Deploying docker setup (jar included, no need to build it)
- Run the following commands:
  - `docker-compose build`
  - `docker-compose up`
  - **OR _(same result)_**
  - `sh deploy_docker.sh`
  
- *Notes:*
  - Port `8080` must be free as it is the one exposed (but can be changed on docker-compose file)
  - To have mongodb available on port `27017` for debug uncomment lines 8 and 9 of docker-compose file
  
### Implementation Details

- **ORM** -> Spring Boot v2.1.5
  - Chosen because it is one of the frameworks used bt heycar and as I don't have a lot of experience with it, seemed like a good opportunity to use it.
- **database** -> mongodb v3.6.3
  - Given the scope of the project, a non-relational database was chosen on a relational database, since the latter would introduce unnecessary complexity;
  - Chosen because it is supported by Spring and I am familiar with it.
  
### Problems discovered1
- Getting used to and working with spring 
- Implementation differences in releases of Spring

### Executed tests
  - Endpoints tested only manually so far with Postman and Robo3T.
  - **TODO** implement automated tests

### Ideas to implement
- Improve exception handling
- Create some extra classes, like:
  - **Car** - could group and better organize data inside Listing;
  - **Dealer** - not implemented as it would only have one attribute (dealer_id);
- Simple interface, not implemented as it would be time-consuming and outside of the scope of the assignment.
- Use two OCI-Containers through Docker
  - One for running the jar with the API;
  - And one running mongodb

### Decisions taken and questions that lead to them
1) *Should Dealer be a class?*
  - No, given that it would only have one attribute and would increase the complexity of the solution.

2) *Should Provider be a class?*
  - No, because as I understood it is only introduced to explain in what way the solution would be used. Could be implemented as a class with a group of Listings associated but in that case, it could interact directly with the db without needing the endpoints.

3) *How to get the dealer_id `/vehicle_listings` endpoint?*
  - As I couldn't understand how the dealer_id would be passed in this endpoint I made it initialized to "" (empty String). Some scenarios that I thought of are:
    - initialization as empty string or null;
    - be in the beginggin of the JSON, before the list with the listings;
    - be a session_id;
    - be a path variable like in `upload_csv` endpoint **(IMPLEMENTATION USED)**.
 
 4) *What should be the key of Listing?*
  - Decided to join dealer_id and code in a String and use it as the key.
