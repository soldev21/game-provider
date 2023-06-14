package com.megafair.cache;

import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.StringJoiner;

public class RedisKeyHasher {

    @SneakyThrows
    public static String hashCompoundKey(String ... keys) {
        StringJoiner joiner = new StringJoiner("_");
        Arrays.stream(keys).forEach(joiner::add);
        byte[] compoundKeyBytes = joiner.toString().getBytes(StandardCharsets.UTF_8);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(compoundKeyBytes);

        return bytesToHex(hashBytes);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : bytes) {
            String hexString = String.format("%02x", b);
            hexStringBuilder.append(hexString);
        }
        return hexStringBuilder.toString();
    }
}