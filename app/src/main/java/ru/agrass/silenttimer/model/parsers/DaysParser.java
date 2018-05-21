package ru.agrass.silenttimer.model.parsers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class DaysParser {

    public static final String SEPARATOR = ";";
    private static final Type type = new TypeToken<Map<String, Boolean>>(){}.getType();

    //Rule like in Open Street Map "Mo;Sa;Su"
    public static String[] parseDays(String days) {
        if (days == null) {
            return new String[7];
        }
        return days.split(SEPARATOR);
    }

    public static Map<String, Boolean> parseDayss(String gson) {
        return new Gson().fromJson(gson, type);
    }

    public static String daysToStr(Map<String, Boolean> map) {
        return new Gson().toJson(map);
    }

}
