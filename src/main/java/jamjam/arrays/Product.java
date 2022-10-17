package jamjam.arrays;

import jdk.incubator.vector.DoubleVector;
import lombok.NonNull;
import lombok.val;

import java.util.Arrays;

import static jamjam.aux.Utils.lengthParity;
import static java.util.stream.IntStream.range;
import static jdk.incubator.vector.DoubleVector.SPECIES_PREFERRED;

public class Product {
    private Product() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Calculates the product of two vectors.
     *
     * @param x1 A vector of values.
     * @param x2 Another vector.
     * @return The resulting product,
     * @implSpec Early benchmarks reveal that employing {@link jdk.incubator.vector} becomes beneficial for vector
     * elementwise multiplications when the size of arrays reaches {@code ~5000}
     */
    public static double @NonNull [] product(final double @NonNull [] x1, final double @NonNull [] x2) {
        lengthParity(x1.length, x2.length);
        if (x1.length >= 5000) {
            int i = 0;

            val upperBound = SPECIES_PREFERRED.loopBound(x1.length);
            val r = new double[x1.length];
            DoubleVector vx, vw, vr;
            for (; i < upperBound; i += SPECIES_PREFERRED.length()) {
                vx = DoubleVector.fromArray(SPECIES_PREFERRED, x1, i);
                vw = DoubleVector.fromArray(SPECIES_PREFERRED, x2, i);
                vr = vx.mul(vw);
                vr.intoArray(r, i);
            }

            for (; i < x1.length; i++) r[i] = x1[i] * x2[i];
            return r;
        } else return range(0, x1.length).mapToDouble(i -> x1[i] * x2[i]).toArray();
    }

    public static double @NonNull [] product(final double @NonNull [] x1, final int @NonNull [] x2) {
        val scratch = Arrays.copyOf(x1, x1.length);
        range(0, scratch.length).forEach(i -> scratch[i] *= x2[i]);
        return scratch;
    }

    public static double @NonNull [] product(final double @NonNull [] x1, final long @NonNull [] x2) {
        val scratch = Arrays.copyOf(x1, x1.length);
        range(0, scratch.length).forEach(i -> scratch[i] *= x2[i]);
        return scratch;
    }

    /**
     * In-place implementation, stores the result in {@code x1}
     *
     * @see #product
     */
    public static void productInPlace(final double @NonNull [] x1, final double @NonNull [] x2) {
        lengthParity(x1.length, x2.length);
        range(0, x1.length).forEach(i -> x1[i] *= x2[i]);
    }

    public static void productInPlace(final double @NonNull [] x1, final int @NonNull [] x2) {
        range(0, x1.length).forEach(i -> x1[i] *= x2[i]);
    }

    public static void productInPlace(final double @NonNull [] x1, final long @NonNull [] x2) {
        range(0, x1.length).forEach(i -> x1[i] *= x2[i]);
    }
}
// todo create a version of the product that supports more arguments, replace where needed
// todo code comprehension: express functions in terms of other functions
