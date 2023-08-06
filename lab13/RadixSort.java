import java.util.Arrays;
import java.util.Random;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     *
     */
    public static String[] sort(String[] asciis) {
        if (asciis == null || asciis.length == 0) {
            return asciis;
        }
        String[] sorted = new String[asciis.length];
        int maxLength = asciis[0].length();
        for (String s: asciis) {
            maxLength = s.length() < maxLength ? maxLength : s.length();
        }
        // should not padding on the original string, because of ambiguity
//        for (int i = 0; i < asciis.length; i++) {
//            int pad = maxLength - asciis[i].length();
//            StringBuilder sb = new StringBuilder(asciis[i]);
//            for (int j = 0; j < pad; j++) {
//                sb.append('\0');
//            }
//            sorted[i] = sb.toString();
//        }
        for (int i = 0; i < asciis.length; i++) {
            sorted[i] = asciis[i];
        }
        for (int i = maxLength - 1; i >= 0; i--) {
            sortHelperLSD(sorted, i);
        }
//        for (int i = 0; i < sorted.length; i++) {
//            sorted[i] = sorted[i].trim();
//        }
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int[] counts = new int[257];
        int[] starts = new int[257];
        int pos = 0;

        for (String s: asciis) {
            counts[charAt(s, index)]++;
        }
        for (int i = 0; i < counts.length; i++) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            int c = charAt(asciis[i], index);
            sorted[starts[c]] = asciis[i];
            starts[c]++;
        }
        for (int i = 0; i < asciis.length; i++) {
            asciis[i] = sorted[i];
        }
    }

    private static int charAt(String s, int index) {
        // padding with zero
        if (index >= s.length()) {
            return 0;
        } else {
            return (int) s.charAt(index) + 1;
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }

    public static void main(String[] args) {
        Random r = new Random();
        while (true) {
            String[] t = randomStrings(r, r.nextInt(20));
            String[] t1 = sort(t);
            Arrays.sort(t);
            if (!Arrays.equals(t, t1)) {
                System.out.println("Expected:");
                System.out.println(Arrays.toString(t));
                System.out.println("Actual:");
                System.out.println(Arrays.toString(t1));
                break;
            }
        }
    }

    private static String[] randomStrings(Random r, int length) {
        String[] res = new String[length];
        for (int i = 0; i < length; i++) {
            res[i] = randomString(r, r.nextInt(50));
        }
        return res;
    }

    private static String randomString(Random r, int length) {
        if (length == 0) {
            return randomString(r, r.nextInt(50));
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) r.nextInt(256));
        }
        return sb.toString();
    }
}
