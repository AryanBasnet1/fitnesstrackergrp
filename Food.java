import java.util.ArrayList;

public class Food {
    String foodName;
    int calories;
    private static ArrayList<Food> selectedFoods = new ArrayList<>();

    public Food(String foodName, int calories) {
        this.foodName = foodName;
        this.calories = calories;
    }

    public String getFoodName() { return foodName; }
    public int getCalories()    { return calories; }

    public static ArrayList<Food> getfFoods() {
        ArrayList<Food> foods = new ArrayList<>();
        foods.add(new Food("Eggs",    150));
        foods.add(new Food("Daal",    150));
        foods.add(new Food("Meat",    250));
        foods.add(new Food("Rice",    200));
        foods.add(new Food("Chicken", 220));
        foods.add(new Food("Salad",    80));
        foods.add(new Food("Pasta",   300));
        foods.add(new Food("Burger",  500));
        return foods;
    }

    public void showFoodList(ArrayList<Food> foods) {
        System.out.println("\n------- Available Foods -------");
        for (Food f : foods) {
            System.out.println(f.getFoodName() + " - " + f.getCalories() + " cal/100g");
        }
    }

    // selectFood by name string, as per the proposal
    public boolean selectFood(String name, ArrayList<Food> foods) {
        for (Food f : foods) {
            if (f.getFoodName().equalsIgnoreCase(name)) {
                selectedFoods.add(f);
                System.out.println(f.getFoodName() + " added! (" + f.getCalories() + " cal)");
                return true;
            }
        }
        System.out.println("Food not found: " + name);
        return false;
    }

    // getCalories returns total of selected foods
    public int getCalories() {
        // renamed to avoid clash — see getTotalCalories()
        return getTotalCalories();
    }

    public int getTotalCalories() {
        int total = 0;
        for (Food f : selectedFoods) total += f.getCalories();
        return total;
    }

    public ArrayList<Food> getSelectedFoods() { return selectedFoods; }
}
