package com.marketlogicsoftware.parser;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class CalenderBookings {

    private LocalTime start;
    private LocalTime end;
    private List<Reservation> reservations = new ArrayList<>();

    public void setOfficeHours(LocalTime start, LocalTime end){
        this.start = start;
        this.end = end;

    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public void printCalender() {
        reservations.stream().forEach(System.out::println);

    }
}
