package com.example.ingrediscan.BackEnd

import com.example.ingrediscan.BackEnd.ApiCalls.FoodApiService
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.test.runTest // Import this

/**
 * Unit tests for the FoodApiService, which mock API responses
 * to verify correct handling of successful, empty, and error cases.
 * These tests run on the local development machine (host) using MockWebServer.
 *
 * See [MockWebServer documentation](https://square.github.io/okhttp/mockwebserver/)
 * for details on mocking API responses in unit tests.
 */

class FoodApiServiceTest {
    // A mock web server that simulates API responses.
    private lateinit var mockWebServer: MockWebServer
    // A test instance of FoodDataApi created with the mock server.
    private lateinit var api: FoodApiService.FoodDataApi

    /**
     * Set up the test environment before each test case.
     * Initializes the MockWebServer and configures Retrofit to use it.
     */
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Configure Retrofit to use the MockWebServer's URL and Gson for JSON parsing
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(FoodApiService.FoodDataApi::class.java)
    }

    /**
     * Clean up the test environment after each test case.
     * Shuts down the MockWebServer to release resources.
     */
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Test case for a successful food search response.
     * Verifies that the API correctly parses and returns food data.
     */
    // SUCCESSFUL CASE
    @Test
    fun `test successful food search response`() = runBlocking {
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "foods": [
                        {
                            "description": "Apple",
                            "ingredients": "Apple",
                            "foodNutrients": [
                                {
                                    "nutrientName": "Energy",
                                    "value": 52.0,
                                    "unitName": "kcal"
                                }
                            ]
                        }
                    ]
                }
            """)
            .setResponseCode(200) // Simulate a successful HTTP response

        // Enqueue the mock response to be returned by the mock server
        mockWebServer.enqueue(mockResponse)

        // Call the API and verify the response
        val response = api.searchFoods("Apple", "test_api_key")
        assertEquals(1, response.foods.size) // Verify that one food item is returned
        assertEquals("Apple", response.foods[0].description) // Verify the food description
        assertEquals("Energy", response.foods[0].foodNutrients[0].nutrientName) // Verify nutrient name
        assertEquals(52.0, response.foods[0].foodNutrients[0].value, 0.1) // Verify nutrient value
    }

    /**
     * Test case for an empty food search response.
     * Verifies that the API correctly handles cases where no food items are found.
     */
    // EMPTY CASE
    @Test
    fun `test empty food search response`() = runBlocking {
        // Mock response with an empty list of foods
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "foods": []
                }
            """)
            .setResponseCode(200) // Simulate a successful HTTP response

        // Enqueue the mock response to be returned by the mock server
        mockWebServer.enqueue(mockResponse)

        // Call the API and verify the response
        val response = api.searchFoods("UnknownFood", "test_api_key")
        assertEquals(0, response.foods.size) // Verify that no food items are returned
    }

    /**
     * Test case for an API error response.
     * Verifies that the API correctly handles HTTP errors (e.g., server errors).
     */
    // ERROR CASE
    @Test
    fun `test API error response`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        // Use runCatching to capture the exception
        val exception = runCatching { api.searchFoods("Apple", "test_api_key") }.exceptionOrNull()

        // Verify that the exception is an HttpException with status code 500
        assert(exception is retrofit2.HttpException)
        assertEquals(500, (exception as retrofit2.HttpException).code())
    }

    /**
     * Test case for an empty API key.
     * Verifies that the API correctly handles cases where the API key is an empty string.
     */
    @Test
    fun `test empty API key`() = runBlocking {
        // Mock response with an empty list of foods
        val mockResponse = MockResponse()
            .setBody("""
                {
                    "foods": []
                }
            """)
            .setResponseCode(401) // Simulate an unauthorized HTTP response (empty API key)

        // Enqueue the mock response to be returned by the mock server
        mockWebServer.enqueue(mockResponse)

        // Use runCatching to capture the exception
        val exception = runCatching { api.searchFoods("Apple", "") }.exceptionOrNull()

        // Verify that the exception is an HttpException with status code 401
        assert(exception is retrofit2.HttpException)
        assertEquals(401, (exception as retrofit2.HttpException).code())
    }
}