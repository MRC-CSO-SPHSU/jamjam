package jamjam.aux;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UtilsTest {
    @Test
    @DisplayName("Test correct shuffling")
    void shuffleDoubleArray() {
        val generator = new Random(0);
        val x = new double[10];
        val y = new double[]{5.0, 9.0, 10.0, 7.0, 4.0, 6.0, 3.0, 2.0, 8.0, 1.0};

        Arrays.setAll(x, i -> i + 1);
        Utils.shuffleDoubleArray(x, generator);
        assertArrayEquals(x, y);

        var scratch = new double[]{};
        Utils.shuffleDoubleArray(scratch, new Random());
        assertArrayEquals(new double[]{}, scratch);

        scratch = new double[]{0.};
        Utils.shuffleDoubleArray(scratch, new Random());
        assertArrayEquals(new double[]{0.}, scratch);
    }

    @Test
    @DisplayName("Shuffle throws")
    void testShuffleDoubleArray() {
        assertThrows(NullPointerException.class, () -> Utils.shuffleDoubleArray(new double[]{}, null));
        assertThrows(NullPointerException.class, () -> Utils.shuffleDoubleArray(null, new Random()));
    }

    @Test
    @DisplayName("Test relative accuracy status")
    void returnRelativeAccuracyStatus() {
        assertAll("Should return a status value from the set {-1, 0, 1}",
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(Double.NaN, 1, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(1, Double.NaN, 1)),
            () -> assertEquals(0, Utils.returnRelativeAccuracyStatus(Double.NaN, Double.NaN, 1)),
            () -> assertEquals(0, Utils.returnRelativeAccuracyStatus(1, 1, 0.5)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY, 1, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(1, Double.POSITIVE_INFINITY, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY, 1, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(1, Double.NEGATIVE_INFINITY, 1)),
            () -> assertEquals(0, Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY, 1)),
            () -> assertEquals(0, Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY,
                Double.NEGATIVE_INFINITY, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, 1)),
            () -> assertEquals(1, Utils.returnRelativeAccuracyStatus(1, 0, .1)),
            () -> assertEquals(-1, Utils.returnRelativeAccuracyStatus(1, -Double.MIN_NORMAL / 2, 1)));
    }

    @Test
    void testReturnRelativeAccuracyStatus() {
        assertEquals(0, Utils.returnRelativeAccuracyStatus(10.0d, 10.0d, 10.0d));
        assertEquals(0, Utils.returnRelativeAccuracyStatus(Double.MIN_NORMAL, 10.0d, 10.0d));
        assertEquals(0, Utils.returnRelativeAccuracyStatus(0.0d, 10.0d, 10.0d));
        assertEquals(0, Utils.returnRelativeAccuracyStatus(0.5d, 10.0d, 10.0d));
        assertEquals(1, Utils.returnRelativeAccuracyStatus(10.0d, Double.MIN_NORMAL, 10.0d));
        assertEquals(0, Utils.returnRelativeAccuracyStatus(10.0d, 0.0d, 10.0d));
        assertEquals(1, Utils.returnRelativeAccuracyStatus(10.0d, 0.0d, 0.0d));
    }

    @Test
    @DisplayName("Test data reading")
    void readTestingValues() {
        val is = getClass().getClassLoader().getResourceAsStream("lew.csv");
        val dataColumn = Utils.readTestingValues(is);

        assert dataColumn != null;
        assertEquals(dataColumn[0], -177.435000000000, "Corrupted data");
        assertNull(Utils.readTestingValues(null), "Has to return null if there is null input");
    }

    @Test
    void testReadTestingValues() {
        assertThrows(RuntimeException.class,
            () -> Utils.readTestingValues(new ByteArrayInputStream("AAAAAAAA".getBytes(StandardCharsets.UTF_8))));
        assertEquals(0, Utils.readTestingValues(new ByteArrayInputStream(new byte[]{})).length);
    }

    @Test
    void testTrim() {
        assertEquals(2.0d, Utils.trim(2.0d));
    }

    @Test
    void trim() {
        assertAll("Any number that is less than Double.MIN_NORMAL must result in Double.MIN_NORMAL returned.",
            () -> assertEquals(Utils.trim(0), Double.MIN_NORMAL, "Comparison to 0 fails."),
            () -> assertEquals(Utils.trim(-1), Double.MIN_NORMAL, "Comparison to -1 fails."),
            () -> assertEquals(Utils.trim(1), 1, "Comparison to 1 fails."));
    }
}
