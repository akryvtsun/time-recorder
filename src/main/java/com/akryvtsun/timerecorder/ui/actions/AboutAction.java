package com.akryvtsun.timerecorder.ui.actions;

import com.akryvtsun.timerecorder.ui.TimeRecorderFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Year;

public final class AboutAction extends AbstractAction {
    private final Component frame;

    public AboutAction(Component frame) {
        super("About...");
        this.frame = frame;
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('A'));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int year = Year.now().getValue();
        JOptionPane.showMessageDialog(frame,
            new JLabel("<html><center>" + TimeRecorderFrame.APP_NAME
                + " " + TimeRecorderFrame.APP_VER
                + "<br>Copyright &copy; 2007-" + year
                + "<br>by Andriy Kryvtsun"),
            "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
