package jamjam.arrays;

import lombok.val;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.fma;

public class LinearSpace {
    private LinearSpace() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Creates {@code totalNumber} sub-intervals of the same length from {@code start} to {@code stop}.
     *
     * @param start            Starting (leftmost) point, not included when {@code totalNumber < 1}.
     * @param stop             Stopping (rightmost) point, excluded when {@code endpointIncluded} is {@code false},
     *                         included otherwise.
     * @param totalNumber      Total number of intervals.
     * @param endpointIncluded A flag to include the stop point.
     * @return An array of values, an empty array, null.
     * @throws IllegalArgumentException When arguments are {@code NaN} or {@code Infinity}, the length of the interval
     *                                  is {@code Infinity}, the size of the step is too small.
     */
    public static double @Nullable [] linspace(final double start, final double stop, final int totalNumber,
                                               final boolean endpointIncluded) {
        if (totalNumber < 0)
            return null;
        if (totalNumber == 0)
            return new double[]{};
        if (totalNumber == 1)
            return new double[]{start};
        if (isInfinite(start) || isInfinite(stop))
            throw new IllegalArgumentException("Infinite ranges are not accepted.");
        if (isNaN(start) || isNaN(stop))
            throw new IllegalArgumentException("NaN is passed as an argument");

        val intervalLength = stop - start;
        if (isInfinite(intervalLength)) throw new IllegalArgumentException("Total length of the interval is infinite.");

        val stepSize = intervalLength / (endpointIncluded ? totalNumber - 1 : totalNumber);
        if (abs(stepSize) < Double.MIN_NORMAL)
            throw new IllegalArgumentException("Underflow, step size is too small, possible loss of precision.");

        return IntStream.range(0, totalNumber).mapToDouble(i -> fma(i, stepSize, start)).toArray();
    }

    /**
     * {@code num} defaults to {@code 50}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(final double start, final double stop, final boolean endpoint) {
        return linspace(start, stop, 50, endpoint);
    }

    /**
     * {@code endpoint} defaults to {@code true}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(final double start, final double stop, final int num) {
        return linspace(start, stop, num, true);
    }

    /**
     * {@code endpoint} defaults to {@code true}, {@code num} defaults to {@code 50}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(final double start, final double stop) {
        return linspace(start, stop, 50, true);
    }
}
