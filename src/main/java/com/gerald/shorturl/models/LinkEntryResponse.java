package com.gerald.shorturl.models;

public class LinkEntryResponse {
    private String shortUrl;

    public LinkEntryResponse(){

    }

    private LinkEntryResponse(String shortUrl){
        this.shortUrl = shortUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public static LinkEntryResponse of(String shortUrlKey){
        return new LinkEntryResponse(shortUrlKey);
    }
}
