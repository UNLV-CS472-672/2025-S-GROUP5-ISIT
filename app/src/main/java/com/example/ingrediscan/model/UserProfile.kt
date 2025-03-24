package com.example.ingrediscan.model

import java.time.LocalDate
import java.util.UUID

/**
 * Represents a user's profile information, which is optional and can be added after initial user creation.
 *
 * This class is designed to be associated with a [User] object.
 * The relationship between [User] and [UserProfile] is one-to-one, meaning each [User] can have at most one [UserProfile].
 * The link between a [User] and its [UserProfile] is established through the [userId], which must be identical in both objects.
 *
 * The [User] class primarily holds core account information (email, password, etc.),
 * while this [UserProfile] class holds personal details and preferences.
 *
 *
 * @property userId The UUID of the corresponding User object.
 * @property firstName User's first name.
 * @property lastName User's last name.
 * @property dateOfBirth User's date of birth (optional).
 * @property sex User's sex assigned at birth (optional).
 * @property weightLogs List of [WeightLog] entries for tracking weight history (optional).
 * @property weightGoalKg User's current weight goal (optional).
 * @property heightCm User's height in centimeters (optional).
 * @property profileImageUrl URL to the user's profile image (optional).
 * @property updatedAt Timestamp indicating the last time the user's profile data was updated.
 */
data class UserProfile(
    val userId: UUID, // Crucial link to the User
    private var firstName: String? = null,
    private var lastName: String? = null,
    var dateOfBirth: LocalDate? = null,
    var sex: Sex? = null,
    var weightLogs: List<WeightLog> = emptyList(),
    var weightGoalKg: Double? = null,
    var heightCm: Double? = null,
    var profileImageUrl: String? = null,
    val updatedAt: LocalDate = LocalDate.now()
) {
    init{
        require(firstName?.isNotBlank() == true) {"First name cannot be blank."}
        require(lastName?.isNotBlank() == true) {"Last name cannot be blank."}
        //require(weightLogs.all { it.weightKg > 0 }) {"Weight logs must be greater than zero."}
    }

    /**
     * Sets the first name of the user.
     *
     * @param newFirstName the new first name of the user.
     * @throws IllegalArgumentException if the name is empty.
     */
    fun setFirstName(newFirstName: String) {
        require(newFirstName.isNotBlank()) { "First name cannot be empty." }
        firstName = newFirstName.trim().replaceFirstChar { it.uppercase() }
    }

    /**
     * Gets the first name of the user.
     *
     * @return the first name of the user or null if there is none.
     */
    fun getFirstName(): String? {
        return firstName
    }

    /**
     * Sets the last name of the user.
     *
     * @param newLastName the new last name of the user.
     * @throws IllegalArgumentException if the name is empty.
     */
    fun setLastName(newLastName: String) {
        require(newLastName.isNotBlank()) { "Last name cannot be empty." }
        lastName = newLastName.trim().replaceFirstChar { it.uppercase() }
    }

    /**
     * Gets the last name of the user.
     *
     * @return the last name of the user or null if there is none.
     */
    fun getLastName(): String? {
        return lastName
    }

    /**
     * Sets the date of birth of the user.
     *
     * @param newDateOfBirth the new date of birth of the user.
     */
    fun updateDateOfBirth(newDateOfBirth: LocalDate) {
        dateOfBirth = newDateOfBirth
    }

    /**
     * Sets the Sex of the user.
     *
     * @param newSex the new Sex of the user.
     */
    fun updateSex(newSex: Sex) {
        sex = newSex
    }

    /**
     * Gets the current weight of the user from the weight logs.
     *
     * @return the latest weight of the user or null if there is no weight log.
     */
    fun getCurrentWeight(): Double? {
        return weightLogs.maxByOrNull { it.date }?.weightKg
    }

    /**
     * Calculates the current BMI of the user.
     *
     * @return the bmi of the user, or null if the user's current weight is not available.
     */
    fun calculateCurrentBmi(): Double? {
        val currentWeight = getCurrentWeight() ?: return null
        val heightMeters = heightCm?.div(100.0) ?: return null
        return currentWeight / (heightMeters * heightMeters)
    }
}

enum class Sex {
    MALE, FEMALE, OTHER
}