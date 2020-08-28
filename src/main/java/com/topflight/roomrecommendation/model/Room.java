package com.topflight.roomrecommendation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/*
 * This is POJO class
 * Lombok dependency has been used to reduce the boiler plate code
 * */
@Getter
@Setter
@NoArgsConstructor
public class Room {
    //Using the Lombok dependency to reduce the boiler plate code
    private int reference;
    private String roomType;
    private int minGuests;
    private int maxGuests;
    private int price;
    private String priceModel;
    @JsonIgnore
    private String startDate;
    @JsonIgnore
    private String endDate;
}
