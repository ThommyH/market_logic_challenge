package com.marketlogicsoftware.parser;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class CalenderBookings {

    private LocalTime start;
    private LocalTime end;
    private List<Reservation> reservations = new ArrayList<>();
    private TreeMap<LocalDate, List<Reservation>> reservationsByDay = new TreeMap<LocalDate, List<Reservation>>();

    public void setOfficeHours(LocalTime start, LocalTime end){
        this.start = start;
        this.end = end;

    }

    public void addReservation(Reservation newReservation) {
        reservations.add(newReservation);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // check if newReservation is within business hours
        if (!checkValidReservation(newReservation)) return;

        LocalDate dateOfReservation = newReservation.getReservationStart().toLocalDate();

        if (reservationsByDay.containsKey(dateOfReservation)){
            List<Reservation> reservationsOfDay = reservationsByDay.get(dateOfReservation);
            for (Reservation registeredReservation : reservationsOfDay) {
                if (registeredReservation.doesIntersect(newReservation)){
                    if (newReservation.isSubmittedEarlier(registeredReservation)) {
                        // overwrite existing newReservation since if was submitted later
                        reservationsOfDay.remove(registeredReservation);
                        reservationsOfDay.add(newReservation);
                        return;
                    }
                } else {
                    reservationsOfDay.add(newReservation);
                    return;
                }
            }
        } else {
            List<Reservation> newListOfReservations = new ArrayList<>();
            newListOfReservations.add(newReservation);
            reservationsByDay.put(dateOfReservation, newListOfReservations);
            return;
        }

    }

    private boolean checkValidReservation(Reservation reservation) {
        return (reservation.getReservationStart().toLocalTime().isAfter(start.minusNanos(1)) &&
                reservation.getReservationEnd().toLocalTime().isBefore(end.plusNanos(1)));
    }

    public void printCalender() {
        //reservations.stream().forEach(System.out::println);
        for (LocalDate date : reservationsByDay.keySet()){
            System.out.println(date);
            reservationsByDay.get(date).stream().sorted().forEach(r -> r.print());
        }
    }
}
