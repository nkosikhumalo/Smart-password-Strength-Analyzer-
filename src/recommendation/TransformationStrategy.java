package recommendation;

import java.util.Random;

public interface TransformationStrategy {
    void apply(TransformationResult result, PasswordWeaknessReport report, Random random, PasswordPolicy policy);
}
