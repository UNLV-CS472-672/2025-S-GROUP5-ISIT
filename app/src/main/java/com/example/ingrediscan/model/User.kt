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
 *
 * @property userScannedData A list of user's scanned data.
 */
class User(
    private val userId: UUID = UUID.randomUUID(),
    val email: Email,
    val password: Password,
    val createdAt: LocalDate = LocalDate.now(),
    val updatedAt: LocalDate = LocalDate.now(),

    private val userScannedData: MutableList<ScannedItem> = mutableListOf()
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

    /**
     * Getter for user to access/retrieve the list of scanned item.
     *
     * @return the list of user's list of scanned item.
     */
    fun getScannedData(): List<ScannedItem>{
        return userScannedData.toList()
    }

    /**
     * For user to add a new scanned item into the list.
     *
     * @param newItem an object of ScannedItem.
     */
    fun addScannedItem(newItem: ScannedItem) {
        userScannedData.add(newItem)
    }

    /**
     * Method to remove an item from the userScannedData list.
     *
     * @param id the itemID of the the scannedItem.
     * @return true if the id can be found in the list and remove the scannedItem from list,
     *         false if the id can't be found
     */
    fun removeScannedItem(id: Int): Boolean {
        val index = userScannedData.indexOfFirst { it.itemID == id }

        return if (index != -1) {
            userScannedData.removeAt(index)
            true
        } else {
            // maybe needed to print out an error message
            false  // Item not found
        }
    }

    /**
     * Method to modify/update an scanned item by using scanned item id.
     *
     * @param id the itemID of the the scannedItem.
     * @param newItem an object of ScannedItem.
     * @return true if the id can be found in the list and update that scannedItem,
     *         false if the id can't be found
     */
    fun updateScannedItem(id: Int, newItem: ScannedItem): Boolean {
        val index = userScannedData.indexOfFirst { it.itemID == id }

        return if (index != -1) {
            userScannedData[index] = newItem
            true
        } else {
            // maybe needed to print out an error message
            false  // Item not found
        }
    }
}
