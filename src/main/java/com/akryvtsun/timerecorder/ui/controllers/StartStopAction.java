package com.akryvtsun.timerecorder.ui.controllers;

import com.akryvtsun.timerecorder.properties.Storable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

/**
 * Start/stop action for time controllers.
 * All actions share single timer for synchronous time changing.
 *
 * @author kontiky
 */
public final class StartStopAction extends AbstractAction implements Storable {
    private static final String ENTITY_PREFIX = "time.";
    private static final String PERIOD_PROPERTY = ".periodMillis";
    private static final String LAST_START_PROPERTY = ".lastStartMillis";

    private static final String START_TEXT = "Start";
    private static final String PAUSE_TEXT = "Pause";

    private final String name;
    private final Timer clock;
    private final Icon startIcon;
    private final Icon pauseIcon;

    private ActionListener clockListener;
    private Runnable onToggle;
    private long periodMillis, lastStartMillis;
    private boolean isStarted = false;

    public StartStopAction(String name, Timer clock, Icon startIcon, Icon pauseIcon) {
        this.name = name;
        this.clock = clock;
        this.startIcon = startIcon;
        this.pauseIcon = pauseIcon;
        setEnabled(false);
        prepareToStart();
    }

    public void setActionListener(ActionListener clockListener) {
        this.clockListener = clockListener;
    }

    public void setOnToggle(Runnable onToggle) {
        this.onToggle = onToggle;
    }

    protected String getName() {
        return name;
    };

    public final boolean isStarted() {
        return isStarted;
    }

    public void startNewDay() {
        if (isStarted())
            actionPerformed(null);
        periodMillis = 0;
    }

    @Override
    public void store(Properties props) {
        props.setProperty(ENTITY_PREFIX + getName() + PERIOD_PROPERTY, String.valueOf(periodMillis));
        if (isStarted())
            props.setProperty(ENTITY_PREFIX + getName() + LAST_START_PROPERTY, String.valueOf(lastStartMillis));
    }

    @Override
    public void restore(Properties props) {
        periodMillis = Long.parseLong(props.getProperty(ENTITY_PREFIX + getName() + PERIOD_PROPERTY, "0"));
        if (isStarted())
            lastStartMillis = Long.parseLong(props.getProperty(ENTITY_PREFIX + getName() + LAST_START_PROPERTY, "0"));
        else
            lastStartMillis = System.currentTimeMillis();
        clockListener.actionPerformed(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isStarted()) {
            // stop action
            clock.removeActionListener(clockListener);
            isStarted = false;
            doAction();
            prepareToStart();
        } else {
            // start action
            isStarted = true;
            prepareToStop();
            doAction();
            clock.addActionListener(clockListener);
        }
        if (onToggle != null)
            onToggle.run();
    }

    private void doAction() {
        long currentTimeMillis = System.currentTimeMillis();
        if (isStarted()) {
            lastStartMillis = currentTimeMillis;
        } else {
            periodMillis += currentTimeMillis - lastStartMillis;
        }
    }

    public long getPeriod() {
        return periodMillis + (System.currentTimeMillis() - lastStartMillis);
    }

    private void prepareToStart() {
        putValue(Action.SMALL_ICON, startIcon);
        putValue(Action.NAME, START_TEXT);
    }

    private void prepareToStop() {
        putValue(Action.SMALL_ICON, pauseIcon);
        putValue(Action.NAME, PAUSE_TEXT);
    }
}

