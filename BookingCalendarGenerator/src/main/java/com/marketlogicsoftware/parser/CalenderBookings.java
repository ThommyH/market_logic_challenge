package com.marketlogicsoftware.parser;
import java.time.*;

public class CalenderBookings {

    private LocalDate start;
    private LocalDate end;

    public void setOfficeHours(LocalDate start, LocalDate end){
        this.start = start;
        this.end = end;
    }
}
