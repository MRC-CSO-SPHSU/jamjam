package jamjam;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import static jamjam.Sum.sum;
import static jamjam.aux.Utils.lengthParity;
import static jamjam.aux.Utils.meanLengthCheck;
import static java.util.stream.IntStream.*;

public final class Mean {

    /**
     * Calculates a weighted arithmetic average of the provided data input.
     *
     * @param x       Initial double values.
     * @param weights Corresponding weights.
     * @return mean, NaN or Inf.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     * more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered.
     */
    public static double mean(final double @NonNull [] x, final double @Nullable [] weights) {
        meanLengthCheck(x);

        // fixme filter out subnormal values?
        if (weights != null) {
            lengthParity(x, weights);
            return sum(range(0, x.length).mapToDouble(i -> x[i] * weights[i]).toArray()) / sum(weights);
        } else return uncheckedMean(x);
    }

    /**
     * Calculates arithmetic average of all input values.
     *
     * @param x An array of doubles.
     * @return mean, Inf, -Inf, or NaN.
     * @throws NullPointerException When input is {@code null}.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     * more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered
     */
    public static double mean(final double @NonNull [] x) {
        meanLengthCheck(x);
        return sum(x) / x.length;
    }

    private static double uncheckedMean(final double @NonNull [] x) {
        return sum(x) / x.length;
    }
}
