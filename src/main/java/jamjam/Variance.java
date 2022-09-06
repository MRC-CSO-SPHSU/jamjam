package jamjam;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import static jamjam.Mean.mean;
import static jamjam.Sum.sum;
import static java.lang.Double.*;
import static java.lang.StrictMath.abs;

public class Variance {
    /**
     * Calculates the population (biased) variance. Depending on the parameters passed to the method, employs weights or
     * its mean calculated elsewhere.
     *
     * @param x            An array with values to be processed.
     * @param weights      An array with corresponding weights, when it's {@code null} no weights provided, i.e., the all are
     *                     the same (1/N).
     * @param expectedMean Precalculated mean.
     * @return Population (biased) variance.
     * @throws IllegalArgumentException When the size of the sample is 1 or 0; when the array with weights differs from
     *                                  the array of values in terms of shape; when values/weights provided contain
     *                                  {@code NaN}, {@code +/-Inf} or subnormal; weights are negative.
     * @implNote If the weight value is {@code NaN} or {@code +/-Inf}, the actual value is calculated using the data
     * provided.
     */
    public static double biasedVariance(double @NonNull [] x, double @Nullable [] weights,
                                        double expectedMean) {
        if (valuesOutOfRange(x))
            throw new IllegalArgumentException("Array is too small or contains invalid values.");

        if (weights != null && weights.length != x.length)
            throw new IllegalArgumentException("Input arrays have different lengths.");

        if (weights != null)
            for (double w : weights)
                if (isNaN(w) || isInfinite(w) || w < Double.MIN_NORMAL)
                    throw new IllegalArgumentException(String.format("Value |% 6.16e| is out of expected range.", w));

        double meanValue = meanValueValidator(expectedMean, x);
        double[] scratch = new double[x.length];

        if (weights != null) {
            for (int i = 0; i < scratch.length; i++) {
                double t = sum(x[i], -meanValue);
                scratch[i] = weights[i] * t;
                scratch[i] *= t;
            }
            double s = sum(weights);
            return s == 1 ? sum(scratch) : sum(scratch) / s;
        } else {
            for (int i = 0; i < scratch.length; i++) {
                double t = sum(x[i], -meanValue);
                scratch[i] = t * t;
            }
            return mean(scratch);
        }
    }

    /**
     * Works just like {@link Variance#biasedVariance(double[], double[], double)} except weights and mean are not passed
     * to the method.
     *
     * @see Variance#biasedVariance(double[], double[], double)
     */
    public static double biasedVariance(double[] x) {
        return biasedVariance(x, null, NaN);
    }

    /**
     * Works just like {@link Variance#biasedVariance(double[], double[], double)}, results in weighted mean.
     *
     * @see Variance#biasedVariance(double[], double[], double)
     */
    public static double biasedVariance(double[] x, double[] weights) {
        return biasedVariance(x, weights, NaN);
    }

    /**
     * Works just like {@link Variance#biasedVariance(double[], double[], double)}, the actual mean value is calculated
     * inside the method.
     *
     * @see Variance#biasedVariance(double[], double[], double)
     */
    public static double biasedVariance(double[] x, double expectedMean) {
        return biasedVariance(x, null, expectedMean);
    }

    /**
     * Calculates sample(unbiased) variance.
     *
     * @param x            Provided values.
     * @param expectedMean The value of mean calculated elsewhere.
     * @return Sample(unbiased) variance.
     * @throws IllegalArgumentException When the sample size is too small; data values are invalid, i.e., {@code NaN},
     *                                  {@code +/-Inf}, or subnormal.
     * @implNote If the weight value is {@code NaN} or {@code +/-Inf}, the actual value is calculated using the data
     * provided.
     **/
    public static double unbiasedVariance(double @NonNull [] x, double expectedMean) {
        if (valuesOutOfRange(x))
            throw new IllegalArgumentException("Array is too small or contains invalid values.");

        double t;
        double[] accumulator = new double[x.length];

        double meanValue = meanValueValidator(expectedMean, x);

        for (int i = 0; i < x.length; i++) {
            t = sum(x[i], -meanValue);
            accumulator[i] = t * t;
        }
        return sum(accumulator) / (accumulator.length - 1);
    }

    /**
     * Works just like {@link Variance#unbiasedVariance(double[], double)}, the actual mean value is calculated
     * inside the method.
     *
     * @see Variance#unbiasedVariance(double[], double)
     */
    public static double unbiasedVariance(double @NonNull [] x) {
        return unbiasedVariance(x, NaN);
    }

    static double meanValueValidator(final double actualMeanValue, final double @NonNull [] sample) {
        val boolflag = isNaN(actualMeanValue) || isInfinite(actualMeanValue);
        return boolflag ? mean(sample) : actualMeanValue;
    }

    static boolean valuesOutOfRange(final double @NonNull [] actualValues) {
        if (actualValues.length <= 1)
            return true;
        for (double v : actualValues) {
            double a = abs(v);
            if (isNaN(v) || isInfinite(v) || a > 0 && a < Double.MIN_NORMAL)
                return true;
        }
        return false;
    }

}
