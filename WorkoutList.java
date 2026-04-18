package projectcode;

/*
  enum of workouts with effort level, duration, and calories burned per minute.
 */
public enum WorkoutList
{
    // name  effort  minMinutes  calPerMin
	
	//Low Effort Workouts
    Walking         (Effort.LOW, 10, 3.5),
    Stretching      (Effort.LOW, 10, 2.5),
    Yoga            (Effort.LOW, 20, 3.0),
    LightCycling    (Effort.LOW, 20, 5.0),
    PilatesBasic    (Effort.LOW, 20, 4.0),

    
    //Medium Effort Workouts
    Jogging         (Effort.MEDIUM, 20, 7.0),
    Swimming        (Effort.MEDIUM, 20, 8.0),
    Cycling         (Effort.MEDIUM, 30, 6.5),
    Dancing         (Effort.MEDIUM, 20, 6.0),
    Aerobics        (Effort.MEDIUM, 30, 7.5),
    JumpRope        (Effort.MEDIUM, 15, 9.0),
    BodyweightCircuit(Effort.MEDIUM, 30, 7.0),

    //High Effort Workouts
    Running         (Effort.HIGH, 30, 11.0),
    HIIT            (Effort.HIGH, 20, 12.5),
    WeightLifting   (Effort.HIGH, 30, 6.0),
    CrossFit        (Effort.HIGH, 30, 13.0),
    Boxing          (Effort.HIGH, 30, 11.5),
    Sprinting       (Effort.HIGH, 15, 14.0);

	
    //class variables
    private final Effort effort; 
    private final int minMinutes;      
    private final double calPerMinute;

    WorkoutList(Effort effort, int minMinutes, double calPerMinute)
    { //sets default values
        this.effort = effort;
        this.minMinutes = minMinutes;
        this.calPerMinute = calPerMinute;
    }

    
    
    //Getters
    
    public Effort getEffort() 
    { //returns the amount of effort for a enum
    	return effort;       
    }
    
    public int getMinMinutes()
    { //returns the min minutes for a enum
    	return minMinutes; 
    }
    
    
    public double getCalPerMinute()  
    { //returns the cal per minutes the workout burns
    	return calPerMinute; 
    }

    // Calories burned for a given session length (minutes).
    public double caloriesBurned(int minutes)
    {
        return Math.round(calPerMinute * minutes * 10.0) / 10.0;
    }

    @Override
    public String toString()
    {//returns it in a neat format
        return name() + " [" + effort + ", min " + minMinutes + " min]";
    }
}
