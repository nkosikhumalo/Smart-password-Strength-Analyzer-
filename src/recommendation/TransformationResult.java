package recommendation;

import java.util.ArrayList;
import java.util.List;

public class TransformationResult {
    private String password;
    private final List<String> explanation = new ArrayList<>();

    public TransformationResult(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getExplanation() {
        return explanation;
    }

    public void addExplanation(String detail) {
        explanation.add(detail);
    }
}
