package com.acmecorp.users;

public class User {

    // Looks like PII, no classification annotation -- flagged.
    String email;

    // Classified with a real annotation -- not flagged.
    @Sensitive
    String phoneNumber;

    // Not PII-shaped -- not flagged.
    String username;
}
