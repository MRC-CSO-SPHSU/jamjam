package jamjam;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorMask;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static jamjam.arrays.Product.product;
import static jamjam.aux.Utils.lengthParity;
import static java.lang.StrictMath.abs;
import static jdk.incubator.vector.DoubleVector.SPECIES_PREFERRED;
import static jdk.incubator.vector.DoubleVector.broadcast;
import static jdk.incubator.vector.VectorOperators.GE;

@SuppressWarnings("unused")
public class Sum {
    private Sum() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Implements a compensated summation algorithm to reduce accumulated error.
     *
     * @param x An array of doubles.
     * @return total sum, -Inf, Inf, or NaN.
     * @throws NullPointerException When the input is {@code null}.
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
                var uncorrectedSum = -0.d;
                var corrector = 0.d;
                double temp;
                for (double v : x) {
                    temp = uncorrectedSum + v;
                    corrector -= abs(uncorrectedSum) >= abs(v) ?
                        ((uncorrectedSum - temp) + v) : ((v - temp) + uncorrectedSum);
                    uncorrectedSum = temp;
                }
                return uncorrectedSum - corrector;
        }
    }

    /**
     * @param x A stream of doubles.
     * @see #sum(double...)
     */
    public static double sum(final @NonNull DoubleStream x) {
        val acc = new Accumulator();
        x.forEach(acc::sum);
        return acc.getSum();
    }


    /**
     * A vectorized compensated sum.
     *
     * @param values A conventional array of {@link DoubleVector}.
     * @return a {@link DoubleVector} of the same size with all the sums.
     * @throws NullPointerException When the input is {@code null}.
     * @implSpec All summation is done line-wise.
     */
    public static @NonNull DoubleVector sum(final DoubleVector @NonNull ... values) {
        if (values.length == 0) return broadcast(SPECIES_PREFERRED, 0.d);
        var uncorrectedSum = broadcast(SPECIES_PREFERRED, -0.d);
        var corrector = broadcast(SPECIES_PREFERRED, 0.d);
        var temp = broadcast(SPECIES_PREFERRED, 0.d);
        VectorMask<Double> mask;

        for (var v : values) {
            temp = uncorrectedSum.add(v);
            mask = uncorrectedSum.abs().compare(GE, v.abs());
            corrector = corrector.sub(v.blend(uncorrectedSum, mask).sub(temp).add(v.blend(uncorrectedSum, mask.not())));
            uncorrectedSum = temp;
        }
        return uncorrectedSum.sub(corrector);
    }

    /**
     * Implements a weighted version of the compensated summation algorithm {@link #sum(double[])}. When weights are
     * {@code null} the conventional scheme is used, else values are multiplied by corresponding weights and passed to
     * {@link #sum(double[])}.
     *
     * @param x       An array of doubles.
     * @param weights An array with weights of values, nullable.
     * @return sum, -Inf, Inf, or NaN.
     * @throws NullPointerException When the {@code x} vector is {@code null}.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     * undefined operation such as Infinity - Infinity as per Java specification.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double weightedSum(final double @NonNull [] x, final double @Nullable [] weights) {
        if (weights != null) {
            lengthParity(x.length, weights.length);
            return sum(product(x, weights));
        } else return sum(x);
    }

    /**
     * This method generates an array of cumulative sums using the {@link #sum(double[])} method. For a given input
     * array {@code {x1, x2, x3}} the result is {@code {x1, x1 + x2, x1 + x2 + x3}}.
     *
     * @param x An array of initial values.
     * @return An array of cumulative sums.
     * @throws NullPointerException When the input is {@code null}.
     * @see #sum(double[])
     */
    public static double @NonNull [] cumulativeSum(final double @NonNull ... x) {
        switch (x.length) {
            case 0:
                return new double[]{0.};
            case 1:
                return x.clone();
            default:
                val cumulativeSum = new double[x.length];
                val acc = new Accumulator();
                for (var i = 0; i < x.length; i++) {
                    acc.sum(x[i]);
                    cumulativeSum[i] = acc.getSum();
                }
                return cumulativeSum;
        }
    }

    /**
     * A weighted version of the {@link #cumulativeSum(double[])} method, when weights are {@code null}, runs
     * {@link #cumulativeSum(double[])} instead.
     *
     * @param x       Actual values.
     * @param weights Corresponding weights.
     * @return An array of weighted cumulative sums.
     * @throws NullPointerException When the {@code x} vector is {@code null}.
     */
    public static double @NonNull [] weightedCumulativeSum(final double @NonNull [] x,
                                                           final double @Nullable [] weights) {
        if (weights != null) {
            lengthParity(x.length, weights.length);
            return cumulativeSum(product(x, weights));
        } else return cumulativeSum(x);
    }

    /**
     * Adds {@code shiftValue} to every value of {@code x}.
     *
     * @param x          An array of doubles.
     * @param shiftValue The value to be shifted by.
     * @return A copy of {@code x} with shifted values.
     */
    public static double @NonNull [] broadcastAdd(final double @NonNull [] x, final double shiftValue) {
        val scratch = Arrays.copyOf(x, x.length);
        IntStream.range(0, x.length).forEach(i -> scratch[i] += shiftValue);
        return scratch;
    }

    /**
     * Calculates the sum of every element of {@code x} and {@code shiftValue.}
     *
     * @param x          An array of doubles.
     * @param shiftValue The value to be shifted by.
     * @return A copy of {@code x} with shifted values.
     */
    public static double @NonNull [] broadcastSub(final double @NonNull [] x, final double shiftValue) {
        val scratch = Arrays.copyOf(x, x.length);
        IntStream.range(0, x.length).forEach(i -> scratch[i] -= shiftValue);
        return scratch;
    }

    public static double @NonNull [] broadcastSub(final int @NonNull [] x, final double shiftValue) {
        val scratch = new double[x.length];
        IntStream.range(0, x.length).forEach(i -> scratch[i] = x[i] - shiftValue);
        return scratch;
    }

    public static double @NonNull [] broadcastSub(final long @NonNull [] x, final double shiftValue) {
        val scratch = new double[x.length];
        IntStream.range(0, x.length).forEach(i -> scratch[i] = x[i] - shiftValue);
        return scratch;
    }

    /**
     * In-place implementation.
     *
     * @see #broadcastAdd(double[], double)
     */
    public static void broadcastAddInPlace(final double @NonNull [] x, final double shiftValue) {
        IntStream.range(0, x.length).forEach(i -> x[i] += shiftValue);
    }

    /**
     * In-place implementation.
     *
     * @see #broadcastAdd(double[], double)
     */
    public static void broadcastSubInPlace(final double @NonNull [] x, final double shiftValue) {
        IntStream.range(0, x.length).forEach(i -> x[i] -= shiftValue);
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
        private double corrector; // todo keep data in vectors and do return v.sub()?

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
         * @throws NullPointerException When the input is {@code null}.
         */
        public void sum(final double @NonNull ... x) {
            if (x.length == 0) return;
            Arrays.stream(x).forEach(this::sum);
        }

        /**
         * @param x A stream of doubles.
         * @see #sum(double...)
         */
        public void sum(final @NonNull DoubleStream x) {
            x.forEach(this::sum);
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
            uncorrectedSum = -0.d;
        }
    }
}
