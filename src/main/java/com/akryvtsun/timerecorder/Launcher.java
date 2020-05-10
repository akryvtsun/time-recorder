package com.akryvtsun.timerecorder;

public class Launcher {

    public static void main(String... args) {
        AppMainFrame frame = new AppMainFrame();
        frame.restoreProperties();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
