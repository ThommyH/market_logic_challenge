package com.marketlogicsoftware.parser;

import java.time.LocalDateTime;

public class Reservation {

    private LocalDateTime bookingTime;
    private String userId;
    private LocalDateTime reservationTime;
    private int duration;

    public Reservation(LocalDateTime bookingTime, String userId, LocalDateTime reservationTime, int duration) {
        this.bookingTime = bookingTime;
        this.userId = userId;
        this.reservationTime = reservationTime;
        this.duration = duration;
    }

    public String toString(){
        return String.format("Booked room at: %s by: %s beginning at: %s for %d hours",
                bookingTime.toString(), userId, reservationTime.toString(), duration);
    }
}
