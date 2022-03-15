package jamjam;

import org.jetbrains.annotations.NotNull;

public final class Mean {
    public static double mean(double @NotNull [] input){
        return Sum.KBKSum(input)/input.length;
    }
}
