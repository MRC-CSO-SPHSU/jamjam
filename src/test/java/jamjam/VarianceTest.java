package jamjam;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VarianceTest {

    @Test void testBiasedVariance() {
        assertEquals(0.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}));
        assertEquals(0.75d, Variance.biasedVariance(new double[]{Double.MIN_NORMAL, 2.0d, 2.0d, 2.0d}));
        assertEquals(0.75d, Variance.biasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}));
        assertThrows(IllegalArgumentException.class, () -> Variance.biasedVariance(new double[]{}));
        assertEquals(64.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertEquals(73.0d, Variance.biasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance.biasedVariance(new double[]{}, 10.0d));
        assertEquals(0.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, Double.NaN));
        assertEquals(0.0d,
                Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertThrows(IllegalArgumentException.class, () -> Variance
                .biasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(0.75d, Variance.biasedVariance(new double[]{Double.MIN_NORMAL, 2.0d, 2.0d, 2.0d},
                new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(0.75d,
                Variance.biasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d},
                        new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertThrows(IllegalArgumentException.class, () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d},
                new double[]{Double.NaN, 10.0d, 10.0d, 10.0d}));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, new double[]{0.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(0.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, (double[]) null));
        assertEquals(64.0d,
                Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertEquals(73.0d,
                Variance.biasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance
                .biasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{}, new double[]{10.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d, 2.0d},
                        new double[]{10.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d},
                new double[]{0.0d, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d},
                new double[]{Double.NaN, 10.0d, 10.0d, 10.0d}, 10.0d));
        assertEquals(64.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, null, 10.0d));
        assertEquals(0.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d},
                new double[]{10.0d, 10.0d, 10.0d, 10.0d}, Double.NaN));

        assertThrows(IllegalArgumentException.class,
                () -> Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d},
                        new double[]{2.0d, 2.0d, Double.POSITIVE_INFINITY}));

        assertEquals(0.0d, Variance.biasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d},
                new double[]{0.25d, 0.25d, 0.25d, 0.25d}));
    }

    @Test void testBiasedVariance2() {
        assertThrows(NullPointerException.class, () -> Variance.biasedVariance(null));
    }

    @Test void testBiasedVariance3() {
        assertThrows(NullPointerException.class, () -> Variance.biasedVariance(null, 10.0d));
    }

    @Test void testBiasedVariance4() {
        assertThrows(NullPointerException.class, () -> Variance.biasedVariance(null, null));
    }

    @Test void testBiasedVariance5() {
        assertThrows(NullPointerException.class, () -> Variance.biasedVariance(null, null, 10.0d));
    }

    @Test void testUnbiasedVariance() {
        assertEquals(0.0d, Variance.unbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.unbiasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}));
        assertEquals(1.0d, Variance.unbiasedVariance(new double[]{Double.MIN_NORMAL, 2.0d, 2.0d, 2.0d}));
        assertEquals(1.0d, Variance.unbiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}));
        assertThrows(IllegalArgumentException.class, () -> Variance.unbiasedVariance(new double[]{}));
        assertEquals(85.33333333333333d, Variance.unbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertEquals(97.33333333333333d, Variance.unbiasedVariance(new double[]{0.0d, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class,
                () -> Variance.unbiasedVariance(new double[]{Double.NaN, 2.0d, 2.0d, 2.0d}, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> Variance.unbiasedVariance(new double[]{}, 10.0d));
        assertEquals(0.0d, Variance.unbiasedVariance(new double[]{2.0d, 2.0d, 2.0d, 2.0d}, Double.NaN));
    }

    @Test void testUnbiasedVariance2() {
        assertThrows(NullPointerException.class, () -> Variance.unbiasedVariance(null));
    }

    @Test void testUnbiasedVariance3() {
        assertThrows(NullPointerException.class, () -> Variance.unbiasedVariance(null, 10.0d));
    }

    @Test void testMeanValueValidator() {
        assertEquals(10.0d, Variance.meanValueValidator(10.0d, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(10.0d, Variance.meanValueValidator(Double.NaN, new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertEquals(7.5d, Variance.meanValueValidator(Double.NaN, new double[]{Double.MIN_NORMAL, 10.0d,
                10.0d, 10.0d}));

        assertThrows(NullPointerException.class, () -> Variance.meanValueValidator(0,null));
        assertEquals(0.15000000000000002,  Variance.meanValueValidator(Double.NaN, new double[]{0.1, 0.2}));
        assertEquals(0.15000000000000002,  Variance.meanValueValidator(Double.POSITIVE_INFINITY,
                new double[]{0.1, 0.2}));
        assertEquals(1.0,  Variance.meanValueValidator(1.0, new double[]{0.1, 0.2}));
    }

    @Test void testValuesOutOfRange() {
        assertFalse(Variance.valuesOutOfRange(new double[]{10.0d, 10.0d, 10.0d, 10.0d}));
        assertFalse(Variance.valuesOutOfRange(new double[]{0.0d, 10.0d, 10.0d, 10.0d}));

        assertTrue(Variance.valuesOutOfRange(new double[]{Double.NaN, 10.0d, 10.0d, 10.0d}));
        assertTrue(Variance.valuesOutOfRange(new double[]{}));
        assertTrue(Variance.valuesOutOfRange(new double[]{Double.MIN_NORMAL / 2, 10.0d, 10.0d, 10.0d}));
        assertTrue(Variance.valuesOutOfRange(new double[]{Double.POSITIVE_INFINITY, 10.0d, 10.0d, 10.0d}));

        assertThrows(NullPointerException.class, () -> Variance.valuesOutOfRange(null));
    }
}

