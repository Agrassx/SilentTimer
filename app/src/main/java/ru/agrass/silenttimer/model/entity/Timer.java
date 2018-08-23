package ru.agrass.silenttimer.model.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.Gson;

import java.util.Map;

import ru.agrass.silenttimer.model.parsers.DaysParser;


@Entity(tableName = "timers")
public class Timer {

    @Ignore
    private static final String INIT_VALUE_TIME_FROM = "08:00";

    @Ignore
    private static final String INIT_VALUE_TIME_TO = "20:00";

    @PrimaryKey(autoGenerate = true)
    private long uid;

    @ColumnInfo(name = "time_from")
    private String timeFrom;

    @ColumnInfo(name = "time_to")
    private String timeTo;

    @ColumnInfo(name = "is_enable")
    private boolean isEnable;

    @ColumnInfo(name = "days_str")
    private String daysStr;

    @Ignore
    private Map<String, Boolean> daysMap;

    @Ignore
    public static Timer generateTimer() {
        return new Timer(INIT_VALUE_TIME_FROM, INIT_VALUE_TIME_TO, false);
    }

    @Ignore
    public Timer(String timeFrom, String timeTo, boolean isEnable) {
        initTimer(timeFrom, timeTo, isEnable, "");
    }


    public Timer(String timeFrom, String timeTo, boolean isEnable, String daysStr) {
        initTimer(timeFrom, timeTo, isEnable, daysStr);
    }

    private void initTimer(String timeFrom, String timeTo, boolean isEnable, String daysStr) {
        setTimeFrom(timeFrom);
        setTimeTo(timeTo);
        setEnable(isEnable);
        setDaysStr(daysStr);
    }


    public String getDaysStr() {
        return daysStr;
    }

    public Map<String, Boolean> getDaysMap() {
        return daysMap;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setDaysStr(String daysStr) {
        this.daysStr = daysStr;
        if (daysStr == null || daysStr.isEmpty()) {
            this.daysStr =  DaysParser.daysToStr(Week.initWeek());
            return;
        }
//        TODO: Maybe replace it to initTimer
        setDaysMap(DaysParser.parseDayss(daysStr));
    }

    public void setDaysMap(Map<String, Boolean> daysMap) {
        this.daysMap = daysMap;
        this.daysStr = DaysParser.daysToStr(daysMap);
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public long getUid() {
        return uid;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public boolean isEnable() {
        return isEnable;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Timer other = (Timer) obj;
        return getUid() == other.getUid();
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
