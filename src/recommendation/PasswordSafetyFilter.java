package recommendation;

import dsa.Trie;

public class PasswordSafetyFilter {
    private final Trie trie;
    private final int minUnsafeSubstrLength;

    public PasswordSafetyFilter(Trie trie, int minUnsafeSubstrLength) {
        this.trie = trie;
        this.minUnsafeSubstrLength = minUnsafeSubstrLength;
    }

    public boolean isSafe(String candidate) {
        if (candidate == null || candidate.isBlank()) return false;
        return trie.findFirstSubstringMatch(candidate, minUnsafeSubstrLength) == null;
    }
}
