package dsa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Detects weak patterns inside a password using:
 *  - Sliding window  → sequential runs (ascending/descending)
 *  - HashMap         → character frequency & repeated-char runs
 *  - Hardcoded sets  → keyboard row sequences
 *  - Trie            → embedded dictionary words (injected externally)
 */
public class PatternDetector {

    // Minimum run length to flag as a sequential pattern
    private static final int SEQ_RUN_MIN   = 3;
    // Minimum repeated-char run to flag
    private static final int REPEAT_RUN_MIN = 3;

    // Keyboard adjacency rows (horizontal sequences)
    private static final String[] KEYBOARD_ROWS = {
        "qwertyuiop", "asdfghjkl", "zxcvbnm",
        "1234567890", "0987654321",
        "qwerty", "asdfgh", "zxcvbn",
        "abcdefghijklmnopqrstuvwxyz"
    };

    // ── Public result type ────────────────────────────────────────────────

    public static class DetectionResult {
        public final List<String> issues      = new ArrayList<>();
        public final List<String> matchedSeqs = new ArrayList<>(); // for scoring
        public int penaltyPoints = 0;
    }

    // ── Main entry point ──────────────────────────────────────────────────

    /**
     * Runs all pattern checks and returns a DetectionResult.
     *
     * @param password  raw password string
     * @param trie      pre-loaded Trie of common/dictionary words
     */
    public DetectionResult detect(String password, Trie trie) {
        DetectionResult result = new DetectionResult();

        checkSequentialRuns(password, result);
        checkRepeatedChars(password, result);
        checkKeyboardPatterns(password, result);
        checkCharFrequency(password, result);
        checkTrieSubstrings(password, trie, result);

        return result;
    }

    // ── 1. Sequential runs (sliding window) ───────────────────────────────

    /**
     * Sliding window scan for ascending or descending ASCII runs.
     * e.g. "12345", "abcde", "EDCBA"
     */
    private void checkSequentialRuns(String password, DetectionResult result) {
        if (password.length() < SEQ_RUN_MIN) return;

        int ascRun  = 1;
        int descRun = 1;

        for (int i = 1; i < password.length(); i++) {
            int diff = password.charAt(i) - password.charAt(i - 1);

            if (diff == 1)  ascRun++;  else ascRun  = 1;
            if (diff == -1) descRun++; else descRun = 1;

            if (ascRun == SEQ_RUN_MIN) {
                String seq = password.substring(i - SEQ_RUN_MIN + 1, i + 1);
                result.issues.add("Sequential ascending run detected: \"" + seq + "\"");
                result.matchedSeqs.add(seq);
                result.penaltyPoints += 15;
            }
            if (descRun == SEQ_RUN_MIN) {
                String seq = password.substring(i - SEQ_RUN_MIN + 1, i + 1);
                result.issues.add("Sequential descending run detected: \"" + seq + "\"");
                result.matchedSeqs.add(seq);
                result.penaltyPoints += 15;
            }
        }
    }

    // ── 2. Repeated character runs ────────────────────────────────────────

    /**
     * Detects runs of the same character using a single pass.
     * e.g. "aaa", "111"
     */
    private void checkRepeatedChars(String password, DetectionResult result) {
        int run = 1;
        for (int i = 1; i < password.length(); i++) {
            if (password.charAt(i) == password.charAt(i - 1)) {
                run++;
                if (run == REPEAT_RUN_MIN) {
                    String seq = password.substring(i - REPEAT_RUN_MIN + 1, i + 1);
                    result.issues.add("Repeated character run: \"" + seq + "\"");
                    result.matchedSeqs.add(seq);
                    result.penaltyPoints += 10;
                }
            } else {
                run = 1;
            }
        }
    }

    // ── 3. Keyboard row patterns ──────────────────────────────────────────

    /**
     * Checks whether the password (lowercased) contains any known
     * keyboard-row sequence. Uses simple substring containment — O(n*k).
     */
    private void checkKeyboardPatterns(String password, DetectionResult result) {
        String lower = password.toLowerCase();
        for (String row : KEYBOARD_ROWS) {
            // Sliding window of length 4+ over the keyboard row
            int windowMin = 4;
            for (int wLen = row.length(); wLen >= windowMin; wLen--) {
                for (int start = 0; start <= row.length() - wLen; start++) {
                    String sub = row.substring(start, start + wLen);
                    if (lower.contains(sub)) {
                        result.issues.add("Keyboard pattern detected: \"" + sub + "\"");
                        result.matchedSeqs.add(sub);
                        result.penaltyPoints += 20;
                        return; // one keyboard hit is enough
                    }
                }
            }
        }
    }

    // ── 4. Character frequency distribution (HashMap) ─────────────────────

    /**
     * Builds a frequency map and flags passwords where a single character
     * accounts for more than 40% of the total length (low diversity).
     */
    private void checkCharFrequency(String password, DetectionResult result) {
        if (password.length() < 4) return;

        Map<Character, Integer> freq = new HashMap<>();
        for (char c : password.toCharArray()) {
            freq.merge(c, 1, Integer::sum);
        }

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            double ratio = (double) entry.getValue() / password.length();
            if (ratio > 0.40) {
                result.issues.add(String.format(
                    "Low character diversity: '%c' appears %.0f%% of the time.",
                    entry.getKey(), ratio * 100));
                result.penaltyPoints += 12;
            }
        }
    }

    // ── 5. Trie substring matching ────────────────────────────────────────

    /**
     * Uses the Trie's sliding-window substring search to find embedded
     * dictionary/common-password words inside the password.
     */
    private void checkTrieSubstrings(String password, Trie trie, DetectionResult result) {
        if (trie == null) return;
        String match = trie.findFirstSubstringMatch(password, 4);
        if (match != null) {
            result.issues.add("Contains common word/pattern: \"" + match + "\"");
            result.matchedSeqs.add(match);
            result.penaltyPoints += 25;
        }
    }
}
