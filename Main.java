package projectcode;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Food food = new Food();
        Workout workout = new Workout();
        FitnessRank rank = new FitnessRank();

        while (true) {
            System.out.println("\n=== FITNESS TRACKER ===");
            System.out.println("1. Show Food List");
            System.out.println("2. Add Food");
            System.out.println("3. Suggest Workout");
            System.out.println("4. View Rank");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice) {

                case 1:
                    food.showFoodList();
                    break;

                case 2:
                    System.out.print("Enter food name: ");
                    String name = sc.nextLine();

                    System.out.print("Enter grams: ");
                    double grams = sc.nextDouble();

                    if (food.selectFood(name, grams)) {
                        System.out.println("Total Calories: " + food.getCalories());
                    } else {
                        System.out.println("Food not found!");
                    }
                    break;

                case 3:
                    System.out.print("Enter time (minutes): ");
                    int time = sc.nextInt();

                    sc.nextLine();
                    System.out.print("Enter effort (Low/Medium/High): ");
                    String effort = sc.nextLine();

                    workout.setTime(time);
                    workout.setEffort(effort);

                    int burned = workout.suggestWorkout();

                    rank.updatePoints((int) food.getCalories(), burned);
                    break;

                case 4:
                    rank.displayRank();
                    break;

                case 5:
                    System.out.println("Goodbye!");
                    return;
            }
        }
    }
}
