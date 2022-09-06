package bench;

import org.openjdk.jmh.annotations.*;

import java.util.Random;

@State(Scope.Benchmark)
public class ExecutionPlan {

    //@Param({"100", "200", "300", "500", "1000", "10000"})
    @Param({"1000", "10000"})
    public int size;

    public Random rng;
    public double[] testArray;

    @Setup(Level.Invocation)
    public void setUp() {
        rng = new Random();
        testArray = rng.doubles(size).toArray();

    }
}
