package com.marketlogicsoftware.calendar;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public class Calendar {

    private LocalTime openingTime;
    private LocalTime closingTime;
    private TreeMap<LocalDate, List<Reservation>> reservationsByDay = new TreeMap<>();

    protected Calendar(LocalTime openingTime, LocalTime closingTime, List<Reservation> reservations) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        reservations
                .stream()
                .sorted()
                .forEach(this::addReservation);
    }

    /**
     * Prints the calendar to std out
     * e.g.
     * 2015-08-21
     * 09:00 11:00 EMP002
     * 2015-08-22
     * 14:00 16:00 EMP003
     * 16:00 17:00 EMP004
     */
    public void printCalender() {
        for (LocalDate date : reservationsByDay.keySet()){
            System.out.println(date);
            reservationsByDay.get(date).stream().sorted().forEach(Reservation::print);
        }
    }

    /**
     *
     * @return The list of registered and filtered reservations
     */
    public List<Reservation> getRegisteredReservations(){
        List<Reservation> reservations = new ArrayList<>();
        for (LocalDate day : reservationsByDay.keySet()){
            reservations.addAll(reservationsByDay.get(day));
        }
        return reservations;
    }

    private void addReservation(Reservation newReservation) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // check if newReservation is within business hours
        if (!checkReservationIsWithinOpeningHours(newReservation)) return;
        LocalDate dateOfReservation = newReservation.getReservationStart().toLocalDate();
        if (reservationsByDay.containsKey(dateOfReservation)){
            List<Reservation> reservationsOfDay = reservationsByDay.get(dateOfReservation);
            boolean isIntersectingWithOther = checkForIntersectingReservations(reservationsOfDay, newReservation);
            if (!isIntersectingWithOther){
                reservationsOfDay.add(newReservation);
            }
        } else {
            List<Reservation> newListOfReservations = new ArrayList<>();
            newListOfReservations.add(newReservation);
            reservationsByDay.put(dateOfReservation, newListOfReservations);
        }
    }

    private boolean checkForIntersectingReservations(List<Reservation> reservationsOfDay, Reservation newReservation) {
        for (Reservation reservation : reservationsOfDay){
            if (reservation.doesOverlapWith(newReservation)){
                return true;
            }
        }
        return false;
    }

    private boolean checkReservationIsWithinOpeningHours(Reservation reservation) {
        // reservation must be at opening time of after
        boolean valid = true;
        if (!reservation.getReservationStart().toLocalTime().isAfter(openingTime) &&
                !reservation.getReservationStart().toLocalTime().equals(openingTime)){
            valid = false;
        }
        if (!reservation.getReservationEnd().toLocalTime().isBefore(closingTime) &&
                !reservation.getReservationEnd().toLocalTime().equals(closingTime)){
            valid = false;
        }
        return valid;
    }

    public LocalTime getOpeningTime(){
        return openingTime;
    }

    public LocalTime getClosingTime(){
        return closingTime;
    }
}
