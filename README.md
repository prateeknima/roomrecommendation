The project helps us to get the best cheapest room combinations to the users based on the input value which is the number of persons for which the rooms need to be recommended. 
The code was Unit tested and also passed the quality check on the SonarQube application. 
The application uses Java 11 and runs on the server port 8081. 

The Core Implementation Algorithm:

1)	The RoomServiceImpl.java service class helps us to get the desired results. Recursion has been used to achieve the same.
2)	Repeated recursion calls are stored in the form of cache memory which greatly helps to reduce the number of recursion calls made. 
3) Storing the value helps to retrieve the value quickly without repeating the steps again and again. 
4) Initially without storing the values for the input parameter 20 the number of calls went near around 73000 calls while by storing the values, for the input parameter 50, the recursion count stayed around 600, thereby reducing the time complexity of the code immensely.


Docker:-
The application has also been deployed to the docker hub and can be pulled using docker pull 72221/roomrecommendation-docker.jar:latest

Threshold Values:-
- The minimum input value has been set to 1 and the max value has been set to 40.
- The code returns an empty output for the cases below and above the threshold limits, and are logged on the console with the message “ Input not valid. Returning. ”
- The application is thread-safe and has been tested on JMeter application with 2000 concurrent users. The error rate for the thread safe application was 0.0% while the error rate was 4.25% for non thread-safe application.

The files and its descriptions in each of the packages.
1)	Package controller:

RoomResource.java: 
It handles the mapping of the requests which we receive from an external application. The following are the methods

-	 Method Name: getRecommendedRooms()
Type of Mapping: GetMapping
Input: It takes person count as an input in its argument.
Return type: The return type is ReponseEntity of type ArrayList<RoomCombination>  



2)	Package service:

Interface RoomService.java:
This is an interface which includes the methods implemented by the RoomServiceImpl.java class. Declaring the methods in interface helps us to utilize the Object Oriented principles.

-	 Method Name: getRecommendation()	
Input: It takes person count as an input in its argumet.
Return type: The return type is ReponseEntity of type ArrayList<RoomCombination>

Class RoomServiceImpl:
This class provides services to the RoomResource.class in the controller package and implements the methods declared in the RoomService.java Interface.

-	Method Name: getRecommendation()	
Input: It takes person count as an input in its argumet.
Return type: The return type is of type ArrayList<RoomCombination>

3)	Package model:
It consists of POJO(Plain Old Java Objects) which helps us to encapsulate the business logic. The following are the classes along with fields present in each one of them. Lombok dependency has been used in each class to reduce the boiler plate code. 
Note: No restrictions have been applied with respect to the input value each value can hold. 
Class Room.java:
    private int reference;
    private String roomType;
    private int minGuests;
    private int maxGuests;
    private int price;
    private String priceModel;
    private String startDate;
    private String endDate;


Class RoomCombination.java:
 	    int cost;
   	    ArrayList<String> roomCombination;

