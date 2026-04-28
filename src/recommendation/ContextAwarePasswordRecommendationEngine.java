package recommendation;

import dsa.EntropyCalculator;
import dsa.Trie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class ContextAwarePasswordRecommendationEngine {
    private static final String[] COMMON_UNSAFE = {
            "password", "admin", "qwerty", "letmein", "welcome", "123456", "abc123", "passw0rd"
    };

    private final PasswordPolicy policy;
    private final PasswordWeaknessAnalyzer analyzer;
    private final PasswordSafetyFilter safetyFilter;
    private final EntropyCalculator entropyCalculator = new EntropyCalculator();
    private final List<TransformationStrategy> strategies;

    public ContextAwarePasswordRecommendationEngine(PasswordPolicy policy) {
        Trie trie = new Trie();
        trie.insertAll(COMMON_UNSAFE);
        this.policy = policy;
        this.analyzer = new PasswordWeaknessAnalyzer(trie);
        this.safetyFilter = new PasswordSafetyFilter(trie, 4);
        this.strategies = List.of(
                new SubstitutionStrategy(),
                new SequenceBreakerStrategy(),
                new LengthAndDiversityStrategy(),
                new ControlledShuffleStrategy()
        );
    }

    public List<PasswordSuggestion> generateRecommendations(String inputPassword, int count) {
        PasswordWeaknessReport report = analyzer.analyze(inputPassword, policy);
        Set<String> seen = new LinkedHashSet<>();
        List<PasswordSuggestion> suggestions = new ArrayList<>();

        for (int i = 0; i < count * 4; i++) {
            long seed = stableSeed(report.getOriginalPassword(), i);
            Random random = new Random(seed);
            TransformationResult transformed = new TransformationResult(report.getOriginalPassword());

            for (TransformationStrategy strategy : strategies) {
                strategy.apply(transformed, report, random, policy);
            }

            String candidate = transformed.getPassword();
            if (!passesBasicChecks(candidate)) continue;
            if (!safetyFilter.isSafe(candidate)) continue;
            if (!seen.add(candidate)) continue;

            EntropyCalculator.EntropyResult er = entropyCalculator.calculate(candidate);
            int score = scoreCandidate(candidate, er.entropy);
            List<String> notes = new ArrayList<>(transformed.getExplanation());
            notes.add("entropy improved to " + String.format("%.1f bits", er.entropy));

            suggestions.add(new PasswordSuggestion(candidate, score, er.entropy, notes));
            if (suggestions.size() >= count) break;
        }

        suggestions.sort(Comparator.comparingInt(PasswordSuggestion::getScore).reversed()
                .thenComparingDouble(PasswordSuggestion::getEntropy).reversed());
        return suggestions;
    }

    public PasswordWeaknessReport analyzeWeakness(String password) {
        return analyzer.analyze(password, policy);
    }

    private long stableSeed(String base, int variantIndex) {
        return 31L * base.hashCode() + variantIndex * 997L;
    }

    private boolean passesBasicChecks(String password) {
        EntropyCalculator.EntropyResult er = entropyCalculator.calculate(password);
        return password.length() >= policy.getMinLength()
                && er.hasUpper
                && er.hasLower
                && er.hasDigit
                && er.hasSpecial;
    }

    private int scoreCandidate(String password, double entropy) {
        int score = 0;
        score += Math.min(40, password.length() * 2);

        EntropyCalculator.EntropyResult er = entropyCalculator.calculate(password);
        int classes = 0;
        if (er.hasUpper) classes++;
        if (er.hasLower) classes++;
        if (er.hasDigit) classes++;
        if (er.hasSpecial) classes++;
        score += classes * 10;

        score += (int) Math.min(20, entropy / 5);
        return Math.min(100, score);
    }
}
