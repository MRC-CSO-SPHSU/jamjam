package jamjam;

import jamjam.aux.Utils;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static jamjam.Sum.sum;
import static jamjam.arrays.Product.product;
import static jamjam.aux.Utils.lengthParity;
import static jamjam.aux.Utils.momentLengthCheck;

public final class Mean {
    private Mean() {
    }

    /**
     * Calculates a weighted arithmetic average of the provided data input.
     *
     * @param x       Initial double values.
     * @param weights Corresponding weights.
     * @return mean, NaN or Inf.
     * @throws NullPointerException When the {@code x} vector is {@code null}.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     * more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered.
     */
    public static double weightedMean(final double @NonNull [] x, final double @Nullable [] weights) {
        momentLengthCheck(x.length, Utils.MomentQualifiers.MEAN);
        if (weights != null) {
            lengthParity(x.length, weights.length);
            val sw = sum(weights);
            if (sw == 0.) throw new ArithmeticException("Division by zero is encountered.");
            return sum(product(x, weights)) / sum(weights);
        } else return mean(x);
    }

    public static double weightedMean(final int @NonNull [] x, final double @Nullable [] weights) {
        return weightedMean(Arrays.stream(x).asDoubleStream().toArray(), weights);
    }

    public static double weightedMean(final long @NonNull [] x, final double @Nullable [] weights) {
        return weightedMean(Arrays.stream(x).asDoubleStream().toArray(), weights);
    }

    /**
     * Calculates arithmetic average of all input values.
     *
     * @param x An array of doubles.
     * @return mean, Inf, -Inf, or NaN.
     * @throws NullPointerException When the input is {@code null}.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     * more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered
     */
    public static double mean(final double @NonNull [] x) {
        momentLengthCheck(x.length, Utils.MomentQualifiers.MEAN);
        return sum(x) / x.length;
    }
}
