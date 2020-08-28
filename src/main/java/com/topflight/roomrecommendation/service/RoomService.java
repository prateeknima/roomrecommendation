package com.topflight.roomrecommendation.service;


import com.topflight.roomrecommendation.model.RoomCombination;

import java.util.ArrayList;

public interface RoomService {

    ArrayList<RoomCombination> getRecommendation(int personCount);
}
