package com.example.ingrediscan.model

import java.time.LocalDate
import java.util.UUID

/**
 * Represents a User in the application.
 *
 * This class holds the core account information necessary for user identification, authentication, and authorization.
 * Each [User] can have an associated [UserProfile], but the [User] object can exist independently of the profile.
 * The link between a [User] and its [UserProfile] is established through the [userId], which is identical in both objects.
 * The [UserProfile] class will hold optional user preferences and details
 *
 * @property userId Unique identifier for the user.
 * @property email User's email address (used for login).
 * @property password User's password (hashed and salted).
 * @property createdAt Timestamp indicating when the user was created.
 * @property updatedAt Timestamp indicating the last time the user's data was updated.
 */
data class User(
    private val userId: UUID = UUID.randomUUID(),
    val email: Email,
    val password: Password,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now(),
) {
    init {
        // Basic validation upon creating a user. May need to add requirements as proj progresses
    }

    fun getUserID(): UUID {
        return userId
    }

    companion object {
        /**
         * Creates a new user with the required attributes.
         *
         * @param email the email of the new user.
         * @param password the password of the new user.
         * @return the user object.
         */
        fun createUser(email: Email, password: Password): User {
            return User(email = email, password = password)
        }
    }
}
