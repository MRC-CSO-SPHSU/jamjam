package jamjam;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import static java.lang.StrictMath.abs;

public class Sum {
    /**
     * Implements a compensated summation algorithm to reduce accumulated error.
     * @param x An array of doubles.
     * @return sum, -Inf, Inf, or NaN.
     * @implSpec Uses Kahan-Babushka-Neumaier scheme (scalar), potentially the slowest one available.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     *           undefined operation such as Infinity - Infinity as per Java specification.
     * @throws NullPointerException When input is {@code null}.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double sum(double @NonNull ... x){
        switch (x.length) {
            case 0:
                return 0;
            case 1:
                return x[0];
            default:
                var s = x[0];
                var c = 0.f;
                for (var i = 1; i < x.length; i++) {
                    var t = s + x[i];
                    c -= abs(s) >= abs(x[i]) ? ((s - t) + x[i]) : ((x[i] - t) + s);
                    s = t;
                }
                return s - c;
        }
    }

    /**
     * Implements a weighted version of the compensated summation algorithm {@link #sum(double[])}. When weights are
     * {@code null} the conventional scheme is used, else values are multiplied by corresponding weights and passed to
     * {@link #sum(double[])}.
     * @param x An array of doubles.
     * @param weights An array with weights of values, nullable.
     * @return sum, -Inf, Inf, or NaN.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     *           undefined operation such as Infinity - Infinity as per Java specification.
     * @throws NullPointerException When input is {@code null}.
     * @throws ArithmeticException When dimensions of input arrays are different.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double weightedSum(double @NonNull [] x, double @Nullable [] weights){
        if (weights != null && (x.length != weights.length))
            throw new ArithmeticException("The total number of weights differs from the sample size.");

        if (weights == null) return sum(x);
        else return sum(IntStream.range(0, x.length).mapToDouble(i -> x[i] * weights[i]).toArray());
    }

    /**
     * This method generates an array of cumulative sums using the {@link #sum(double[])} method.
     * For a given input array {@code {x1, x2, x3}} the result is {@code {x1, x1 + x2, x1 + x2 + x3}}.
     * @param x An array of initial values.
     * @return An array of cumulative sums.
     * @throws NullPointerException When input is {@code null}.
     * @see #sum(double[])
     */
    // fixme this version is slow and probably less accurate; think about a more general function for this.
    // think how to test this
    public static double[] cumulativeSum(double @NonNull [] x){
        double[] cSum = new double[x.length];
        cSum[0] = x[0];

        for (var i = 1; i < x.length; i++){
            double[] chunk = new double[i + 1];
            System.arraycopy(x, 0, chunk, 0, i + 1);
            cSum[i] = sum(chunk);
        }
        return cSum;
    }

    public static double[] weightedCumulativeSum(double @NonNull [] x, double @Nullable [] weights){
        if (weights != null && (x.length != weights.length))
            throw new ArithmeticException("The total number of weights differs from the sample size.");

        if (weights == null) return cumulativeSum(x);
        else return cumulativeSum(IntStream.range(0, x.length).mapToDouble(i -> x[i] * weights[i]).toArray());
    }
}
