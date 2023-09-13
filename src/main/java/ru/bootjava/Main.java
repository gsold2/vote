package ru.bootjava;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Hello world!
 */
public class Main {

    public static void main(String[] args) {
        ZoneId zoneId = ZoneId.of("Europe/London");
        ZoneId tz = ZoneId.of("Europe/Moscow");
        ZoneId nepalZoneId = ZoneId.of("Asia/Kathmandu");
        // of LocalTime class
//        LocalTime time = LocalTime.now(ZoneId.systemDefault());
//        LocalTime tz1 = LocalTime.now(nepalZoneId);
//        zoneId.getId();
//        zoneId.getRules();
//        LocalTime currentTime = LocalTime.now(Clock.system(ZoneId.of("America/Chicago")));
//        System.out.println("Hello World!");
//        System.out.println(LocalTime.now(Clock.system(ZoneId.of("America/Chicago"))));
//        System.out.println((ZoneId.of("Europe/London")).getId());

        System.out.println(ZonedDateTime.now(ZoneId.systemDefault()));
        System.out.println(ZonedDateTime.now(ZoneId.systemDefault()).toLocalTime());
        System.out.println(ZonedDateTime.now(ZoneId.systemDefault()).toLocalTime().plusHours(2));
//        System.out.println(ZonedDateTime.now(tz));
//        System.out.println(ZonedDateTime.now(zoneId));
//        System.out.println(ZonedDateTime.now(zoneId).toLocalDateTime());
//        System.out.println(ZonedDateTime.now(nepalZoneId));
    }
}
