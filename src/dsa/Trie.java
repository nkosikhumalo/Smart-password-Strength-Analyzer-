package dsa;

import java.util.HashMap;
import java.util.Map;

/**
 * Trie (prefix tree) for O(m) insertion and lookup of strings,
 * where m = length of the word.
 *
 * Used to store common passwords and dictionary words so that
 * substring matching inside a password can be done efficiently.
 */
public class Trie {

    // ── Node ─────────────────────────────────────────────────────────────

    private static class Node {
        final Map<Character, Node> children = new HashMap<>();
        boolean isEndOfWord = false;
    }

    private final Node root = new Node();
    private int wordCount  = 0;

    // ── Public API ────────────────────────────────────────────────────────

    /** Insert a word into the trie (case-insensitive). */
    public void insert(String word) {
        if (word == null || word.isEmpty()) return;
        Node cur = root;
        for (char c : word.toLowerCase().toCharArray()) {
            cur = cur.children.computeIfAbsent(c, k -> new Node());
        }
        if (!cur.isEndOfWord) {
            cur.isEndOfWord = true;
            wordCount++;
        }
    }

    /** Exact match — O(m). */
    public boolean contains(String word) {
        if (word == null) return false;
        Node cur = root;
        for (char c : word.toLowerCase().toCharArray()) {
            cur = cur.children.get(c);
            if (cur == null) return false;
        }
        return cur.isEndOfWord;
    }

    /**
     * Checks whether any substring of {@code text} of length >= minLen
     * exists in the trie.
     *
     * Uses a sliding-window approach: for each start index we walk the
     * trie character-by-character, recording every complete word hit.
     * Time complexity: O(n * m) where n = text length, m = max word length.
     *
     * @return the first matching substring found, or null if none.
     */
    public String findFirstSubstringMatch(String text, int minLen) {
        if (text == null || text.isEmpty()) return null;
        String lower = text.toLowerCase();
        int n = lower.length();

        for (int start = 0; start < n; start++) {
            Node cur = root;
            for (int end = start; end < n; end++) {
                cur = cur.children.get(lower.charAt(end));
                if (cur == null) break;
                int len = end - start + 1;
                if (cur.isEndOfWord && len >= minLen) {
                    return lower.substring(start, end + 1);
                }
            }
        }
        return null;
    }

    /** Bulk load from an array of words. */
    public void insertAll(String[] words) {
        for (String w : words) insert(w);
    }

    public int getWordCount() { return wordCount; }
}
