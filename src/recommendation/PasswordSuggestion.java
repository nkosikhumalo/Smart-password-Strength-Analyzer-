package recommendation;

import java.util.List;

public class PasswordSuggestion {
    private final String password;
    private final int score;
    private final double entropy;
    private final List<String> explanation;

    public PasswordSuggestion(String password, int score, double entropy, List<String> explanation) {
        this.password = password;
        this.score = score;
        this.entropy = entropy;
        this.explanation = List.copyOf(explanation);
    }

    public String getPassword() {
        return password;
    }

    public int getScore() {
        return score;
    }

    public double getEntropy() {
        return entropy;
    }

    public List<String> getExplanation() {
        return explanation;
    }
}
