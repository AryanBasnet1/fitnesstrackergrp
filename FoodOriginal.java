@@ -1,27 +1,61 @@

Create a list of foods
Add each food with its name and calories into the list
User types a food name
Loop through the list and find the matching food
Display its calories



import java.util.ArrayList;



public class Food {
String foodName;
int calories;
    private static ArrayList<Food> selectedFoods = new ArrayList<>();

public Food(String foodName , int calories){
    this.foodName = foodName;
    this.calories = calories;
}
public String getFoodName(){
    return foodName;
}
public int getCalories(){
    return calories;
}
public static ArrayList<Food> getfFoods(){
    ArrayList<Food> foods = new ArrayList<>();
    foods.add(new Food("Eggs" , 150));
    foods.add(new Food("Daal" , 150));
    foods.add(new Food("Meat" , 150));
    foods.add(new Food("Rice" , 150));
    foods.add(new Food("Chicken" , 150));
return foods;


}
public void showFoodList(ArrayList<Food> foods){
    System.out.println("-------Avaialbale foods are:");
    for(Food f: foods){
        System.out.println(f.getFoodName() +  " -" + f.getCalories() +"- "+ "cal/100g");
    }


}
    public void selectFood(Food f) {
        selectedFoods.add(f);
        System.out.println(f.getFoodName() + " added!");
    }
    public int getTotalCalories() {
        int total = 0;
        for (Food f : selectedFoods) total += f.getCalories();
        return total;
    }
}




