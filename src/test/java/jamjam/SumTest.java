package jamjam;

import jamjam.aux.Utils;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.StrictMath.pow;
import static org.junit.jupiter.api.Assertions.*;

class SumTest extends Utils {

    @DisplayName("Test calculating KBK sums") @Test public void sum(){
        Random generator = new Random(0);
        val temp = new double[200];
        for (var i = 0; i < 100000; i++){
            val vals = new double[]{7, 1e100, -7, -1e100, -9e-20, 8e-20};
            val values = new double[260];
            IntStream.range(0, 10).forEach(n -> System.arraycopy(vals, 0, values, n * 6, 6));
            double s = 0;
            for (var j = 0; j < temp.length; j++){
                temp[j] = pow(generator.nextGaussian() * generator.nextDouble(), 7) - s;
                s += temp[j];
            }
            System.arraycopy(temp, 0, values, 60, temp.length);
            shuffleDoubleArray(values, generator);

            assertNotEquals(Sum.KBKSum(values), Arrays.stream(values).sum(), 0.0);
        }
    }
}