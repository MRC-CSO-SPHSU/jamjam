package jamjam.arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ARangeTest {
    @Test void testArange() {
        assertEquals(1, ARange.arange(10.0d, 10.0d).length);
        assertEquals(0, ARange.arange(1.0d, 10.0d).length);
        assertEquals(0, ARange.arange(0.0d, 10.0d).length);
        assertEquals(0, ARange.arange(-0.5d, 10.0d).length);
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(Double.NaN, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(10.0d, 0.0d));
        assertEquals(0, ARange.arange(10.0d, -0.5d).length);
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(10.0d, Double.NaN));
        assertEquals(1, ARange.arange(-0.5d, -0.5d).length);
        assertEquals(0, ARange.arange(10.0d, 10.0d, 10.0d).length);
        assertEquals(1, ARange.arange(0.0d, 10.0d, 10.0d).length);
        assertEquals(0, ARange.arange(1.0d, 10.0d, 10.0d).length);
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(Double.NaN, 10.0d, 10.0d));
        assertEquals(0, ARange.arange(10.0d, 0.0d, 10.0d).length);
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(10.0d, Double.NaN, 10.0d));
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(10.0d, 10.0d, 0.0d));
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(10.0d, 10.0d, Double.NaN));
        assertEquals(0, ARange.arange(0.0d, 10.0d, -0.5d).length);
        assertEquals(20, ARange.arange(10.0d, 0.0d, -0.5d).length);
    }

    @Test void testArange2() {
        double[] actualArangeResult = ARange.arange(10.0d, 1.0d);
        assertEquals(10, actualArangeResult.length);
        assertEquals(0.0d, actualArangeResult[0]);
        assertEquals(1.0d, actualArangeResult[1]);
        assertEquals(2.0d, actualArangeResult[2]);
        assertEquals(3.0d, actualArangeResult[3]);
        assertEquals(4.0d, actualArangeResult[4]);
        assertEquals(5.0d, actualArangeResult[5]);
        assertEquals(6.0d, actualArangeResult[6]);
        assertEquals(7.0d, actualArangeResult[7]);
        assertEquals(8.0d, actualArangeResult[8]);
        assertEquals(9.0d, actualArangeResult[9]);
    }

    @Test void testArange3() {
        double[] actualArangeResult = ARange.arange(-0.5d, 10.0d, 10.0d);
        assertEquals(1, actualArangeResult.length);
        assertEquals(-0.5d, actualArangeResult[0]);
    }

    @Test void testArange4() {
        double[] actualArangeResult = ARange.arange(0.0d, 10.0d, 1.0d);
        assertEquals(10, actualArangeResult.length);
        assertEquals(0.0d, actualArangeResult[0]);
        assertEquals(1.0d, actualArangeResult[1]);
        assertEquals(2.0d, actualArangeResult[2]);
        assertEquals(3.0d, actualArangeResult[3]);
        assertEquals(4.0d, actualArangeResult[4]);
        assertEquals(5.0d, actualArangeResult[5]);
        assertEquals(6.0d, actualArangeResult[6]);
        assertEquals(7.0d, actualArangeResult[7]);
        assertEquals(8.0d, actualArangeResult[8]);
        assertEquals(9.0d, actualArangeResult[9]);
    }

    @Test void testGenerateRange() {
        assertEquals(0, ARange.generateRange(10.0d, 10.0d, 10.0d).length);
        assertEquals(0, ARange.generateRange(1.0d, 10.0d, 10.0d).length);
    }

    @Test void testGenerateRange2() {
        double[] actualGenerateRangeResult = ARange.generateRange(-0.5d, 10.0d, 10.0d);
        assertEquals(1, actualGenerateRangeResult.length);
        assertEquals(-0.5d, actualGenerateRangeResult[0]);
    }

    @Test void testGenerateRange3() {
        double[] actualGenerateRangeResult = ARange.generateRange(1.0d, 10.0d, 1.0d);
        assertEquals(9, actualGenerateRangeResult.length);
        assertEquals(1.0d, actualGenerateRangeResult[0]);
        assertEquals(2.0d, actualGenerateRangeResult[1]);
        assertEquals(3.0d, actualGenerateRangeResult[2]);
        assertEquals(4.0d, actualGenerateRangeResult[3]);
        assertEquals(5.0d, actualGenerateRangeResult[4]);
        assertEquals(6.0d, actualGenerateRangeResult[5]);
        assertEquals(7.0d, actualGenerateRangeResult[6]);
        assertEquals(8.0d, actualGenerateRangeResult[7]);
        assertEquals(9.0d, actualGenerateRangeResult[8]);
    }

    @Test void testGenerateRange4() {
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(Double.POSITIVE_INFINITY, 10.0d, 0.0d));
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(1.0d, Double.POSITIVE_INFINITY, 0.0d));
        assertThrows(IllegalArgumentException.class, () -> ARange.arange(1.0d, 10.0d, Double.POSITIVE_INFINITY));
    }

    @Test void testGenerateRange5() {
        assertThrows(IllegalArgumentException.class, () -> ARange.generateRange(Double.MAX_VALUE, -Double.MAX_VALUE/2,
                1.0d));
        assertThrows(IndexOutOfBoundsException.class, () -> ARange.generateRange(0., Integer.MAX_VALUE * 1.0, 0.5d));
    }
}

