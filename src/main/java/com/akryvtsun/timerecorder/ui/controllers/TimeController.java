package com.akryvtsun.timerecorder.ui.controllers;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Abstract time controller implementation.
 *
 * @author kontiky
 */
public abstract class TimeController {
    protected static final String ENTITY_PREFIX = "controller";

    private StartStopAction timeAction;
    private Component viewComponent;
    private JButton timeButton;

    public Component getViewComponent() {
        if (viewComponent == null)
            viewComponent = createViewComponent();
        return viewComponent;
    }

    public JButton getTimeButton() {
        if (timeButton == null)
            timeButton = new JButton(getTimeAction());
        return timeButton;
    }

    public StartStopAction getTimeAction() {
        if (timeAction == null)
            timeAction = createTimeAction();
        return timeAction;
    }

    protected static JTextField createTimeField() {
        JTextField field = new JTextField(5);
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setEditable(false);
        return field;
    }

    protected abstract StartStopAction createTimeAction();

    protected abstract Component createViewComponent();

    public abstract void startNewDay();
}
