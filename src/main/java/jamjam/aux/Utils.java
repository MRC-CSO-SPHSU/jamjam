package jamjam.aux;

import lombok.NonNull;

import java.util.Random;

public class Utils {
    public static void shuffleDoubleArray(double @NonNull [] array, Random generator)
    {
        int index;
        double temp;
        for (var i = array.length - 1; i > 0; i--)
        {
            index = generator.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }
}
