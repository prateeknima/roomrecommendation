package com.topflight.roomrecommendation;

import com.topflight.roomrecommendation.model.RoomCombination;
import com.topflight.roomrecommendation.service.RoomService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;

@SpringBootTest
class RoomrecommendationApplicationTests {
	private ArrayList<RoomCombination> result;
	ArrayList<RoomCombination> roomCombinations;
	ArrayList<String> currentSet;
	RoomCombination roomCombinationObject;
	Boolean flag;
	@Autowired
	RoomService roomService;

	/* Test case for valid input value 7 */
	@Test
	void testForValidInputData() {
		roomCombinationObject = new RoomCombination();
		roomCombinations = new ArrayList<>();
		currentSet = new ArrayList<>() {{
			add("DOUBLE");
			add("TWINTRIPLE");
			add("TWINTRIPLE");
		}};
		roomCombinationObject.setCost(590);
		roomCombinationObject.setDistinctCombination(new ArrayList<>(currentSet));
		roomCombinations.add(roomCombinationObject);

		roomCombinationObject = new RoomCombination();
		currentSet = new ArrayList<>() {{
			add("TWIN");
			add("TWINTRIPLE");
			add("TWINTRIPLE");
		}};
		roomCombinationObject.setCost(515);
		roomCombinationObject.setDistinctCombination(new ArrayList<>(currentSet));
		roomCombinations.add(roomCombinationObject);

		roomCombinationObject = new RoomCombination();
		currentSet = new ArrayList<>() {{
			add("TWIN");
			add("TWIN");
			add("TWINTRIPLE");
		}};
		roomCombinationObject.setCost(590);
		roomCombinationObject.setDistinctCombination(new ArrayList<>(currentSet));
		roomCombinations.add(roomCombinationObject);
		flag = Boolean.TRUE;
		result = roomService.getRecommendation(7);
		flag = Boolean.TRUE;
		for (int i = 0; i < roomCombinations.size(); i++) {
			if (result.get(i).getDistinctCombination().toString().equals(roomCombinations.get(i).getDistinctCombination().toString()) && result.get(i).getCost() == roomCombinations.get(i).getCost())
				continue;
			flag = Boolean.FALSE;
		}
		Assert.assertEquals(Boolean.TRUE, flag);
	}

	@Test
	void testNullForInputZeroAndBelow() {
		Assert.assertEquals(new ArrayList<>().toString(),roomService.getRecommendation(0).toString());
	}


}