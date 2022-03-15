package jamjam;

import lombok.NonNull;

import static java.lang.StrictMath.abs;

public class Sum {
    public static double KBKSum(double @NonNull [] input){
        var sum = 0.0;
        var cs = 0.0;
        var ccs = 0.0;
        double c, cc;

        for (var v : input) {
            double t = sum + v;

            c = abs(sum) >= abs(v) ? (sum - t) + v : (v - t) + sum;

            sum = t;
            t = cs + c;

            cc = abs(cs) >= abs(c) ? (cs - t) + c : (c - t) + cs;

            cs = t;
            ccs = ccs + cc;
        }
        return sum + cs + ccs;
    }
}
