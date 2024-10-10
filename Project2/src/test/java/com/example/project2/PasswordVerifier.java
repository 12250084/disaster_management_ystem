package com.example.project2;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerifier {

    // Method to verify if the entered password matches the stored hash
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static void main(String[] args) {
        // The plain password entered by the user
        String plainPassword = "12345";

        // The hashed password stored in the database (for testing purposes)
        String storedHashedPassword = "$2a$10$Qh1uK0nnA1iWBTXL/4SjTuL5NQOS6RZcmrAld30CUoSnZb5YLvIVC";

        // Verify if the entered password matches the stored hash
        if (verifyPassword(plainPassword, storedHashedPassword)) {
            System.out.println("Password matches.");
        } else {
            System.out.println("Password does not match.");
        }
    }
}
