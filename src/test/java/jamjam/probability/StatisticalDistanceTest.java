package jamjam.probability;

import lombok.val;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatisticalDistanceTest {

    @Test
    void kullbackLeiblerDivergence() {
        val minValue = Math.ulp(1.0);
        assertAll("Method throws a NullPointerException when any parameter is null.",
            () -> assertThrows(NullPointerException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(null, new double[]{0, 1}),
                "Null input test fails."),
            () -> assertThrows(NullPointerException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{0, 1}, null),
                "Null input test fails.")
        );

        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{0, 1}, new double[]{0, 0, 1}),
            "Check for uneven input shapes fails.");

        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{1}, new double[]{1}),
            "Distributions with one outcome only are not filtered out.");

        assertAll("All distributions must add up to 1 exactly.",
            () -> assertThrows(IllegalArgumentException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{1, minValue}, new double[]{1, 0}),
                "Distributions with accumulated errors shall not pass, but they do."),
            () -> assertThrows(IllegalArgumentException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{1, 0}, new double[]{1, minValue}),
                "Distributions with accumulated errors shall not pass, but they do."));

        assertAll("Method throws a NullPointerException when any of the passed values is out of [0, 1]",
            () -> assertThrows(IllegalArgumentException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{1, 0},
                    new double[]{0 - minValue, 1 + minValue}),
                "Check of the actual distribution fails to fail."),
            () -> assertThrows(IllegalArgumentException.class,
                () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{0 - minValue,
                    1 + minValue}, new double[]{1, 0}),
                "Check of the reference distribution fails to fail."));

        assertTrue(Double.isFinite(StatisticalDistance.KullbackLeiblerDivergence(new double[]{0, 1},
            new double[]{1, 0})), "Trimming should result in limited values, not Infinity");

        assertEquals(Double.MIN_NORMAL,
            StatisticalDistance.KullbackLeiblerDivergence(new double[]{0.5, 0.5}, new double[]{0.5, 0.5}),
            "Equal distributions must result in 0 (actual) or Double.MIN_NORMAL (trimmed), but they don't");
    }

    @Test
    void kullbackLeiblerDivergence2() {
        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{1.5, -0.5}, new double[]{-1.5, 2.5}));
        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{-1.5, 0.5}, new double[]{-1.5, 2.5}));
        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{0.5, 0.5}, new double[]{-1.5, 2.5}));
        assertThrows(IllegalArgumentException.class,
            () -> StatisticalDistance.KullbackLeiblerDivergence(new double[]{0.5, 0.5}, new double[]{2.5, -1.5}));
    }
}
