package jamjam.aux;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test void shuffleDoubleArray() {
    }

    @Test void returnRelativeAccuracyStatus() {
        //GSL_rel(Double.NaN, 1, 1); // expected to fail
        //GSL_rel(1, Double.NaN, 1); // expected to fail
        //testRelative(Double.NaN, Double.NaN, 1);
        //testRelative(1,1,0.5);

        //GSL_rel(Double.POSITIVE_INFINITY, 1, 1); // expected to fail
        //GSL_rel(1, Double.POSITIVE_INFINITY, 1); // expected to fail
        //GSL_rel(Double.NEGATIVE_INFINITY, 1, 1);  // expected to fail
        //GSL_rel(1, Double.NEGATIVE_INFINITY, 1); // expected to fail
        //testRelative(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 1);
        //testRelative(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 1);
        //GSL_rel(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, 1); // expected to fail
        //GSL_rel(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 1); // expected to fail

        //GSL_rel(1, -Double.MIN_NORMAL/2, 1); // expected to fail
    }

    @Test void readTestingValues() {
    }
}