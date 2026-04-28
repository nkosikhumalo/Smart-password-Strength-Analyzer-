package recommendation;

import java.util.Random;

public class ControlledShuffleStrategy implements TransformationStrategy {
    @Override
    public void apply(TransformationResult result, PasswordWeaknessReport report, Random random, PasswordPolicy policy) {
        char[] chars = result.getPassword().toCharArray();
        if (chars.length < 6) return;

        int swaps = Math.max(2, chars.length / 4);
        for (int i = 0; i < swaps; i++) {
            int left = 1 + random.nextInt(chars.length - 2);
            int right = 1 + random.nextInt(chars.length - 2);
            char tmp = chars[left];
            chars[left] = chars[right];
            chars[right] = tmp;
        }
        result.setPassword(new String(chars));
        result.addExplanation("shuffled internal characters to reduce patterns");
    }
}
