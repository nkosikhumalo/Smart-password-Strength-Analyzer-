package dsa;

import java.util.List;

/**
 * Immutable data model holding the full analysis result for a password.
 */
public class AnalysisResult {

    public enum Strength { WEAK, MODERATE, STRONG }

    private final int score;
    private final Strength strength;
    private final String crackTime;
    private final List<String> issues;
    private final List<String> suggestions;
    private final double entropy;

    public AnalysisResult(int score, Strength strength, String crackTime,
                          List<String> issues, List<String> suggestions, double entropy) {
        this.score       = score;
        this.strength    = strength;
        this.crackTime   = crackTime;
        this.issues      = List.copyOf(issues);
        this.suggestions = List.copyOf(suggestions);
        this.entropy     = entropy;
    }

    public int getScore()              { return score; }
    public Strength getStrength()      { return strength; }
    public String getCrackTime()       { return crackTime; }
    public List<String> getIssues()    { return issues; }
    public List<String> getSuggestions() { return suggestions; }
    public double getEntropy()         { return entropy; }
}
