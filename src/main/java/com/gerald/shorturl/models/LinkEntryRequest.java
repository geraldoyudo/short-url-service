package com.gerald.shorturl.models;

import org.hibernate.validator.constraints.URL;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.temporal.ChronoUnit;

public class LinkEntryRequest {
    @URL(message = "url is invalid")
    @NotEmpty
    private String url;
    @NotNull
    @Valid
    private UrlDuration duration;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UrlDuration getDuration() {
        return duration;
    }

    public void setDuration(UrlDuration duration) {
        this.duration = duration;
    }
}
