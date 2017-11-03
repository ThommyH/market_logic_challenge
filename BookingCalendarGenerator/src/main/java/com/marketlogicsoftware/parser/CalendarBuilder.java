package com.marketlogicsoftware.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

public class CalendarBuilder {

    public CalenderBookings build(Path inputFile) {
        String submissionEntry;
        String reservationEntry;
        CalenderBookings cb = new CalenderBookings();
        try (BufferedReader br = Files.newBufferedReader(inputFile)) {
            // first line is office hours
            String officeHoursStr = br.readLine();
            LocalTime[] officeHours = parseOfficeHours(officeHoursStr);
            cb.setOfficeHours(officeHours[0], officeHours[1]);
            // read 2 lines at a time. no error handling
            while ( (submissionEntry = br.readLine()) != null &&
                    (reservationEntry = br.readLine()) != null ){
                Reservation reservation = createReservation(submissionEntry, reservationEntry);
                cb.addReservation(reservation);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cb;
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
        LocalDateTime reservationTime = LocalDateTime.parse(reservationTimeStr, dateTimeFormatterWOSeconds);
        int roomId = Integer.parseInt(reservationSplit[2]);

        return new Reservation(bookingTime,userId,reservationTime, roomId);
    }

    /**
     * Parses office hours string into LocalDates [start,end]
     * @param officeHours e.g. 0900 1730
     * @return
     */
    LocalTime[] parseOfficeHours(String officeHours){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String[] split = officeHours.split(" ");
        LocalTime start = LocalTime.parse(split[0],timeFormatter);
        LocalTime end = LocalTime.parse(split[1],timeFormatter);
        return new LocalTime[]{start, end};
    }

}
