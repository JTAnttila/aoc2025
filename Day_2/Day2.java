import java.nio.file.*;
import java.util.*;

public class Day2 {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Paths.get("input.txt")).trim();
        String[] ranges = input.split(",");

        long part1 = 0, part2 = 0;

        for (String range : ranges) {
            int dash = range.indexOf('-');
            long lo = Long.parseLong(range.substring(0, dash));
            long hi = Long.parseLong(range.substring(dash + 1));

            Set<Long> found = new HashSet<>();
            int maxDigits = String.valueOf(hi).length();

            // for each pattern length
            for (int patLen = 1; patLen <= maxDigits / 2; patLen++) {
                long patStart = (patLen == 1) ? 1 : (long) Math.pow(10, patLen - 1);
                long patEnd = (long) Math.pow(10, patLen) - 1;

                for (long pat = patStart; pat <= patEnd; pat++) {
                    String s = String.valueOf(pat);
                    String repeated = s + s;

                    while (repeated.length() <= maxDigits) {
                        long num = Long.parseLong(repeated);
                        if (num > hi)
                            break;
                        if (num >= lo && found.add(num)) {
                            part2 += num;
                        }
                        repeated += s;
                    }

                    // Part 1: just doubled (exactly 2 repetitions)
                    String doubled = s + s;
                    long dnum = Long.parseLong(doubled);
                    if (dnum >= lo && dnum <= hi) {
                        part1 += dnum;
                    }
                }
            }
        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
