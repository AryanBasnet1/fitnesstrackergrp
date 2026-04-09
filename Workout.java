package projectcode;

public class Workout {

    private String effort;
    private int time;

    public void setEffort(String effort) {
        this.effort = effort.toLowerCase();
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int suggestWorkout() {

        System.out.println("\nSuggested Workout:");

        if (effort.equals("low")) {
            System.out.println("Walking - Burns 150 calories");
            return 150;
        }

        else if (effort.equals("medium")) {
            System.out.println("Cycling - Burns 300 calories");
            return 300;
        }

        else if (effort.equals("high")) {
            System.out.println("Running - Burns 500 calories");
            return 500;
        }

        else {
            System.out.println("Invalid effort level.");
            return 0;
        }
    }
}
