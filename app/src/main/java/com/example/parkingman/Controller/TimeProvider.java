package com.example.parkingman.Controller;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.TimeZone;

public class TimeProvider {

    public static Date getUniversalTime(){
        return getUtcFromLocal(new Date());
    }

    public Date getLocalFromUtcEpoch(long utcEpoch){
        TimeZone localZone = TimeZone.getDefault(); // get system time zone
        int offset = localZone.getOffset(utcEpoch); // get the offset in ms from UTC (0)
        return new Date(utcEpoch + offset);
    }

    public static long getUtcEpochFromLocal(Date localDate){
        long localTime = localDate.getTime();
        TimeZone localZone = TimeZone.getDefault(); // get system time zone
        int offset = localZone.getOffset(localTime); // get the offset in ms from UTC (0)
        return (localTime - offset);
    }

    public static Date getLocalFromUtc(Date utcDate){
        long utcTime = utcDate.getTime();
        TimeZone localZone = TimeZone.getDefault(); // get system time zone
        int offset = localZone.getOffset(utcTime); // get the offset in ms from UTC (0)
        return new Date(utcTime + offset);
    }

    public static Date getUtcFromLocal(Date localDate){
        long localTime = localDate.getTime();
        TimeZone localZone = TimeZone.getDefault(); // get system time zone
        int offset = localZone.getOffset(localTime); // get the offset in ms from UTC (0)
        return new Date(localTime - offset);
    }

}
