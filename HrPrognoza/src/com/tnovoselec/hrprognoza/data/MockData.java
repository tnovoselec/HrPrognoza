package com.tnovoselec.hrprognoza.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomislav on 19/11/13.
 */
public class MockData {

    public static List<String> ICONS = new ArrayList<String>();

    static {
        ICONS.add("10d");
        ICONS.add("11d");
        ICONS.add("13d");
        ICONS.add("01d");
        ICONS.add("02d");
        ICONS.add("03d");
        ICONS.add("04d");
        ICONS.add("09d");
        ICONS.add("50d");
    }

    public static String getIcon(int i){
        return ICONS.get(i%ICONS.size());
    }

}
