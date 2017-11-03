package com.marketlogicsoftware.calendar;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarBuilder {

    public CalenderBookings build(Path inputFile) throws IOException {
        String submissionEntry;
        String reservationEntry;
        List<Reservation> reservationsList = new ArrayList<>();

        BufferedReader br = Files.newBufferedReader(inputFile);

        // first line is office hours
        String officeHoursStr = br.readLine();
        LocalTime[] officeHours = parseOfficeHours(officeHoursStr);
        LocalTime openingTime = officeHours[0];
        LocalTime closingTime = officeHours[1];
        /*
         collect reservations
         read 2 lines at a time
          */
        while ( (submissionEntry = br.readLine()) != null &&
                (reservationEntry = br.readLine()) != null ){
            reservationsList.add(createReservation(submissionEntry, reservationEntry));
        }
        return new CalenderBookings(openingTime, closingTime, reservationsList);
    }

    /**
     * parses the two input lines and creates a reservation object
     *
     * @param submissionEntry  e.g. 2015-08-17 10:17:06 EMP001
     * @param reservationEntry e.g. 2015-08-21 09:00 2
     * @return
     */
    private Reservation createReservation(String submissionEntry, String reservationEntry) {
        String[] submissionSplit = submissionEntry.split(" ");
        String[] reservationSplit = reservationEntry.split(" ");
        DateTimeFormatter dateTimeFormatterWithSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatterWOSeconds = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // parse submissionEntry
        String submissionTimeStr = submissionSplit[0] + " " + submissionSplit[1];
        LocalDateTime bookingTime = LocalDateTime.parse(submissionTimeStr, dateTimeFormatterWithSeconds);
        String userId = submissionSplit[2];

        // parse reservationEntry
        String reservationTimeStr = reservationSplit[0] + " " + reservationSplit[1];
        LocalDateTime reservationStartTime = LocalDateTime.parse(reservationTimeStr, dateTimeFormatterWOSeconds);
        int duration = Integer.parseInt(reservationSplit[2]);
        LocalDateTime reservationEndTime = reservationStartTime.plusHours(duration);

        return new Reservation(bookingTime,userId,reservationStartTime, reservationEndTime);
    }

    /**
     * Parses office hours string into LocalDates
     * @param officeHours e.g. "0900 1730"
     * @return [start,end]
     */
    LocalTime[] parseOfficeHours(String officeHours){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String[] split = officeHours.split(" ");
        LocalTime start = LocalTime.parse(split[0],timeFormatter);
        LocalTime end = LocalTime.parse(split[1],timeFormatter);
        return new LocalTime[]{start, end};
    }

}
