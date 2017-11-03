package com.marketlogicsoftware.parser;

import java.time.LocalDateTime;

public class Reservation {

    private LocalDateTime bookingTime;
    private String userId;
    private LocalDateTime reservationTime;
    private int roomId;

    public Reservation(LocalDateTime bookingTime, String userId, LocalDateTime reservationTime, int roomId) {
        this.bookingTime = bookingTime;
        this.userId = userId;
        this.reservationTime = reservationTime;
        this.roomId = roomId;
    }
}
