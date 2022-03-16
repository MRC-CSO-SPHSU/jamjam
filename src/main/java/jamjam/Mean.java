package jamjam;

import lombok.NonNull;

import java.util.Arrays;

public final class Mean {

    /**
     * Calculates arithmetic average of all input values.
     * @param x An array of doubles.
     * @return mean, NaN or Inf.
     * @implSpec Has to check the data size & scale it first to reduce chances of overflows.
     * @implNote A relatively slow version due to extra memory management and *ab initio* scaling.
     * @throws ArithmeticException When input contains 1 element only.
     * @throws NullPointerException When input is {@code null}.
     */
    public static double mean(double @NonNull [] x){
        if (x.length < 2)
            throw new ArithmeticException("The size of the array has to be at least 2.");
        else
            return Sum.KBKSum(Arrays.stream(x).map(v -> v / x.length).toArray());
    }


    /** No scaling
     * @param input
     * @return
     */
    static double meanFast(double[] x){
        return Sum.KBKSum(x) / x.length;
    }

    /** pre-scales the data
     * @param input
     * @return
     */
    static double meanScaled(double[] x){
        return Sum.KBKSum(Arrays.stream(x).map(v -> v / x.length).toArray());
    }


    /** weird pairwise sorting?
     * @param input
     * @return
     */
    static double meanSorted(double[] x){
        return 0;
    }

    /** A mix of both
     * @param input
     * @return
     */
    static double meanSortedScaled(double[] x){
        return 0;
    }
}
