package com.marketlogicsoftware.calendar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1){
            printUsage();
            return;
        }
        Path inputFile = Paths.get(args[0]);
        CalendarBuilder cp = new CalendarBuilder();
        CalenderBookings cb = null;
        try {
            cb = cp.build(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cb.printCalender();
    }

    private static void printUsage() {
        System.out.println("Usage: <programName> pathToInputFile");
    }
}
