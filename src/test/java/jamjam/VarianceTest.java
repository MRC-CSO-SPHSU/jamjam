package jamjam;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static jamjam.Variance.weightedUnbiasedVariance;
import static java.lang.Double.NaN;
import static org.junit.jupiter.api.Assertions.*;

class VarianceTest {

    @Test
    void testBiasedVariance() {
        assertEquals(0.0d, Variance.unweightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}));
        assertEquals(0.75d, Variance.unweightedBiasedVariance(new double[]{Double.MIN_NORMAL, 2.0d, 2.0d, 2.0d}));
        assertEquals(0.75d, Variance.unweightedBiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}));
        assertEquals(64.0d, Variance.unweightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertEquals(73.0d, Variance.unweightedBiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertEquals(0.0d, Variance.unweightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, NaN));
        assertEquals(0.0d, Variance.weightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, NaN,
            new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(1.0d, Variance.weightedBiasedVariance(new double[]{Double.MIN_NORMAL, 2.0d, 2.0d, 2.0d}, NaN,
            new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(1.0d,
            Variance.weightedBiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, NaN, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(0.0d,
            Variance.weightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, 2.0d, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(1.0d,
            Variance.weightedBiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, 1.5d, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(0.0d, Variance.weightedBiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, NaN,
            new double[]{0.25d, 0.25d, 0.25d, 0.25d}));
    }

    @Test
    void testBiasedVariance2() {
        assertThrows(NullPointerException.class, () -> Variance.unweightedBiasedVariance(null));
    }

    @Test
    void testBiasedVariance3() {
        assertThrows(NullPointerException.class, () -> Variance.unweightedBiasedVariance((double[]) null, 10.0d));
    }

    @Test
    void testBiasedVariance4() {
        assertThrows(NullPointerException.class, () -> Variance.weightedBiasedVariance(null, NaN, null));
    }

    @Test
    void testBiasedVariance5() {
        assertThrows(NullPointerException.class, () -> Variance.weightedBiasedVariance(null,  10.0d, null));
    }

    @Test
    void testWeightedUnbiasedVariance() {
        assertEquals(0.0d, Variance.unweightedUnbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}));
        assertEquals(1.0d, Variance.unweightedUnbiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}));
        assertThrows(IllegalArgumentException.class, () -> Variance.unweightedUnbiasedVariance(new double[]{}));
        assertEquals(85.33333333333333d, Variance.unweightedUnbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertEquals(97.33333333333333d, Variance.unweightedUnbiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance.unweightedUnbiasedVariance(new double[]{}, 10.0d));
        assertEquals(0.0d, Variance.unweightedUnbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, NaN));
    }

    @Test
    void testUnbiasedVariance2() {
        assertThrows(NullPointerException.class, () -> Variance.unweightedUnbiasedVariance(null));
    }

    @Test
    void testUnbiasedVariance3() {
        assertThrows(NullPointerException.class, () -> Variance.unweightedUnbiasedVariance(null, 10.0d));
    }

    @Test
    void testMeanValueValidator() {
        assertEquals(10.0d, Variance.meanValueValidator(10.0d, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, null));
        assertEquals(10.0d, Variance.meanValueValidator(NaN, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, null));
        assertEquals(7.5d, Variance.meanValueValidator(NaN, new double[]{Double.MIN_NORMAL, 10.0d,
            10.0d, 10.0d}, null));
        assertEquals(0.15000000000000002, Variance.meanValueValidator(NaN, new double[]{0.1, 0.2}, null));
        assertEquals(0.15000000000000002, Variance.meanValueValidator(Double.POSITIVE_INFINITY,
            new double[]{0.1, 0.2}, null));
        assertEquals(1.0, Variance.meanValueValidator(1.0, new double[]{0.1, 0.2}, null));
        assertThrows(NullPointerException.class, () -> Variance.meanValueValidator(0, (double[]) null, null));
    }

    @Test
    void biasedVariance() {
        val x = new double[50_000_000];
        val weights = new double[50_000_000];

        Arrays.fill(x, 2);
        Arrays.fill(weights, 2);

        assertEquals(0, Variance.weightedBiasedVariance(x, NaN, weights));
        assertEquals(0, Variance.weightedBiasedVariance(x, NaN, weights));

        assertThrows(NullPointerException.class, () -> Variance.weightedBiasedVariance(null, NaN, weights));
    }

    @Test
    void unweightedUnbiasedVariance() {
        assertEquals(0, Variance.unweightedUnbiasedVariance(new double[]{10.d, 10.d, 10.d, 10.d}, NaN));
        assertEquals(1, Variance.unweightedUnbiasedVariance(new double[]{1, 2, 3}, NaN));
    }

    @Test
    void unweightedBiasedVariance() {
        assertEquals(0, Variance.unweightedBiasedVariance(new double[]{10.d, 10.d, 10.d, 10.d}));
        assertEquals(0.6666666666666666, Variance.unweightedBiasedVariance(new double[]{1, 2, 3}));
    }

    @Test
    void wikiExample() {
        val x1 = new double[]{2, 2, 4, 5, 5, 5};
        val x2 = new double[]{2, 4, 5};
        val weights = new int[]{2, 1, 3};

        assertEquals(Variance.unweightedUnbiasedVariance(x1), weightedUnbiasedVariance(x2, weights));
    }

    @Test
    void testMeanValueValidator1() {
        assertEquals(10.0d, Variance.meanValueValidator(10.0d, new int[]{10, 10, 10, 10}, null));
        assertEquals(10.0d, Variance.meanValueValidator(NaN, new int[]{10, 10, 10, 10}, null));
        assertEquals(7.5d, Variance.meanValueValidator(NaN, new int[]{0, 10, 10, 10}, null));
        assertEquals(1.5, Variance.meanValueValidator(NaN, new int[]{1, 2}, null));
        assertEquals(1.5, Variance.meanValueValidator(Double.POSITIVE_INFINITY,
            new int[]{1, 2}, null));
        assertEquals(1.0, Variance.meanValueValidator(1.0, new int[]{1, 2}, null));
        assertThrows(NullPointerException.class, () -> Variance.meanValueValidator(0, (int[]) null, null));
    }

    @Test
    void testMeanValueValidator2() {
        assertEquals(10.0d, Variance.meanValueValidator(10.0d, new long[]{10, 10, 10, 10}, null));
        assertEquals(10.0d, Variance.meanValueValidator(NaN, new long[]{10, 10, 10, 10}, null));
        assertEquals(7.5d, Variance.meanValueValidator(NaN, new long[]{0, 10, 10, 10}, null));
        assertEquals(1.5, Variance.meanValueValidator(NaN, new long[]{1, 2}, null));
        assertEquals(1.5, Variance.meanValueValidator(Double.POSITIVE_INFINITY,
            new long[]{1, 2}, null));
        assertEquals(1.0, Variance.meanValueValidator(1.0, new long[]{1, 2}, null));
        assertThrows(NullPointerException.class, () -> Variance.meanValueValidator(0, (long[]) null, null));
    }
}

