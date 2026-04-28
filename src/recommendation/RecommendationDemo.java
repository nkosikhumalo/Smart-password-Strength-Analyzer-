package recommendation;

import java.util.List;
import java.util.Scanner;

public class RecommendationDemo {
    public static void main(String[] args) {
        ContextAwarePasswordRecommendationEngine engine =
                new ContextAwarePasswordRecommendationEngine(new PasswordPolicy(12));

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a base password: ");
        String input = scanner.nextLine();

        PasswordWeaknessReport report = engine.analyzeWeakness(input);
        System.out.println("\nDetected weaknesses:");
        for (String issue : report.getIssues()) {
            System.out.println("- " + issue);
        }

        List<PasswordSuggestion> suggestions = engine.generateRecommendations(input, 5);
        System.out.println("\nRecommended stronger variants:");
        int rank = 1;
        for (PasswordSuggestion suggestion : suggestions) {
            System.out.println(rank + ". " + suggestion.getPassword());
            System.out.println("   score: " + suggestion.getScore()
                    + " | entropy: " + String.format("%.1f", suggestion.getEntropy()) + " bits");
            System.out.println("   why: " + String.join("; ", suggestion.getExplanation()));
            rank++;
        }
    }
}
