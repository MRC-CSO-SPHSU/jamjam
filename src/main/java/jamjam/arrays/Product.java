package jamjam.arrays;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import lombok.NonNull;
import lombok.val;

import static jamjam.aux.Utils.lengthParity;
import static java.util.stream.IntStream.range;

public class Product {
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_256;


    /**
     * Adds a size check.
     *
     * @see #productNoCheck
     */
    public static double @NonNull [] product(double @NonNull [] x, double @NonNull [] y) {
        lengthParity(x, y);
        return productNoCheck(x, y);
    }


    /**
     * Calculates the product of two vectors.
     *
     * @param x A vector of values.
     * @param y Another vector.
     * @return The resulting product,
     * @implSpec Early benchmarks reveal that employing {@link jdk.incubator.vector} becomes beneficial for vector
     * elementwise multiplications when the size of arrays reaches {@code ~5000}
     */
    public static double @NonNull [] productNoCheck(double @NonNull [] x, double @NonNull [] y) {
        if (x.length <= 5000) {
            int i = 0;

            val upperBound = SPECIES.loopBound(x.length);
            val r = new double[x.length];
            DoubleVector vx, vw, vr;
            for (; i < upperBound; i += SPECIES.length()) {
                vx = DoubleVector.fromArray(SPECIES, x, i);
                vw = DoubleVector.fromArray(SPECIES, y, i);
                vr = vx.mul(vw);
                vr.intoArray(r, i);
            }

            for (; i < x.length; i++) r[i] = x[i] * y[i];
            return r;
        } else return range(0, x.length).mapToDouble(i -> x[i] * y[i]).toArray();
    }
}
