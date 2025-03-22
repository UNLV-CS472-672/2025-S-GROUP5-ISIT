package com.example.ingrediscan.BackEnd.ApiCalls

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object FoodApiService {
    private const val BASE_URL = "https://api.nal.usda.gov/fdc/"
    // API key for authentication (replace with your actual API key)
    private const val API_KEY = "ApiKeyGoesHere"

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
    suspend fun fetchFoodInfo(foodName: String) {
        try {
            val response = api.searchFoods(foodName, API_KEY)

            val foodItem = response.foods.firstOrNull()
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
        } catch (e: Exception) {
            // Handle any errors that occur during the API call
            println("Error fetching food data: ${e.message}")
        }
    }
}

