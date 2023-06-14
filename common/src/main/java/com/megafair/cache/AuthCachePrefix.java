package com.megafair.cache;

public enum AuthCachePrefix {
    TOKEN("token"),
    SECRET("secret"),
    SESSION_TOKENS("session_tokens");

    AuthCachePrefix(String name) {
        this.name=name;
    }
    private final String name;
}
