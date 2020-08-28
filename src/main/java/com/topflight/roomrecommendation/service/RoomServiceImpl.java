package com.topflight.roomrecommendation.service;

import com.topflight.roomrecommendation.RoomrecommendationApplication;
import com.topflight.roomrecommendation.model.Room;
import com.topflight.roomrecommendation.model.RoomCombination;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger logger = LogManager.getLogger(RoomServiceImpl.class);
    List<Room> rooms;
    Room[] distinctRooms;
    long noOfDistinctRooms;
    int totalPersonCount;
    ArrayList<String> tempArr;
    ArrayList<ArrayList<String>> roomCombinations;
    ArrayList<Integer> cost;
    long[] roomCount;
    ArrayList<RoomCombination> result;
    int maxCost;
    int countCalls;
    int[][] cacheCostData;
    ArrayList<String>[][] cacheRoomCombinationData;
    int desiredNoOfDistinctCombinations;
    int noOfRecursiveCalls;

    /*
     * Provides service to the getRecommendedRooms() get mapping method in the controller class
     *
     * @param personCount
     * @return <ArrayList<RoomCombination>
     *  */
    @Override
    public ArrayList<RoomCombination> getRecommendation(int personCount) {
        //logger.info("Call to getRecommendation method Successful");
        if (personCount <= 0) {
            logger.info("Input not valid. Returning.");
            return new ArrayList<>();
        }
        //logger.info("Initialising Values");
        initialiseValues(personCount);
        //logger.info("Analysing Distinct Room Combination: Recursion starts here");
        analyseRoomCombinations(totalPersonCount, 0, new ArrayList<>(), roomCount, 0, 0);
        for (int i = 0; i < roomCombinations.size(); i++) {
            RoomCombination data = new RoomCombination();
            data.setCost(cost.get(i));
            data.setDistinctCombination(roomCombinations.get(i));
            result.add(data);
        }
        //logger.info("Analysis Complete");
        /*System.out.println("MAtrix is ");
        for (int i = 0; i < cacheRoomCombinationData.length; i++){
            for (int j = 0; j < cacheRoomCombinationData[0].length;j++){
                System.out.print(" "+cacheRoomCombinationData[i][j]+" "+cacheCostData[i][j]);
            }
            System.out.println();
        }
         */
        return result;
    }

    /*
     * Called by getRecommendation() method and recursively analysis the Unique Cheapest combinations for the user
     * The core logic is to analyse each of the possible room combinations and store the cheapest ones
     *
     * @param remainingPersonCount
     * @param currentCost
     * @param currentRoomCombination
     * @param roomCount
     * @param currentRoomIndex
     * @param partialCost
     * @return RoomCombination
     * */
    private RoomCombination analyseRoomCombinations(int remainingPersonCount, int currentCost, ArrayList<String> currentRoomCombination, long[] roomCount, int currentRoomIndex, int partialCost) {
        noOfRecursiveCalls++;
        ArrayList<String> roomCombinationDataCopy = new ArrayList<>(currentRoomCombination);
        RoomCombination responseFromRecursionHierarchy = new RoomCombination();
        RoomCombination secondResponseFromRecursionHierarchy = new RoomCombination();
        secondResponseFromRecursionHierarchy.setCost(Integer.MAX_VALUE);
        int minValueInCurrentCall = Integer.MAX_VALUE;
        ArrayList<String> combinationSetForMinValue = new ArrayList<>();

        if (remainingPersonCount > 0 && cacheCostData[totalPersonCount - remainingPersonCount][currentRoomIndex] != Integer.MAX_VALUE) {
            currentCost += cacheCostData[totalPersonCount - remainingPersonCount][currentRoomIndex];
            partialCost += cacheCostData[totalPersonCount - remainingPersonCount][currentRoomIndex];
            for (String s : cacheRoomCombinationData[totalPersonCount - remainingPersonCount][currentRoomIndex]) {
                roomCombinationDataCopy.add(s);
                combinationSetForMinValue.add(s);
            }
            remainingPersonCount = 0;
        }

        if (remainingPersonCount <= 0) { //If the remaining person count is 0 or less than 0 move forward to check if it can be added to the final results
            combinationSetForMinValue.add(roomCombinationDataCopy.get(roomCombinationDataCopy.size() - 1));
            minValueInCurrentCall = partialCost;
            responseFromRecursionHierarchy.setDistinctCombination(combinationSetForMinValue);
            responseFromRecursionHierarchy.setCost(minValueInCurrentCall);

            if (roomCombinations.size() < desiredNoOfDistinctCombinations) { //If the result set size is less than desiredNoOfDistinctCombinations, add the currentRoomCombination to the list

                tempArr = new ArrayList<>(roomCombinationDataCopy);
                Collections.sort(tempArr);
                if (roomCombinations.contains(tempArr)) { //If the currentRoomCombination is already present in the list and update the price
                    int tempIndex = roomCombinations.indexOf(tempArr);
                    if (cost.get(tempIndex) > currentCost)
                        cost.set(tempIndex, currentCost);
                    maxCost = Collections.max(cost);
                    return responseFromRecursionHierarchy;
                }
                roomCombinations.add(tempArr);
                cost.add(currentCost);
                maxCost = Math.max(maxCost, currentCost);
                return responseFromRecursionHierarchy;
            } else { //If the current cost is greater than the max cost value present in the list then return else replace it with the existing max value list
                if (currentCost > maxCost)
                    return responseFromRecursionHierarchy;
                int indexOfMaxElement = cost.indexOf(maxCost);
                tempArr = new ArrayList<>(roomCombinationDataCopy);
                Collections.sort(tempArr);
                if (roomCombinations.contains(tempArr)) {
                    int tempIndex = roomCombinations.indexOf(tempArr);
                    if (cost.get(tempIndex) > currentCost)
                        cost.set(tempIndex, currentCost);
                    maxCost = Collections.max(cost);
                    return responseFromRecursionHierarchy;
                }
                roomCombinations.set(indexOfMaxElement, tempArr);
                cost.set(indexOfMaxElement, currentCost);
                maxCost = Collections.max(cost);
            }
            return responseFromRecursionHierarchy;
        }

        for (int i = 0; i < noOfDistinctRooms; i++) {
            if (roomCount[i] <= 0)
                continue;
            Room currentRoomType = distinctRooms[i];
            if (currentRoomType.getMinGuests() <= remainingPersonCount) {
                int minTempCost = 0;
                int maxTempCost = 0;
                if (currentRoomType.getPriceModel().equalsIgnoreCase("pp")) {
                    if (remainingPersonCount >= currentRoomType.getMaxGuests()) {
                        maxTempCost += currentRoomType.getMaxGuests() * currentRoomType.getPrice();
                        minTempCost += currentRoomType.getMinGuests() * currentRoomType.getPrice();
                    }
                    else {
                        maxTempCost += (remainingPersonCount) * (currentRoomType.getPrice());
                        minTempCost = maxTempCost;
                    }
                } else {
                    maxTempCost += currentRoomType.getPrice();
                    minTempCost = maxTempCost;
                }
                currentRoomCombination.add(currentRoomType.getRoomType());
                roomCount[i] -= 1;
                //Making the next recursive call
                responseFromRecursionHierarchy = analyseRoomCombinations(remainingPersonCount - currentRoomType.getMaxGuests(), maxTempCost + currentCost, currentRoomCombination, roomCount, i, maxTempCost);
                secondResponseFromRecursionHierarchy = analyseRoomCombinations(remainingPersonCount - currentRoomType.getMinGuests(), minTempCost + currentCost, currentRoomCombination, roomCount, i, minTempCost);
                if(secondResponseFromRecursionHierarchy.getCost()<responseFromRecursionHierarchy.getCost()) {
                    responseFromRecursionHierarchy = secondResponseFromRecursionHierarchy;
                }
                if (responseFromRecursionHierarchy.getCost() > 0 && responseFromRecursionHierarchy.getCost() < minValueInCurrentCall) { //Checking the cheapest combination received from the next recursive calls
                    minValueInCurrentCall = responseFromRecursionHierarchy.getCost();
                    combinationSetForMinValue = responseFromRecursionHierarchy.getDistinctCombination();
                }
                currentRoomCombination.remove(currentRoomCombination.size() - 1);
                roomCount[i] += 1;
            }
        }
        cacheRoomCombinationData[totalPersonCount - remainingPersonCount][currentRoomIndex] = new ArrayList<>(combinationSetForMinValue);
        cacheCostData[totalPersonCount - remainingPersonCount][currentRoomIndex] = minValueInCurrentCall;
        minValueInCurrentCall += partialCost;
        combinationSetForMinValue.add(distinctRooms[currentRoomIndex].getRoomType());
        responseFromRecursionHierarchy.setCost(minValueInCurrentCall);
        responseFromRecursionHierarchy.setDistinctCombination(combinationSetForMinValue);
        return responseFromRecursionHierarchy;
    }


    /*
     * Helps to initialise the values before we start the recursive calls
     *
     * @param personCount
     * */
    private void initialiseValues(int personCount) {
        //Set the number of distinct combinations as required
        desiredNoOfDistinctCombinations = 3;
        noOfRecursiveCalls = 0;
        roomCombinations = new ArrayList<>();
        maxCost = Integer.MIN_VALUE;
        countCalls = 0;
        rooms = RoomrecommendationApplication.room;
        totalPersonCount = personCount;
        cost = new ArrayList<>();

        result = new ArrayList<>();
        noOfDistinctRooms = rooms.stream().map(Room::getRoomType).distinct().count();
        roomCount = new long[]{
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("DOUBLE")).count(),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("TWIN")).count(),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("TWINTRIPLE")).count(),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("FAMILY")).count()
        };
        distinctRooms = new Room[]{
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("DOUBLE")).findAny().orElse(new Room()),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("TWIN")).findAny().orElse(new Room()),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("TWINTRIPLE")).findAny().orElse(new Room()),
                rooms.stream().filter(e -> e.getRoomType().equalsIgnoreCase("FAMILY")).findAny().orElse(new Room())
        };
        cacheCostData = new int[personCount][(int) noOfDistinctRooms];
        cacheRoomCombinationData = new ArrayList[personCount][(int) noOfDistinctRooms];
        for (int[] i : cacheCostData)
            Arrays.fill(i, Integer.MAX_VALUE);
    }
}
