package projectcode;

import java.util.HashMap;
import java.util.Map;

public class Food 
{
    private double totalCalories;
    private Map<FoodList, Double> selectedFood;

    public Food() 
    {
        this.totalCalories = 0;
        selectedFood = new HashMap<>();
    }

    public void selectFood(FoodList food, double amount)
    {
        amount = Math.round(amount * 10.0) / 10.0;
        if (food == null || amount < 0.0) return;
        selectedFood.put(food, (selectedFood.getOrDefault(food, 0.0) + amount));
        this.totalCalories += (amount * food.getCaloriesPerGram());
    }

    public boolean removeFood(FoodList food, double amountToRemove)
    {
        amountToRemove = Math.round(amountToRemove * 10.0) / 10.0;
        if (food == null || amountToRemove < 0.0) return false;

        double currentAmount = selectedFood.getOrDefault(food, 0.0);
        if (currentAmount == 0.0) return false;
        if (amountToRemove > currentAmount) return false;

        selectedFood.put(food, currentAmount - amountToRemove);
        if (amountToRemove == currentAmount) selectedFood.remove(food);
        totalCalories -= food.getCaloriesPerGram() * amountToRemove;
        return true;
    }

    public double getTotalCalories() 
    {
        return (Math.round(this.totalCalories * 10.0) / 10.0);
    }

    public Map<FoodList, Double> getAllFood() 
    {
        return this.selectedFood;
    }
}
