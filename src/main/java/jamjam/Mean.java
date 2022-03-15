package jamjam;

import lombok.NonNull;

public final class Mean {
    public static double mean(double @NonNull [] input){
        return Sum.KBKSum(input)/input.length;
    }
}
