package projectcode;

import java.util.LinkedHashMap;
import java.util.Map;

/*
  Manages the user's food selections and running calorie total.
 */
public class Food
{
    private double totalCalories;
    private final Map<FoodList, Double> selectedFood;   // food → grams

    public Food()
    {
        this.totalCalories = 0.0;
        this.selectedFood  = new LinkedHashMap<>();      //create a new linked hash map
    }

    

    /*
     selects a new food from the FoodList
     */
    public void selectFood(FoodList food, double amount)
    {
        if (food == null || amount < 0.0) 
        	{ //checks if inputs are valid
        	return;
        	}
        amount = round1(amount); //round
        selectedFood.merge(food, amount, Double::sum); //add it to the linkedhashmap
        totalCalories += amount * food.getCaloriesPerGram(); //increment the total calories
    }

    

    /*
      Removes a food from the food list
     */
    public boolean removeFood(FoodList food, double amountToRemove)
    {
        if (food == null || amountToRemove < 0.0)
        	{ //checks if input is valid
        	return false;
        	}
        amountToRemove = round1(amountToRemove); //round the amount to remove

        double current = selectedFood.getOrDefault(food, 0.0); //get the amount of grams the food currently has
        if (current == 0.0 || amountToRemove > current)
        	{ //if the amount the remove is greater then the current amount in there then exit function
        	return false;
        	}

        totalCalories -= food.getCaloriesPerGram() * amountToRemove; //deincrement the total calories

        double remaining = round1(current - amountToRemove); //round
        if (remaining == 0.0) //if there is no remaining grams of food remove it from the map
        {
            selectedFood.remove(food);
        }
        else
        {
            selectedFood.put(food, remaining); //otherwise update the total
        }

        return true;
    }

    //Getters

    // Returns the total logged calories, rounded to 1 decimal place. 
    public double getTotalCalories()
    {
        return round1(totalCalories);
    }

    // Returns the map
    public Map<FoodList, Double> getAllFood()
    {
        return selectedFood;
    }

    // Clears all food entries and resets the calorie counter. 
    public void reset()
    {
        selectedFood.clear();
        totalCalories = 0.0;
    }

    

    private static double round1(double v)
    { //round function so we dont have to repeat it
        return Math.round(v * 10.0) / 10.0;
    }
}
