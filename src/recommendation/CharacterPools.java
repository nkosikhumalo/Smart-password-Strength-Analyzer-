package recommendation;

import java.util.HashSet;
import java.util.Set;

public final class CharacterPools {
    public static final Set<Character> UPPER = new HashSet<>();
    public static final Set<Character> LOWER = new HashSet<>();
    public static final Set<Character> DIGITS = new HashSet<>();
    public static final Set<Character> SYMBOLS = new HashSet<>();

    static {
        for (char c = 'A'; c <= 'Z'; c++) UPPER.add(c);
        for (char c = 'a'; c <= 'z'; c++) LOWER.add(c);
        for (char c = '0'; c <= '9'; c++) DIGITS.add(c);
        for (char c : "!@#$%^&*()-_=+[]{};:,.?/".toCharArray()) SYMBOLS.add(c);
    }

    private CharacterPools() {
    }
}
