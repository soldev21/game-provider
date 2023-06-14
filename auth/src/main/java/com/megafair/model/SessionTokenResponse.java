package com.megafair.model;

public record SessionTokenResponse(Response response, Payload data) {
    public record Payload(String sessionToken, String url) {
    }
}
