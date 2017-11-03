package com.marketlogicsoftware.calendar;

import java.time.LocalDateTime;

public class Reservation implements Comparable<Reservation> {

    private LocalDateTime bookingTime;
    private String userId;
    private LocalDateTime reservationStart;
    private LocalDateTime reservationEnd;

    public Reservation(LocalDateTime bookingTime, String userId, LocalDateTime reservationStart, LocalDateTime reservationEnd) {
        this.bookingTime = bookingTime;
        this.userId = userId;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;
    }

    public String toString(){
        return String.format("Booked room at: %s by: %s beginning at: %s ending %s",
                bookingTime.toString(), userId, reservationStart.toString(), reservationEnd);
    }

    /**
     * checks if this Reservation overlaps with another Reservation
     * @param other
     * @return
     */
    public boolean doesOverlapWith(Reservation other){
        // no intersection if reservations are on different days
        if (!other.getReservationStart().toLocalDate().equals(reservationStart.toLocalDate())) return false;
        // order reservations by startdate
        Reservation first = (reservationStart.isBefore(other.getReservationStart())) ? this : other;
        Reservation second = (reservationStart.isBefore(other.getReservationStart())) ? other : this;
        // reservations overlap if end of the first reservation is after the start of the other
        // however, they can be the same value without overlapping
        return (first.getReservationEnd().isAfter(second.getReservationStart()) ||
                !first.getReservationEnd().equals(second.getReservationStart()));
    }

    public boolean isSubmittedEarlier(Reservation other) {
        return bookingTime.isBefore(other.getBookingTime());
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getReservationStart() {
        return reservationStart;
    }

    public LocalDateTime getReservationEnd() {
        return reservationEnd;
    }

    public void print() {
        System.out.println(String.format("%s %s %s", reservationStart.toLocalTime(), reservationEnd.toLocalTime(), userId));
    }

    @Override
    public int compareTo(Reservation o) {
        return bookingTime.compareTo(o.getBookingTime());
    }
}
