package projectcode;

public class FitnessRank 
{
    private int totalPoints;

    public FitnessRank() {}

    public void updatePoints(int var1, int var2) 
    {
        this.totalPoints += var2 - var1 / 10;
    }

    public String calculateRank() 
    {
        if      (this.totalPoints < 100) return "Beginner";
        else if (this.totalPoints < 300) return "Intermediate";
        else if (this.totalPoints < 600) return "Advanced";
        else                             return "Elite";
    }

    public void displayRank() 
    {
        String rank = this.calculateRank();
        System.out.println("\n=== FITNESS RANK ===");
        System.out.println("Points : " + this.totalPoints);
        System.out.println("Rank   : " + rank);
        switch (rank) 
        {
            case "Elite":        System.out.println("Outstanding! You are at the top!");  break;
            case "Advanced":     System.out.println("Great work! Keep pushing forward!"); break;
            case "Intermediate": System.out.println("Good progress! Stay consistent!");   break;
            default:             System.out.println("Keep going - every step counts!");   break;
        }
    }

    public int getTotalPoints() { return this.totalPoints; }
}
