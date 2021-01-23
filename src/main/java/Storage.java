import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Loads and saves tasks from and into the specified file
 */
public class Storage {

    protected String filePath;
    public static ArrayList<String> storedList = new ArrayList<>();

    /**
     * Initalizes a Storage object
     * @param filePath  the file path in which tasks are loaded from or saved into
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the file
     * @return the lists of tasks loaded from the file
     * @throws FileNotFoundException  if the file at the specified filepath does not exist
     */
    public ArrayList<String> load() throws FileNotFoundException {
        File f = new File(filePath);
        Scanner s = new Scanner(f);
        while (s.hasNext()) {
            String task = s.nextLine();
            storedList.add(task);
        }
        return storedList;
    }

    /**
     * Stores tasks into the file
     * @param filePath the file path in which tasks are saved into
     */
    public static void store(String filePath) {
        try {
            FileWriter fw = new FileWriter(filePath);
            for(int i = 0; i < TaskList.taskList.size(); i++) {
                fw.write(TaskList.taskList.get(i) + " \r\n");
            }
            fw.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

}
