package com.akryvtsun.timerecorder.ui.controllers;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract time controller implementation.
 *
 * @author kontiky
 */
public abstract class TimeController {
    protected static final String ENTITY_PREFIX = "controller";

    private final StartStopAction timeAction;
    private final JButton timeButton;
    private Component viewComponent;

    protected TimeController(StartStopAction timeAction) {
        this.timeAction = timeAction;
        this.timeButton = new JButton(timeAction);
    }

    public Component getViewComponent() {
        return viewComponent;
    }

    public JButton getTimeButton() {
        return timeButton;
    }

    public StartStopAction getTimeAction() {
        return timeAction;
    }

    protected void setViewComponent(Component viewComponent) {
        this.viewComponent = viewComponent;
    }

    protected static JTextField createTimeField() {
        JTextField field = new JTextField(5);
        field.setHorizontalAlignment(JTextField.RIGHT);
        field.setEditable(false);
        return field;
    }

    public abstract void startNewDay();
}
