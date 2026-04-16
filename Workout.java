package projectcode;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles workout suggestion logic: filters by effort level and available time,
 * tracks the session, and reports calories burned.
 */
public class Workout
{
    private Effort effort;
    private int    availableMinutes;
    private double totalCaloriesBurned;

    // last suggested workout chosen by the user
    private WorkoutList activeWorkout;
    private int         activeMinutes;

    public Workout()
    {
        this.effort              = Effort.MEDIUM;
        this.availableMinutes    = 30;
        this.totalCaloriesBurned = 0.0;
    }

    // ── Setters ──────────────────────────────────────────────────────────────

    public void setEffort(Effort effort)
    {
        if (effort != null) this.effort = effort;
    }

    public void setTime(int minutes)
    {
        if (minutes > 0) this.availableMinutes = minutes;
    }

    // ── Suggestion ───────────────────────────────────────────────────────────

    /**
     * Returns all workouts that match the current effort level
     * and fit within the available time window.
     */
    public List<WorkoutList> getSuggestedWorkouts()
    {
        List<WorkoutList> results = new ArrayList<>();
        for (WorkoutList w : WorkoutList.values())
        {
            if (w.getEffort() == effort && w.getMinMinutes() <= availableMinutes)
                results.add(w);
        }
        return results;
    }

    /**
     * Returns workouts filtered by a recommended effort level (from UserProfile)
     * and available time. Falls back to one level lower if no results found.
     */
    public List<WorkoutList> getSuggestedWorkoutsByEffort(Effort recommendedEffort)
    {
        List<WorkoutList> results = new ArrayList<>();
        for (WorkoutList w : WorkoutList.values())
        {
            if (w.getEffort() == recommendedEffort && w.getMinMinutes() <= availableMinutes)
                results.add(w);
        }
        // fallback: try one level lower if nothing matched
        if (results.isEmpty() && recommendedEffort != Effort.LOW)
        {
            Effort fallback = (recommendedEffort == Effort.HIGH) ? Effort.MEDIUM : Effort.LOW;
            for (WorkoutList w : WorkoutList.values())
            {
                if (w.getEffort() == fallback && w.getMinMinutes() <= availableMinutes)
                    results.add(w);
            }
        }
        return results;
    }

    // ── Log a completed session ───────────────────────────────────────────────

    /**
     * Records that the user completed {@code minutes} of {@code workout}.
     * Accumulates calories burned.
     *
     * @return calories burned in this session
     */
    public double logWorkout(WorkoutList workout, int minutes)
    {
        if (workout == null || minutes <= 0) return 0.0;
        double burned = workout.caloriesBurned(minutes);
        totalCaloriesBurned += burned;
        activeWorkout = workout;
        activeMinutes = minutes;
        return burned;
    }

    // ── Queries ──────────────────────────────────────────────────────────────

    public double getTotalCaloriesBurned() { return Math.round(totalCaloriesBurned * 10.0) / 10.0; }
    public Effort getEffort()              { return effort;           }
    public int    getAvailableMinutes()    { return availableMinutes; }
    public WorkoutList getActiveWorkout()  { return activeWorkout;    }
    public int    getActiveMinutes()       { return activeMinutes;    }

    /**
     * Converts calories burned into fitness rank points.
     * 10 calories burned = 1 point.
     */
    public int getWorkoutPoints()
    {
        return (int)(totalCaloriesBurned / 10.0);
    }

    public void reset()
    {
        totalCaloriesBurned = 0.0;
        activeWorkout       = null;
        activeMinutes       = 0;
    }
}
