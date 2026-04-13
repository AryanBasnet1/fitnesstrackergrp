package projectcode;

/**
 * Accumulates fitness points from calories consumed and workouts completed,
 * then maps the total to a rank tier.
 *
 * Point formula:
 *   +workoutPoints   (calories burned ÷ 10, supplied by Workout)
 *   -caloriePoints   (calories consumed ÷ 100, supplied by Food)
 *
 * Net points climb when the user works out more than they eat (calorie-wise).
 */
public class FitnessRank
{
    private int totalPoints;

    public FitnessRank() {}

    // ── Point update ─────────────────────────────────────────────────────────

    /**
     * Recalculates total points from scratch.
     *
     * @param caloriesConsumed  total food calories (from Food.getTotalCalories())
     * @param workoutPoints     points earned from workouts (from Workout.getWorkoutPoints())
     */
    public void updatePoints(int caloriesConsumed, int workoutPoints)
    {
        // deduct 1 point per 100 calories consumed; add workout points
        int penalty = caloriesConsumed / 100;
        this.totalPoints = Math.max(0, workoutPoints - penalty);
    }

    // ── Rank calculation ─────────────────────────────────────────────────────

    public String calculateRank()
    {
        if      (totalPoints <  100) return "Beginner";
        else if (totalPoints <  300) return "Intermediate";
        else if (totalPoints <  600) return "Advanced";
        else                         return "Elite";
    }

    public String getRankEmoji()
    {
        switch (calculateRank())
        {
            case "Elite":        return "🏆";
            case "Advanced":     return "🥇";
            case "Intermediate": return "🥈";
            default:             return "🌱";
        }
    }

    public String getMotivation()
    {
        switch (calculateRank())
        {
            case "Elite":        return "Outstanding! You are at the top!";
            case "Advanced":     return "Great work! Keep pushing forward!";
            case "Intermediate": return "Good progress! Stay consistent!";
            default:             return "Keep going — every step counts!";
        }
    }

    /** 0-100 progress within the current tier (for progress bars). */
    public int getTierProgress()
    {
        switch (calculateRank())
        {
            case "Beginner":     return (int)((totalPoints / 100.0)  * 100);
            case "Intermediate": return (int)(((totalPoints - 100)  / 200.0) * 100);
            case "Advanced":     return (int)(((totalPoints - 300)  / 300.0) * 100);
            default:             return 100;
        }
    }

    // ── Console display (kept for non-GUI use) ───────────────────────────────

    public void displayRank()
    {
        System.out.println("\n=== FITNESS RANK ===");
        System.out.println("Points : " + totalPoints);
        System.out.println("Rank   : " + calculateRank() + " " + getRankEmoji());
        System.out.println(getMotivation());
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getTotalPoints() { return totalPoints; }
}
