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
        try {
            Calendar cb = cp.build(inputFile);
            cb.printCalender();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printUsage() {
        System.out.println("Usage: <programName> pathToInputFile");
    }
}
