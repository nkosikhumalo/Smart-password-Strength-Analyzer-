package recommendation;

import java.util.List;

public class PasswordWeaknessReport {
    private final String originalPassword;
    private final boolean hasUpper;
    private final boolean hasLower;
    private final boolean hasDigit;
    private final boolean hasSpecial;
    private final boolean hasSequentialPattern;
    private final boolean hasRepeatedPattern;
    private final boolean hasCommonSubstring;
    private final double entropy;
    private final List<String> issues;

    public PasswordWeaknessReport(
            String originalPassword,
            boolean hasUpper,
            boolean hasLower,
            boolean hasDigit,
            boolean hasSpecial,
            boolean hasSequentialPattern,
            boolean hasRepeatedPattern,
            boolean hasCommonSubstring,
            double entropy,
            List<String> issues) {
        this.originalPassword = originalPassword;
        this.hasUpper = hasUpper;
        this.hasLower = hasLower;
        this.hasDigit = hasDigit;
        this.hasSpecial = hasSpecial;
        this.hasSequentialPattern = hasSequentialPattern;
        this.hasRepeatedPattern = hasRepeatedPattern;
        this.hasCommonSubstring = hasCommonSubstring;
        this.entropy = entropy;
        this.issues = List.copyOf(issues);
    }

    public String getOriginalPassword() {
        return originalPassword;
    }

    public boolean hasUpper() {
        return hasUpper;
    }

    public boolean hasLower() {
        return hasLower;
    }

    public boolean hasDigit() {
        return hasDigit;
    }

    public boolean hasSpecial() {
        return hasSpecial;
    }

    public boolean hasSequentialPattern() {
        return hasSequentialPattern;
    }

    public boolean hasRepeatedPattern() {
        return hasRepeatedPattern;
    }

    public boolean hasCommonSubstring() {
        return hasCommonSubstring;
    }

    public double getEntropy() {
        return entropy;
    }

    public List<String> getIssues() {
        return issues;
    }
}
