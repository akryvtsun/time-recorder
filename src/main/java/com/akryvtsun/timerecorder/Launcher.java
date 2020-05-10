package com.akryvtsun.timerecorder;

import javax.swing.*;

public final class Launcher {

    public static void main(String... args) {
        JFrame frame = new TimeRecorderFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
