package com.akryvtsun.timerecorder.controllers;

import java.awt.Component;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JTextField;

/**
 * Abstract time controller implementation.
 *
 * @author kontiky
 */
abstract class TimeController {
    private final static String DATE_FORMAT = "%2d:%02d:%02d";
    
    protected static final String ENTITY_PREFFIX = "controller";
    
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

	protected static String getStringTime(long millis) {
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long mins = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(mins);
		long secs = TimeUnit.MILLISECONDS.toSeconds(millis);
		return String.format(DATE_FORMAT, hours, mins, secs);
	}
}
