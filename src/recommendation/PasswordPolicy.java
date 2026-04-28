package recommendation;

public class PasswordPolicy {
    private final int minLength;

    public PasswordPolicy(int minLength) {
        this.minLength = minLength;
    }

    public int getMinLength() {
        return minLength;
    }
}
