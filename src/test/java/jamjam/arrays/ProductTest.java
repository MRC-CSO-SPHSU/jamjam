package jamjam.arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void product() {
        assertThrows(NullPointerException.class, () -> Product.product(null, new double[]{0.}));
        assertThrows(NullPointerException.class, () -> Product.product(new double[]{0.}, (double[]) null));
        assertArrayEquals(new double[]{4, 4}, Product.product(new double[]{2, 2}, new double[]{2, 2}));
    }

    @Test
    void productNoCheck() {
        assertThrows(NullPointerException.class, () -> Product.product(null, new double[]{0.}));
        assertThrows(NullPointerException.class, () -> Product.product(new double[]{0.}, (double[]) null));
        assertArrayEquals(new double[]{4, 4}, Product.product(new double[]{2, 2}, new double[]{2, 2}));

        var x = new double[50_000_000];
        var y = new double[50_000_000];

        Arrays.fill(x, 1);
        assertArrayEquals(x, Product.product(x, x));

        Arrays.fill(x, 2);
        Arrays.fill(y, 4);
        assertArrayEquals(y, Product.product(x, x));

        x = new double[500];
        y = new double[500];
        Arrays.fill(x, 1);
        assertArrayEquals(x, Product.product(x, x));

        Arrays.fill(x, 2);
        Arrays.fill(y, 4);
        assertArrayEquals(y, Product.product(x, x));

        x = new double[512];
        y = new double[512];
        Arrays.fill(x, 1);
        assertArrayEquals(x, Product.product(x, x));

        Arrays.fill(x, 2);
        Arrays.fill(y, 4);
        assertArrayEquals(y, Product.product(x, x));
    }
}
