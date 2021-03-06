package jamjam;

import jamjam.aux.Utils;

import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MeanTest extends Utils {

    @Test @DisplayName("Test calculating mean values of NIST datasets") void mean() {
        val testSize = 100000;
        val re = 1e-15;
        val generator = new Random(0);

        val files = new String[] {"lew", "mavro", "lottery", "michelson", "pidigits", "acc1", "acc2", "acc3", "acc4"};

        var testCounter = 0;

        for (var f : files) {
            val is = getClass().getClassLoader().getResourceAsStream( f + ".csv");

            val dataColumn = readTestingValues(is);
            if (dataColumn != null) testCounter++;

            val v = new double[dataColumn.length - 3];

            val  expectedMean = dataColumn[0];
            System.arraycopy(dataColumn, 3, v, 0, v.length);

            for (var i = 0; i < testSize; i++){
                shuffleDoubleArray(v, generator);
                var mean = Mean.mean(v);
                var status = returnRelativeAccuracyStatus(mean, expectedMean, re);

                var m = String.format(" (|% 6.16e| observed vs |% 6.16e| expected);", mean, expectedMean);

                assertAll("Should return a neutral test status value i.e. 0",
                        () -> assertEquals(status, 0, m),
                        () -> assertNotEquals(status, -1, m + " [test uses subnormal value]"));

            }
        }
        assertEquals(files.length, testCounter, "Failed to run through all datasets");
    }

    @Test @DisplayName("Test size checks") void size() {
        assertThrows(ArithmeticException.class, () -> Mean.mean(new double[]{0.}), "Size check fails.");
    }

    /**
     * @implNote IDEs might throw a warning regarding the argument being {@code null}. However, since the null check is
     * implemented implicitly via lombok, this test *has* to stay here for the sake of future releases.
     */
    @Test @DisplayName("Test null input") void input() {
        assertThrows(NullPointerException.class, () -> Mean.mean(null), "Null input test fails.");
    }

    @Disabled @Test @DisplayName("Test overflow") void overflow() {
        val testData = new double[]{Double.MAX_VALUE, Double.MAX_VALUE / 2};
        assertEquals(Mean.mean(testData), 0.75 * Double.MAX_VALUE, "Overflow check fails.");
    }

    @Test @DisplayName("Test weighted mean") void weightedMean() {
        assertAll("Should pass all basic checks, the rest is done by regular mean tests.",
                () -> assertThrows(NullPointerException.class, () -> Mean.mean(null, null),
                                   "Null input test fails."),
                () -> assertThrows(ArithmeticException.class, () -> Mean.mean(new double[1], null),
                        "Input size check fails."),
                () -> assertThrows(ArithmeticException.class, () -> Mean.mean(new double[2], new double[3]),
                        "Input size comparison fails."),
                () -> assertEquals(Mean.mean(new double[]{80., 90.}, new double[]{20., 30.}), 86.,
                                   "Weighting fails."),
                () -> assertEquals(Mean.mean(new double[]{2., 2.}, new double[]{2., 2.}), 2.,
                        "Can't pass identical weights check."));

    }

    @Test void testMean() {
        assertThrows(ArithmeticException.class, () -> Mean.mean(new double[]{20., 30.}, new double[]{20., 30., 40.}));
        assertEquals(2., Mean.mean(new double[]{2., 2.}, null), "Can't pass null weights check.");
    }

}

