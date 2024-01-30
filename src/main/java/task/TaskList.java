package task;

import exception.TobiasException;
import ui.Ui;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public void addToList(Task task) {
        tasks.add(task);
    }

    public Task getTask(int index) {
        return tasks.get(index);
    }

    public void removeFromList(int index) {
        tasks.remove(index);
    }

    public String saveMechanism() {
        String result = "";

        for (Task task : tasks) {
            result += task.storagePrinter() + System.lineSeparator();
        }

        return result;
    }

    public int taskNum() {
        return tasks.size();
    }

    public void printList() {
        try {
            if (tasks.isEmpty()) {
                throw new TobiasException("    Your list is empty at the moment !");
            } else {
                Ui.printDivider();

                System.out.println("    Here are the tasks in your list:");
                for (Task task : tasks) {
                    int index = tasks.indexOf(task);
                    task.taskPrinter(index);
                }

                Ui.printDivider();
            }
        } catch (TobiasException e) {
            e.printMessage();
        }
    }

    public void printListFind(String keyword) throws TobiasException {
        List<Task> foundResults = new ArrayList<>();

        for (Task task : tasks) {
            if (task.hasKeyword(keyword)) {
                foundResults.add(task);
            }
        }

        try {
            if (foundResults.isEmpty()) {
                throw new TobiasException("    Could not find any results containing : " + keyword);
            } else {
                Ui.printDivider();

                System.out.println("    Here are the matching tasks in your list:");
                for (Task task : foundResults) {
                    int index = tasks.indexOf(task);
                    task.taskPrinter(index);
                }

                Ui.printDivider();
            }
        } catch (TobiasException e) {
            e.printMessage();
        }
    }
}
