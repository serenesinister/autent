package com.example.demo.dto;

public record UserRequest(
        String name,
        String email,
        String password
) {
}
