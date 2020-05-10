package com.akryvtsun.timerecorder;

public final class Launcher {

    public static void main(String... args) {
        AppMainFrame frame = new AppMainFrame();
        frame.restoreProperties();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
