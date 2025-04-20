package com.example.ingrediscan.BackEnd.ApiCalls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
// commented out since main isn't being used
// import kotlinx.coroutines.runBlocking
import com.example.ingrediscan.BuildConfig

object FoodApiService {
    private const val BASE_URL = "https://api.nal.usda.gov/fdc/"
    // API key for authentication
    // ADD "USDA_API_KEY = yourApiKey" to your local.properties file
    private const val API_KEY = BuildConfig.USDA_API_KEY

    // Retrofit instance configured with the base URL and Gson converter for JSON parsing
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Set the base URL for the API
        //// Add Gson converter for JSON parsing
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api: FoodDataApi = retrofit.create(FoodDataApi::class.java)

    // API Response Models
    data class FoodSearchResponse(
        val foods: List<FoodItem>
    )

    data class FoodItem(
        val description: String,
        val ingredients: String?, //Ingredients list (nullable if not provided)
        val foodNutrients: List<FoodNutrient>,
        val dataType: String
    )

    data class FoodNutrient(
        val nutrientName: String,
        val value: Double,
        val unitName: String
    )

    data class RequiredFoodNutrients(
        var protein: String = "0.0 G",
        var carbs: String = "0.0 G",
        var fats: String = "0.0 G",
        var calories: String = "0.0 KCAL",
        var sugar: String = "0.0 G",
        var fiber: String = "0.0 G",
        var cholesterol: String = "0.0 MG",
        var calcium: String = "0.0 MG",
        var iron: String = "0.0 MG",
        var vitaminA: String = "0.0 IU",
        var vitaminC: String = "0.0 MG",
        var vitaminD: String = "0.0 IU",
        var vitaminB6: String = "0.0 MG",
        var vitaminB12: String = "0.0 UG",
        var magnesium: String = "0.0 MG"
    )

    // Retrofit API interface defining the endpoint and parameters for food search
    interface FoodDataApi {
        @GET("v1/foods/search") // HTTP GET request to the food search endpoint
        suspend fun searchFoods(
            @Query("query") query: String, //Query parameter for the food name
            @Query("api_key") apiKey: String // // API key for authentication
        ): FoodSearchResponse // Returns a FoodSearchResponse object
    }

