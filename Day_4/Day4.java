import java.io.*;
import java.util.*;

public class Day4 {
    
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
        int accessibleCount = 0;
        
        // Direction offsets for 8 adjacent positions
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                // Only check paper rolls (@)
                if (grid[row][col] != '@') {
                    continue;
                }
                
                // Count adjacent paper rolls
                int adjacentRolls = 0;
                for (int d = 0; d < 8; d++) {
                    int newRow = row + dx[d];
                    int newCol = col + dy[d];
                    
                    if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                        if (grid[newRow][newCol] == '@') {
                            adjacentRolls++;
                        }
                    }
                }
                
                // Accessible if fewer than 4 adjacent rolls
                if (adjacentRolls < 4) {
                    accessibleCount++;
                }
            }
        }
        
        System.out.println("Accessible paper rolls: " + accessibleCount);
    }
}
