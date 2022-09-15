package jamjam;

import lombok.NonNull;

import static jamjam.Variance.unweightedBiasedVariance;
import static jamjam.Variance.unweightedUnbiasedVariance;
import static jamjam.aux.Utils.MomentQualifiers.CORRECTED_STD;
import static jamjam.aux.Utils.MomentQualifiers.UNCORRECTED_STD;
import static jamjam.aux.Utils.momentLengthCheck;
import static java.lang.Double.NaN;
import static java.lang.StrictMath.sqrt;

public class StandardDeviation {

    /**
     * Calculates STD employing {@code 1 / N} factor.
     *
     * @param x A sample.
     * @return STD.
     */
    public static double uncorrectedSampleSTD(final double @NonNull [] x) {
        momentLengthCheck(x.length, CORRECTED_STD);
        return sqrt(unweightedBiasedVariance(x, NaN));
    }

    /**
     * Calculates STD employing {@code 1 / (N - 1)} factor.
     *
     * @param x A sample.
     * @return STD.
     */
    public static double correctedSampleSTD(final double @NonNull [] x) {
        momentLengthCheck(x.length, UNCORRECTED_STD);
        return sqrt(unweightedUnbiasedVariance(x, NaN));
    }
}
