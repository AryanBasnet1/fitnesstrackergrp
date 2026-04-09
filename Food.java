package projectcode;

import java.util.HashMap;
import java.util.Map;

public class Food {

    private Map<String, Double> foodMap;
    private double totalCalories;

    public Food() {
        foodMap = new HashMap<>();

        foodMap.put("pizza", 266.0);
        foodMap.put("burger", 295.0);
        foodMap.put("salad", 152.0);
        foodMap.put("pasta", 131.0);
        foodMap.put("rice", 130.0);
    }

    public void showFoodList() {
        System.out.println("\nAvailable Foods:");
        for (String food : foodMap.keySet()) {
            System.out.println(food + " (" + foodMap.get(food) + " cal per 100g)");
        }
    }

    public boolean selectFood(String name, double grams) {

        name = name.toLowerCase();

        if (!foodMap.containsKey(name)) return false;

        double calPer100 = foodMap.get(name);
        double calories = (grams / 100.0) * calPer100;

        totalCalories += calories;
        return true;
    }

    public double getCalories() {
        return totalCalories;
    }
}
