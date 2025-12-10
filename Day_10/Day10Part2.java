import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day10Part2 {

    public static void main(String[] args) throws IOException {
        String filename = args.length > 0 ? args[0] : "input.txt";
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        }

        long totalPresses = 0;
        for (String line : lines) {
            int presses = solveMachine(line);
            totalPresses += presses;
        }

        System.out.println("Total minimum button presses: " + totalPresses);
    }

    static int solveMachine(String line) {
        // Parse buttons (...)
        Pattern buttonPattern = Pattern.compile("\\(([0-9,]+)\\)");
        Matcher buttonMatcher = buttonPattern.matcher(line);

        List<int[]> buttons = new ArrayList<>();
        int maxIdx = 0;
        while (buttonMatcher.find()) {
            String buttonStr = buttonMatcher.group(1);
            String[] parts = buttonStr.split(",");
            int[] indices = new int[parts.length];
            for (int i = 0; i < parts.length; i++) {
                indices[i] = Integer.parseInt(parts[i].trim());
                maxIdx = Math.max(maxIdx, indices[i]);
            }
            buttons.add(indices);
        }

        // Parse joltage requirements {...}
        Pattern joltagePattern = Pattern.compile("\\{([0-9,]+)\\}");
        Matcher joltageMatcher = joltagePattern.matcher(line);
        joltageMatcher.find();
        String joltageStr = joltageMatcher.group(1);
        String[] joltageParts = joltageStr.split(",");
        int[] targets = new int[joltageParts.length];
        for (int i = 0; i < joltageParts.length; i++) {
            targets[i] = Integer.parseInt(joltageParts[i].trim());
        }

        int numCounters = targets.length;
        int numButtons = buttons.size();

        double[][] aug = new double[numCounters][numButtons + 1];
        for (int j = 0; j < numButtons; j++) {
            for (int idx : buttons.get(j)) {
                if (idx < numCounters) {
                    aug[idx][j] = 1;
                }
            }
        }
        for (int i = 0; i < numCounters; i++) {
            aug[i][numButtons] = targets[i];
        }

        // Gaussian elimination with partial pivoting
        int[] pivotCol = new int[numCounters]; // which column is the pivot for each row
        Arrays.fill(pivotCol, -1);
        int row = 0;
        for (int col = 0; col < numButtons && row < numCounters; col++) {
            // Find pivot
            int maxRow = row;
            for (int i = row + 1; i < numCounters; i++) {
                if (Math.abs(aug[i][col]) > Math.abs(aug[maxRow][col])) {
                    maxRow = i;
                }
            }
            if (Math.abs(aug[maxRow][col]) < 1e-9)
                continue; // No pivot in this column

            // Swap rows
            double[] temp = aug[row];
            aug[row] = aug[maxRow];
            aug[maxRow] = temp;

            pivotCol[row] = col;

            // Eliminate below
            for (int i = row + 1; i < numCounters; i++) {
                double factor = aug[i][col] / aug[row][col];
                for (int j = col; j <= numButtons; j++) {
                    aug[i][j] -= factor * aug[row][j];
                }
            }
            row++;
        }

        int rank = row;

        // Back substitution to get row echelon form
        for (int r = rank - 1; r >= 0; r--) {
            int pc = pivotCol[r];
            // Normalize pivot row
            double pivot = aug[r][pc];
            for (int j = pc; j <= numButtons; j++) {
                aug[r][j] /= pivot;
            }
            // Eliminate above
            for (int i = 0; i < r; i++) {
                double factor = aug[i][pc];
                for (int j = pc; j <= numButtons; j++) {
                    aug[i][j] -= factor * aug[r][j];
                }
            }
        }

        // Identify free variables (columns not in pivotCol)
        boolean[] isPivot = new boolean[numButtons];
        for (int r = 0; r < rank; r++) {
            if (pivotCol[r] >= 0)
                isPivot[pivotCol[r]] = true;
        }

        List<Integer> freeVars = new ArrayList<>();
        for (int j = 0; j < numButtons; j++) {
            if (!isPivot[j])
                freeVars.add(j);
        }

        int maxTarget = 0;
        for (int t : targets)
            maxTarget = Math.max(maxTarget, t);

        int numFree = freeVars.size();
        int[] freeUpper = new int[numFree];
        Arrays.fill(freeUpper, maxTarget);

        // BFS/iterative search over free variable space
        int minPresses = Integer.MAX_VALUE;
        int[] freeVals = new int[numFree];

        // Iterate through all combinations of free variables
        while (true) {
            // Compute pivot variables
            double[] x = new double[numButtons];
            for (int i = 0; i < numFree; i++) {
                x[freeVars.get(i)] = freeVals[i];
            }

            boolean valid = true;
            for (int r = rank - 1; r >= 0 && valid; r--) {
                int pc = pivotCol[r];
                double val = aug[r][numButtons];
                for (int j = pc + 1; j < numButtons; j++) {
                    val -= aug[r][j] * x[j];
                }
                x[pc] = val;
                // Check if non-negative integer
                if (val < -1e-9 || Math.abs(val - Math.round(val)) > 1e-6) {
                    valid = false;
                }
            }

            if (valid) {
                int sum = 0;
                for (int j = 0; j < numButtons; j++) {
                    int v = (int) Math.round(x[j]);
                    if (v < 0) {
                        valid = false;
                        break;
                    }
                    sum += v;
                }
                if (valid && sum < minPresses) {
                    minPresses = sum;
                }
            }

            // Increment free variables
            int carry = 1;
            for (int i = 0; i < numFree && carry > 0; i++) {
                freeVals[i] += carry;
                if (freeVals[i] > freeUpper[i]) {
                    freeVals[i] = 0;
                } else {
                    carry = 0;
                }
            }
            if (carry > 0)
                break; // All combinations exhausted
        }

        return minPresses;
    }
}
