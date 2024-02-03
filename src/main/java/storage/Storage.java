package storage;

import exception.TobiasException;
import parser.Parser;
import task.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Storage {
    private String filePath;

    /**
     * Constructor for a Storage.
     *
     * @param filePath The relative string file path of the saved data.
     * */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Checks if there is a local saved txt file at the filePath.
     * If there exists no such .txt file, it will create a new save file at the filePath.
     * */
    public void createLocalStorage() {
        try {
            File file = new File(filePath);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            if (file.createNewFile()) {
                System.out.println("   File created successfully: " + file.getAbsolutePath());
            } else {
                System.out.println("   File alr exists: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Reads data of commands and adds them to the given TaskList.
     *
     * @param data String of commands.
     * @param tasks TaskList.
     * @throws TobiasException If the given data contains corrupted commands.
     * */
    public static void localToList(String data, TaskList tasks) throws TobiasException {
        try {
            if (data.startsWith("T")) {
                boolean isDone = Integer.parseInt(data.substring(8,9)) == 1;

                int desc = data.indexOf("|desc");
                String description = data.substring(desc+5);

                Task newTask = new ToDo(description, isDone);
                tasks.addToList(newTask);

            }
            else if (data.startsWith("D")) {
                boolean isDone = Integer.parseInt(data.substring(8,9)) == 1;

                int desc = data.indexOf("|desc");
                int by = data.indexOf("|by");
                String description = data.substring(desc+5, by);
                String byDate = data.substring(by+3);
                LocalDateTime dd = Parser.dateFromString(byDate);

                Task newTask = new Deadline(description, isDone, dd);

                tasks.addToList(newTask);

            }
            else if (data.startsWith("E")) {
                boolean isDone = Integer.parseInt(data.substring(8,9)) == 1;

                int desc = data.indexOf("|desc");
                int from = data.indexOf("|from");
                int to = data.indexOf("|to");
                String description = data.substring(desc+5, from);
                String fromDate = data.substring(from+5, to);
                String toDate = data.substring(to+3);

                LocalDateTime f = Parser.dateFromString(fromDate);
                LocalDateTime t = Parser.dateFromString(toDate);

                Task newTask = new Event(description, isDone, f, t);
                tasks.addToList(newTask);
            }
            else {
                throw new TobiasException("   Saved file is corrupted!");
            }
        } catch (Exception e) {
            System.out.println("local to list function " + e.getMessage());
        }
    }

    /**
     * Reads the local saved .txt file at the filePath.
     * Calls localToList to interpret and add the commands that have been saved in the local .txt file.
     * */
    public TaskList localToCurrent() {
        TaskList tasks = new TaskList();
        try {
            File f = new File(filePath);
            Scanner s = new Scanner(f);

            while (s.hasNext()) {
                localToList(s.nextLine(), tasks);
            }
            s.close();
        } catch (TobiasException tE) {
            tE.printMessage();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return tasks;
    }


    public void storeToLocal(TaskList tasks) {
        String result = tasks.saveMechanism();

        try {
            FileWriter fw = new FileWriter("data/tobias.txt");
            fw.write(result);
            fw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
