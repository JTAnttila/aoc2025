import java.io.*;
import java.util.*;

public class Day9 {
    static int[][] tiles;

    public static void main(String[] args) throws IOException {
        List<int[]> tileList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                String[] parts = line.split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                tileList.add(new int[] { x, y });
            }
        }

        tiles = tileList.toArray(new int[0][]);

        // Warmup runs for JIT
        for (int i = 0; i < 100; i++)
            solve();

        // Timed run
        long startTime = System.nanoTime();
        long[] result = solve();
        long endTime = System.nanoTime();

        System.out.println("Part 1: " + result[0]);
        System.out.println("Part 2: " + result[1]);
        System.out.printf("Time: %.3f ms%n", (endTime - startTime) / 1_000_000.0);
    }

    static long[] solve() {
        int n = tiles.length;

        // Part 1: Find largest rectangle using any two red tiles
        long maxArea1 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long width = Math.abs(tiles[j][0] - tiles[i][0]) + 1;
                long height = Math.abs(tiles[j][1] - tiles[i][1]) + 1;
                long area = width * height;
                if (area > maxArea1)
                    maxArea1 = area;
            }
        }

        // Part 2: Find largest rectangle where all tiles are red or green
        // Build coordinate compression
        TreeSet<Integer> xSet = new TreeSet<>();
        TreeSet<Integer> ySet = new TreeSet<>();
        for (int[] t : tiles) {
            xSet.add(t[0]);
            ySet.add(t[1]);
        }
        int[] xs = xSet.stream().mapToInt(Integer::intValue).toArray();
        int[] ys = ySet.stream().mapToInt(Integer::intValue).toArray();

        // Map coordinate to index
        Map<Integer, Integer> xIdx = new HashMap<>();
        Map<Integer, Integer> yIdx = new HashMap<>();
        for (int i = 0; i < xs.length; i++)
            xIdx.put(xs[i], i);
        for (int i = 0; i < ys.length; i++)
            yIdx.put(ys[i], i);

        // Build polygon edges (horizontal and vertical segments)
        // Each edge is between consecutive tiles (wrapping around)
        Set<Long> vEdges = new HashSet<>(); // vertical edges: x constant

        for (int i = 0; i < n; i++) {
            int[] a = tiles[i];
            int[] b = tiles[(i + 1) % n];

            if (a[0] == b[0]) {
                // Vertical edge
                int minY = Math.min(yIdx.get(a[1]), yIdx.get(b[1]));
                int maxY = Math.max(yIdx.get(a[1]), yIdx.get(b[1]));
                int x = xIdx.get(a[0]);
                for (int y = minY; y < maxY; y++) {
                    vEdges.add(((long) x << 20) | y);
                }
            }
        }

        // Mark cells as inside/outside using scanline (ray casting from left)
        int cols = xs.length;
        int rows = ys.length;
        boolean[][] inside = new boolean[cols - 1][rows - 1];

        for (int cy = 0; cy < rows - 1; cy++) {
            boolean in = false;
            for (int cx = 0; cx < cols - 1; cx++) {
                // Check if we cross a vertical edge at x=cx going from left
                if (vEdges.contains(((long) cx << 20) | cy)) {
                    in = !in;
                }
                inside[cx][cy] = in;
            }
        }

        // For each pair of red tiles, check if rectangle is valid
        long maxArea2 = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int x1 = xIdx.get(tiles[i][0]);
                int y1 = yIdx.get(tiles[i][1]);
                int x2 = xIdx.get(tiles[j][0]);
                int y2 = yIdx.get(tiles[j][1]);

                int minX = Math.min(x1, x2);
                int maxX = Math.max(x1, x2);
                int minY = Math.min(y1, y2);
                int maxY = Math.max(y1, y2);

                // Check if all cells in rectangle are inside or on boundary
                boolean valid = true;
                outer: for (int cx = minX; cx < maxX && valid; cx++) {
                    for (int cy = minY; cy < maxY && valid; cy++) {
                        if (!inside[cx][cy]) {
                            valid = false;
                        }
                    }
                }

                if (valid) {
                    long width = Math.abs(tiles[j][0] - tiles[i][0]) + 1;
                    long height = Math.abs(tiles[j][1] - tiles[i][1]) + 1;
                    long area = width * height;
                    if (area > maxArea2)
                        maxArea2 = area;
                }
            }
        }

        return new long[] { maxArea1, maxArea2 };
    }
}
