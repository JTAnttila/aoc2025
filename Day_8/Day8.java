import java.nio.file.*;
import java.util.*;

public class Day8 {
    static int[] parent, rank, size;

    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Path.of("input.txt"));
        int n = lines.size() - 1; // last line might be empty

        // Parse coordinates
        int[][] coords = new int[n][3];
        for (int i = 0; i < n; i++) {
            String[] parts = lines.get(i).split(",");
            coords[i][0] = Integer.parseInt(parts[0]);
            coords[i][1] = Integer.parseInt(parts[1]);
            coords[i][2] = Integer.parseInt(parts[2]);
        }

        // Calculate all pairwise distances
        List<long[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                long dx = coords[i][0] - coords[j][0];
                long dy = coords[i][1] - coords[j][1];
                long dz = coords[i][2] - coords[j][2];
                long distSq = dx * dx + dy * dy + dz * dz;
                edges.add(new long[] { distSq, i, j });
            }
        }

        // Sort by distance
        edges.sort((a, b) -> Long.compare(a[0], b[0]));

        // Initialize Union-Find
        parent = new int[n];
        rank = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }

        // Part 1: Connect 1000 closest pairs
        int connections = 0;
        for (long[] edge : edges) {
            if (connections >= 1000)
                break;
            int a = (int) edge[1];
            int b = (int) edge[2];
            union(a, b);
            connections++;
        }

        // Find sizes of all circuits
        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = find(i);
            circuitSizes.put(root, size[root]);
        }

        // Get top 3 sizes
        List<Integer> sizes = new ArrayList<>(circuitSizes.values());
        sizes.sort(Collections.reverseOrder());

        long result = (long) sizes.get(0) * sizes.get(1) * sizes.get(2);
        System.out.println("Part 1: " + result);

        // Part 2: Reset and continue until all are connected
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            size[i] = 1;
        }

        int numCircuits = n;
        int lastA = -1, lastB = -1;

        for (long[] edge : edges) {
            int a = (int) edge[1];
            int b = (int) edge[2];
            if (find(a) != find(b)) {
                union(a, b);
                numCircuits--;
                lastA = a;
                lastB = b;
                if (numCircuits == 1)
                    break;
            }
        }

        long part2 = (long) coords[lastA][0] * coords[lastB][0];
        System.out.println("Part 2: " + part2);
    }

    static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    static void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py)
            return;

        if (rank[px] < rank[py]) {
            parent[px] = py;
            size[py] += size[px];
        } else if (rank[px] > rank[py]) {
            parent[py] = px;
            size[px] += size[py];
        } else {
            parent[py] = px;
            size[px] += size[py];
            rank[px]++;
        }
    }
}
