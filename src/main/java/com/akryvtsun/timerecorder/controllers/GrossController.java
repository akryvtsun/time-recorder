package com.akryvtsun.timerecorder.controllers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.akryvtsun.timerecorder.properties.Storable;

/**
 * Gross time calculation controller.
 *
 * @author kontiky
 */
public class GrossController extends TimeController implements Storable {
    private static final String TIME_FORMAT = "%tT";

    private static final String PROPERTY_PREFIX = ENTITY_PREFIX + ".gross";
    private static final String START_TIME_PROPERTY = PROPERTY_PREFIX + ".startTime";
    private static final String IS_ENABLED_PROPERTY = PROPERTY_PREFIX + ".isEnabled";
    private static final String IS_STARTED_PROPERTY = PROPERTY_PREFIX + ".isStarted";

    private final NetController netController;

    private final JTextField startTimeField = createTimeField();
    private final JTextField grossTimeField = createTimeField();

    private long startTimeMillis;

    public GrossController(NetController netController) {
        this.netController = netController;
    }

    @Override
    protected Component createViewComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Gross control"));

        panel.add(new JLabel("Start Time:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        panel.add(startTimeField, new GridBagConstraints(1, 0, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

        panel.add(new JLabel("Gross Time:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        panel.add(grossTimeField, new GridBagConstraints(1, 1, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

        panel.add(getTimeButton(), new GridBagConstraints(0, 2, 2, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(7, 5, 5, 5), 0, 0));

        return panel;
    }

    @Override
    protected StartStopAction createTimeAction() {
        return new GrossTimerAction();
    }

    @Override
    public void startNewDay() {
        setStartTime(System.currentTimeMillis());

        getTimeAction().startNewDay();
        grossTimeField.setText(getStringTime(0));

        if (!getTimeAction().isStarted()) {
            getTimeButton().setEnabled(true);
            getTimeButton().doClick();
        }
    }

    @Override
    public void restore(Properties props) {
        try {
            setStartTime(Long.parseLong(props.getProperty(START_TIME_PROPERTY)));
        } catch (NumberFormatException e) {
            setStartTime(System.currentTimeMillis());
        }
        if (Boolean.parseBoolean(props.getProperty(IS_ENABLED_PROPERTY, "false")))
            getTimeButton().setEnabled(true);
        if (Boolean.parseBoolean(props.getProperty(IS_STARTED_PROPERTY, "false")))
            getTimeButton().doClick();
        getTimeAction().restore(props);
    }

    @Override
    public void store(Properties props) {
        getTimeAction().store(props);
        props.setProperty(START_TIME_PROPERTY, String.valueOf(startTimeMillis));
        props.setProperty(IS_STARTED_PROPERTY, String.valueOf(getTimeAction().isStarted()));
        props.setProperty(IS_ENABLED_PROPERTY, String.valueOf(getTimeButton().isEnabled()));
    }

    private void setStartTime(long millis) {
        startTimeMillis = millis;
        startTimeField.setText(String.format(TIME_FORMAT, startTimeMillis));
    }

    private void updateCounters(long periodMillis) {
        grossTimeField.setText(getStringTime(periodMillis));
        netController.updateRatio(periodMillis);
    }

    public void updateRatio() {
        updateCounters(getTimeAction().getPeriod());
    }

    private class GrossTimerAction extends StartStopAction {

        protected void doAction() {
            super.doAction();
            netController.setEnabled(isStarted());
        }

        @Override
        protected void updatePeriod(long periodMillis) {
            updateCounters(periodMillis);
        }

        @Override
        protected String getName() {
            return "gross";
        }
    }
}
