package com.example.server.security.jwt;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.Key;
import java.util.Base64;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        // Tạo khóa JWT mới với thuật toán HS256 (256 bits)
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Mã hóa khóa thành Base64
        String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());

        // In khóa Base64 ra màn hình
        System.out.println("Base64 Encoded JWT Key: " + base64Key);
    }
}
