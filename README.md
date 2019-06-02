# olacarro-proj

### Instructions

- Download the repository and unzip it
- Run the following commands:
  - `cd olacarro-proj`
  - `mvn clean package` to generate the jar inside `target` folder (Optional as the jar is included)
  - `sudo service mongodb start` to start mongo (make sure port 27017 is free)
  - `java -jar target/olacarro-0.0.1-SNAPSHOT.jar`
  
### Implementation Details

- **ORM** -> Spring Boot v2.1.5
  - Chosen because is one of the frameworks used bt heycar and I don't have a big experience with it so seemed like a good opportunity.
- **database** - mongodb v3.6.3
  - Chosen because it is supported by Spring and I am familiar with it.
  
### Problems discovered
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
    - be in the of the JSON, before the list with the listings;
    - be a session_id;
    - be a path variable just like in `upload_csv` endpoint.
