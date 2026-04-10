package projectcode;

public enum FoodList 
{
    Pizza(22.5),
    Burger(13.6),
    Pasta(5.6),
    Sushi(12.7),
    Steak(14.6),
    Chicken(11.2),
    Salad(3.3),
    Sandwich(7.6),
    Taco(3.6),
    Burrito(12.45),
    Soup(1.7),
    Fries(9.0),
    IceCream(23.1),
    Cake(25.7),
    Donut(18.9),
    Pancakes(9.3),
    Waffles(7.4),
    Rice(20.0),
    Noodles(13.8),
    Seafood(14.3),
    Brownie(17.4),
    Cupcake(26.3),
    Cheesecake(35.2),
    Pudding(11.2),
    Milkshake(45.2),
    Smoothie(13.2),
    Yogurt(16.1),
    Granola(4.0),
    Oatmeal(2.3),
    Cereal(10.0);

	 private final double caloriesPerGram;
	 
	 FoodList(double caloriesPerGram)
	 {
	        this.caloriesPerGram = caloriesPerGram;
	 }
	 
	 public double getCaloriesPerGram() 
	 {
	        return caloriesPerGram;
	 }
	 
	 public String toString() 
	 {
	        return this.name() + " (" + this.caloriesPerGram + " cal/gram)";
	    }
}
