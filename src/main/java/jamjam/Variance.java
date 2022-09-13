package jamjam;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;

import static jamjam.Mean.weightedMean;
import static jamjam.Sum.broadcastSub;
import static jamjam.Sum.sum;
import static jamjam.arrays.Product.product;
import static jamjam.arrays.Product.productInPlace;
import static jamjam.aux.Utils.lengthParity;
import static jamjam.aux.Utils.momentLengthCheck;
import static java.lang.Double.*;

public class Variance {

    /**
     * Calculates unbiased variance of weighted data.
     *
     * @param x       Original data
     * @param weights Integer weights (int).
     * @return unbiased weighted variance.
     * @implNote This implementation assumes "repeat"-type weights (integers counting the number of occurrences for each
     * observation).
     * @see <a href="https://stats.stackexchange.com/questions/47325/bias-correction-in-weighted-variance">Bias
     * correction in weighted variance</a>
     */
    public static double weightedUnbiasedVariance(final double @NonNull [] x, final int @NonNull [] weights) { // fixme add mean?
        momentLengthCheck(x);
        lengthParity(x.length, weights.length);

        var weightSum = 0L;
        for (int weight : weights) weightSum += weight;

        val actualMean = sum(product(x, weights)) / weightSum;
        val scratch = broadcastSub(x, actualMean);
        productInPlace(scratch, scratch);
        productInPlace(scratch, weights);
        return sum(scratch) / (weightSum - 1);
    }

    /**
     * Calculates unbiased variance of weighted data.
     *
     * @param x       Original data
     * @param weights Integer weights (long).
     * @return unbiased weighted variance.
     * @implNote This implementation assumes "repeat"-type weights (integers counting the number of occurrences for each
     * observation).
     * @see <a href="https://stats.stackexchange.com/questions/47325/bias-correction-in-weighted-variance">Bias
     * correction in weighted variance</a>
     */
    public static double weightedUnbiasedVariance(final double @NonNull [] x, final long @NonNull [] weights) { // fixme add mean?
        momentLengthCheck(x);
        lengthParity(x.length, weights.length);

        var weightSum = 0L;
        for (long weight : weights) weightSum += weight;

        val actualMean = sum(product(x, weights)) / weightSum;
        val scratch = broadcastSub(x, actualMean);
        productInPlace(scratch, scratch);
        productInPlace(scratch, weights);
        return sum(scratch) / (weightSum - 1);

    }

    /**
     * Calculates the population (biased) variance. Depending on the parameters passed to the method, employs weights or
     * its mean calculated elsewhere.
     *
     * @param x            An array with values to be processed.
     * @param expectedMean Precalculated mean.
     * @param weights      An array with corresponding weights, when it's {@code null} no weights provided, i.e., the
     *                     all are the same {@code (1/N)}
     * @return Population (biased) variance.
     * @implNote If the weight value is {@code NaN} or {@code +/-Inf}, the actual value is calculated using the data
     * provided.
     */
    public static double weightedBiasedVariance(final double @NonNull [] x, final double expectedMean,
                                                final double @NonNull [] weights) {
        momentLengthCheck(x);
        val meanValue = meanValueValidator(expectedMean, x, weights);

        lengthParity(x.length, weights.length);
        val sumWeights = sum(weights);
        val scratch = broadcastSub(x, meanValue);
        productInPlace(scratch, scratch);
        productInPlace(scratch, weights);

        var variance = sum(scratch) * sumWeights;
        System.arraycopy(weights, 0, scratch, 0, scratch.length);
        productInPlace(scratch, scratch);
        return variance / (sumWeights * sumWeights - sum(scratch));
    }

    /**
     * Calculates the biased variance of a sample.
     *
     * @param x The sample.
     * @return The variance.
     */
    public static double unweightedBiasedVariance(final double @NonNull [] x) {
        return unweightedBiasedVariance(x, NaN);
    }

    public static double unweightedBiasedVariance(final double @NonNull [] x, final double expectedMean) {
        momentLengthCheck(x);
        val actualMean = meanValueValidator(expectedMean, x, null);
        val scratch = broadcastSub(x, actualMean);
        productInPlace(scratch, scratch);
        return sum(scratch) / x.length;
    }

    /**
     * Calculates sample(unbiased) variance.
     *
     * @param x            Provided values.
     * @param expectedMean The value of mean calculated elsewhere.
     * @return Sample(unbiased) variance.
     * @implNote If the weight value is {@code NaN} or {@code +/-Inf}, the actual value is calculated using the data
     * provided.
     **/
    public static double unweightedUnbiasedVariance(final double @NonNull [] x, final double expectedMean) {
        momentLengthCheck(x);
        val actualMean = meanValueValidator(expectedMean, x, null);
        val scratch = broadcastSub(x, actualMean);
        productInPlace(scratch, scratch);
        return sum(scratch) / (scratch.length - 1);
    }

    /**
     * Works just like {@link Variance#unweightedUnbiasedVariance(double[], double)}, the actual mean value is
     * calculated inside the method.
     *
     * @see Variance#unweightedUnbiasedVariance(double[], double)
     */
    public static double unweightedUnbiasedVariance(final double @NonNull [] x) {
        return unweightedUnbiasedVariance(x, NaN);
    }

    /**
     * Checks the actual mean value for NaN/Infinity, if true, calculates the actual using the provided sample.
     *
     * @param actualMeanValue The precalculated mean
     * @param sample          An array of actual values,
     * @return {@code actualMeanValue} if it's not NaN/Infinity, {@code mean(sample)} otherwise.
     */
    static double meanValueValidator(final double actualMeanValue, final double @NonNull [] sample,
                                     final double @Nullable [] weights) {
        return isNaN(actualMeanValue) || isInfinite(actualMeanValue) ? weightedMean(sample, weights) : actualMeanValue;
    }
}
