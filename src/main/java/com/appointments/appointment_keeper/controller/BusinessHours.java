package com.appointments.appointment_keeper.controller;

import java.time.LocalTime;
import java.time.ZoneId;

public class BusinessHours {
    private static final ZoneId BIZ_HR_ZONE_ID = ZoneId.of("America/New_York");
    private static final LocalTime open = LocalTime.parse("08:00");
    private static final LocalTime close = LocalTime.parse("22:00");
    
    public static ZoneId getBIZ_HR_ZONE_ID() {
        return BIZ_HR_ZONE_ID;
    }

    public static LocalTime getOpen() {
        return open;
    }

    public static LocalTime getClose() {
        return close;
    }
     
}
