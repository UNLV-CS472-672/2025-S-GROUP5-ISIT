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
        val foodNutrients: List<FoodNutrient>
    )

    data class FoodNutrient(
        val nutrientName: String,
        val value: Double,
        val unitName: String
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
    suspend fun fetchFoodInfo(foodName: String): FoodItem? {
        return try {
            val response = api.searchFoods(foodName, API_KEY)
            // Return the first FoodItem if available, otherwise null
            response.foods.firstOrNull()
        } catch (e: Exception) {
            // Handle any errors that occur during the API call
            println("Error fetching food data: ${e.message}")
            null // return null
        }
    }
}

// Example of how to retrieve data from the foodItem object
/*
fun main() = runBlocking {
    val foodName = "bubble gum" // Example food name to search
    val foodItem: FoodApiService.FoodItem? = FoodApiService.fetchFoodInfo(foodName)

    if (foodItem != null) {
        println("Food: ${foodItem.description}")
        println("Ingredients: ${foodItem.ingredients ?: "No ingredients listed"}")

        // Print the list of nutrients
        println("Nutrients:")
        foodItem.foodNutrients.forEach { nutrient ->
            println("${nutrient.nutrientName}: ${nutrient.value} ${nutrient.unitName}")
        }
    } else {
        // Print a message if no food items were found
        println("No results found for $foodName")
    }
}
*/
