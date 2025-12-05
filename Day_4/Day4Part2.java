import java.io.*;
import java.util.*;

public class Day4Part2 {

    private static final int[] DX = { -1, -1, -1, 0, 0, 1, 1, 1 };
    private static final int[] DY = { -1, 0, 1, -1, 1, -1, 0, 1 };

    public static void main(String[] args) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        char[][] grid = new char[lines.size()][];
        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }

        int rows = grid.length;
        int cols = grid[0].length;
        int totalRemoved = 0;

        // Keep removing until no more accessible rolls
        while (true) {
            List<int[]> toRemove = findAccessibleRolls(grid, rows, cols);

            if (toRemove.isEmpty()) {
                break;
            }

            // Remove all accessible rolls
            for (int[] pos : toRemove) {
                grid[pos[0]][pos[1]] = '.';
            }

            totalRemoved += toRemove.size();
        }

        System.out.println("Total rolls removed: " + totalRemoved);
    }

    private static List<int[]> findAccessibleRolls(char[][] grid, int rows, int cols) {
        List<int[]> accessible = new ArrayList<>();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] != '@') {
                    continue;
                }

                int adjacentRolls = 0;
                for (int d = 0; d < 8; d++) {
                    int newRow = row + DX[d];
                    int newCol = col + DY[d];

                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                        if (grid[newRow][newCol] == '@') {
                            adjacentRolls++;
                        }
                    }
                }

                if (adjacentRolls < 4) {
                    accessible.add(new int[] { row, col });
                }
            }
        }

        return accessible;
    }
}
