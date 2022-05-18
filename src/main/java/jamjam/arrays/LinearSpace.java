package jamjam.arrays;

import org.jetbrains.annotations.Nullable;

import static java.lang.StrictMath.fma;

public class LinearSpace {
    /**
     * Creates {@code totalNumber} sub-intervals of the same length from {@code start} to {@code stop}.
     * @param start Starting (leftmost) point, not included when {@code totalNumber < 1}.
     * @param stop Stopping (rightmost) point, excluded when {@code endpointIncluded} is {@code false}, included
     *            otherwise.
     * @param totalNumber Total number of intervals.
     * @param endpointIncluded A flag to include the stop point.
     * @return An array of values, an empty array, null.
     */
    public static double @Nullable [] linspace(double start, double stop, int totalNumber, boolean endpointIncluded) {
        if (totalNumber < 0)
            return null;
        if (totalNumber == 0)
            return new double[]{};
        if (totalNumber == 1)
            return new double[]{start};

        double[] values = new double[totalNumber];

        double stepSize = (stop - start) / (endpointIncluded ? totalNumber - 1 : totalNumber);

        for (var i = 0; i < totalNumber; i++)
            values[i] = fma(i, stepSize, start);

        return values;
    }

    /**
     * {@code num} defaults to {@code 50}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(double start, double stop, boolean endpoint) {
        return linspace(start, stop, 50, endpoint);
    }

    /**
     * {@code endpoint} defaults to {@code true}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(double start, double stop, int num) {
        return linspace(start, stop, num, true);
    }

    /**
     * {@code endpoint} defaults to {@code true}, {@code num} defaults to {@code 50}.
     *
     * @see LinearSpace#linspace(double, double, int, boolean)
     */
    public static double @Nullable [] linspace(double start, double stop) {
        return linspace(start, stop, 50, true);
    }
}