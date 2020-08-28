package com.topflight.roomrecommendation.controller;

import com.topflight.roomrecommendation.model.RoomCombination;
import com.topflight.roomrecommendation.service.RoomService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/roomservice")
public class RoomResource {
    private static final Logger logger = LogManager.getLogger(RoomResource.class);
    @Autowired
    RoomService roomService;

    /*
    *
    * The method accepts the GetMapping requests and further calls the appropriate service class to get the results
    * @Param personCount
    * @return ResponseEntity<ArrayList<RoomCombination>>
    * */
    @GetMapping("/getRecommendedRooms/{personCount}")
    public ResponseEntity<ArrayList<RoomCombination>> getRecommendedRooms(@PathVariable int personCount) throws ExecutionException, InterruptedException {
        //logger.info("Get Successful");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Callable<ArrayList<RoomCombination>> callableTask = ()->{
                return roomService.getRecommendation(personCount);
        };
        Future<ArrayList<RoomCombination>> future;
        ArrayList<RoomCombination> list;
        synchronized (this) {
            future = executorService.submit(callableTask);
            list = future.get();

            //for (RoomCombination rc: list)
            //    System.out.println(rc.getDistinctCombination());
            //executorService.shutdown();
            return ResponseEntity.ok().body(list);
        }
        //return ResponseEntity.ok().body(roomService.getRecommendation(personCount));
    }
}
