package jamjam.arrays;

import lombok.NonNull;
import lombok.val;

import java.util.stream.IntStream;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.StrictMath.fma;
import static java.lang.StrictMath.signum;

public class ARange {
    private ARange() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Generates a range of numbers starting with {@code start}, but not including {@code stop}. The length of the step
     * is given by {@code step}.
     *
     * @param start Starting point, included into the interval by default.
     * @param stop  End point.
     * @param step  The length if the step, can be negative.
     * @return An array of {@code double} with evenly spaced points.
     * @throws IllegalArgumentException when input parameters are infinite or NaNs, or {@code step} is zero.
     * @implSpec In the case when {@code stop} falls into the range it is omitted, for instance, here a set
     * {@code start=0., stop=1., step=0.1} must end with {@code 0.9}.
     */
    public static double @NonNull [] arange(final double start, final double stop, final double step) {
        if (isInfinite(start) || isInfinite(stop) || isInfinite(step))
            throw new IllegalArgumentException("Parameters can't be infinite.");
        if (isNaN(start) || isNaN(stop) || isNaN(step))
            throw new IllegalArgumentException("Parameters can't be NaN.");
        if (step == 0.)
            throw new IllegalArgumentException("Zero step causes division by 0.");

        if (stop < start) {
            if (signum(step) == 1) return new double[]{};
            else return generateRange(start, stop, step);
        } else if (stop > start) {
            if (signum(step) == 1) return generateRange(start, stop, step);
            else return new double[]{};
        } else return new double[]{};
    }

    /**
     * The generalized method to generate a range.
     *
     * @throws IllegalArgumentException  when the length of the range is infinite.
     * @throws IndexOutOfBoundsException when the number of intervals exceeds {@code Integer.MAX_VALUE}.
     * @see ARange#arange(double, double, double)
     */
    public static double @NonNull [] generateRange(final double start, final double stop, final double step) {
        val totalLength = stop - start;
        if (isInfinite(totalLength))
            throw new IllegalArgumentException("The interval length is too long to fit into double.");
        val totalIntervals = (long) (totalLength / step);
        if (totalIntervals > Integer.MAX_VALUE)
            throw new IndexOutOfBoundsException("The step size is too small, arrays can't store that many.");
        else {
            val arraySize = (int) totalIntervals;
            return IntStream.range(0, arraySize).mapToDouble(i -> fma(i, step, start)).toArray();
        }
    }

    /**
     * The {@link ARange#arange(double, double, double)}, but the starting point defaults to 0.
     *
     * @see ARange#arange(double, double, double)
     */
    public static double @NonNull [] arange(final double stop, final double step) {
        return arange(0, stop, step);
    }
}
