package com.topflight.roomrecommendation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topflight.roomrecommendation.model.Room;
import org.apache.logging.log4j.LogManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.File;
import java.util.List;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class RoomrecommendationApplication implements CommandLineRunner {
	public static List<Room> room;
	private static final Logger logger = LogManager.getLogger(RoomrecommendationApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(RoomrecommendationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//logger.info("Loading the JSON Data using the command line runner interface");
		ObjectMapper objectMapper = new ObjectMapper();

		//read json file and map it to room object
		room = objectMapper.readValue(new File("RoomsData.json"), new TypeReference<>() {
		});
	}
}
