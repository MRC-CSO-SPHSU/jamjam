package jamjam.arrays;

import lombok.val;

import java.util.stream.IntStream;

import static java.lang.Double.isInfinite;
import static java.lang.Double.isNaN;
import static java.lang.StrictMath.fma;
import static java.lang.StrictMath.signum;

public class ARange {// use records to deal with parameters?

    /**
     *
     * @param start
     * @param stop
     * @param step
     * @return
     * @implSpec In the case when {@code stop} falls into the range it is omitted, for instance, here a set
     * {@code start=0., stop=1., step=0.1} must end with {@code 0.9}.
     */
    public static double[] arange(double start, double stop, double step){
        if (isInfinite(start) || isInfinite(stop) || isInfinite(step))
            throw new IllegalArgumentException("Parameters can't be infinite.");
        if (isNaN(start) || isNaN(stop) || isNaN(step))
            throw new IllegalArgumentException("Parameters can't be NaN.");
        if (step == 0.)
            throw new IllegalArgumentException("Zero step causes division by 0.");

        if (stop < start){
            if (signum(step) == 1) return new double[]{};
            else return generateRange(start, stop, step);
        } else if (stop > start) {
            if (signum(step) == 1) return generateRange(start, stop, step);
            else return new double[]{};
        } else return new double[]{};
    }

    static double[] generateRange(double start, double stop, double step) {
        val totalLength = stop - start;
        if (isInfinite(totalLength))
            throw new IllegalArgumentException("The interval length is too long to fit into double.");
        val totalIntervals = (long) (totalLength / step);
        if (totalIntervals > Integer.MAX_VALUE)
            throw new IndexOutOfBoundsException("The step size is too small, arrays can't store that many.");
        else {
            val arraySize = (int) (totalIntervals * step == totalLength ? totalIntervals - 1 : totalIntervals);
            return IntStream.range(0, arraySize).mapToDouble(i -> fma(i, step, start)).toArray();
        }
    }

    public static double[] arange(double stop, double step){
        return arange(0, stop, step);
    }
}
