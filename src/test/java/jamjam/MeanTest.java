package jamjam;

import jamjam.aux.Utils;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MeanTest extends Utils {

    @Test
    @DisplayName("Test calculating mean values of NIST datasets")
    void mean() {
        val testSize = 1;
        val re = 1e-15;
        val generator = new Random(0);

        val map = new HashMap<String, Double>();
        map.put("lew", 1e-16);
        map.put("mavro", 1e-16);
        map.put("lottery", 1e-15);
        map.put("michelson", 1e-16);
        map.put("pidigits", 1e-16);
        map.put("acc1", 1e-16);
        map.put("acc2", 1e-16);
        map.put("acc3", 1e-15);
        map.put("acc4", 1e-15);

        var testCounter = 0;

        for (var f : map.keySet()) {
            val is = getClass().getClassLoader().getResourceAsStream("nist/" + f + ".csv");

            val dataColumn = readTestingValues(is);
            if (dataColumn != null) testCounter++;

            val v = new double[dataColumn.length - 3];

            val expectedMean = dataColumn[0];
            System.arraycopy(dataColumn, 3, v, 0, v.length);

            for (var i = 0; i < testSize; i++) {
                shuffleDoubleArray(v, generator);
                var mean = Mean.mean(v);
                var status = returnRelativeAccuracyStatus(mean, expectedMean, map.get(f));

                var m = String.format(" (|% 6.16e| observed vs |% 6.16e| expected), see %s;", mean, expectedMean, f);

                assertAll("Should return a neutral test status value i.e. 0",
                    () -> assertEquals(status, 0, m),
                    () -> assertNotEquals(status, -1, m + " [test uses subnormal value]"));

            }
        }
        assertEquals(map.keySet().size(), testCounter, "Failed to run through all datasets");
    }


    @Test
    @DisplayName("Test null input")
    void input() {
        assertThrows(NullPointerException.class, () -> Mean.mean(null), "Null input test fails.");
    }

    @Disabled
    @Test
    @DisplayName("Test overflow")
    void overflow() {
        val testData = new double[]{Double.MAX_VALUE, Double.MAX_VALUE / 2};
        assertEquals(Mean.mean(testData), 0.75 * Double.MAX_VALUE, "Overflow check fails.");
    }

    @Test
    @DisplayName("Test weighted mean")
    void weightedMean() {
        assertAll("Should pass all basic checks, the rest is done by regular mean tests.",
            () -> assertThrows(NullPointerException.class, () -> Mean.weightedMean(null, null),
                "Null input test fails."),
            () -> assertEquals(Mean.weightedMean(new double[]{80., 90.}, new double[]{20., 30.}), 86.,
                "Weighting fails."),
            () -> assertEquals(Mean.weightedMean(new double[]{2., 2.}, new double[]{2., 2.}), 2.,
                "Can't pass identical weights check."));

    }

    @Test
    void testMean() {
        assertEquals(2., Mean.weightedMean(new double[]{2., 2.}, null), "Can't pass null weights check.");
    }
}
