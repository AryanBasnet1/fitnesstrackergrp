package projectcode;

/*
 Stores user physical stats and computes BMI, BMI category,
 and daily calorie goal (TDEE) using the Mifflin-St Jeor equation.
 Weight in kg, Height in cm, Age in years.
 */
public class UserProfile
{
    public enum ActivityLevel
    { 
        SEDENTARY    ("Sedentary (little/no exercise)", 1.2),
        LIGHT        ("Lightly active (1-3 days/week)", 1.375),
        MODERATE     ("Moderately active (3-5 days/week)", 1.55),
        VERY_ACTIVE  ("Very active (6-7 days/week)", 1.725),
        EXTRA_ACTIVE ("Extra active (athlete/hard labour)", 1.9);

        private final String label;
        private final double multiplier;

        ActivityLevel(String label, double multiplier)
        {
            this.label = label;
            this.multiplier = multiplier;
        }

        public double getMultiplier() 
        { 
        	return multiplier;
        }

        
        @Override 
        public String toString() 
        { 
        	return label; 
        }
    }

    public enum Goal
    { //enum for what the users goal is 
        LOSE_WEIGHT  ("Lose Weight", -500),
        MAINTAIN     ("Maintain Weight", 0),
        GAIN_MUSCLE  ("Gain Muscle", +300);

        private final String label;
        private final int calorieDelta; // kcal adjustment from TDEE

        Goal(String label, int calorieDelta)
        { //sets info
            this.label = label;
            this.calorieDelta = calorieDelta;
        }

        public int getCalorieDelta() 
        {  //gets calorie delta
        	return calorieDelta;
        }

        @Override 
        public String toString() 
        { 
        	return label; 
        }
    }

    //Class Variables
    private double        weightKg   = 0;
    private double        heightCm   = 0;
    private int           age        = 0;
    private boolean       isMale     = true;
    private ActivityLevel activity   = ActivityLevel.MODERATE;
    private Goal          goal       = Goal.MAINTAIN;
    private boolean       profileSet = false;

    //Setters

    public void setProfile(double weightKg, double heightCm, int age,
                           boolean isMale, ActivityLevel activity, Goal goal)
    {
        this.weightKg  = weightKg;
        this.heightCm  = heightCm;
        this.age       = age;
        this.isMale    = isMale;
        this.activity  = activity;
        this.goal      = goal;
        this.profileSet = true;
    }

    //Bmi Info

    /* BMI = weight(kg) / (height(m))^2 */
    public double getBMI()
    {
        if (!profileSet || heightCm <= 0) 
        	{ //makes sure fields are valid
        	return 0;
        	}
        	
        double heightM = heightCm / 100.0;
        return Math.round((weightKg / (heightM * heightM)) * 10.0) / 10.0;
    }

    public String getBMICategory()
    {
        double bmi = getBMI();
        if (bmi <= 0)
        	{
        	return "—";
        	}
        if (bmi < 18.5)
        	{
        	return "Underweight";
        	}
        if (bmi < 25.0)
        	{
        	return "Normal weight";
        	}
        if (bmi < 30.0)
        	{
        	return "Overweight";
        	}
        return "Obese"; //if all other fields not true
    }

    /* Emoji indicator for BMI category */
    public String getBMIEmoji()
    {
        switch (getBMICategory())
        {
            case "Underweight":   return "⚠️";
            case "Normal weight": return "✅";
            case "Overweight":    return "🟡";
            case "Obese":         return "🔴";
            default:              return "—";
        }
    }

    //Daily Recomendation

    /*
      Basal Metabolic Rate via Mifflin-St Jeor.
      Male:   10*weight + 6.25*height - 5*age + 5
      Female: 10*weight + 6.25*height - 5*age - 161
     */
    public double getBMR()
    {
        if (!profileSet)
        	{
        	return 0;
        	}
        double bmr = 10 * weightKg + 6.25 * heightCm - 5 * age;
        bmr += isMale ? 5 : -161;
        return Math.round(bmr);
    }

