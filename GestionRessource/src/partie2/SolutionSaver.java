package partie2;

import java.io.*;
import java.util.Map;

public class SolutionSaver {
    public static void saveSolution(String filePath, Map<String, Integer> solution) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        for (Map.Entry<String, Integer> entry : solution.entrySet()) {
            writer.write(entry.getKey() + ":" + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }
}