    // Wrapper Function to Fetch Food Information
    suspend fun fetchFoodInfo(foodName: String): RequiredFoodNutrients {
        var requiredFoodNutrients = RequiredFoodNutrients()

        try {
            val response = api.searchFoods(foodName, API_KEY)
            val filtered = response.foods.filter {
                it.dataType == "SR Legacy" || it.dataType == "Foundation"
            }
            // Will look for item with the most fields filled in
            val foodItem = response.foods.maxByOrNull { it.foodNutrients.size }

            if (foodItem != null) {
                foodItem.foodNutrients.forEach { nutrient ->
                    when (nutrient.nutrientName.lowercase()) {
                        "protein" -> requiredFoodNutrients.protein = "${nutrient.value} ${nutrient.unitName}"
                        "total lipid (fat)" -> requiredFoodNutrients.fats = "${nutrient.value} ${nutrient.unitName}"
                        "carbohydrate, by difference" -> requiredFoodNutrients.carbs = "${nutrient.value} ${nutrient.unitName}"
                        "energy" -> requiredFoodNutrients.calories = "${nutrient.value} ${nutrient.unitName}"
                        "total sugars" -> requiredFoodNutrients.sugar = "${nutrient.value} ${nutrient.unitName}"
                        "fiber, total dietary" -> requiredFoodNutrients.fiber = "${nutrient.value} ${nutrient.unitName}"
                        "cholesterol" -> requiredFoodNutrients.cholesterol = "${nutrient.value} ${nutrient.unitName}"
                        "calcium, ca" -> requiredFoodNutrients.calcium = "${nutrient.value} ${nutrient.unitName}"
                        "iron, fe" -> requiredFoodNutrients.iron = "${nutrient.value} ${nutrient.unitName}"
                        "vitamin a, iu" -> requiredFoodNutrients.vitaminA = "${nutrient.value} ${nutrient.unitName}"
                        "vitamin b-6" -> requiredFoodNutrients.vitaminB6 = "${nutrient.value} ${nutrient.unitName}"
                        "vitamin b-12" -> requiredFoodNutrients.vitaminB12 = "${nutrient.value} ${nutrient.unitName}"
                        "magnesium, mg" -> requiredFoodNutrients.magnesium = "${nutrient.value} ${nutrient.unitName}"
                    }
                    // Special logic for Vitamin D (prefer D2+D3)
                    // First, try to find the most specific entry: "Vitamin D (D2 + D3)"
                    val d2d3 = foodItem.foodNutrients.find { it.nutrientName.equals("Vitamin D (D2 + D3)", ignoreCase = true) }
                    // If not found, fall back to any entry that contains "vitamin d"
                    val dFallback = foodItem.foodNutrients.find { it.nutrientName.contains("vitamin d", ignoreCase = true) }
                    // Use whichever was found first (D2+D3 preferred)
                    val dFinal = d2d3 ?: dFallback
                    dFinal?.let {
                        requiredFoodNutrients.vitaminD = "${it.value} ${it.unitName}"
                    }

                    // --- Fallback logic for Vitamin C ---
                    // If no Vitamin C was already captured, attempt to find any nutrient that contains "vitamin c"
                    if (requiredFoodNutrients.vitaminC == "0.0 MG") {
                        foodItem.foodNutrients.firstOrNull {
                            it.nutrientName.contains("vitamin c", ignoreCase = true)
                        }?.let {
                            requiredFoodNutrients.vitaminC = "${it.value} ${it.unitName}"
                        }
                    }
                }
                // --- Averages ---
                val proteinValues = filtered.mapNotNull {
                    it.foodNutrients.find { n -> n.nutrientName.equals("Protein", ignoreCase = true) }?.value
                }
                val avgProtein = proteinValues.averageOrNull()

                val calorieValues = filtered.mapNotNull {
                    it.foodNutrients.find { n -> n.nutrientName.equals("Energy", ignoreCase = true) }?.value
                }
                val avgCalories = calorieValues.averageOrNull()

                val carbValues = filtered.mapNotNull {
                    it.foodNutrients.find { n -> n.nutrientName.equals("Carbohydrate, by difference", ignoreCase = true) }?.value
                }
                val avgCarbs = carbValues.averageOrNull()

                //println("\nAverages across ${filtered.size} items:")
                println("Protein: ${"%.2f".format(avgProtein ?: 0.0)} G")
                println("Calories: ${"%.2f".format(avgCalories ?: 0.0)} KCAL")
                println("Carbohydrates: ${"%.2f".format(avgCarbs ?: 0.0)} G")

                println("Fats: ${requiredFoodNutrients.fats}")
                println("Sugar: ${requiredFoodNutrients.sugar}")
                println("Fiber: ${requiredFoodNutrients.fiber}")
                println("Cholesterol: ${requiredFoodNutrients.cholesterol}")
                println("Calcium: ${requiredFoodNutrients.calcium}")
                println("Iron: ${requiredFoodNutrients.iron}")
                println("Magnesium: ${requiredFoodNutrients.magnesium}")
                println("Vitamin A: ${requiredFoodNutrients.vitaminA}")
                println("Vitamin B-6: ${requiredFoodNutrients.vitaminB6}")
                println("Vitamin B-12: ${requiredFoodNutrients.vitaminB12}")
                println("Vitamin C: ${requiredFoodNutrients.vitaminC}")
                println("Vitamin D: ${requiredFoodNutrients.vitaminD}")
            } else {
                println("No results found for $foodName")
                requiredFoodNutrients = RequiredFoodNutrients(
                    protein = "N/A",
                    carbs = "N/A",
                    fats = "N/A",
                    calories = "N/A",
                    sugar = "N/A",
                    fiber = "N/A",
                    cholesterol = "N/A",
                    calcium = "N/A",
                    iron = "N/A",
                    vitaminA = "N/A",
                    vitaminC = "N/A",
                    vitaminD = "N/A",
                    vitaminB6 = "N/A",
                    vitaminB12 = "N/A",
                    magnesium = "N/A"
                )
            }
        } catch (e: Exception) {
            println("Error fetching food data: ${e.message}")
            requiredFoodNutrients = RequiredFoodNutrients(
                protein = "N/A",
                carbs = "N/A",
                fats = "N/A",
                calories = "N/A",
                sugar = "N/A",
                fiber = "N/A",
                cholesterol = "N/A",
                calcium = "N/A",
                iron = "N/A",
                vitaminA = "N/A",
                vitaminC = "N/A",
                vitaminD = "N/A",
                vitaminB6 = "N/A",
                vitaminB12 = "N/A",
                magnesium = "N/A"
            )
        }

        return requiredFoodNutrients
    }
    private fun List<Double>.averageOrNull(): Double? = if (this.isNotEmpty()) this.average() else null
}

// Example of how to retrieve data from the foodItem object
/*
fun main() = runBlocking {
    val foodName = "clam chowder" // Example food name to search

    val nutrients = FoodApiService.fetchFoodInfo(foodName)
}
*/
