import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day6 {

    public static void main(String[] args) {
        try {
            List<String> lines = readInput("input.txt");
            long grandTotal = solveProblems(lines);
            System.out.println("Grand total: " + grandTotal);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private static List<String> readInput(String filename) throws IOException {
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        return lines;
    }

    private static long solveProblems(List<String> lines) {
        // Find the operator line (contains * and +)
        int operatorLineIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("*") || line.contains("+")) {
                operatorLineIndex = i;
                break;
            }
        }

        if (operatorLineIndex == -1) {
            return 0;
        }

        String operatorLine = lines.get(operatorLineIndex);
        List<String> numberLines = lines.subList(0, operatorLineIndex);

        // Ensure all lines have the same length by padding
        int maxLength = operatorLine.length();
        for (String line : numberLines) {
            maxLength = Math.max(maxLength, line.length());
        }

        // Pad all lines to the same length
        List<String> paddedNumberLines = new ArrayList<>();
        for (String line : numberLines) {
            paddedNumberLines.add(padRight(line, maxLength));
        }
        String paddedOperatorLine = padRight(operatorLine, maxLength);

        long grandTotal = 0;
        int col = 0;

        while (col < maxLength) {
            // Skip separator columns (all spaces)
            if (isColumnAllSpaces(paddedNumberLines, paddedOperatorLine, col)) {
                col++;
                continue;
            }

            // Find the end of this problem (next all-space column or end of line)
            int problemStart = col;
            while (col < maxLength && !isColumnAllSpaces(paddedNumberLines, paddedOperatorLine, col)) {
                col++;
            }
            int problemEnd = col;

            // Extract numbers and operator for this problem
            List<Long> numbers = new ArrayList<>();
            char operator = ' ';

            // Get the operator from this problem's range
            for (int c = problemStart; c < problemEnd; c++) {
                char ch = paddedOperatorLine.charAt(c);
                if (ch == '*' || ch == '+') {
                    operator = ch;
                    break;
                }
            }

            // Extract numbers from each row within this problem's columns
            for (String line : paddedNumberLines) {
                String segment = line.substring(problemStart, Math.min(problemEnd, line.length()));
                // Parse all numbers from this segment
                String[] tokens = segment.trim().split("\\s+");
                for (String token : tokens) {
                    if (!token.isEmpty()) {
                        try {
                            numbers.add(Long.parseLong(token));
                        } catch (NumberFormatException e) {
                            // Skip non-numeric tokens
                        }
                    }
                }
            }

            // Calculate result for this problem
            if (!numbers.isEmpty() && operator != ' ') {
                long result;
                if (operator == '*') {
                    result = 1;
                    for (long num : numbers) {
                        result *= num;
                    }
                } else {
                    result = 0;
                    for (long num : numbers) {
                        result += num;
                    }
                }
                grandTotal += result;
            }
        }

        return grandTotal;
    }

    private static boolean isColumnAllSpaces(List<String> numberLines, String operatorLine, int col) {
        // Check if this column is all spaces in number lines and operator line
        for (String line : numberLines) {
            if (col < line.length() && line.charAt(col) != ' ') {
                return false;
            }
        }
        if (col < operatorLine.length() && operatorLine.charAt(col) != ' ') {
            return false;
        }
        return true;
    }

    private static String padRight(String s, int length) {
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
