import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day5 {

    public static void main(String[] args) {
        try {
            ArrayList<long[]> freshRanges = new ArrayList<>();
            ArrayList<Long> ingredientIds = new ArrayList<>();
            
            parseInput("input.txt", freshRanges, ingredientIds);
            
            int freshCount = countFreshIngredients(freshRanges, ingredientIds);
            System.out.println("Fresh ingredient count: " + freshCount);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static void parseInput(String filename, ArrayList<long[]> ranges, 
                                   ArrayList<Long> ids) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        boolean parsingRanges = true;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            
            if (line.isEmpty()) {
                parsingRanges = false;
                continue;
            }

            if (parsingRanges) {
                // Parse range like "3-5"
                String[] parts = line.split("-");
                long start = Long.parseLong(parts[0]);
                long end = Long.parseLong(parts[1]);
                ranges.add(new long[]{start, end});
            } else {
                // Parse ingredient ID
                ids.add(Long.parseLong(line));
            }
        }
        reader.close();
    }

    private static int countFreshIngredients(ArrayList<long[]> ranges, 
                                              ArrayList<Long> ingredientIds) {
        int count = 0;

        for (long id : ingredientIds) {
            if (isFresh(id, ranges)) {
                count++;
            }
        }

        return count;
    }

    private static boolean isFresh(long id, ArrayList<long[]> ranges) {
        for (long[] range : ranges) {
            if (id >= range[0] && id <= range[1]) {
                return true;
            }
        }
        return false;
    }
}
