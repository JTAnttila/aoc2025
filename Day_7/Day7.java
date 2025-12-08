import java.nio.file.*;

public class Day7 {
    static byte[] data;
    static int cols, rows, lineLen, startCol;

    public static void main(String[] args) throws Exception {
        data = Files.readAllBytes(Path.of("input.txt"));

        cols = 0;
        while (data[cols] != '\r' && data[cols] != '\n')
            cols++;
        lineLen = cols + (data[cols] == '\r' ? 2 : 1);
        rows = (data.length + lineLen - 1) / lineLen;

        for (int c = 0; c < cols; c++) {
            if (data[c] == 'S') {
                startCol = c;
                break;
            }
        }

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
        // Part 1
        boolean[] beams = new boolean[cols];
        boolean[] nextBeams = new boolean[cols];
        beams[startCol] = true;
        int totalSplits = 0;

        for (int row = 1; row < rows; row++) {
            int off = row * lineLen;
            for (int col = 0; col < cols; col++) {
                if (!beams[col])
                    continue;
                beams[col] = false;
                if (data[off + col] == '^') {
                    totalSplits++;
                    if (col > 0)
                        nextBeams[col - 1] = true;
                    if (col < cols - 1)
                        nextBeams[col + 1] = true;
                } else {
                    nextBeams[col] = true;
                }
            }
            boolean[] tmp = beams;
            beams = nextBeams;
            nextBeams = tmp;
        }

        // Part 2
        long[] timelines = new long[cols];
        long[] nextTimelines = new long[cols];
        timelines[startCol] = 1L;

        for (int row = 1; row < rows; row++) {
            int off = row * lineLen;
            for (int col = 0; col < cols; col++) {
                long count = timelines[col];
                if (count == 0)
                    continue;
                timelines[col] = 0;
                if (data[off + col] == '^') {
                    if (col > 0)
                        nextTimelines[col - 1] += count;
                    if (col < cols - 1)
                        nextTimelines[col + 1] += count;
                } else {
                    nextTimelines[col] += count;
                }
            }
            long[] tmp = timelines;
            timelines = nextTimelines;
            nextTimelines = tmp;
        }

        long total = 0;
        for (int i = 0; i < cols; i++)
            total += timelines[i];

        return new long[] { totalSplits, total };
    }
}
