import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Day3 {

    public static void main(String[] args) {
        try {
            ArrayList<String> banks = readInput("input.txt");
            int totalJoltage = calculateTotalJoltage(banks);
            System.out.println("Total output joltage: " + totalJoltage);
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

    private static int calculateTotalJoltage(ArrayList<String> banks) {
        int total = 0;

        for (String bank : banks) {
            int maxJoltage = findMaxJoltage(bank);
            total += maxJoltage;
        }

        return total;
    }

    private static int findMaxJoltage(String bank) {
        int max = 0;

        // Check every possible pair of batteries (i before j)
        for (int i = 0; i < bank.length() - 1; i++) {
            for (int j = i + 1; j < bank.length(); j++) {
                char first = bank.charAt(i);
                char second = bank.charAt(j);

                // Convert the two digits to a number
                int joltage = (first - '0') * 10 + (second - '0');

                if (joltage > max) {
                    max = joltage;
                }
            }
        }

        return max;
    }
}
