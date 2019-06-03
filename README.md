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
  
### Problems discovered
- Getting used to and working with spring 
- Implementation differences in releases of Spring

### Executed tests (numbered the same way in OlacarroApplicationTests.java)
  1) Search on an empty repository returns an empty JSON array;
  2) Inserts one `Listing` in the database through `/vehicles_listing`
  3) Inserts many `Listing`s in the database through `/vehicles_listing`
  4) Inserts many `Listing`s in the database through `/upload_csv`
  5) Search for `Listing`s with `make` mercedes and `model` c 250;
  6) Search for `Listing`s with `color` black and `year` 1996;
  7) Search for all `Listing`s;
  8) Inserts `Listing`s with existing code for a certain dealer through `/vehicles_listing`.
  
  - Endpoints also tested manually with Postman and Robo3T.

### Ideas to implement
- Improve exception handling;
- Create documentation through Javadoc and code coverage with EclEmma;
- Implement search by price interval;
- Create some extra classes, like:
  - **Car** - could group and better organize data inside Listing;
  - **Dealer** - not implemented as it would only have one attribute (dealer_id);
- Simple interface, not implemented as it would be time-consuming and outside of the scope of the assignment;
- Improve integration tests of the docker setup.

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

5) *Should search return 404 or and empty JSON array?*
    - Decided to return a JSON array as it is the standard.

6) *Should the power in the csv file be converted from ps to kW?*
    - I implemented assuming the answer was yes and the conversion was made according to  https://www.unitconverters.net/power/pferdestarke-ps-to-kilowatt.htm

7) *How should I name the project?*
    - Why not the portuguese translation of `heycar`? :thinking:
