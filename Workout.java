import java.util.ArrayList;

public class Workout {
    private String workoutName;
    private int caloriesBurned;
    private String effortLevel;   // low / medium / high
    private int timeAvailable;    // minutes
    private int duration;         // minutes this workout takes

    private String effort = "";
    private int time = 0;

    private static ArrayList<Workout> allWorkouts = buildWorkouts();
    private static ArrayList<Workout> completedWorkouts = new ArrayList<>();

    public Workout(String workoutName, int caloriesBurned, String effortLevel, int duration) {
        this.workoutName  = workoutName;
        this.caloriesBurned = caloriesBurned;
        this.effortLevel  = effortLevel;
        this.duration     = duration;
    }

    private static ArrayList<Workout> buildWorkouts() {
        ArrayList<Workout> list = new ArrayList<>();
        list.add(new Workout("Walking",      150, "low",    30));
        list.add(new Workout("Yoga",         180, "low",    45));
        list.add(new Workout("Cycling",      300, "medium", 40));
        list.add(new Workout("Swimming",     350, "medium", 45));
        list.add(new Workout("Running",      400, "high",   30));
        list.add(new Workout("Weightlifting",420, "high",   50));
        list.add(new Workout("HIIT",         500, "high",   25));
        list.add(new Workout("Jump Rope",    450, "high",   20));
        return list;
    }

    // proposal: setEffort(level)
    public void setEffort(String level) {
        this.effort = level.toLowerCase();
    }

    // proposal: setTime(minutes)
    public void setTime(int minutes) {
        this.time = minutes;
    }

    // proposal: suggestWorkout() — filters by effort + time
    public void suggestWorkout() {
        System.out.println("\n------- Suggested Workouts -------");
        boolean found = false;
        for (Workout w : allWorkouts) {
            boolean effortMatch = effort.isEmpty() || w.effortLevel.equalsIgnoreCase(effort);
            boolean timeMatch   = time == 0 || w.duration <= time;
            if (effortMatch && timeMatch) {
                System.out.println(w.workoutName + " | Effort: " + w.effortLevel
                        + " | Duration: " + w.duration + " min"
                        + " | Calories Burned: " + w.caloriesBurned);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No workouts match your criteria. Try adjusting effort or time.");
        }
    }

    // select a workout by name and log it
    public boolean selectWorkout(String name) {
        for (Workout w : allWorkouts) {
            if (w.workoutName.equalsIgnoreCase(name)) {
                completedWorkouts.add(w);
                System.out.println(w.workoutName + " logged! (" + w.caloriesBurned + " cal burned)");
                return true;
            }
        }
        System.out.println("Workout \"" + name + "\" not found.");
        return false;
    }

    public int getTotalCaloriesBurned() {
        int total = 0;
        for (Workout w : completedWorkouts) total += w.caloriesBurned;
        return total;
    }

    public String getWorkoutName()    { return workoutName; }
    public int getCaloriesBurned()    { return caloriesBurned; }
}
