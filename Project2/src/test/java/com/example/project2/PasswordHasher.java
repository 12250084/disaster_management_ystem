package com.example.project2;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    public static void main(String[] args) {
        // Password to hash
        String passwordToHash = "admin123";

        // Hash the password with BCrypt
        String hashedPassword = BCrypt.hashpw(passwordToHash, BCrypt.gensalt());

        // Output the hashed password
        System.out.println("Hashed password: " + hashedPassword);
    }
}

