package projectcode;

public enum FoodList
{
    //Meals
    Pizza       (22.5),
    Burger      (13.6),
    Pasta       ( 5.6),
    Sushi       (12.7),
    Steak       (14.6),
    Chicken     (11.2),
    Salad       ( 3.3),
    Sandwich    ( 7.6),
    Taco        ( 3.6),
    Burrito     (12.45),
    Soup        ( 1.7),
    Fries       ( 9.0),
    Rice        (20.0),
    Noodles     (13.8),
    Seafood     (14.3),
    Oatmeal     ( 2.3),
    Cereal      (10.0),
    Granola     ( 4.0),
    Pancakes    ( 9.3),
    Waffles     ( 7.4),

    //Desserts
    IceCream    (23.1),
    Cake        (25.7),
    Donut       (18.9),
    Brownie     (17.4),
    Cupcake     (26.3),
    Cheesecake  (35.2),
    Pudding     (11.2),

    //Drinks
    Milkshake   (45.2),
    Smoothie    (13.2),
    Yogurt      (16.1);

   //class variable
    private final double caloriesPerGram;

    FoodList(double caloriesPerGram)
    { //sets the calories
        this.caloriesPerGram = caloriesPerGram;
    }

    public double getCaloriesPerGram()
    { //returns num of calories associated with the constant
        return caloriesPerGram;
    }

    /* Shown in combo-boxes: "Pizza (22.5 cal/g)" */
    @Override
    public String toString()
    {
        return this.name() + " (" + this.caloriesPerGram + " cal/g)";
    }
}
