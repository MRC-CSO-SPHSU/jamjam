package jamjam;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import static jamjam.Sum.sum;

public final class Mean {

    /**
     * Calculates a weighted arithmetic average of the provided data input.
     * @param x Initial double values.
     * @param weights Corresponding weights.
     * @return mean, NaN or Inf.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     *           more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered.
     * @throws ArithmeticException When input contains 1 element only.
     * @throws NullPointerException When input is {@code null}.
     */
    public static double mean(double @NonNull [] x, double @Nullable [] weights) {
        if (x.length < 2)
            throw new ArithmeticException("The size of the array has to be at least 2.");

        if (weights != null && (x.length != weights.length))
            throw new ArithmeticException("The total number of weights differs from the sample size.");

        if (weights == null) return mean(x);
        else return sum(IntStream.range(0, x.length).mapToDouble(i -> x[i] * weights[i]).toArray()) / sum(weights);
    }

    /**
     * Calculates arithmetic average of all input values.
     * @param x An array of doubles.
     * @return mean, Inf, -Inf, or NaN.
     * @implSpec Based on the most accurate numerical scheme for mean, slower than the naive version, but faster than
     *           more sophisticated options.
     * @implNote Might result in Inf, -Inf, or NaN if the input is poorly filtered
     * @throws ArithmeticException When input contains 1 element only.
     * @throws NullPointerException When input is {@code null}.
     */
    public static double mean(double @NonNull [] x){
        if (x.length < 2)
            throw new ArithmeticException("The size of the array has to be at least 2.");
        else
            return sum(x) / x.length;
    }
}
