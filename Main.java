import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Food        food    = new Food("", 0);
        Workout     workout = new Workout("", 0, "", 0);
        FitnessRank rank    = new FitnessRank();

        ArrayList<Food> foodList = Food.getfFoods();
        int choice = -1;

        System.out.println("=============================");
        System.out.println("   Welcome to FitnessTracker ");
        System.out.println("=============================");

        while (choice != 4) {
            System.out.println("\n======= Main Menu =======");
            System.out.println("1. Food Selection");
            System.out.println("2. Workout Setup & Suggestions");
            System.out.println("3. View Fitness Rank");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number 1-4.");
                continue;
            }

            switch (choice) {

                case 1: // Food Selection — proposal Step 2
                    food.showFoodList(foodList);
                    System.out.print("Enter food name to add (or 'done' to stop): ");
                    while (true) {
                        String input = scanner.nextLine().trim();
                        if (input.equalsIgnoreCase("done")) break;
                        food.selectFood(input, foodList);
                        System.out.print("Add another food (or 'done'): ");
                    }
                    int totalCal = food.getCalories();
                    System.out.println("Total Calories Consumed: " + totalCal + " cal");
                    rank.updatePoints(totalCal); // pass calorie data to FitnessRank via Main
                    break;

                case 2: // Workout Setup — proposal Step 3
                    System.out.print("Enter effort level (low / medium / high): ");
                    String effort = scanner.nextLine().trim();
                    workout.setEffort(effort);

                    System.out.print("Enter time available (minutes): ");
                    int time = 0;
                    try {
                        time = Integer.parseInt(scanner.nextLine().trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid time, showing all durations.");
                    }
                    workout.setTime(time);

                    workout.suggestWorkout(); // proposal: displays matching workouts

                    System.out.print("Enter workout name to log (or 'skip'): ");
                    String wName = scanner.nextLine().trim();
                    if (!wName.equalsIgnoreCase("skip")) {
                        workout.selectWorkout(wName);
                        int burned = workout.getTotalCaloriesBurned();
                        System.out.println("Total Calories Burned: " + burned + " cal");
                        rank.updatePoints(burned); // pass workout data to FitnessRank via Main
                    }
                    break;

                case 3: // View Rank — proposal Step 4 & 5
                    rank.displayRank();
                    break;

                case 4: // Exit with summary — proposal Step 6
                    System.out.println("\n======= Session Summary =======");
                    System.out.println("Calories Consumed : " + food.getCalories() + " cal");
                    System.out.println("Calories Burned   : " + workout.getTotalCaloriesBurned() + " cal");
                    System.out.println("Net Calories      : " + (food.getCalories() - workout.getTotalCaloriesBurned()) + " cal");
                    rank.displayRank();
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Please enter 1, 2, 3, or 4.");
            }
        }
        scanner.close();
    }
}
