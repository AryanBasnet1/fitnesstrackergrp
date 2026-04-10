public class FitnessRank {
    private int totalPoints = 0;
    private String rank = "Beginner";

    // proposal: updatePoints(p)
    public void updatePoints(int p) {
        totalPoints += p;
    }

    // proposal: calculateRank() — Beginner → Elite tiers
    public void calculateRank() {
        if      (totalPoints >= 2000) rank = "Elite";
        else if (totalPoints >= 1200) rank = "Advanced";
        else if (totalPoints >= 600)  rank = "Intermediate";
        else if (totalPoints >= 200)  rank = "Beginner";
        else                          rank = "Beginner";
    }

    // proposal: displayRank() with motivational feedback
    public void displayRank() {
        calculateRank();
        System.out.println("\n======= Fitness Rank =======");
        System.out.println("Total Points : " + totalPoints);
        System.out.println("Your Rank    : " + rank);
        switch (rank) {
            case "Elite":        System.out.println("Outstanding! You are at the top!"); break;
            case "Advanced":     System.out.println("Great work! Keep pushing forward!"); break;
            case "Intermediate": System.out.println("Good progress! Stay consistent!"); break;
            default:             System.out.println("Keep going — every step counts!"); break;
        }
    }

    public String getRank()    { return rank; }
    public int getTotalPoints(){ return totalPoints; }
}
