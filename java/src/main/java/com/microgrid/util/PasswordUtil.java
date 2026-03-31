package com.microgrid.util;

/**
 * Utility class for password hashing and verification.
 * It follows SOLID single responsibility by focusing only on password security.
 * It also uses a private constructor and static methods to prevent instantiation,
 * which is a lightweight creational pattern for utility classes.
 */

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordUtil {

    private static final int BCRYPT_COST = 12;

    private PasswordUtil() {} // Prevent instantiation

    /**
     * Hash a plain-text password.
     */
    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }

    /**
     * Verify a plain-text password against a BCrypt hash.
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword);
        return result.verified;
    }
}
