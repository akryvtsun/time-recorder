package com.akryvtsun.timerecorder.ui.controllers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.akryvtsun.timerecorder.ui.Functions;
import com.akryvtsun.timerecorder.ui.RecorderState;
import com.akryvtsun.timerecorder.ui.actions.AboutAction;
import com.akryvtsun.timerecorder.ui.actions.ExitAction;
import com.akryvtsun.timerecorder.ui.actions.NewDayAction;
import com.akryvtsun.timerecorder.ui.controllers.GrossController;
import com.akryvtsun.timerecorder.ui.controllers.NetController;
import com.akryvtsun.timerecorder.properties.Storage;

/**
 * Time Recorder main frame.
 *
 * @author kontiky
 */
public final class TimeRecorderFrame extends JFrame {
    public static final String APP_NAME = "Time Recorder";
    public static final String APP_VER = "1.41";

    private final GrossController grossController;
    private final NetController netController;

    private final Storage storage;

    private TrayIcon trayIcon;

    public TimeRecorderFrame() {
        super(APP_NAME);

        Image logo = Functions.getLogo(RecorderState.INIT);

        setIconImage(logo);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    try {
                        SystemTray.getSystemTray().add(trayIcon);
                        setVisible(false);
                    } catch (AWTException ex) {
                        System.err.println("TrayIcon could not be added.");
                    }
                }
            }
        });

        final ChangeListener l = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                RecorderState state = RecorderState.INIT;
                if (grossController.getTimeAction().isStarted()) {
                    state = netController.getTimeAction().isStarted()
                        ? RecorderState.NET
                        : RecorderState.GROSS;
                }
                setState(TimeRecorderFrame.this, state);
            }
        };

        netController = new NetController();
        netController.getTimeAction().setChangeListener(l);
        grossController = new GrossController(netController);
        grossController.getTimeAction().setChangeListener(l);

        storage = new Storage(netController, grossController);

        netController.setRatioListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                String title = APP_NAME + " - " + netController.getRatio();
                setTitle(title);
                if (SystemTray.isSupported())
                    trayIcon.setToolTip(title);
            }
        });

        if (SystemTray.isSupported()) {
            trayIcon = new TrayIcon(logo, APP_NAME, null);
            trayIcon.setImageAutoSize(true);

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(Frame.NORMAL);
                    SystemTray.getSystemTray().remove(trayIcon);
                }
            };
            trayIcon.addActionListener(actionListener);
        }

        setJMenuBar(createMenuBar());
        add(createContent());
        pack();

        storage.restoreProperties();
        grossController.updateRatio();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menu = new JMenuBar();
        menu.add(createFileMenu());
        menu.add(createHelpMenu());
        return menu;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic('f');
        menu.add(new JMenuItem(new NewDayAction(netController, grossController)));
        menu.addSeparator();
        menu.add(new JMenuItem(new ExitAction(this, storage, trayIcon)));
        return menu;
    }

    private JMenu createHelpMenu() {
        JMenu menu = new JMenu("Help");
        menu.setMnemonic('h');
        menu.add(new JMenuItem(new AboutAction(this)));
        return menu;
    }

    private JPanel createContent() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panel.add(grossController.getViewComponent());
        panel.add(netController.getViewComponent());
        return panel;
    }

    private void setState(TimeRecorderFrame frame, RecorderState state) {
        Image logo = Functions.getLogo(state);
        if (SystemTray.isSupported()) {
            frame.trayIcon.setImage(logo);
        }
        frame.setIconImage(logo);
    }
}
