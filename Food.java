package projectcode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manages the user's food selections and running calorie total.
 */
public class Food
{
    private double totalCalories;
    private final Map<FoodList, Double> selectedFood;   // food → grams

    public Food()
    {
        this.totalCalories = 0.0;
        this.selectedFood  = new LinkedHashMap<>();      // insertion-order for display
    }

    // ── Select / Add ─────────────────────────────────────────────────────────

    /**
     * Adds {@code amount} grams of {@code food} to the log.
     * Silently ignores null food or negative amounts.
     */
    public void selectFood(FoodList food, double amount)
    {
        if (food == null || amount < 0.0) return;
        amount = round1(amount);
        selectedFood.merge(food, amount, Double::sum);
        totalCalories += amount * food.getCaloriesPerGram();
    }

    // ── Remove ───────────────────────────────────────────────────────────────

    /**
     * Removes {@code amountToRemove} grams of {@code food} from the log.
     *
     * @return {@code true} on success; {@code false} if the food isn't
     *         present or the requested amount exceeds what was logged.
     */
    public boolean removeFood(FoodList food, double amountToRemove)
    {
        if (food == null || amountToRemove < 0.0) return false;
        amountToRemove = round1(amountToRemove);

        double current = selectedFood.getOrDefault(food, 0.0);
        if (current == 0.0 || amountToRemove > current) return false;

        totalCalories -= food.getCaloriesPerGram() * amountToRemove;

        double remaining = round1(current - amountToRemove);
        if (remaining == 0.0)
            selectedFood.remove(food);
        else
            selectedFood.put(food, remaining);

        return true;
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    /** Returns the total logged calories, rounded to 1 decimal place. */
    public double getTotalCalories()
    {
        return round1(totalCalories);
    }

    /** Returns a read-only view of the current food log (food → grams). */
    public Map<FoodList, Double> getAllFood()
    {
        return selectedFood;
    }

    /** Clears all food entries and resets the calorie counter. */
    public void reset()
    {
        selectedFood.clear();
        totalCalories = 0.0;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private static double round1(double v)
    {
        return Math.round(v * 10.0) / 10.0;
    }
}
