package com.akryvtsun.timerecorder.ui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;

import com.akryvtsun.timerecorder.ui.Functions;
import com.akryvtsun.timerecorder.properties.Storable;

/**
 * Start/stop action for time controllers.
 * All actions share single timer for synchronous time changing.
 *
 * @author kontiky
 */
public /*final*/ class StartStopAction extends AbstractAction implements Storable {
    static final int TIMER_TICK = 1000;
    private static final Timer Clock = new Timer(TIMER_TICK, null);

    private static final String ENTITY_PREFIX = "time.";
    private static final String PERIOD_PROPERTY = ".periodMillis";
    private static final String LAST_START_PROPERTY = ".lastStartMillis";

    private static final String START_TEXT = "Start";
    private static final Icon START_ICON = Functions.getIcon("start");

    private static final String PAUSE_TEXT = "Pause";
    private static final Icon PAUSE_ICON = Functions.getIcon("pause");

    static {
        Clock.start();
    }

    private final String name;

    private ActionListener clockListener;
    private long periodMillis, lastStartMillis;
    private boolean isStarted = false;

    public StartStopAction(String name) {
        this.name = name;
        setEnabled(false);
        prepareToStart();
    }

    public void setActionListener(ActionListener clockListener) {
        this.clockListener = clockListener;
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
            Clock.removeActionListener(clockListener);
            isStarted = false;
            doAction();
            prepareToStart();
        } else {
            // start action
            isStarted = true;
            prepareToStop();
            doAction();
            Clock.addActionListener(clockListener);
        }
    }

    protected void doAction() {
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
        putValue(Action.SMALL_ICON, START_ICON);
        putValue(Action.NAME, START_TEXT);
    }

    private void prepareToStop() {
        putValue(Action.SMALL_ICON, PAUSE_ICON);
        putValue(Action.NAME, PAUSE_TEXT);
    }
}

