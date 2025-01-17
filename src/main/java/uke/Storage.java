package uke;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import uke.exception.UkeDataFileException;
import uke.exception.UkeException;
import uke.exception.UkeInvalidTaskStringException;
import uke.task.Deadline;
import uke.task.Event;
import uke.task.Task;
import uke.task.TaskList;
import uke.task.Todo;

/**
 * Deals with loading tasks from file and saving tasks in file.
 */
public class Storage {

    private String path;

    /**
     * Constructor to initialise Storage object.
     *
     * @param path File path to the text file which stores task information.
     */
    public Storage(String path) {
        this.path = path;
    }

    /**
     * Creates a directory and text file if and only if directory and text file do not already exist.
     *
     * @throws UkeException If error is encountered creating the text file.
     */
    public void createDataFile() throws UkeException {
        File dataFile = new File(this.path);
        File dataDir = new File(dataFile.getParent());
        dataDir.mkdir();

        try {
            dataFile.createNewFile();
        } catch (IOException e) {
            throw new UkeDataFileException();
        }
    }

    /**
     * Converts the given task list to a string ready to be saved in the text file.
     *
     * @param stored TaskList with all stored tasks.
     * @return String representation of the task list to be saved in the text file.
     */
    public String generateTaskListString(TaskList stored) {
        int len = stored.getLength();
        String tL = "";

        if (len > 0) {
            for (int i = 1; i < len + 1; i++) {
                tL = tL + stored.getTask(i - 1).convertToSavedString() + "\n";
            }
        }
        return tL;
    }

    /**
     * Updates the text file with the newest state of the task list.
     *
     * @param storedTasks Task list with all stored tasks.
     * @throws UkeDataFileException If error is encountered accessing the text file.
     */
    public void update(TaskList storedTasks) throws UkeDataFileException {
        File dataFile = new File(this.path);

        try {
            FileWriter writer = new FileWriter(dataFile);
            String updatedTaskList = generateTaskListString(storedTasks);
            writer.write(updatedTaskList);
            writer.close();
        } catch (IOException e) {
            throw new UkeDataFileException();
        }
    }

    /**
     * Loads the task list from the text file into an ArrayList.
     * Create a text file if file does not exist yet.
     *
     * @return ArrayList containing the loaded tasks.
     * @throws UkeException If line in text file is incorrectly formatted or error is encountered creating a text file.
     */
    public ArrayList<Task> loadTaskList() throws UkeException {
        ArrayList<Task> taskList = new ArrayList<>();
        File dataFile = new File(this.path);

        try {
            Scanner sc = new Scanner(dataFile);
            while (sc.hasNextLine()) {
                String taskStr = sc.nextLine();
                taskList.add(convertStrToTask(taskStr));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            createDataFile();
            return taskList;
        }

        return taskList;
    }

    /**
     * Converts the string representation of a task saved into the text file into a Task object.
     *
     * @param str Stored string representation a task.
     * @return Task object represented by the given string.
     * @throws UkeInvalidTaskStringException If line in text file is not correctly formatted.
     */
    public Task convertStrToTask(String str) throws UkeInvalidTaskStringException {
        String[] strArr = str.split("//");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
        Task t;
        boolean isDone = strArr[1].equals("X");

        switch (str.substring(0, 3)) {
        case "[T]":
            t = new Todo(strArr[2]);
            break;
        case "[D]":
            t = new Deadline(strArr[2], LocalDateTime.parse(strArr[3], dateTimeFormatter));
            break;
        case "[E]":
            t = new Event(strArr[2], LocalDateTime.parse(strArr[3], dateTimeFormatter),
                    LocalDateTime.parse(strArr[4], dateTimeFormatter));
            break;
        default:
            throw new UkeInvalidTaskStringException();
        }

        if (isDone) {
            t.markAsDone();
        }
        return t;
    }
}
