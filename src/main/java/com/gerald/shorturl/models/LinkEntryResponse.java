package com.gerald.shorturl.models;

public class LinkEntryResponse {
    private String shortUrlKey;

    public LinkEntryResponse(){

    }

    private LinkEntryResponse(String shortUrlKey){
        this.shortUrlKey = shortUrlKey;
    }

    public String getShortUrlKey() {
        return shortUrlKey;
    }

    public void setShortUrlKey(String shortUrlKey) {
        this.shortUrlKey = shortUrlKey;
    }

    public static LinkEntryResponse of(String shortUrlKey){
        return new LinkEntryResponse(shortUrlKey);
    }
}
