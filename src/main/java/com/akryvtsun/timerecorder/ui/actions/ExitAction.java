package com.akryvtsun.timerecorder.ui.actions;

import com.akryvtsun.timerecorder.properties.Storage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static com.akryvtsun.timerecorder.ui.TimeRecorderFrame.APP_NAME;

public final class ExitAction extends AbstractAction {
    private final Window window;
    private final Storage storage;
    private TrayIcon trayIcon;

    public ExitAction(Window window, Storage storage, TrayIcon trayIcon) {
        super("Exit");
        this.window = window;
        this.storage = storage;
        this.trayIcon = trayIcon;
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('X'));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(window,
            "Do you want to exit " + APP_NAME + "?",
            "Confirm Exit", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            storage.storeProperties();
            if (SystemTray.isSupported()) {
                SystemTray.getSystemTray().remove(trayIcon);
            }
            window.dispose();
            System.exit(0);
        }
    }
}
