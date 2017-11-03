package com.marketlogicsoftware.calendar;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Calendar {

    private LocalTime openingTime;
    private LocalTime closingTime;
    private TreeMap<LocalDate, List<Reservation>> reservationsByDate = new TreeMap<>();

    /**
     * created a calendar object by filtering out reservations who overlap
     * for resolving overlapping reservations, the calendar prioritize reservations that came first
     * @param openingTime the time when the office opens
     * @param closingTime the time when the office closes
     * @param reservations list of Reservations
     */
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
        for (LocalDate date : reservationsByDate.keySet()){
            System.out.println(date);
            reservationsByDate.get(date).stream().sorted().forEach(Reservation::print);
        }
    }

    /**
     *
     * @return The list of registered and valid reservations
     */
    public List<Reservation> getRegisteredReservations(){
        List<Reservation> reservations = new ArrayList<>();
        for (LocalDate day : reservationsByDate.keySet()){
            reservations.addAll(reservationsByDate.get(day));
        }
        return reservations;
    }

    private void addReservation(Reservation newReservation) {
        // check if newReservation is within business hours
        if (!isReservationWithinOpeningHours(newReservation)) return;
        LocalDate dateOfReservation = newReservation.getReservationStart().toLocalDate();
        if (reservationsByDate.containsKey(dateOfReservation)){
            List<Reservation> reservationsOfDay = reservationsByDate.get(dateOfReservation);
            boolean isIntersectingWithOther = checkForIntersectingReservations(reservationsOfDay, newReservation);
            if (!isIntersectingWithOther){
                reservationsOfDay.add(newReservation);
            }
        } else {
            List<Reservation> newListOfReservations = new ArrayList<>();
            newListOfReservations.add(newReservation);
            reservationsByDate.put(dateOfReservation, newListOfReservations);
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

    private boolean isReservationWithinOpeningHours(Reservation reservation) {
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
