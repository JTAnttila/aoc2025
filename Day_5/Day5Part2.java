import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Day5Part2 {

    public static void main(String[] args) {
        try {
            ArrayList<long[]> freshRanges = parseRanges("input.txt");

            // Merge overlapping ranges and count total fresh IDs
            ArrayList<long[]> mergedRanges = mergeRanges(freshRanges);
            long totalFreshIds = countTotalIds(mergedRanges);

            System.out.println("Total fresh ingredient IDs: " + totalFreshIds);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static ArrayList<long[]> parseRanges(String filename) throws IOException {
        ArrayList<long[]> ranges = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Stop at empty line (we only need the ranges section)
            if (line.isEmpty()) {
                break;
            }

            // Parse range like "3-5"
            String[] parts = line.split("-");
            long start = Long.parseLong(parts[0]);
            long end = Long.parseLong(parts[1]);
            ranges.add(new long[] { start, end });
        }
        reader.close();
        return ranges;
    }

    private static ArrayList<long[]> mergeRanges(ArrayList<long[]> ranges) {
        if (ranges.isEmpty()) {
            return new ArrayList<>();
        }

        // Sort ranges by start value
        Collections.sort(ranges, Comparator.comparingLong(r -> r[0]));

        ArrayList<long[]> merged = new ArrayList<>();
        long[] current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            long[] next = ranges.get(i);

            // Check if ranges overlap or are adjacent
            if (next[0] <= current[1] + 1) {
                // Merge: extend the current range if needed
                current[1] = Math.max(current[1], next[1]);
            } else {
                // No overlap, add current to result and start new range
                merged.add(current);
                current = next;
            }
        }

        // Add the last range
        merged.add(current);

        return merged;
    }

    private static long countTotalIds(ArrayList<long[]> ranges) {
        long total = 0;

        for (long[] range : ranges) {
            // +1 because ranges are inclusive
            total += range[1] - range[0] + 1;
        }

        return total;
    }
}
