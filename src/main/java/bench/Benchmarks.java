package bench;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

import static jamjam.Mean.mean;
import static jamjam.Sum.sum;
import static jamjam.Variance.biasedVariance;
import static jamjam.Variance.unbiasedVariance;

public class Benchmarks {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Fork(value = 3, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Group("parallel")
    @GroupThreads()
    public void sumBench(ExecutionPlan plan) {
        for (int i = plan.size; i > 0; i--) {
            sum(plan.testArray);
        }
    }

    @Fork(value = 3, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Group("parallel")
    @GroupThreads()
    public void meanBench(ExecutionPlan plan) {
        for (int i = plan.size; i > 0; i--) {
            mean(plan.testArray);
        }
    }

    @Fork(value = 3, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Group("parallel")
    @GroupThreads()
    public void ubvarBench(ExecutionPlan plan) {
        for (int i = plan.size; i > 0; i--) {
            unbiasedVariance(plan.testArray);
        }
    }

    @Fork(value = 3, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @Group("parallel")
    @GroupThreads()
    public void varBench(ExecutionPlan plan) {
        for (int i = plan.size; i > 0; i--) {
            biasedVariance(plan.testArray);
        }
    }
}
