package dsa;

import java.util.HashSet;
import java.util.Set;

/**
 * Calculates Shannon-inspired password entropy and estimates crack time.
 *
 * Entropy formula:  H = L * log2(R)
 *   L = password length
 *   R = size of the character pool actually used
 *
 * Uses HashSet for O(1) character-category membership checks.
 */
public class EntropyCalculator {

    // ── Character category sets (HashSet → O(1) lookup) ──────────────────

    private static final Set<Character> DIGITS   = new HashSet<>();
    private static final Set<Character> SPECIALS = new HashSet<>();

    static {
        for (char c = '0'; c <= '9'; c++) DIGITS.add(c);
        for (char c : "!@#$%^&*()-_=+[]{}|;:',.<>?/`~\"\\".toCharArray())
            SPECIALS.add(c);
    }

    // ── Public API ────────────────────────────────────────────────────────

    public static class EntropyResult {
        public final double entropy;       // bits
        public final int    charsetSize;
        public final boolean hasUpper;
        public final boolean hasLower;
        public final boolean hasDigit;
        public final boolean hasSpecial;

        EntropyResult(double entropy, int charsetSize,
                      boolean hasUpper, boolean hasLower,
                      boolean hasDigit, boolean hasSpecial) {
            this.entropy     = entropy;
            this.charsetSize = charsetSize;
            this.hasUpper    = hasUpper;
            this.hasLower    = hasLower;
            this.hasDigit    = hasDigit;
            this.hasSpecial  = hasSpecial;
        }
    }

    /**
     * Calculates entropy for the given password.
     * Character pool size is determined by which categories are present.
     */
    public EntropyResult calculate(String password) {
        if (password == null || password.isEmpty()) {
            return new EntropyResult(0, 0, false, false, false, false);
        }

        boolean hasUpper   = false;
        boolean hasLower   = false;
        boolean hasDigit   = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c))  hasUpper   = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (DIGITS.contains(c))   hasDigit   = true;
            else if (SPECIALS.contains(c)) hasSpecial = true;
        }

        int pool = 0;
        if (hasUpper)   pool += 26;
        if (hasLower)   pool += 26;
        if (hasDigit)   pool += 10;
        if (hasSpecial) pool += 32;
        if (pool == 0)  pool  = 26; // fallback: treat as lowercase alpha

        double entropy = password.length() * (Math.log(pool) / Math.log(2));

        return new EntropyResult(entropy, pool, hasUpper, hasLower, hasDigit, hasSpecial);
    }

    /**
     * Estimates time to crack via brute force.
     * Assumes a fast offline GPU attack: 10^10 guesses/second.
     *
     * @param entropy bits of entropy
     * @return human-readable crack time string
     */
    public String estimateCrackTime(double entropy) {
        double combinations     = Math.pow(2, entropy);
        double guessesPerSecond = 1e10; // 10 billion/sec
        double seconds          = combinations / guessesPerSecond;

        if (seconds < 1)                    return "Instant";
        if (seconds < 60)                   return fmt(seconds, "second");
        if (seconds < 3_600)                return fmt(seconds / 60, "minute");
        if (seconds < 86_400)               return fmt(seconds / 3_600, "hour");
        if (seconds < 2_592_000)            return fmt(seconds / 86_400, "day");
        if (seconds < 31_536_000)           return fmt(seconds / 2_592_000, "month");
        if (seconds < 3.1536e10)            return fmt(seconds / 31_536_000, "year");
        if (seconds < 3.1536e13)            return fmt(seconds / 31_536_000 / 1_000, "thousand years");
        if (seconds < 3.1536e19)            return fmt(seconds / 31_536_000 / 1_000_000, "million years");
        return "Longer than the age of the universe";
    }

    private String fmt(double value, String unit) {
        long v = Math.round(value);
        return v + " " + unit + (v == 1 ? "" : "s");
    }
}
