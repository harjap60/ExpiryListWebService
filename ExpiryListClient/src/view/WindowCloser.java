package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * frame closes and exit program
 */

public class WindowCloser extends WindowAdapter {

    public WindowCloser() {
    }

    public void windowClosing(WindowEvent evt) {
        String[] command = {"curl", "-i", "-X", "GET", "localhost:8080/exit"};

        ProcessBuilder process = new ProcessBuilder(command);
        Process p;
        try {
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
