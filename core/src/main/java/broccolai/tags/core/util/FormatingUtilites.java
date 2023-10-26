package broccolai.tags.core.util;

import java.util.ArrayList;
import java.util.List;

public final class FormatingUtilites {

    public static List<String> splitString(String input, int maxLength) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        String[] words = input.split(" ");

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= maxLength) {
                if (!currentLine.isEmpty()) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }

        lines.add(currentLine.toString());

        return lines;
    }

    private FormatingUtilites() {
    }

}
