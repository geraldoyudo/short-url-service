package com.gerald.shorturl.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.temporal.ChronoUnit;

public class UrlDuration {
    @PositiveOrZero
    private long value;
    @NotNull
    private ChronoUnit unit = ChronoUnit.SECONDS;

    public UrlDuration() {
    }

    public UrlDuration(long value, ChronoUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public ChronoUnit getUnit() {
        return unit;
    }

    public void setUnit(ChronoUnit unit) {
        this.unit = unit;
    }

    public static UrlDuration of(long value, ChronoUnit unit){
        return new UrlDuration(value, unit);
    }
}
