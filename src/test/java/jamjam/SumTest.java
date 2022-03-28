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

class SumTest extends Utils {

    /**
     * Generates an ill-conditioned sum and some variations.
     */
    @Disabled @DisplayName("Test calculating KBK sums") @Test void sum(){
        Random generator = new Random(0);
        val sampleSize = 100000;
        val temp = new double[200];
        val samples = new double[sampleSize];

        val vals = new double[]{7, 1e100, -7, -1e100, -9e-20, 8e-20};
        val values = new double[260];
        IntStream.range(0, 10).forEach(n -> System.arraycopy(vals, 0, values, n * 6, 6));
        double s = 0;
        for (var j = 0; j < temp.length; j++){
            temp[j] = pow(generator.nextGaussian() * generator.nextDouble(), 7) - s;
            s += temp[j];
        }
        System.arraycopy(temp, 0, values, 60, temp.length);

        for (var i = 0; i < sampleSize; i++){
            shuffleDoubleArray(values, generator);
            samples[i] = Sum.sum(values);
        }

        IntStream.range(0, sampleSize - 1).forEach(i -> assertEquals(samples[i], samples[i + 1], 1e-16));
    }

    /**
     * Generates multiple ill-conditioned sums.
     */
    @DisplayName("Test summation and function sanity") @Test void input(){
        assertAll("KBK summation scheme works incorrectly.",
                () -> assertEquals(Sum.sum(1, 1e100, 1, -1e100), 2.0, "Incorrect rounding-off."),
                () -> assertEquals(Sum.sum(IntStream.range(1, 11).mapToDouble(i -> 1.0 * i).toArray()), 55.0,
                                   "Correct summation fails."),
                () -> assertEquals(Sum.sum(7, 8, 9), Sum.sum(8, 9, 7), "Permutation affects the answer."),
                () -> assertEquals(Sum.sum(IntStream.range(1, 11).mapToDouble(i -> 1.0 * i).toArray()),
                        Sum.sum(IntStream.iterate(11 - 1, i -> i - 1).limit(10).mapToDouble(i -> 1.0 * i).toArray()),
                           "Inverse order of elements breaks the sum."),
                () -> assertEquals(Sum.sum(-0.0, -0.0), -0.0, ""),
                () -> assertThrows(ArithmeticException.class, () -> Sum.sum(new double[]{0.}), "Size check fails."),
                () -> assertThrows(NullPointerException.class, () -> Sum.sum(null), "Null check fails.")
        );
    }

    /**
     * Calculates integral of {@code sin(x)} from 0 to pi. This is mostly a smoke test, but still works well.
     * @implNote Based on the midpoint rule
     */
    @DisplayName("Test accuracy of summation and stability after permutations") @Test void integralSum(){
        Random generator = new Random(0);
        val NLIM = 50000000;
        val startPoint = 0.;
        val endPoint = Math.PI;
        val dx = (endPoint - startPoint) / NLIM;
        val midpoints = IntStream.range(0, NLIM).mapToDouble(i -> sin(i * dx + dx / 2)).toArray();

        assertEquals(2.0, Sum.sum(midpoints) * dx, 1e-16);

        for (var i = 0; i < 5; i++){
            shuffleDoubleArray(midpoints, generator);
            assertEquals(2.0, Sum.sum(midpoints) * dx, 1e-16);
        }
    }

    @DisplayName("Test weighted sum") @Test void weightedSum() {
        assertAll("Should pass all basic checks, the rest is done by regular mean tests.",
                () -> assertThrows(NullPointerException.class, () -> Sum.weightedSum(null, null),
                        "Null input test fails."),
                () -> assertThrows(ArithmeticException.class, () -> Sum.weightedSum(new double[1], null),
                        "Input size check fails."),
                () -> assertThrows(ArithmeticException.class, () -> Sum.weightedSum(new double[2], new double[3]),
                        "Input size comparison fails."),
                () -> assertEquals(Sum.weightedSum(new double[]{80., 90.}, new double[]{20., 30.}), 4300.,
                        "Weighting fails."),
                () -> assertEquals(Sum.weightedSum(new double[]{2., 2.}, new double[]{2., 2.}), 8.,
                        "Can't pass identical weights check."));
    }
}
