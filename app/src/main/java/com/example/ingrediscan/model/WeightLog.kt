package com.example.ingrediscan.model

import java.time.LocalDate

/**
 * Represents a weight log entry for a user.
 *
 * @property date The date of the weight measurement.
 * @property weightKg The weight measured in kilograms.
 */
data class WeightLog(
    val date: LocalDate,
    val weightKg: Double
)