package jamjam.aux;

import com.github.skjolber.stcsv.sa.StringArrayCsvReader;
import lombok.NonNull;
import lombok.val;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static java.lang.StrictMath.max;

public class Utils {
    /**
     * This function loops over all values in the input array and permutates them randomly.
     * @param x An input array of doubles.
     * @param generator A random generator object.
     * @implNote Based on the Fisher–Yates shuffle.
     * @implSpec No XOR is used to keep the code readable.
     */
    public static void shuffleDoubleArray(double @NonNull [] x, Random generator)
    {
        int index;
        double temp;
        for (var i = x.length - 1; i > 0; i--)
        {
            index = generator.nextInt(i + 1);
            temp = x[index];
            x[index] = x[i];
            x[i] = temp;
        }
    }

    /**
     * A decision tree that shows when a given algorithm fails.
     * @param result A particular value returned by a given method.
     * @param expected The expected return value of the same method.
     * @param relativeError *A priori* relative error expected from the method. Relevant only when both expected and
     *                      actual values both are not Inf, -Inf, or NaN.
     * @return Status value, 0 when computations are withing the margin of error, -1 when a subnormal number (underflow)
     *         is encountered, 1 when there are unexpected -Inf, Inf, or NaN.
     * @see  <a href="https://docs.oracle.com/cd/E60778_01/html/E60763/z4000ac020351.html">Undeflow</a>
     */
    public static int returnRelativeAccuracyStatus(double result, double expected, double relativeError){
        int status;
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
        return status;
    }

    /**
     * An auxiliary method to read testing data from attached CSV files.
     * @param is An {@code InputStream} object, nullable.
     * @return values read from file or null.
     */
    public static double[] readTestingValues(InputStream is){
        if (is != null) {
            val in = new BufferedReader(new InputStreamReader(is));
            try {
                val reader = StringArrayCsvReader.builder().build(in);
                List<String> rawVal = new ArrayList<>(10000);

                String[] next;
                while ((next = reader.next()) != null) rawVal.add(next[0]);

                val v = new double[rawVal.size()];
                IntStream.range(0, rawVal.size()).forEach(i -> v[i] = Double.parseDouble(rawVal.get(i)));
                return v;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Checks if the value is positive and normal, returns the value itself or {@code MIN_NORMAL} when the check fails.
     * @param x Double value.
     * @return x or MIN_NORMAL.
     */
    public static double trim(double x){
        return max(x, Double.MIN_NORMAL);
    }
}
