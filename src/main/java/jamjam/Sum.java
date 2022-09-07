package jamjam;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static jamjam.arrays.Product.productNoCheck;
import static jamjam.aux.Utils.lengthParity;
import static java.lang.StrictMath.abs;
import static java.util.stream.IntStream.range;

@SuppressWarnings("unused")
public class Sum {

    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;

    /**
     * Implements a compensated summation algorithm to reduce accumulated error.
     *
     * @param x An array of doubles.
     * @return total sum, -Inf, Inf, or NaN.
     * @implSpec Uses Kahan-Babushka-Neumaier scheme (scalar), potentially the slowest one available.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     * undefined operation such as Infinity - Infinity as per Java specification.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double sum(final double @NonNull ... x) {
        switch (x.length) {
            case 0:
                return 0;
            case 1:
                return x[0];
            default:
                val acc = new Accumulator();
                acc.sum(x);
                return acc.getSum();
        }
    }


    /**
     * Implements a weighted version of the compensated summation algorithm {@link #sum(double[])}. When weights are
     * {@code null} the conventional scheme is used, else values are multiplied by corresponding weights and passed to
     * {@link #sum(double[])}.
     *
     * @param x       An array of doubles.
     * @param weights An array with weights of values, nullable.
     * @return sum, -Inf, Inf, or NaN.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     * undefined operation such as Infinity - Infinity as per Java specification.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double weightedSum(final double @NonNull [] x, final double @Nullable [] weights) {
        if (weights != null) {
            lengthParity(x, weights);
            return sum(productNoCheck(x, weights));
        } else return sum(x);
    }

    /**
     * This method generates an array of cumulative sums using the {@link #sum(double[])} method. For a given input
     * array {@code {x1, x2, x3}} the result is {@code {x1, x1 + x2, x1 + x2 + x3}}.
     *
     * @param x An array of initial values.
     * @return An array of cumulative sums.
     * @see #sum(double[])
     */
    public static double @NotNull [] cumulativeSum(final double @NonNull [] x) {
        if (x.length == 0)
            return new double[]{0. };
        if (x.length == 1)
            return x;
        val cumulativeSum = new double[x.length];
        cumulativeSum[0] = x[0];
        val acc = new Accumulator();
        acc.sum(x[0]);
        for (var i = 1; i < x.length; i++) {
            acc.sum(x[i]);
            cumulativeSum[i] = acc.getSum();
        }
        return cumulativeSum;
    }

    /**
     * A weighted version of the {@link #cumulativeSum(double[])} method, when weights are {@code null}, runs
     * {@link #cumulativeSum(double[])} instead.
     *
     * @param x       Actual values.
     * @param weights Corresponding weights.
     * @return An array of weighted cumulative sums.
     */
    public static double @NonNull [] weightedCumulativeSum(final double @NonNull [] x,
                                                           final double @Nullable [] weights) {
        if (weights != null)
            lengthParity(x, weights);

        return cumulativeSum(weights == null ? x : range(0, x.length).mapToDouble(i -> x[i] * weights[i]).toArray());
    }

    /**
     * A complementary class for the cases when the sum is accumulated over time rather that calculated immediately.
     */
    public static class Accumulator {

        /**
         * The conventional sum with no corrections.
         */
        private double uncorrectedSum;
        /**
         * The first order error corrector.
         */
        private double corrector;

        private double temp;

        public Accumulator() {
            uncorrectedSum = -0.d;
            corrector = 0;
        }

        /**
         * Calculates both the conventional sum and the corrector.
         *
         * @param x A double to be added to the sum.
         */
        public void sum(final double x) {
            temp = uncorrectedSum + x;
            corrector -= abs(uncorrectedSum) >= abs(x) ? ((uncorrectedSum - temp) + x) : ((x - temp) + uncorrectedSum);
            uncorrectedSum = temp;
        }

        /**
         * Calculates both the conventional sum and the corrector.
         *
         * @param x An array of doubles to be added to the sum.
         */
        public void sum(final double @NonNull ... x) {
            if (x.length == 0) return;
            Arrays.stream(x).forEach(this::sum);
        }

        /**
         * @return the compensated sum defined as {@code uncorrectedSum - corrector}.
         */
        public double getSum() {
            return uncorrectedSum - corrector;
        }

        /**
         * Flushes the class state, sets {@code corrector} & {@code uncorrectedSum} both to 0.
         */
        public void flush() {
            corrector = 0;
            uncorrectedSum = 0;
        }
    }
}
