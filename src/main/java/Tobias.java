import command.Command;
import exception.TobiasException;
import parser.Parser;
import storage.Storage;
import task.TaskList;
import ui.Ui;

public class Tobias {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructor for the bot Tobias.
     *
     * @param filePath The relative string file path of the saved data.
     * */
    public Tobias(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        storage.createLocalStorage();
        try {
            tasks = storage.localToCurrent();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Runs the bot Tobias until isExit is true
     * */
    public void run() {
        ui.helloPrinter();
        boolean isExit = false;

        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parseCommands(fullCommand, tasks);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (TobiasException e) {
                e.printMessage();
            }
        }
    }

    /**
     * Main function that kick-starts the whole program
     * */
    public static void main(String[] args) {
        new Tobias("data/Tobias.txt").run();
    }
}
