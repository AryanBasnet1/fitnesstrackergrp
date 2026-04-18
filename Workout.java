package projectcode;

import java.util.ArrayList;
import java.util.List;

/*
  Handles workout suggestion logic: filters by effort level and available time,
  tracks the session, and reports calories burned.
 */
public class Workout
{ //class variables
    private Effort effort;
    private int availableMinutes;
    private double totalCaloriesBurned;
    private WorkoutList activeWorkout;
    private int activeMinutes;

    public Workout()
    {
        this.effort = Effort.MEDIUM;
        this.availableMinutes = 30;
        this.totalCaloriesBurned = 0.0;
    }

    //Setters

    public void setEffort(Effort effort)
    { //sets the effort as long as its not null
        if (effort != null)
        	{
        	this.effort = effort;
        	}
        
    }

    public void setTime(int minutes)
    { //sets the time as long as its greater then 0
        if (minutes > 0)
        	{
        	this.availableMinutes = minutes;
        	}
    }

    //Workout Suggestion

    /*
      Returns all workouts that match the current effort level
      and fit within the available time window.
     */
    
    public List<WorkoutList> getSuggestedWorkouts()
    {
        List<WorkoutList> results = new ArrayList<>(); //creates a new workoutlist list array
        for (WorkoutList w : WorkoutList.values())
        { //for each loop to check which workouts fit wanted time
            if (w.getEffort() == effort && w.getMinMinutes() <= availableMinutes)
            {
                results.add(w);
            }
        }
        return results;
    }

    /*
      Returns workouts filtered by a recommended effort level (from UserProfile)
      and available time. Falls back to one level lower if no results found.
     */
    public List<WorkoutList> getSuggestedWorkoutsByEffort(Effort recommendedEffort)
    {
        List<WorkoutList> results = new ArrayList<>(); //creates new arraylist of workouts
        for (WorkoutList w : WorkoutList.values())
        { //for each loop that checks if the recommended Effort level given in avaliable minutes chosen by user
            if (w.getEffort() == recommendedEffort && w.getMinMinutes() <= availableMinutes)
            {
                results.add(w);
            }
        }
        // fallback: try one level lower if nothing matched
        if (results.isEmpty() && recommendedEffort != Effort.LOW)
        {
            Effort fallback = (recommendedEffort == Effort.HIGH) ? Effort.MEDIUM : Effort.LOW;
            for (WorkoutList w : WorkoutList.values())
            {
                if (w.getEffort() == fallback && w.getMinMinutes() <= availableMinutes)
                {
                    results.add(w);
                }
            }
        }
        return results;
    }

    //Log a completed session

   
    public double logWorkout(WorkoutList workout, int minutes)
    {
        if (workout == null || minutes <= 0)
        	{ //verify inputs are valid
        	return 0.0;
        	}
        double burned = workout.caloriesBurned(minutes);
        totalCaloriesBurned += burned;
        activeWorkout = workout;
        activeMinutes = minutes;
        return burned;
    }

    //Getters

    public double getTotalCaloriesBurned() 
    { //gets total calories burned rounded
    	return Math.round(totalCaloriesBurned * 10.0) / 10.0;
    }
    
    
    public Effort getEffort()              
    { //gets the effort input
    	return effort; 
    }
    
    
    public int getAvailableMinutes()    
    { //gets avaliable minutes
    	return availableMinutes;
    }
    
    
    public WorkoutList getActiveWorkout()  
    {  //gets active workout
    	return activeWorkout;    
    }
    
    
    
    public int getActiveMinutes()       
    {  //gets active minutes
    	return activeMinutes;    
    }

    /*
     Converts calories burned into fitness rank points.
     10 calories burned = 1 point.
     */
    public int getWorkoutPoints()
    {
        return (int)(totalCaloriesBurned / 10.0);
    }

    public void reset()
    { //resets the class
        totalCaloriesBurned = 0.0;
        activeWorkout       = null;
        activeMinutes       = 0;
    }
}
