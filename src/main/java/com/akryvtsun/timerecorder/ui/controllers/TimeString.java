package com.akryvtsun.timerecorder.ui.controllers;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public final class TimeString implements Supplier<String> {
    private final static String DATE_FORMAT = "%2d:%02d:%02d";

    private final long millis;

    public TimeString(long millis) {
        this.millis = millis;
    }

    @Override
    public String get() {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long balance = millis - TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(balance);
        balance -= TimeUnit.MINUTES.toMillis(minutes);
        long secs = TimeUnit.MILLISECONDS.toSeconds(balance);
        return String.format(DATE_FORMAT, hours, minutes, secs);
    }
}
