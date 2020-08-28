package com.topflight.roomrecommendation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;


/*
* This is POJO class
* Lombok dependency has been used to reduce the boiler plate code
* */
@Getter
@Setter
@NoArgsConstructor
public class RoomCombination {

    int cost;
    ArrayList<String> distinctCombination;

}
