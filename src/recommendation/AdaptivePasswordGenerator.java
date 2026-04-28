package recommendation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AdaptivePasswordGenerator {
    private final Set<Character> upperPool = new HashSet<>();
    private final Set<Character> lowerPool = new HashSet<>();
    private final Set<Character> digitPool = new HashSet<>();
    private final Set<Character> symbolPool = new HashSet<>();

    public AdaptivePasswordGenerator() {
        for (char c = 'A'; c <= 'Z'; c++) upperPool.add(c);
        for (char c = 'a'; c <= 'z'; c++) lowerPool.add(c);
        for (char c = '0'; c <= '9'; c++) digitPool.add(c);
        for (char c : "!@#$%^&*()_+-=[]{};:,.?/".toCharArray()) symbolPool.add(c);
    }

    public List<GeneratedRecommendation> generate(String input, int count) {
        int targetCount = Math.max(3, Math.min(5, count));
        String base = extractBase(input);
        Set<String> unique = new LinkedHashSet<>();
        List<GeneratedRecommendation> output = new ArrayList<>();

        for (int i = 0; i < targetCount * 5 && output.size() < targetCount; i++) {
            Random random = new Random(31L * input.hashCode() + i * 97L + 17L);
            String candidate = buildCandidate(base, random);
            if (!unique.add(candidate)) continue;

            output.add(new GeneratedRecommendation(
                    candidate,
                    "capitalized base, appended random digits/symbols, expanded length"
            ));
        }
        return output;
    }

    private String buildCandidate(String base, Random random) {
        String transformedBase = capitalizeVariant(base, random);
        StringBuilder sb = new StringBuilder(transformedBase);

        appendRandomDigits(sb, random, 4 + random.nextInt(3));
        appendRandomSymbols(sb, random, 2 + random.nextInt(2));
        injectMixedCharacters(sb, random, 2 + random.nextInt(2));
        controlledShuffle(sb, random);

        while (sb.length() < 12 + random.nextInt(5)) {
            int pick = random.nextInt(4);
            if (pick == 0) sb.append(randomFromSet(upperPool, random));
            else if (pick == 1) sb.append(randomFromSet(lowerPool, random));
            else if (pick == 2) sb.append(randomFromSet(digitPool, random));
            else sb.append(randomFromSet(symbolPool, random));
        }

        ensureCharacterDiversity(sb, random);
        return sb.toString();
    }

    private String extractBase(String input) {
        if (input == null || input.isBlank()) return "SecureBase";

        String cleaned = input.replaceAll("[^A-Za-z]", " ").trim();
        if (cleaned.isEmpty()) return "SecureBase";

        String[] tokens = cleaned.split("\\s+");
        String longest = tokens[0];
        for (String token : tokens) {
            if (token.length() > longest.length()) longest = token;
        }
        if (longest.length() < 4) return "Secure" + longest;
        return longest;
    }

    private String capitalizeVariant(String base, Random random) {
        String lower = base.toLowerCase();
        StringBuilder sb = new StringBuilder(lower);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

        if (sb.length() > 4) {
            int index = 1 + random.nextInt(sb.length() - 1);
            sb.setCharAt(index, Character.toUpperCase(sb.charAt(index)));
        }
        return sb.toString();
    }

    private void appendRandomDigits(StringBuilder sb, Random random, int count) {
        for (int i = 0; i < count; i++) sb.append(randomFromSet(digitPool, random));
    }

    private void appendRandomSymbols(StringBuilder sb, Random random, int count) {
        for (int i = 0; i < count; i++) sb.append(randomFromSet(symbolPool, random));
    }

    private void injectMixedCharacters(StringBuilder sb, Random random, int count) {
        for (int i = 0; i < count; i++) {
            int idx = 1 + random.nextInt(Math.max(1, sb.length() - 1));
            int pick = random.nextInt(3);
            char c = pick == 0
                    ? randomFromSet(upperPool, random)
                    : (pick == 1 ? randomFromSet(digitPool, random) : randomFromSet(symbolPool, random));
            sb.insert(idx, c);
        }
    }

    private void controlledShuffle(StringBuilder sb, Random random) {
        if (sb.length() < 7) return;
        int swaps = Math.max(2, sb.length() / 5);
        for (int i = 0; i < swaps; i++) {
            int left = 1 + random.nextInt(sb.length() - 2);
            int right = 1 + random.nextInt(sb.length() - 2);
            char tmp = sb.charAt(left);
            sb.setCharAt(left, sb.charAt(right));
            sb.setCharAt(right, tmp);
        }
    }

    private void ensureCharacterDiversity(StringBuilder sb, Random random) {
        if (!containsFromSet(sb, upperPool)) sb.append(randomFromSet(upperPool, random));
        if (!containsFromSet(sb, lowerPool)) sb.append(randomFromSet(lowerPool, random));
        if (!containsFromSet(sb, digitPool)) sb.append(randomFromSet(digitPool, random));
        if (!containsFromSet(sb, symbolPool)) sb.append(randomFromSet(symbolPool, random));
    }

    private boolean containsFromSet(CharSequence text, Set<Character> set) {
        for (int i = 0; i < text.length(); i++) {
            if (set.contains(text.charAt(i))) return true;
        }
        return false;
    }

    private char randomFromSet(Set<Character> set, Random random) {
        int idx = random.nextInt(set.size());
        int i = 0;
        for (char c : set) {
            if (i == idx) return c;
            i++;
        }
        return 'X';
    }

    public static class GeneratedRecommendation {
        private final String password;
        private final String explanation;

        public GeneratedRecommendation(String password, String explanation) {
            this.password = password;
            this.explanation = explanation;
        }

        public String getPassword() {
            return password;
        }

        public String getExplanation() {
            return explanation;
        }
    }
}