    /* Total Daily Energy Expenditure = BMR × activity multiplier */
    public double getTDEE()
    {
        return Math.round(getBMR() * activity.getMultiplier());
    }

    /* Daily calorie target = TDEE + goal adjustment */
    public double getDailyCalorieGoal()
    { 
        return getTDEE() + goal.getCalorieDelta();
    }

    /*
     Remaining calories for today based on what the user has eaten.
      Positive = can still eat more. Negative = already over goal.
     */
    public double getRemainingCalories(double consumed)
    {
        return Math.round((getDailyCalorieGoal() - consumed) * 10.0) / 10.0;
    }

    /*
      Returns a recommendation about the user's calorie status.
     */
    public String getCalorieStatusMessage(double consumed)
    { 
        if (!profileSet) return "Set up your profile to see personalised advice.";
        double remaining = getRemainingCalories(consumed);
        String cat = getBMICategory();

        if (remaining > 0)
        {
            return String.format("You have %.0f kcal remaining today. %s",
                remaining, goalHint(cat));
        }
        else
        {
            return String.format("You are %.0f kcal OVER your daily goal. Consider a workout to burn the surplus!",
                Math.abs(remaining));
        }
    }

    private String goalHint(String bmiCat)
    { //gives a hint to user on what they should do
        switch (goal)
        {
            case LOSE_WEIGHT: return "Stay within your limit to reach your weight-loss goal.";
            case GAIN_MUSCLE: return "Make sure to hit your protein targets for muscle gain.";
            default:
                if ("Underweight".equals(bmiCat))
                {
                    return "Your BMI is low — try to eat nutritious, calorie-dense foods.";
                }
                if ("Overweight".equals(bmiCat) || "Obese".equals(bmiCat))
                {
                    return "Consider keeping remaining calories for a lighter meal.";
                }
                return "You're on track — keep it balanced!";
        }
    }

    /*
      Recommends a workout effort level based on calorie surplus/deficit.
      If the user ate more than their daily goal, a harder workout is recommended.
     */
    public Effort getRecommendedEffort(double consumed)
    {
        double surplus = consumed - getDailyCalorieGoal();
        if (surplus > 400)
        	{
        	return Effort.HIGH;
        	}
        if (surplus > 100)
        	{
        	return Effort.MEDIUM;
        	}
        return Effort.LOW;
    }

    /*
      Human-readable explanation of the workout recommendation.
     */
    public String getWorkoutRecommendationReason(double consumed)
    {
        if (!profileSet)
        	{
        	return "Set up your profile for smart workout recommendations.";
        	}
        double surplus = consumed - getDailyCalorieGoal();
        double remaining = getRemainingCalories(consumed);

        if (surplus > 400)
        {
            return String.format(
                "You're %.0f kcal over your goal — a HIGH intensity workout is recommended to burn the surplus.", surplus);
        }
        if (surplus > 100)
        {
            return String.format(
                "You're %.0f kcal over your goal — a MEDIUM intensity workout will help balance your day.", surplus);
        }
        if (remaining > 0)
        {
            return String.format(
                "You still have %.0f kcal to go — a light/rest workout is fine, or skip and hit your food goal.", remaining);
        }
        return "You're right on target — any workout will be a bonus!";
    }

    //Getters

    public boolean isProfileSet()      
    {
    	return profileSet;
    }
    
    
    public double getWeightKg()
    { 
    	return weightKg;
    }
    
    
    public double getHeightCm()       
    { 
    	return heightCm;     
    }
    
    
    public int getAge()            
    { 
    	return age;
    }
    
    
    
    public boolean isMale()           
    { 
    	return isMale;       
    }
    
    
    public ActivityLevel getActivity() 
    { 
    	return activity;     
    }
    
    
    
    public Goal getGoal()           
    { 
    	return goal;         
    }
}
