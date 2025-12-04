import java.nio.file.*;
import java.util.*;

public class Day1 {
    public static void main(String[] args) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("input.txt"));

        int pos = 50;
        int part1 = 0, part2 = 0;

        for (String line : lines) {
            if (line.isEmpty())
                continue;

            char dir = line.charAt(0);
            int dist = Integer.parseInt(line.substring(1));

            if (dir == 'L') {
                // count zeros while going left
                if (pos > 0)
                    part2 += (dist + 100 - pos) / 100;
                else
                    part2 += dist / 100;

                pos = ((pos - dist) % 100 + 100) % 100;
            } else {
                // count zeros while going right
                part2 += (pos + dist) / 100;
                pos = (pos + dist) % 100;
            }

            if (pos == 0)
                part1++;
        }

        System.out.println("Part 1: " + part1);
        System.out.println("Part 2: " + part2);
    }
}
