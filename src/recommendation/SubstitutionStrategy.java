package recommendation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SubstitutionStrategy implements TransformationStrategy {
    private static final Map<Character, Character> REPLACEMENTS = new HashMap<>();

    static {
        REPLACEMENTS.put('a', '@');
        REPLACEMENTS.put('s', '$');
        REPLACEMENTS.put('i', '!');
        REPLACEMENTS.put('o', '0');
        REPLACEMENTS.put('e', '3');
        REPLACEMENTS.put('t', '7');
    }

    @Override
    public void apply(TransformationResult result, PasswordWeaknessReport report, Random random, PasswordPolicy policy) {
        char[] chars = result.getPassword().toCharArray();
        boolean changed = false;
        for (int i = 0; i < chars.length; i++) {
            char lower = Character.toLowerCase(chars[i]);
            if (REPLACEMENTS.containsKey(lower) && random.nextDouble() < 0.45) {
                chars[i] = REPLACEMENTS.get(lower);
                changed = true;
            }
        }
        if (changed) {
            result.setPassword(new String(chars));
            result.addExplanation("added symbol substitutions");
        }
    }
}
