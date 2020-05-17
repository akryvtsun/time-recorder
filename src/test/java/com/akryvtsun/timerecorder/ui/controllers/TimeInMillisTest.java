package com.akryvtsun.timerecorder.ui.controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeInMillisTest {

    @Test
    public void correctZeroInterval() {
        assertEquals(" 0:00:00", new TimeInMillis(0).toString());
    }

    @Test
    public void correctOneSecInterval() {
        assertEquals(" 0:00:01", new TimeInMillis(1000L).toString());
    }

    @Test
    public void correctOneMinuteInterval() {
        assertEquals(" 0:01:00", new TimeInMillis(60*1000L).toString());
    }
}