package jamjam;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static jamjam.StandardDeviation.correctedSampleSTD;
import static jamjam.StandardDeviation.uncorrectedSampleSTD;
import static jamjam.aux.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

class StandardDeviationTest {

    @Test
    void uncorrectedSTD() {
        assertEquals(0.5, uncorrectedSampleSTD(new double[]{104, 105, 104, 105}));
    }

    @Test
    void correctedSTD() {
        val testSize = 100000;
        val generator = new Random(0);

        val map = new HashMap<String, Double>();
        map.put("lew", 1e-15);
        map.put("mavro", 1e-13);
        map.put("lottery", 1e-15);
        map.put("michelson", 1e-13);
        map.put("pidigits", 1e-15);
        map.put("acc1", 1e-16);
        map.put("acc2", 1e-15);
        map.put("acc3", 1e-9);
        map.put("acc4", 1e-8);

        var testCounter = 0;
        double std;
        double expectedSTD;

        for (var f : map.keySet()) {
            val is = getClass().getClassLoader().getResourceAsStream("nist/" + f + ".csv");

            val dataColumn = readTestingValues(is);
            if (dataColumn != null) testCounter++;

            val v = new double[dataColumn.length - 3];

            expectedSTD = dataColumn[1];
            System.arraycopy(dataColumn, 3, v, 0, v.length);

            for (var i = 0; i < testSize; i++) {
                shuffleDoubleArray(v, generator);
                std = correctedSampleSTD(v);
                val status = returnRelativeAccuracyStatus(std, expectedSTD, map.get(f));

                val m = String.format(" (|% 6.16e| observed vs |% 6.16e| expected), see %s;", std, expectedSTD, f);

                assertAll("Should return a neutral test status value i.e. 0",
                    () -> assertEquals(0, status, m),
                    () -> assertNotEquals(status, -1, m + " [test uses subnormal value]"));
            }
        }
        assertEquals(map.keySet().size(), testCounter, "Failed to run through all datasets");
    }
}
