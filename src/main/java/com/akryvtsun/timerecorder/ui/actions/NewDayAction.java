package com.akryvtsun.timerecorder.ui.actions;

import com.akryvtsun.timerecorder.ui.Functions;
import com.akryvtsun.timerecorder.ui.controllers.TimeController;

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
