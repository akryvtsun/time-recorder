package com.akryvtsun.timerecorder;

import com.akryvtsun.timerecorder.ui.controllers.TimeRecorderFrame;

import javax.swing.*;

/**
 * TODO add net start/stop actions to tray menu
 * http://java.sun.com/developer/technicalArticles/J2SE/Desktop/javase6/systemtray
 * <p>
 * TODO Avoid memory leaks (?)
 * TODO NPE while loading icons (?)
 * TODO Prevent start of multi applications at the same time
 * TODO Add date for Start Time when gross control was started
 * <p>
 * TODO for 1.1: turning memory settings for JVM in start.bat
 * http://h21007.www2.hp.com/dspp/tech/tech_TechDocumentDetailPage_IDX/1,1701,1604,00.html
 * http://www.caucho.com/resin-3.0/performance/jvm-tuning.xtp
 * http://www.hp.com/products1/unix/java/infolibrary/prog_guide/hotspot.html
 * <p>
 * TODO for 1.5: new net counter for time switching
 * TODO for 1.5: use JProgressBar for showing ratio at the top or bottom of time controller components
 * TODO for 1.5: change main frame logo icon while net controller is running
 * <p>
 * TODO for 2.0: data base time storage
 * TODO for 2.0: charts on a base of history data
 * <p>
 * TODO use local DB like JavaDB
 * TODO use TrayIcon.displayMessage for workday finish notification
 * TODO use java.util.prefs API instead of ini-file.
 * TODO add gross Start Date field
 * TODO add net Switch Counter field
 */
public final class Launcher {

    public static void main(String... args) {
        JFrame frame = new TimeRecorderFrame();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
