package jamjam;

import com.github.skjolber.stcsv.sa.StringArrayCsvReader;
import jamjam.aux.Utils;

import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.junit.jupiter.api.Assertions.*;

class MeanTest extends Utils {

    @DisplayName("Test calculating mean values of NIST datasets") @Test void mean() {
        val size = 100000;
        val re = 1e-15;
        Random generator = new Random(0);

        val files = new String[] {"lew", "mavro", "lottery", "michelson", "pidigits", "acc1", "acc2", "acc3", "acc4"};

        var testCounter = 0;

        for (var f : files) {
            val is = getClass().getClassLoader().getResourceAsStream( f + ".csv");
            if (is != null) {
                val in = new BufferedReader(new InputStreamReader(is));
                testCounter++;
                try {
                    val reader = StringArrayCsvReader.builder().build(in);
                    List<String> rawVal = new ArrayList<>(10000);

                    String[] next;
                    while ((next = reader.next()) != null) rawVal.add(next[0]);
                    double expectedMean = Double.parseDouble(rawVal.get(0));
                    double[] v = new double[rawVal.size() - 3];
                    IntStream.range(3, rawVal.size()).forEach(i -> v[i - 3] = Double.parseDouble(rawVal.get(i)));
                    auxTestRelative(size, v, generator, expectedMean, 0., re, f);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else break;
        }
        assertEquals(testCounter, files.length, "Failed to run through all datasets");
    }

    @DisplayName("Test size checks") @Test void size() {
        assertThrows(ArithmeticException.class, () -> Mean.mean(new double[]{0.}), "Size check fails.");
    }

    /**
     * @implNote IDEs might throw a warning regarding the argument being {@code null}. However, since the null check is
     * implemented implicitly via lombok, this test *has* to stay here for the sake of future releases.
     */
    @DisplayName("Test null input") @Test void input() {
        assertThrows(NullPointerException.class, () -> Mean.mean(null), "Null input test fails.");
    }

    @Disabled @DisplayName("Test overflow") @Test void overflow() {
        val testData = new double[]{Double.MAX_VALUE, Double.MAX_VALUE / 2};
        assertEquals(Mean.mean(testData), 0.75 * Double.MAX_VALUE, "Overflow check fails.");
    }

    public void auxTestRelative(int sampleSize, double[] values, Random generator, double expectedNISTMean,
                                double actualMean, double relativeError, String datasetName){
            for (var i = 0; i < sampleSize; i++){
                shuffleDoubleArray(values, generator);
                testRelative(Mean.mean(values), expectedNISTMean, relativeError);
            }
    }


    @Test public void testPrecision(){
        //GSL_rel(Double.NaN, 1, 1); // expected to fail
        //GSL_rel(1, Double.NaN, 1); // expected to fail
        testRelative(Double.NaN, Double.NaN, 1);
        testRelative(1,1,0.5);

        //GSL_rel(Double.POSITIVE_INFINITY, 1, 1); // expected to fail
        //GSL_rel(1, Double.POSITIVE_INFINITY, 1); // expected to fail
        //GSL_rel(Double.NEGATIVE_INFINITY, 1, 1);  // expected to fail
        //GSL_rel(1, Double.NEGATIVE_INFINITY, 1); // expected to fail
        testRelative(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
        testRelative(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1);
        //GSL_rel(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1); // expected to fail
        //GSL_rel(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1); // expected to fail

        //GSL_rel(1, -Double.MIN_NORMAL/2, 1); // expected to fail

    }

    private void testRelative(double result, double expected, double relativeError){
        int status;
        val m = String.format(" (|% 6.16e| observed vs |% 6.16e| expected);", result, expected);

        val rNaN = Double.isNaN(result);
        val eNaN = Double.isNaN(expected);

        val rInf = Double.isInfinite(result);
        val eInf = Double.isInfinite(expected);

        val ae = abs(expected);
        if (rNaN || eNaN)
            status = rNaN != eNaN ? 1 : 0;
        else if (rInf || eInf)
            status = (rInf ? (int) signum(result) : 0) != (eInf ? (int) signum(expected) : 0) ? 1 : 0;
        else if (ae > 0 && ae < Double.MIN_NORMAL)
            status = -1;
        else if (expected != 0.)
            status = abs(result - expected) / ae > relativeError ? 1 : 0;
        else
            status = abs(result) > relativeError ? 1 : 0;
        assertAll("Should return a neutral test status value i.e. 0",
                () -> assertEquals(status, 0, m),
                () -> assertNotEquals(status, -1, m + " [test uses subnormal value]")
        );
    }

    @Test
    void weightedMean() {
    }

}

