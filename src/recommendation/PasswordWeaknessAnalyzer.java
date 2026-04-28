package recommendation;

import dsa.EntropyCalculator;
import dsa.PatternDetector;
import dsa.Trie;

import java.util.ArrayList;
import java.util.List;

public class PasswordWeaknessAnalyzer {
    private final EntropyCalculator entropyCalculator = new EntropyCalculator();
    private final PatternDetector patternDetector = new PatternDetector();
    private final Trie commonTrie;

    public PasswordWeaknessAnalyzer(Trie commonTrie) {
        this.commonTrie = commonTrie;
    }

    public PasswordWeaknessReport analyze(String password, PasswordPolicy policy) {
        String source = password == null ? "" : password;
        List<String> issues = new ArrayList<>();

        EntropyCalculator.EntropyResult entropy = entropyCalculator.calculate(source);
        PatternDetector.DetectionResult patterns = patternDetector.detect(source, commonTrie);

        if (source.length() < policy.getMinLength()) {
            issues.add("length below " + policy.getMinLength());
        }
        if (!entropy.hasUpper) issues.add("missing uppercase");
        if (!entropy.hasLower) issues.add("missing lowercase");
        if (!entropy.hasDigit) issues.add("missing digits");
        if (!entropy.hasSpecial) issues.add("missing symbols");
        issues.addAll(patterns.issues);

        boolean hasSequential = patterns.issues.stream()
                .anyMatch(i -> i.contains("Sequential") || i.contains("Keyboard pattern"));
        boolean hasRepeated = patterns.issues.stream().anyMatch(i -> i.contains("Repeated"));
        boolean hasCommonSubstring = patterns.issues.stream().anyMatch(i -> i.contains("common word/pattern"));

        return new PasswordWeaknessReport(
                source,
                entropy.hasUpper,
                entropy.hasLower,
                entropy.hasDigit,
                entropy.hasSpecial,
                hasSequential,
                hasRepeated,
                hasCommonSubstring,
                entropy.entropy,
                issues
        );
    }
}
