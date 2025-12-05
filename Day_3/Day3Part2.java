import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day3Part2 {

    public static void main(String[] args) {
        try {
            ArrayList<String> banks = readInput("input.txt");
            long totalJoltage = calculateTotalJoltage(banks);
            System.out.println("Part 2 answer: " + String.valueOf(totalJoltage));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static ArrayList<String> readInput(String filename) throws IOException {
        ArrayList<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                lines.add(line.trim());
            }
        }
        reader.close();
        return lines;
    }

    private static long calculateTotalJoltage(ArrayList<String> banks) {
        long total = 0;

        for (String bank : banks) {
            long maxJoltage = findMaxJoltage(bank, 12);
            total += maxJoltage;
        }

        return total;
    }

    private static long findMaxJoltage(String bank, int count) {
        StringBuilder result = new StringBuilder();
        int startPos = 0;

        // Greedy approach: for each position in the result,
        // find the maximum digit that leaves enough digits for the remaining positions
        for (int i = 0; i < count; i++) {
            int remainingNeeded = count - i - 1;
            int maxDigit = -1;
            int maxPos = -1;

            // Search for the best digit to pick
            // We can search from startPos to (bank.length() - remainingNeeded - 1)
            int searchEnd = bank.length() - remainingNeeded;
            for (int j = startPos; j < searchEnd; j++) {
                int digit = bank.charAt(j) - '0';
                if (digit > maxDigit) {
                    maxDigit = digit;
                    maxPos = j;

                    // If we found a 9, we can't do better
                    if (maxDigit == 9) {
                        break;
                    }
                }
            }

            result.append(maxDigit);
            startPos = maxPos + 1;
        }

        return Long.parseLong(result.toString());
    }
}
