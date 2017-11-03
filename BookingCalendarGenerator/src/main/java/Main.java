import com.marketlogicsoftware.parser.CalendarBuilder;
import com.marketlogicsoftware.parser.CalenderBookings;

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
        CalenderBookings cb = cp.build(inputFile);
        cb.printCalender();
    }

    private static void printUsage() {
        System.out.println("Usage: <programName> pathToInputFile");
    }
}
