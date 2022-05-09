package jamjam.probability;

import jamjam.aux.Utils;
import lombok.NonNull;

import static jamjam.Sum.sum;
import static jamjam.aux.Utils.trim;
import static java.lang.StrictMath.log10;

public class StatisticalDistance{

    /**
     * Calculates relative entropy of two discrete probability distributions
     * @param referenceDistribution The distribution to be compared to.
     * @param actualDistribution The actual discrete probability distribution presented as an array of values that
     *                           represent probabilities of outcomes.
     * @return Entropy value.
     * @implSpec Trimming of every element via {@link Utils#trim(double)} in the sum is a must here. In addition, one
     *           more trim is applied at the end to keep the actual value non-negative as it has to be by definition.
     *           That makes the returned value biased, but this bias is consistent and should not significantly affect
     *           the outcome.
     */
    public static double KullbackLeiblerDivergence(double @NonNull [] referenceDistribution,
                                                   double @NonNull [] actualDistribution){

        if (referenceDistribution.length != actualDistribution.length)
            throw new IllegalArgumentException("Distributions must have the same shape.");

        if (referenceDistribution.length <= 1)
            throw new IllegalArgumentException("Probability distribution must have at least two outcomes.");

        if (sum(referenceDistribution) != 1. || sum(actualDistribution) != 1.)
            throw new IllegalArgumentException("Probability distribution must add up to 1.");

        for (int i = 0; i < referenceDistribution.length; i++)
            if (referenceDistribution[i] < 0. || referenceDistribution[i] > 1. ||
                    actualDistribution[i] < 0. || actualDistribution[i] > 1.)
                throw new IllegalArgumentException("Probability must be in the range [0; 1].");

        double[] temp = new double[actualDistribution.length];

        for (int outcomeId = 0; outcomeId < actualDistribution.length; outcomeId++)
            temp[outcomeId] = referenceDistribution[outcomeId] *
                                    log10(trim(referenceDistribution[outcomeId]) / trim(actualDistribution[outcomeId]));
        return trim(sum(temp));
    }
}