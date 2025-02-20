package com.shoes.webshoes.security;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Base64;
public class Base64PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] encodedBytes = Base64.getEncoder().encode(rawPassword.toString().getBytes());
        return new String(encodedBytes);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String encodedRawPassword = encode(rawPassword);
        return encodedRawPassword.equals(encodedPassword);
    }
}