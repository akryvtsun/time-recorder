package com.akryvtsun.timerecorder.ui.controllers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeListener;

import com.akryvtsun.timerecorder.properties.Storable;

/**
 * Net time calculation controller.
 *
 * @author kontiky
 */
public final class NetController extends TimeController implements Storable {
    private static final String RATIO_FORMAT = "%.3f";

    private static final String PROPERTY_PREFIX = ENTITY_PREFIX + ".net";
    private static final String NET_TIME_PROPERTY = PROPERTY_PREFIX + ".netTime";
    private static final String IS_ENABLED_PROPERTY = PROPERTY_PREFIX + ".isEnabled";
    private static final String IS_STARTED_PROPERTY = PROPERTY_PREFIX + ".isStarted";

    private final JTextField netTime = createTimeField();
    private final JTextField ratio = createTimeField();

    private long netTimeMillis;
    private ChangeListener listener;

    @Override
    protected Component createViewComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Net control"));

        panel.add(new JLabel("Net Time:"), new GridBagConstraints(0, 0, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 5, 0, 0), 0, 0));
        panel.add(netTime, new GridBagConstraints(1, 0, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 5, 0, 5), 0, 0));

        panel.add(new JLabel("Ratio:"), new GridBagConstraints(0, 1, 1, 1, 0, 0,
            GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 0, 0), 0, 0));
        panel.add(ratio, new GridBagConstraints(1, 1, 1, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 5), 0, 0));

        panel.add(getTimeButton(), new GridBagConstraints(0, 2, 2, 1, 1, 0,
            GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(7, 5, 5, 5), 0, 0));

        return panel;
    }

    @Override
    protected StartStopAction createTimeAction() {
        StartStopAction action = new StartStopAction("net") {
            @Override
            public void setEnabled(boolean value) {
                super.setEnabled(value);
                if (!value && isStarted())
                    actionPerformed(null);
            }
        };
        action.setActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setNetTime(action.getPeriod());
            }
        });
        return action;
    }

    @Override
    public void restore(Properties props) {
        setNetTime(Long.parseLong(props.getProperty(NET_TIME_PROPERTY, "0")));
        if (Boolean.parseBoolean(props.getProperty(IS_ENABLED_PROPERTY, "false")))
            getTimeButton().setEnabled(true);
        if (Boolean.parseBoolean(props.getProperty(IS_STARTED_PROPERTY, "false")))
            getTimeButton().doClick();
        getTimeAction().restore(props);
    }

    @Override
    public void store(Properties props) {
        getTimeAction().store(props);
        props.setProperty(NET_TIME_PROPERTY, String.valueOf(netTimeMillis));
        props.setProperty(IS_STARTED_PROPERTY, String.valueOf(getTimeAction().isStarted()));
        props.setProperty(IS_ENABLED_PROPERTY, String.valueOf(getTimeButton().isEnabled()));
    }

    public void setEnabled(boolean value) {
        getTimeAction().setEnabled(value);
    }

    @Override
    public void startNewDay() {
        setNetTime(0);
        getTimeAction().startNewDay();
        updateRatioImpl(0);

        if (getTimeAction().isStarted()) {
            getTimeButton().doClick();
        }
    }

    public void setRatioListener(ChangeListener listener) {
        this.listener = listener;
    }

    public void updateRatio(long grossTimeMillis) {
        if (netTimeMillis >= StartStopAction.TIMER_TICK)
            updateRatioImpl((double) netTimeMillis / grossTimeMillis);
        else
            updateRatioImpl(0);
    }

    private void setNetTime(long millis) {
        netTimeMillis = millis;
        netTime.setText(new TimeInMillis(netTimeMillis).toString());
    }

    private void updateRatioImpl(double value) {
        ratio.setText(String.format(RATIO_FORMAT, value));
        if (listener != null)
            listener.stateChanged(null);
    }

    public String getRatio() {
        return ratio.getText();
    }
}
