package recommendation;

import java.util.Random;

public class SequenceBreakerStrategy implements TransformationStrategy {
    @Override
    public void apply(TransformationResult result, PasswordWeaknessReport report, Random random, PasswordPolicy policy) {
        String password = result.getPassword();
        if (!report.hasSequentialPattern() && !report.hasRepeatedPattern()) {
            return;
        }

        StringBuilder sb = new StringBuilder(password);
        for (int i = 1; i < sb.length(); i++) {
            int diff = sb.charAt(i) - sb.charAt(i - 1);
            if (diff == 1 || diff == -1 || sb.charAt(i) == sb.charAt(i - 1)) {
                sb.insert(i, pickBreaker(random));
                i++;
            }
        }
        result.setPassword(sb.toString());
        result.addExplanation("removed sequence/repetition pattern");
    }

    private char pickBreaker(Random random) {
        char[] breakers = {'#', '%', '8', 'R', 'x'};
        return breakers[random.nextInt(breakers.length)];
    }
}
