package recommendation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class LengthAndDiversityStrategy implements TransformationStrategy {
    @Override
    public void apply(TransformationResult result, PasswordWeaknessReport report, Random random, PasswordPolicy policy) {
        StringBuilder sb = new StringBuilder(result.getPassword());
        boolean changed = false;
        List<String> actions = new ArrayList<>();

        if (!containsType(sb, CharacterPools.UPPER)) {
            inject(sb, CharacterPools.UPPER, random);
            actions.add("added uppercase");
            changed = true;
        }
        if (!containsType(sb, CharacterPools.LOWER)) {
            inject(sb, CharacterPools.LOWER, random);
            actions.add("added lowercase");
            changed = true;
        }
        if (!containsType(sb, CharacterPools.DIGITS)) {
            inject(sb, CharacterPools.DIGITS, random);
            actions.add("added digits");
            changed = true;
        }
        if (!containsType(sb, CharacterPools.SYMBOLS)) {
            inject(sb, CharacterPools.SYMBOLS, random);
            actions.add("added symbols");
            changed = true;
        }

        while (sb.length() < policy.getMinLength()) {
            int bucket = random.nextInt(4);
            Set<Character> set;
            if (bucket == 0) {
                set = CharacterPools.UPPER;
            } else if (bucket == 1) {
                set = CharacterPools.LOWER;
            } else if (bucket == 2) {
                set = CharacterPools.DIGITS;
            } else {
                set = CharacterPools.SYMBOLS;
            }
            inject(sb, set, random);
            changed = true;
        }

        if (sb.length() > result.getPassword().length()) {
            actions.add("increased length");
        }

        if (changed) {
            result.setPassword(sb.toString());
            if (!actions.isEmpty()) {
                result.addExplanation(String.join(", ", actions));
            }
        }
    }

    private boolean containsType(CharSequence password, Set<Character> type) {
        for (int i = 0; i < password.length(); i++) {
            if (type.contains(password.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private void inject(StringBuilder sb, Set<Character> set, Random random) {
        int index = random.nextInt(sb.length() + 1);
        sb.insert(index, randomChar(set, random));
    }

    private char randomChar(Set<Character> set, Random random) {
        int choice = random.nextInt(set.size());
        int i = 0;
        for (char c : set) {
            if (i == choice) return c;
            i++;
        }
        return 'X';
    }
}
