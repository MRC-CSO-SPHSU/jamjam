package jamjam;

import jamjam.aux.Utils;

import lombok.val;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.StrictMath.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SumTest extends Utils {

    /**
     * Generates an ill-conditioned sum and some variations.
     */
    @Disabled @Test @DisplayName("Test calculating KBK sums") void sum() {
        Random generator = new Random(0);
        val sampleSize = 100000;
        val temp = new double[200];
        val samples = new double[sampleSize];

        val vals = new double[]{7, 1e100, -7, -1e100, -9e-20, 8e-20};
        val values = new double[260];
        IntStream.range(0, 10).forEach(n -> System.arraycopy(vals, 0, values, n * 6, 6));
        double s = 0;
        for (var j = 0; j < temp.length; j++) {
            temp[j] = pow(generator.nextGaussian() * generator.nextDouble(), 7) - s;
            s += temp[j];
        }
        System.arraycopy(temp, 0, values, 60, temp.length);

        for (var i = 0; i < sampleSize; i++) {
            shuffleDoubleArray(values, generator);
            samples[i] = Sum.sum(values);
        }

        IntStream.range(0, sampleSize - 1).forEach(i -> assertEquals(samples[i], samples[i + 1], 1e-16));
    }

    /**
     * Generates multiple ill-conditioned sums.
     */
    @Test @DisplayName("Test summation and function sanity") void input(){
        assertAll("KBK summation scheme works incorrectly.",
                () -> assertEquals(Sum.sum(1, 1e100, 1, -1e100), 2.0, "Incorrect rounding-off."),
                () -> assertEquals(Sum.sum(IntStream.range(1, 11).mapToDouble(i -> 1.0 * i).toArray()), 55.0,
                        "Correct summation fails."),
                () -> assertEquals(Sum.sum(7, 8, 9), Sum.sum(8, 9, 7), "Permutation affects the answer."),
                () -> assertEquals(Sum.sum(IntStream.range(1, 11).mapToDouble(i -> 1.0 * i).toArray()),
                        Sum.sum(IntStream.iterate(11 - 1, i -> i - 1).limit(10).mapToDouble(i -> 1.0 * i).toArray()),
                        "Inverse order of elements breaks the sum."),
                () -> assertEquals(Sum.sum(-0.0, -0.0), -0.0, "Doesn't follow IEEE."),
                () -> assertEquals(Sum.sum(0.), 0., ""),
                () -> assertEquals(Sum.sum(Math.PI), Math.PI, ""),
                () -> assertEquals(Sum.sum(), 0., "")
        );
    }

    /**
     * Calculates integral of {@code sin(x)} from 0 to pi. This is mostly a smoke test, but still works well.
     *
     * @implNote Based on the midpoint rule
     */
    @Test @DisplayName("Test accuracy of summation and stability after permutations") void integralSum() {
        Random generator = new Random(0);
        val NLIM = 50000000;
        val startPoint = 0.;
        val endPoint = Math.PI;
        val dx = (endPoint - startPoint) / NLIM;
        val midpoints = IntStream.range(0, NLIM).mapToDouble(i -> sin(i * dx + dx / 2)).toArray();

        assertEquals(2.0, Sum.sum(midpoints) * dx, 1e-16);

        for (var i = 0; i < 5; i++) {
            shuffleDoubleArray(midpoints, generator);
            assertEquals(2.0, Sum.sum(midpoints) * dx, 1e-16);
        }
    }

    @Test @DisplayName("Test weighted sum") void weightedSum() {
        assertAll("Should pass all basic checks, the rest is done by regular mean tests.",
                () -> assertEquals(Sum.weightedSum(new double[]{80., 90.}, new double[]{20., 30.}), 4300.,
                        "Weighting fails."),
                () -> assertEquals(Sum.weightedSum(new double[]{2., 2.}, new double[]{2., 2.}), 8.,
                        "Can't pass identical weights check."),
                () -> assertEquals(0., Sum.weightedSum(new double[]{}, new double[]{}),
                        "Input size check fails."));

        assertEquals(0.0d, Sum.weightedSum(new double[]{}, null));

    }

    @Test @DisplayName("Test cumulative sum") void cumulativeSum() {
        assertAll("Simple sums that can be easily verified manually must work, but they don't",
                () -> assertArrayEquals(Sum.cumulativeSum(new double[]{1., 1., 1.}), new double[]{1., 2., 3.},
                        "The trivial example fails."),
                () -> assertArrayEquals(Sum.cumulativeSum(new double[]{1., 2., 3.}), new double[]{1., 3., 6.},
                        ""));
        assertAll("Basic arrays of length 0, 1, 2 should pass the test, but they fail.",
                () -> assertArrayEquals(Sum.cumulativeSum(new double[]{}), new double[]{},
                        "Empty input results in empty output."),
                () -> assertArrayEquals(Sum.cumulativeSum(new double[]{Math.PI}), new double[]{Math.PI},
                        "An array of size 1 doesn't return a singular value(array)"),
                () -> assertArrayEquals(Sum.cumulativeSum(new double[]{Math.PI, Math.PI}), new double[]{Math.PI, 2 * Math.PI},
                        "A smoke test for an array of size 2 or more doesn't work."));
    }

    @Test @DisplayName("Test weighted cumulative sum") void weightedCumulativeSum() {
        assertArrayEquals(Sum.cumulativeSum(new double[]{1., 2., 3.}),
                Sum.weightedCumulativeSum(new double[]{1., 2., 3.}, new double[]{1., 1., 1.}),
                "Multiplying by a vector of 1 must give the same result as the conventional cumulative sum method.");
        assertArrayEquals(Sum.cumulativeSum(new double[]{0., 0., 0.}),
                Sum.weightedCumulativeSum(new double[]{1., 2., 3.}, new double[]{0., 0., 0.}),
                "Zero weights do not zero the sum.");
        assertArrayEquals(Sum.cumulativeSum(new double[]{2., 4., 6.}),
                Sum.weightedCumulativeSum(new double[]{1., 2., 3.}, new double[]{2., 2., 2.}),
                "Non-trivial weights (2) do not work.");

        val actualWeightedCumulativeSumResult = Sum.weightedCumulativeSum(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, null);
        assertEquals(4, actualWeightedCumulativeSumResult.length);
        assertEquals(2.0d, actualWeightedCumulativeSumResult[0]);
        assertEquals(4.0d, actualWeightedCumulativeSumResult[1]);
        assertEquals(6.0d, actualWeightedCumulativeSumResult[2]);
        assertEquals(8.0d, actualWeightedCumulativeSumResult[3]);
    }

    @Test @DisplayName("Test trivial compensated sum") void trivialSum() {
        val scratch = new double[]{2.0d, 2.0d, 2.0d, 2.0d};
        assertEquals(8, Sum.sum(scratch));
    }
}
