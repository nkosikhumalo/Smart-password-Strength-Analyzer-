import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Core analysis engine. Pure logic, no UI dependencies.
 * Evaluates password strength deterministically.
 */
public class PasswordAnalyzer {

    // Common weak passwords / patterns
    private static final Set<String> COMMON_PASSWORDS = Set.of(
        "password", "123456", "12345678", "qwerty", "abc123", "monkey",
        "1234567", "letmein", "trustno1", "dragon", "baseball", "iloveyou",
        "master", "sunshine", "ashley", "bailey", "passw0rd", "shadow",
        "123123", "654321", "superman", "qazwsx", "michael", "football"
    );

    private static final String[] KEYBOARD_SEQUENCES = {
        "qwerty", "qwertyuiop", "asdfgh", "asdfghjkl", "zxcvbn",
        "1234567890", "12345", "123456", "1234", "0987654321",
        "abcdef", "abcdefgh"
    };

    /**
     * Analyzes the given password and returns a full AnalysisResult.
     */
    public AnalysisResult analyze(String password) {
        List<String> issues      = new ArrayList<>();
        List<String> suggestions = new ArrayList<>();
        int score = 0;

        if (password == null || password.isEmpty()) {
            return new AnalysisResult(0, AnalysisResult.Strength.WEAK,
                "Instant", List.of("Password is empty."), List.of("Enter a password to analyze."), 0);
        }

        int len = password.length();

        // ── Length scoring ────────────────────────────────────────────────
        if (len < 6) {
            issues.add("Too short (< 6 characters).");
            suggestions.add("Use at least 12 characters for a strong password.");
        } else if (len < 8) {
            score += 5;
            issues.add("Short password (< 8 characters).");
            suggestions.add("Increase length to at least 12 characters.");
        } else if (len < 12) {
            score += 15;
            suggestions.add("Consider using 12+ characters for better security.");
        } else if (len < 16) {
            score += 25;
        } else {
            score += 30;
        }

        // ── Character class checks ────────────────────────────────────────
        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLower   = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> !Character.isLetterOrDigit(c));

        if (hasUpper)   score += 10; else { issues.add("No uppercase letters."); suggestions.add("Add uppercase letters (A–Z)."); }
        if (hasLower)   score += 10; else { issues.add("No lowercase letters."); suggestions.add("Add lowercase letters (a–z)."); }
        if (hasDigit)   score += 10; else { issues.add("No digits.");            suggestions.add("Include numbers (0–9)."); }
        if (hasSpecial) score += 15; else { issues.add("No special characters."); suggestions.add("Add special characters like !@#$%^&*."); }

        // Bonus for using all four classes
        if (hasUpper && hasLower && hasDigit && hasSpecial) score += 10;

        // ── Common password check ─────────────────────────────────────────
        if (COMMON_PASSWORDS.contains(password.toLowerCase())) {
            score = Math.max(0, score - 40);
            issues.add("This is a commonly used password.");
            suggestions.add("Avoid dictionary words and well-known passwords.");
        }

        // ── Keyboard sequence check ───────────────────────────────────────
        String lower = password.toLowerCase();
        for (String seq : KEYBOARD_SEQUENCES) {
            if (lower.contains(seq)) {
                score = Math.max(0, score - 20);
                issues.add("Contains keyboard sequence: \"" + seq + "\".");
                suggestions.add("Avoid keyboard patterns like qwerty or 12345.");
                break;
            }
        }

        // ── Repeated characters check ─────────────────────────────────────
        if (hasRepeatedChars(password)) {
            score = Math.max(0, score - 10);
            issues.add("Contains repeated characters (e.g. aaa, 111).");
            suggestions.add("Avoid repeating the same character consecutively.");
        }

        // ── Entropy calculation ───────────────────────────────────────────
        int charsetSize = calcCharsetSize(hasUpper, hasLower, hasDigit, hasSpecial);
        double entropy  = len * (Math.log(charsetSize) / Math.log(2));

        // Entropy bonus
        if (entropy >= 60) score += 5;
        if (entropy >= 80) score += 5;

        // Clamp score
        score = Math.min(100, Math.max(0, score));

        // ── Strength rating ───────────────────────────────────────────────
        AnalysisResult.Strength strength;
        if (score < 40)      strength = AnalysisResult.Strength.WEAK;
        else if (score < 70) strength = AnalysisResult.Strength.MODERATE;
        else                 strength = AnalysisResult.Strength.STRONG;

        // ── Crack time estimation ─────────────────────────────────────────
        String crackTime = estimateCrackTime(entropy);

        if (issues.isEmpty()) {
            issues.add("No major issues detected.");
        }
        if (suggestions.isEmpty()) {
            suggestions.add("Great password! Keep it safe and don't reuse it.");
        }

        return new AnalysisResult(score, strength, crackTime, issues, suggestions, entropy);
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private boolean hasRepeatedChars(String password) {
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i + 1)
                    && password.charAt(i + 1) == password.charAt(i + 2)) {
                return true;
            }
        }
        return false;
    }

    private int calcCharsetSize(boolean upper, boolean lower, boolean digit, boolean special) {
        int size = 0;
        if (upper)   size += 26;
        if (lower)   size += 26;
        if (digit)   size += 10;
        if (special) size += 32;
        return size == 0 ? 26 : size; // fallback
    }

    /**
     * Estimates crack time based on entropy bits.
     * Assumes a fast offline attack: ~10 billion guesses/second (10^10).
     */
    private String estimateCrackTime(double entropyBits) {
        // combinations = 2^entropy
        // seconds = combinations / guesses_per_second
        double combinations      = Math.pow(2, entropyBits);
        double guessesPerSecond  = 1e10; // 10 billion/sec (GPU cracking)
        double seconds           = combinations / guessesPerSecond;

        if (seconds < 1)                          return "Instant";
        if (seconds < 60)                         return String.format("%.0f second(s)", seconds);
        if (seconds < 3_600)                      return String.format("%.0f minute(s)", seconds / 60);
        if (seconds < 86_400)                     return String.format("%.0f hour(s)", seconds / 3_600);
        if (seconds < 2_592_000)                  return String.format("%.0f day(s)", seconds / 86_400);
        if (seconds < 31_536_000)                 return String.format("%.0f month(s)", seconds / 2_592_000);
        if (seconds < 31_536_000_000L)            return String.format("%.0f year(s)", seconds / 31_536_000);
        if (seconds < 31_536_000_000_000L)        return String.format("%.2f thousand years", seconds / 31_536_000 / 1_000);
        if (seconds < 3.1536e19)                  return String.format("%.2f million years", seconds / 31_536_000 / 1_000_000);
        return "Longer than the age of the universe";
    }
}
