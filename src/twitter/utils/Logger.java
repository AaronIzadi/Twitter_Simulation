package twitter.utils;

import twitter.model.Time;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Logger {
    public void info(String text){
        String path = "resources/Logging/Logging.txt";
        File file = new File(path);
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(file, true));

            Time time = new Time();
            printStream.println(Time.now() + "  :  " + text);
            printStream.println("");



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
