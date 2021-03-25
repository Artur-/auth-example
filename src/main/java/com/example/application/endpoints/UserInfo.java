package com.example.application.endpoints;

import java.util.stream.Stream;

public class UserInfo {

    private String username;
    private String imageUrl;
    private Stream<String> authorities;

    public UserInfo(String username, Stream<String> authorities, String imageUrl) {
        this.username = username;
        this.authorities = authorities;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public Stream<String> getAuthorities() {
        return authorities;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
