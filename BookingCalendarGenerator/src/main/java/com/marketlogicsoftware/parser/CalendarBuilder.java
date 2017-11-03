package com.marketlogicsoftware.parser;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
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
            LocalDate[] officeHours = parseOfficeHours(officeHoursStr);
            cb.setOfficeHours(officeHours[0], officeHours[1]);
            // read 2 lines at a time. no error handling
            while ( (submissionEntry = br.readLine()) != null &&
                    (reservationEntry = br.readLine()) != null ){
                parseSubmissionEntry(submissionEntry);
                parseReservationEntry(reservationEntry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cb;
    }

    private void parseSubmissionEntry(String submissionEntry) {

    }

    private void parseReservationEntry(String reservationEntry) {

    }

    /**
     * Parses office hours string into LocalDates [start,end]
     * @param officeHours
     * @return
     */
    LocalDate[] parseOfficeHours(String officeHours){
        String[] split = officeHours.split(" ");
        LocalDate start = LocalDate.parse(split[0]);
        LocalDate end = LocalDate.parse(split[1]);
        return new LocalDate[]{start, end};
    }
}
