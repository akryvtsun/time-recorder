package com.akryvtsun.timerecorder.ui.controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeStringTest {

    @Test
    public void correctZeroInterval() {
        assertEquals(" 0:00:00", new TimeString(0).get());
    }

    @Test
    public void correctOneSecInterval() {
        assertEquals(" 0:00:01", new TimeString(1000L).get());
    }

    @Test
    public void correctOneMinuteInterval() {
        assertEquals(" 0:01:00", new TimeString(60*1000L).get());
    }
}