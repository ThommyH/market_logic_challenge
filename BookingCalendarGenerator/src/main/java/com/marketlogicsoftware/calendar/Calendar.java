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
    private TreeMap<LocalDate, List<Reservation>> reservationsByDay = new TreeMap<LocalDate, List<Reservation>>();

    public Calendar(LocalTime openingTime, LocalTime closingTime, List<Reservation> reservations) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        reservations
                .stream()
                .sorted()
                .forEach(reservation -> addReservation(reservation));
    }

    private void addReservation(Reservation newReservation) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // check if newReservation is within business hours
        if (!checkReservationIsWithinOpeningHours(newReservation)) return;
        LocalDate dateOfReservation = newReservation.getReservationStart().toLocalDate();
        if (reservationsByDay.containsKey(dateOfReservation)){
            List<Reservation> reservationsOfDay = reservationsByDay.get(dateOfReservation);
            Optional<Reservation> intersectingReservation = findFirstIntersectingReservation(reservationsOfDay, newReservation);
            if (!intersectingReservation.isPresent()){
                reservationsOfDay.add(newReservation);
            }
        } else {
            List<Reservation> newListOfReservations = new ArrayList<>();
            newListOfReservations.add(newReservation);
            reservationsByDay.put(dateOfReservation, newListOfReservations);
        }

    }

    private Optional<Reservation> findFirstIntersectingReservation(List<Reservation> reservationsOfDay, Reservation newReservation) {
        for (Reservation reservation : reservationsOfDay){
            if (reservation.doesOverlapWith(newReservation)){
                return Optional.of(reservation);
            }
        }
        return Optional.empty();
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

    public void printCalender() {
        for (LocalDate date : reservationsByDay.keySet()){
            System.out.println(date);
            reservationsByDay.get(date).stream().sorted().forEach(r -> r.print());
        }
    }
}
