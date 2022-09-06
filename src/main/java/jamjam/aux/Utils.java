package jamjam.aux;

import com.github.skjolber.stcsv.sa.StringArrayCsvReader;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * This function loops over all values in the input array and permutes them randomly inplace.
     *
     * @param x         An input array of doubles.
     * @param generator A random generator object.
     * @implSpec Based on the modified Fisher–Yates shuffle.
     * @implNote No XOR is used to keep the code readable.
     */
    public static void shuffleDoubleArray(final double @NonNull [] x, final @NonNull Random generator) {
        int index;
        double temp;
        if (!((x.length == 0) || (x.length == 1))) {
            for (var i = x.length - 1; i > 0; i--) {
                index = generator.nextInt(i + 1);
                temp = x[index];
                x[index] = x[i];
                x[i] = temp;
            }
        }
    }

    /**
     * Shuffles the elements of an array using a newly constructed RNG.
     *
     * @see #shuffleDoubleArray(double[], Random)
     */
    public static void shuffleDoubleArray(final double @NonNull [] x) {
        shuffleDoubleArray(x, new Random());
    }

    /**
     * A decision tree that shows when a given algorithm fails.
     *
     * @param result        A particular value returned by a given method.
     * @param expected      The expected return value of the same method.
     * @param relativeError *A priori* relative error expected from the method. Relevant only when both expected and
     *                      actual values both are not Inf, -Inf, or NaN.
     * @return Status value, 0 when computations are withing the margin of error, -1 when a subnormal number (underflow)
     * is encountered, 1 when there are unexpected values that fail the check (including -Inf, Inf, or NaN).
     * @see <a href="https://docs.oracle.com/cd/E60778_01/html/E60763/z4000ac020351.html">Undeflow</a>
     */
    public static int returnRelativeAccuracyStatus(double result, double expected, double relativeError) {
        val rNaN = Double.isNaN(result);
        val eNaN = Double.isNaN(expected);

        val rInf = Double.isInfinite(result);
        val eInf = Double.isInfinite(expected);

        val ae = abs(expected);
        if (rNaN || eNaN)
            return rNaN != eNaN ? 1 : 0;
        else if (rInf || eInf)
            return (rInf ? (int) signum(result) : 0) != (eInf ? (int) signum(expected) : 0) ? 1 : 0;
        else if (ae > 0 && ae < Double.MIN_NORMAL)
            return -1;
        else if (expected != 0.)
            return abs(result - expected) / ae > relativeError ? 1 : 0;
        else
            return abs(result) > relativeError ? 1 : 0;
    }

    /**
     * An auxiliary method to read testing data from attached CSV files.
     *
     * @param is An {@code InputStream} object, nullable.
     * @return values read from file or null.
     * @throws RuntimeException When dealing with badly formatted data.
     * @implSpec Deals only numerical values.
     */
    public static double @Nullable [] readTestingValues(final @Nullable InputStream is) {
        if (is != null) {

            try (val in = new BufferedReader(new InputStreamReader(is))) {
                val reader = StringArrayCsvReader.builder().build(in);
                val rawVal = new ArrayList<String>(10000);

                String[] next;
                while ((next = reader.next()) != null) rawVal.add(next[0]);

                val v = new double[rawVal.size()];
                IntStream.range(0, rawVal.size()).forEach(i -> v[i] = Double.parseDouble(rawVal.get(i)));
                return v;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return null;
    }

    /**
     * Checks if the value is positive and normal, returns the value itself or {@code MIN_NORMAL} when the check fails.
     *
     * @param x Double value.
     * @return x or MIN_NORMAL.
     */
    @Contract(pure = true)
    public static double trim(final double x) {
        return max(x, Double.MIN_NORMAL);
    }// or eps

    /**
     * Compares lengths of two arrays.
     *
     * @param x The reference array.
     * @param y The other array for comparison.
     * @throws ArithmeticException when length do not match.
     */
    @Contract(pure = true)
    public static void lengthParity(final double @NotNull [] x, final double @NotNull [] y) {
        if (x.length != y.length) throw new ArithmeticException("Input arrays have different sizes");
    }

    /**
     * Checks if the input is suitable for mean calculation.
     *
     * @param x An array of doubles.
     * @throws ArithmeticException When input contains 1 element only.
     */
    @Contract(pure = true)
    public static void meanLengthCheck(final double @NonNull [] x) {
        if (x.length < 2) throw new ArithmeticException("The size of the array has to be at least 2.");
    }

}
