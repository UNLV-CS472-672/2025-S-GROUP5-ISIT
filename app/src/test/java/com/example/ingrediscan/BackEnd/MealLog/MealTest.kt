package com.example.ingrediscan.BackEnd.MealLog
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
/** Unit Test for MealLogManager*/

class MealTest {
    private lateinit var mealLog : MealLog

    @Before
    fun setUp(){
        mealLog = MealLog()
        mealLog.setCalorieGoal(3000)
    }

    /** test inital state of MealLog*/
    @Test
    fun testInitialState(){
        assertEquals(3000,mealLog.caloriesLeft())
        assertFalse(mealLog.caloriesOver())
        assertEquals(0,mealLog.getTotalConsumed())
    }

    /**test adddes a meal and gives the calories left*/
    @Test
    fun testAddingAmealAndChecksTotatAndRemainingCalories(){
        mealLog.logMeal(500, MealType.LUNCH, "Steak Salad")
        assertEquals("should be equal",500, mealLog.getTotalConsumed() )
        assertEquals("calories should be 2500 left",2500, mealLog.caloriesLeft())
        assertFalse("total calories have not been exceeded",mealLog.caloriesOver() )
    }
    /** Test that exceeds the calories goals trigger an over status*/
    @Test
    fun testCalorieLimitOver(){
        mealLog.logMeal(3500, MealType.MEAL, "Mcdonalds")
        assertTrue("Should be true if calories consumed exceed daily calories",mealLog.caloriesOver())
        assertEquals("should be zero",0, mealLog.caloriesLeft() )
    }

    /** test to remove the last meal*/
    @Test
    fun removesTheLastMeal(){
        mealLog.logMeal(1000, MealType.SNACK, "Spicy Mcchicken")
        mealLog.logMeal(1500, MealType.DINNER, "Pineapple Pizza")
        assertEquals("1000+1500 = 2500",2500, mealLog.getTotalConsumed())

        //should remove pizza
        mealLog.removeLastMeal()
        assertEquals("Mcchicken should be left 1000 cals",1000, mealLog.getTotalConsumed())

        //remove remaining
        mealLog.removeLastMeal()
        assertEquals("should be zero",0,mealLog.getTotalConsumed() )
    }

}