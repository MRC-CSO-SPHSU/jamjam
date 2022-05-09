package jamjam.aux;

import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @DisplayName("Test correct shuffling") @Test void shuffleDoubleArray() {
        val generator = new Random(0);
        val x = new double [10];
        val y = new double []{5.0, 9.0, 10.0, 7.0, 4.0, 6.0, 3.0, 2.0, 8.0, 1.0};

        Arrays.setAll(x, i -> i + 1);
        Utils.shuffleDoubleArray(x, generator);
        assertArrayEquals(x, y);
    }

    @DisplayName("Test relative accuracy status") @Test void returnRelativeAccuracyStatus() {
        assertAll("Should return a status value from the set {-1, 0, 1}",
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.NaN, 1, 1), 1,
                                   "Expected 1, actual - NaN, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(1, Double.NaN, 1), 1,
                                   "Expected NaN, actual - 1, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.NaN, Double.NaN, 1), 0,
                                   "Expected NaN, actual - NaN, this has to return 0 i.e., pass"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(1,1,0.5), 0,
                                   "Expected 1, actual - 1, re is 0.5, this has to return 0 i.e., pass"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY, 1, 1), 1,
                                   "Expected 1, actual - Inf, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(1, Double.POSITIVE_INFINITY, 1), 1,
                                   "Expected Inf, actual - 1, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY, 1, 1), 1,
                                   "Expected 1, actual - -Inf, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(1, Double.NEGATIVE_INFINITY, 1), 1,
                                   "Expected -Inf, actual - 1, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY,
                                   Double.POSITIVE_INFINITY, 1), 0,
                                   "Expected Inf, actual - Inf, this has to return 0 i.e., pass"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY,
                                   Double.NEGATIVE_INFINITY, 1), 0,
                                   "Expected -Inf, actual - -Inf, this has to return 0 i.e., pass"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.POSITIVE_INFINITY,
                                   Double.NEGATIVE_INFINITY, 1), 1,
                                   "Expected -Inf, actual - Inf, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(Double.NEGATIVE_INFINITY,
                                   Double.POSITIVE_INFINITY, 1), 1,
                                   "Expected Inf, actual - -Inf, this has to return 1 i.e., fail"),
                () -> assertEquals(Utils.returnRelativeAccuracyStatus(1, -Double.MIN_NORMAL/2, 1), -1,
                                   "Expected 1, actual - -Double.MIN_NORMAL/2, a sub-normal value, " +
                                           "this has to return -1 i.e., fail"));
    }

    /**
     * @implNote The internal if condition is a temporary solution to avoid {@code null} related warnings.
     */
    @DisplayName("Test data reading") @Test void readTestingValues() {
        val is = getClass().getClassLoader().getResourceAsStream( "lew.csv");
        val dataColumn = Utils.readTestingValues(is);
        if (dataColumn != null) assertEquals(dataColumn[0], -177.435000000000, "Corrupted data");
        assertNull(Utils.readTestingValues(null), "Has to return null if there is null input");
    }

    @Test
    void trim() {
        assertAll("Any number that is less than Double.MIN_NORMAL must result in Double.MIN_NORMAL returned.",
                () -> assertEquals(Utils.trim(0), Double.MIN_NORMAL, "Comparison to 0 fails."),
                () -> assertEquals(Utils.trim(-1), Double.MIN_NORMAL, "Comparison to -1 fails."),
                () -> assertEquals(Utils.trim(1), 1, "Comparison to 1 fails."));
    }
}