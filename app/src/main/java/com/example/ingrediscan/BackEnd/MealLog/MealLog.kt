package com.example.ingrediscan.BackEnd.MealLog

/**user can choose types of meals*/
enum class MealType{ SNACK, BREAKFAST, LUNCH, DINNER, MEAL
}
/**
 * Data class representing a single meal entry.
 *
 * @property intake The calorie intake for this meal entry.
 * @property type The type of meal, represented as a [MealType].
 * @property description A description of the meal.
 */
data class MealEntry(val intake:Int, val type:MealType, val meal: String)

class MealLog {
    private var calorieGoal: Int = 0
    private val mealEntries = mutableListOf<MealEntry>()

    /**set the users daily calorie goal*/
    fun setCalorieGoal(goal: Int){
        calorieGoal = goal
    }
    /**logs a meal entry and add it to a list*/
    fun logMeal(inTake:Int, type:MealType, food:String ){
        val entry = MealEntry(inTake, type, food)
        mealEntries.add(entry)
    }

    /**removes last meal incase it was a mistake*/
    fun removeLastMeal(){
        if(mealEntries.isNotEmpty()){
            mealEntries.removeAt(mealEntries.lastIndex)
        }
        else
            println("No meals to remove")
    }
    /**@returns true if the user has exceeded their daily calorie consumption*/
    fun caloriesOver(): Boolean{
        return getTotalConsumed() > calorieGoal
    }

    /** @returns the calories left a user can consume*/
    fun caloriesLeft(): Int{
        return maxOf(calorieGoal - getTotalConsumed(),0)
    }

    /** @returns the users daily calorie goal*/
    fun getGoal():Int{
        return calorieGoal
    }

    /** @return the total calories consumed*/
    fun getTotalConsumed():Int{
        return mealEntries.sumOf{it.intake }
    }
    /** @return the list of meals log */
    fun getAllMeals():List<MealEntry>{
        return mealEntries.toList()
    }


}