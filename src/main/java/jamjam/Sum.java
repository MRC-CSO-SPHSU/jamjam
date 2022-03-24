package jamjam;

import lombok.NonNull;
import lombok.val;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.signum;

public class Sum {
    /**
     * @param x An array of doubles.
     * @return sum, -Inf, Inf, or NaN.
     * @implSpec Uses Kahan–Babuška-Klein scheme (scalar), potentially the slowest one available.
     * @implNote Returns Inf or -Inf in the case of overflow, NaN if the original data contains one, NaN if there is an
     *           undefined operation such as Infinity - Infinity as per Java specification.
     * @throws NullPointerException When input is {@code null}.
     * @throws ArithmeticException When input contains 1 element only.
     * @see <a href="https://doi.org/10.1007/s00607-005-0139-x">A Generalized Kahan-Babuška-Summation-Algorithm</a>
     */
    public static double sum(double @NonNull ... x){
        System.out.println();
        if (x.length < 2)
            throw new ArithmeticException("The size of the array has to be at least 2.");
        else{
            var s = x[0];
            var c = 0.;
            for (int i = 1; i < x.length; i++){
                var t = s + x[i];
                c -= abs(s) >= abs(x[i]) ? ((s - t) + x[i]) : ((x[i] - t) + s);
                s = t;
            }
            return s - c;
        }
    }
}
