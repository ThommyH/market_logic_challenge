package com.marketlogicsoftware.parser;

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

    public boolean doesIntersect(Reservation other){

        // no intersection if reservations are on different days
        if (!other.getReservationStart().toLocalDate().equals(this.getReservationStart().toLocalDate())) return false;
        // order reservations by startdate
        Reservation first = (this.getReservationStart().isBefore(other.getReservationStart())) ? this : other;
        Reservation second = (this.getReservationStart().isBefore(other.getReservationStart())) ? other : this;
        // reservations overlap if end of the first reservation is after the start of the other
        System.out.println("intersect " + this + "\n" + other + " " + first.getReservationEnd().isAfter(second.getReservationStart().plusNanos(1)));
        return (first.getReservationEnd().isAfter(second.getReservationStart().plusNanos(1)));
    }

    public boolean isSubmittedEarlier(Reservation other) {
        return this.getBookingTime().isBefore(other.getBookingTime());
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
        return this.getReservationStart().compareTo(o.getReservationStart());
    }
}
