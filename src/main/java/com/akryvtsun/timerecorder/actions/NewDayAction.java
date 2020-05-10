package com.akryvtsun.timerecorder.actions;

import com.akryvtsun.timerecorder.Functions;
import com.akryvtsun.timerecorder.controllers.StartStopAction;
import com.akryvtsun.timerecorder.controllers.TimeController;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class NewDayAction extends AbstractAction {
    private final TimeController[] controllers;

    public NewDayAction(TimeController... controllers) {
        super("New Workday", Functions.getIcon("new"));
        this.controllers = controllers;
        putValue(Action.MNEMONIC_KEY, Integer.valueOf('N'));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (TimeController c: controllers) {
            c.startNewDay();
        }
    }
}
