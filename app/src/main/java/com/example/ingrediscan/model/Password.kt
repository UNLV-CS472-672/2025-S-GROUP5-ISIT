package com.example.ingrediscan.model

/**
 * Represents a user's password.
 *
 * @property value The password value.
 *
 * @throws IllegalArgumentException if the password length is less than 8 characters.
 */
class Password(private val value: String) {
    init {
        require(value.length >= 8) { "Password must be at least 8 characters long." }
        // If we're going to handle dealing with passwords ourselves rather then using Google
        // , then this will require much more rigorous validation.
    }

    // Example function for hashing
    fun hash(): String {
        // We will need to replace this with a secure hashing algorithm (e.g., Argon2, bcrypt)
        return "hashed($value)"
    }

    override fun toString(): String {
        return value
    }
}