package dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * Orchestrates the DSA components into a single analyzePassword() call.
 *
 * Pipeline:
 *   1. EntropyCalculator  → entropy bits + character class flags
 *   2. PatternDetector    → issues + penalty points (uses Trie internally)
 *   3. Length scoring     → base score contribution
 *   4. Final score clamp  → 0–100
 *   5. Strength + crack time derivation
 *
 * This class is the public interface for any consumer (GUI, REST, CLI, etc.)
 */
public class ScoringEngine {

    private final Trie              trie      = new Trie();
    private final PatternDetector   detector  = new PatternDetector();
    private final EntropyCalculator entropy   = new EntropyCalculator();

    // ── Common passwords / dictionary words loaded into the Trie ─────────
    private static final String[] COMMON_WORDS = {
        "password", "123456", "12345678", "qwerty", "abc123", "monkey",
        "1234567", "letmein", "trustno1", "dragon", "baseball", "iloveyou",
        "master", "sunshine", "ashley", "bailey", "passw0rd", "shadow",
        "123123", "654321", "superman", "qazwsx", "michael", "football",
        "login", "welcome", "admin", "root", "pass", "test", "guest",
        "hello", "ninja", "mustang", "access", "flower", "solo", "hockey",
        "soccer", "butter", "cheese", "hunter", "ranger", "buster",
        "thomas", "tigger", "robert", "daniel", "george", "jordan",
        "harley", "ranger", "dakota", "cookie", "cheese", "banana",
        "summer", "winter", "spring", "autumn", "monday", "friday",
        "january", "february", "march", "april", "august", "december"
    };

    public ScoringEngine() {
        trie.insertAll(COMMON_WORDS);
    }

    // ── Public interface ──────────────────────────────────────────────────

    /**
     * Full password analysis. Returns a structured {@link AnalysisResult}.
     *
     * @param password the password to evaluate (may be empty, never null)
     */
    public AnalysisResult analyzePassword(String password) {
        if (password == null) password = "";

        List<String> issues      = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();

        // ── Guard: empty ──────────────────────────────────────────────────
        if (password.isEmpty()) {
            return new AnalysisResult(0, AnalysisResult.Strength.WEAK,
                "Instant", List.of("Password is empty."),
                List.of("Enter a password to analyze."), 0);
        }

        int score = 0;
        int len   = password.length();

        // ── Step 1: Entropy + character classes ───────────────────────────
        EntropyCalculator.EntropyResult er = entropy.calculate(password);

        if (!er.hasUpper)   { issues.add("No uppercase letters.");    suggestions.add("Add uppercase letters (A–Z)."); }
        else                  score += 10;
        if (!er.hasLower)   { issues.add("No lowercase letters.");    suggestions.add("Add lowercase letters (a–z)."); }
        else                  score += 10;
        if (!er.hasDigit)   { issues.add("No digits.");               suggestions.add("Include numbers (0–9)."); }
        else                  score += 10;
        if (!er.hasSpecial) { issues.add("No special characters.");   suggestions.add("Add special characters like !@#$%^&*."); }
        else                  score += 15;

        // Bonus: all four classes present
        if (er.hasUpper && er.hasLower && er.hasDigit && er.hasSpecial) score += 10;

        // ── Step 2: Length scoring ────────────────────────────────────────
        if (len < 6) {
            issues.add("Too short (< 6 characters).");
            suggestions.add("Use at least 12 characters.");
        } else if (len < 8) {
            score += 5;
            issues.add("Short password (< 8 characters).");
            suggestions.add("Increase length to at least 12 characters.");
        } else if (len < 12) {
            score += 15;
            suggestions.add("Consider 12+ characters for better security.");
        } else if (len < 16) {
            score += 25;
        } else {
            score += 30;
        }

        // ── Step 3: Entropy bonus ─────────────────────────────────────────
        if (er.entropy >= 60) score += 5;
        if (er.entropy >= 80) score += 5;

        // ── Step 4: Pattern detection (Trie + sliding window + HashMap) ───
        PatternDetector.DetectionResult pd = detector.detect(password, trie);
        issues.addAll(pd.issues);
        score = Math.max(0, score - pd.penaltyPoints);

        // ── Step 5: Suggestions for detected patterns ─────────────────────
        if (!pd.matchedSeqs.isEmpty()) {
            suggestions.add("Avoid predictable sequences and dictionary words.");
        }
        if (pd.penaltyPoints > 30) {
            suggestions.add("Consider using a passphrase with random words instead.");
        }

        // ── Step 6: Clamp + derive strength ──────────────────────────────
        score = Math.min(100, Math.max(0, score));

        AnalysisResult.Strength strength;
        if (score < 40)      strength = AnalysisResult.Strength.WEAK;
        else if (score < 70) strength = AnalysisResult.Strength.MODERATE;
        else                 strength = AnalysisResult.Strength.STRONG;

        String crackTime = entropy.estimateCrackTime(er.entropy);

        if (issues.isEmpty())      issues.add("No major issues detected.");
        if (suggestions.isEmpty()) suggestions.add("Great password! Keep it safe and don't reuse it.");

        return new AnalysisResult(score, strength, crackTime, issues, suggestions, er.entropy);
    }
}
