package com.marketlogicsoftware.calendar;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalendarBuilder {

    /**
     * Creates a Calendar object from the given input string
     * e.g.
     * 0900 1700
     * 2015-08-17 10:17:06 EMP001
     * 2015-08-21 09:00 2
     *
     * @param inputString
     * @return Calendar based on the inputString
     * @throws IOException if String reading fails
     */
    public Calendar build(String inputString) throws IOException {
        return build(new BufferedReader(new StringReader(inputString)));
    }


    /**
     * Creates a Calendar object from the given input file
     * e.g.
     * 0900 1700
     * 2015-08-17 10:17:06 EMP001
     * 2015-08-21 09:00 2
     * @param inputFile
     * @return Calendar based on the inputFile
     * @throws IOException if file cannot be read
     */
    public Calendar build(Path inputFile) throws IOException {
        return build(Files.newBufferedReader(inputFile));
    }

    private Calendar build(BufferedReader br) throws IOException {
        String submissionEntry;
        String reservationEntry;
        List<Reservation> reservationsList = new ArrayList<>();

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
            reservationsList.add(buildReservation(submissionEntry, reservationEntry));
        }
        return new Calendar(openingTime, closingTime, reservationsList);
    }

    /**
     * parses the two input lines and creates a reservation object
     *
     * @param submissionEntry  e.g. 2015-08-17 10:17:06 EMP001
     * @param reservationEntry e.g. 2015-08-21 09:00 2
     * @return a Reservation object containing the information of the input strings
     */
    private Reservation buildReservation(String submissionEntry, String reservationEntry) {
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
    private LocalTime[] parseOfficeHours(String officeHours){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String[] split = officeHours.split(" ");
        LocalTime start = LocalTime.parse(split[0],timeFormatter);
        LocalTime end = LocalTime.parse(split[1],timeFormatter);
        return new LocalTime[]{start, end};
    }

}
