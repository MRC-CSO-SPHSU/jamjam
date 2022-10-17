package jamjam.arrays;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static jamjam.arrays.Product.productInPlace;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductTest {

    @Test
    void testProduct() {
        assertThrows(NullPointerException.class, () -> Product.product(null, new double[]{0.}));
        assertThrows(NullPointerException.class, () -> Product.product(new double[]{0.}, (double[]) null));
        assertArrayEquals(new double[]{4, 4}, Product.product(new double[]{2, 2}, new double[]{2, 2}));
    }

    @Test
    void testProduct2() {
        assertThrows(NullPointerException.class, () -> Product.product(null, new long[]{0}));
        assertThrows(NullPointerException.class, () -> Product.product(new double[]{0.}, (long[]) null));
        assertArrayEquals(new double[]{4, 4}, Product.product(new double[]{2, 2}, new long[]{2, 2}));
    }

    @Test
    void testProduct3() {
        assertThrows(NullPointerException.class, () -> Product.product(null, new int[]{0}));
        assertThrows(NullPointerException.class, () -> Product.product(new double[]{0.}, (int[]) null));
        assertArrayEquals(new double[]{4, 4}, Product.product(new double[]{2, 2}, new int[]{2, 2}));
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

    @Test
    void testProductInPlace() {
        var base = new double[]{2, 2, 2};
        var factor = new long[]{1, 1, 1};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{2, 2, 2}, base);

        factor = new long[]{0, 0, 0};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{0, 0, 0}, base);

        base = new double[]{2, 2, 2};
        factor = new long[]{2, 2, 2};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{4, 4, 4}, base);

        double[] finalBase = base;
        assertThrows(NullPointerException.class, () -> productInPlace(finalBase, (long[]) null));
        assertThrows(NullPointerException.class, () -> productInPlace(null, (long[]) null));
    }

    @Test
    void testProductInPlace2() {
        var base = new double[]{2, 2, 2};
        var factor = new int[]{1, 1, 1};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{2, 2, 2}, base);

        factor = new int[]{0, 0, 0};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{0, 0, 0}, base);

        base = new double[]{2, 2, 2};
        factor = new int[]{2, 2, 2};
        productInPlace(base, factor);

        assertArrayEquals(new double[]{4, 4, 4}, base);

        double[] finalBase = base;
        assertThrows(NullPointerException.class, () -> productInPlace(finalBase, (int[]) null));
        assertThrows(NullPointerException.class, () -> productInPlace(null, (int[]) null));
    }

    @Test
    void testProductInPlace3() {
        var base = new double[]{2, 2, 2};
        assertThrows(NullPointerException.class, () -> productInPlace(base, (double[]) null));
        assertThrows(NullPointerException.class, () -> productInPlace(null, (double[]) null));
    }
}
