package com.example.ingrediscan.BackEnd.MealLog
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Unit Test for MealLogManager*/

class MealTest {
    private lateinit var mealLog : MealLog

    @BeforeEach
    fun setUp(){
        mealLog = MealLog()
        mealLog.setCalorieGoal(3000)
    }

    /** test inital state of MealLog*/
    @Test
    fun `test initial state`(){
        assertEquals(3000,mealLog.caloriesLeft())
        assertFalse(mealLog.caloriesOver())
        assertEquals(0,mealLog.getTotalConsumed())
    }

    /**test adddes a meal and gives the calories left*/
    @Test
    fun `test adding a meal and checks total and remaining calories`(){
        mealLog.logMeal(500, MealType.LUNCH, "Steak Salad")
        assertEquals(500, mealLog.getTotalConsumed(), "should be equal")
        assertEquals(2500, mealLog.caloriesLeft(),"calories should be 2500 left")
        assertFalse(mealLog.caloriesOver(), "total calories have not been exceeded")
    }
    /** Test that exceeds the calories goals trigger an over status*/
    @Test
    fun `test calorie limit over`(){
        mealLog.logMeal(3500, MealType.MEAL, "Mcdonalds")
        assertTrue(mealLog.caloriesOver(),"Should be true if calories consumed exceed daily calories")
        assertEquals(0, mealLog.caloriesLeft(), "should be zero")
    }

    /** test to remove the last meal*/
    @Test
    fun `removes the last meal`(){
        mealLog.logMeal(1000, MealType.SNACK, "Spicy Mcchicken")
        mealLog.logMeal(1500, MealType.DINNER, "Pineapple Pizza")
        assertEquals(2500, mealLog.getTotalConsumed(), "1000+1500 = 2500")

        //should remove pizza
        mealLog.removeLastMeal()
        assertEquals(1000, mealLog.getTotalConsumed(),"Mcchicken should be left 1000 cals")

        //remove remaining
        mealLog.removeLastMeal()
        assertEquals(0,mealLog.getTotalConsumed(), "should be zero")
    }

}