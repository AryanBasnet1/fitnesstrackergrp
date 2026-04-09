package projectcode;

public class FitnessRank {
   private int totalPoints;

   public FitnessRank() {
   }

   public void updatePoints(int var1, int var2) {
      this.totalPoints += var2 - var1 / 10;
   }

   public String calculateRank() {
      if (this.totalPoints < 100) {
         return "Beginner";
      } else {
         return this.totalPoints < 300 ? "Intermediate" : "Elite";
      }
   }

   public void displayRank() {
      System.out.println("\n=== FITNESS RANK ===");
      System.out.println("Points: " + this.totalPoints);
      System.out.println("Rank: " + this.calculateRank());
   }
}
