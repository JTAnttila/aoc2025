import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Day10 {

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

        int totalPresses = 0;
        for (String line : lines) {
            int presses = solveMachine(line);
            totalPresses += presses;
        }

        System.out.println("Total minimum button presses: " + totalPresses);
    }

    static int solveMachine(String line) {
        // Parse the indicator light diagram [...]
        Pattern indicatorPattern = Pattern.compile("\\[([.#]+)\\]");
        Matcher indicatorMatcher = indicatorPattern.matcher(line);
        indicatorMatcher.find();
        String indicator = indicatorMatcher.group(1);

        int numLights = indicator.length();
        int[] target = new int[numLights];
        for (int i = 0; i < numLights; i++) {
            target[i] = indicator.charAt(i) == '#' ? 1 : 0;
        }

        // Parse all button wiring schematics (...)
        Pattern buttonPattern = Pattern.compile("\\(([0-9,]+)\\)");
        Matcher buttonMatcher = buttonPattern.matcher(line);

        List<int[]> buttons = new ArrayList<>();
        while (buttonMatcher.find()) {
            String buttonStr = buttonMatcher.group(1);
            String[] parts = buttonStr.split(",");
            int[] toggles = new int[numLights];
            for (String part : parts) {
                int idx = Integer.parseInt(part.trim());
                if (idx < numLights) {
                    toggles[idx] = 1;
                }
            }
            buttons.add(toggles);
        }

        int numButtons = buttons.size();
        int minPresses = Integer.MAX_VALUE;

        for (int mask = 0; mask < (1 << numButtons); mask++) {
            int[] state = new int[numLights];
            int presses = 0;

            for (int b = 0; b < numButtons; b++) {
                if ((mask & (1 << b)) != 0) {
                    presses++;
                    int[] button = buttons.get(b);
                    for (int i = 0; i < numLights; i++) {
                        state[i] ^= button[i];
                    }
                }
            }

            // Check if state matches target
            boolean matches = true;
            for (int i = 0; i < numLights; i++) {
                if (state[i] != target[i]) {
                    matches = false;
                    break;
                }
            }

            if (matches) {
                minPresses = Math.min(minPresses, presses);
            }
        }

        return minPresses == Integer.MAX_VALUE ? 0 : minPresses;
    }
}
