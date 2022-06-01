package jamjam.arrays;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LinearSpaceTest {

    @Test @DisplayName("General linear space generator.") void linspace() {
        double[] positiveStop = new double[]{0., 0.5, 1., 1.5, 2., 2.5, 3., 3.5, 4., 4.5, 5., 5.5, 6., 6.5, 7., 7.5, 8.,
                8.5, 9., 9.5, 10.};
        assertArrayEquals(positiveStop, LinearSpace.linspace(0., 10., 21, true),
                "Fails to generate a positive range of values.");

        double[] negativeStop = new double[]{0., -0.5, -1., -1.5, -2., -2.5, -3., -3.5, -4., -4.5, -5., -5.5, -6.,
                -6.5, -7., -7.5, -8., -8.5, -9., -9.5, -10.};
        assertArrayEquals(negativeStop, LinearSpace.linspace(0., -10., 21, true),
                "Fails to generate a negative range of values.");

        val v1 = LinearSpace.linspace(0., 10., 100, true);
        val v2 = LinearSpace.linspace(0., 10., 50, true);
        assert v1 != null;
        assert v2 != null;
        assertEquals(v1[99], v2[49]);

        assertNull(LinearSpace.linspace(0., 10., -1, true), "Negative dimension check fails.");
        assertArrayEquals(new double[]{}, LinearSpace.linspace(0., 10., 0, true),
                "Zero interval input fails to return an empty array.");
        assertArrayEquals(new double[]{-10.}, LinearSpace.linspace(-10., 10., 1, true),
                "A single value interval must return the start endpoint value, but it fails.");
    }

    @Test @DisplayName("Linear space generator with fixed length.") void testLinspace() {
        double[] scratch;

        scratch = LinearSpace.linspace(0., 10., true);
        assert scratch != null;
        assertEquals(50, scratch.length,
                "Length of the output array diverges from the expected (default) value");
        assertEquals(scratch[scratch.length - 1], 10., "Actual endpoint value doesn't meet its expected value.");

        scratch = LinearSpace.linspace(0., 10., false);
        assert scratch != null;
        assertEquals(scratch[scratch.length - 1], 9.8, "Actual endpoint value doesn't meet its expected value.");
    }

    @Test @DisplayName("Linear space generator with included endpoint.") void testLinspace1() {
        val scratch = LinearSpace.linspace(0., 10., 50);
        assert scratch != null;
        assertEquals(50, scratch.length,
                "Length of the output array diverges from the expected (default) value");
        assertEquals(scratch[scratch.length - 1], 10., "Actual endpoint value doesn't meet its expected value.");

        val range1 = new int[]{120, -100};
        val range2 = new long[]{120, -100};
        val range3 = new byte[]{120, -100};
        val range4 = new short[]{120, -100};
        assertArrayEquals(LinearSpace.linspace(range1[0], range1[1], 5), LinearSpace.linspace(120., -100., 5),
                "Cast from int to double fails");
        assertArrayEquals(LinearSpace.linspace(range2[0], range2[1], 5), LinearSpace.linspace(120., -100., 5),
                "Cast from long to double fails");
        assertArrayEquals(LinearSpace.linspace(range3[0], range3[1], 5), LinearSpace.linspace(120., -100., 5),
                "Cast from byte to double fails");
        assertArrayEquals(LinearSpace.linspace(range4[0], range4[1], 5), LinearSpace.linspace(120., -100., 5),
                "Cast from short to double fails");
    }

    @Test @DisplayName("Linear space generator with included endpoint and fixed length.") void testLinspace2() {
        val stop = 10.;
        val v = LinearSpace.linspace(0., stop);
        assert v != null;
        assertEquals(50, v.length, "Length of the output array diverges from the expected (default) value");
        assertEquals(stop, v[v.length - 1], "Last value doesn't match the stop endpoint.");
    }

    @Test @DisplayName("Throwables") void testLinspace3() {
        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(0., Double.POSITIVE_INFINITY, 100,
                false));
        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(Double.NEGATIVE_INFINITY, 0, 100,
                false));

        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(0., Double.NaN, 100, false));
        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(Double.NaN, 0, 100, false));

        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(-Double.MAX_VALUE / 2, Double.MAX_VALUE,
                100, false));
        assertThrows(IllegalArgumentException.class, () -> LinearSpace.linspace(10., 10., 100, false));
    }
}