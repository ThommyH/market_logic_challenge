package com.marketlogicsoftware.calendar;


import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalTime;

import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;


public class TestCalendarBuilder {

    private String testFile = String.join("\n",
            "0900 1730",
            "2015-08-17 10:17:06 EMP001",
            "2015-08-21 09:00 2",
            "2015-08-16 12:34:56 EMP002",
            "2015-08-21 09:00 2",
            "2015-08-16 09:28:23 EMP003",
            "2015-08-22 14:00 2",
            "2015-08-17 11:23:45 EMP004",
            "2015-08-22 16:00 1",
            "2015-08-15 17:29:12 EMP005",
            "2015-08-21 16:00 3");

    private Calendar calendar;

    @Before
    public void setUp() throws IOException {
        CalendarBuilder cb = new CalendarBuilder();
        calendar = cb.build(testFile);
    }

    @Test
    public void testCreationFromFile() throws IOException {
        CalendarBuilder cb = new CalendarBuilder();
        Calendar c = cb.build(Paths.get("src/test/resources/booking_requests.txt"));
        assertEquals(c.getRegisteredReservations().size(),3);
    }

    @Test
    public void testOpeningAndClosingTimesAreSetCorrectly() {
        assertTrue(calendar.getOpeningTime().equals(LocalTime.parse("09:00")));
        assertTrue(calendar.getClosingTime().equals(LocalTime.parse("17:30")));
    }

    @Test
    public void testOverlappingSubmissionsHandling(){
        for (Reservation reservation : calendar.getRegisteredReservations()){
            // submission of EMP001 should be overwritten by EMP002 since he submitted the request earlier
            if (reservation.getUserId().equals("EMP001")) fail();
        }
    }

    @Test
    public void testReservationsCannotHappenAfterClosingTime(){
        for (Reservation reservation : calendar.getRegisteredReservations()){
            // EMP005 would book the room from 16 to 19 o clock but the office closes by 17:30
            if (reservation.getUserId().equals("EMP005")) fail();
        }
    }

    @Test
    public void testReservationsCannotHappenBeforeOpening() throws IOException {
        String testString = String.join("\n",
                "0900 1700",
                "2015-08-17 10:17:06 EMP001", // 8 - 10
                "2015-08-21 08:00 2");
        CalendarBuilder cb = new CalendarBuilder();
        Calendar c = cb.build(testString);
        assertEquals(c.getRegisteredReservations().size(), 0);
    }

    @Test
    public void testReservationsCanStartWhenOthersEnd(){
        boolean foundEMP003 = false;
        boolean foundEMP004 = false;
        for (Reservation reservation : calendar.getRegisteredReservations()){
            // EMP005 would book the room from 16 to 19 o clock but the office closes by 17:30
            if (reservation.getUserId().equals("EMP003")) foundEMP003 = true;
            if (reservation.getUserId().equals("EMP004")) foundEMP004 = true;
        }
        assertTrue(foundEMP003 && foundEMP004);
    }

    @Test
    public void testOutputFormatting(){
        PrintStream save_out = System.out;
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        calendar.printCalender();
        String expectedOutput = String.join("\n",
                "2015-08-21",
                        "09:00 11:00 EMP002",
                        "2015-08-22",
                        "14:00 16:00 EMP003",
                        "16:00 17:00 EMP004\n"
                );
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testBookingFromOpeningToClosing() throws IOException {
        String testString = String.join("\n",
                "0900 1700",
                "2015-08-17 10:17:06 EMP001",
                "2015-08-21 09:00 8");
        CalendarBuilder cb = new CalendarBuilder();
        Calendar c = cb.build(testString);
        assertEquals(c.getRegisteredReservations().size(), 1);
    }

    @Test
    public void testOverlappingOfMultipleBookings() throws IOException {
        String testString = String.join("\n",
                "0900 1700",
                "2015-08-17 11:00:00 EMP001", // 9-10
                "2015-08-21 09:00 1",
                "2015-08-17 11:01:00 EMP001", // 10-11
                "2015-08-21 10:00 1",
                "2015-08-17 10:50:00 EMP003", // 16-17
                "2015-08-21 16:00 1",
                "2015-08-17 10:00:00 EMP002", // 9-16 should overwrite first 2 bookings
                "2015-08-21 09:00 7");
        CalendarBuilder cb = new CalendarBuilder();
        Calendar c = cb.build(testString);
        assertEquals(c.getRegisteredReservations().size(), 2);
    }

}
