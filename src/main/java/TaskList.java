import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;

/**
 * Contains the task list and takes charge of operations involving tasks and the task list
 */
public class TaskList {
    protected static ArrayList<Task> updatedTaskList;
    protected static ArrayList<String> loadedList;

    /**
     * Initializes a TaskList object in the situation where the specified
     * file from which tasks are supposed to be loaded from is not found
     */
    public TaskList() {
        updatedTaskList = new ArrayList<>();
    }

    public TaskList(ArrayList<String> loadedList) throws DukeException {
        updatedTaskList = new ArrayList<>();
        TaskList.loadedList = loadedList;
        updateTaskList();
    }

    public static void updateTaskList() throws DukeException {
        for (int i = 0; i < loadedList.size(); i++) {
            String storedTaskString = loadedList.get(i);
            if (storedTaskString.startsWith("[T]")) {
                String task = getTodoTask(storedTaskString);

                createTodoTask(task);
                checkIfTaskDone(storedTaskString);
            } else if (storedTaskString.startsWith("[D]")) {
                String task = getDeadlineTask(storedTaskString);
                String date = getDeadlineDate(storedTaskString);

                DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM d yyyy");
                LocalDate by = LocalDate.parse(date, format);

                createDeadlineTask(task, by);
                checkIfTaskDone(storedTaskString);
            } else if (storedTaskString.startsWith("[E]")) {
                String task = getEventTask(storedTaskString);
                String at = getEventDate(storedTaskString);

                createEventTask(task, at);
                checkIfTaskDone(storedTaskString);
            } else {
                throw new DukeException("Invalid task.");
            }
        }
    }

    /**
     * Returns todo task description from filtering the input
     * @param input the task line previously loaded into the file
     * @return the todo task description
     */
    public static String getTodoTask(String input) {
        return input.substring(16);
    }

    /**
     * Returns deadline task description from filtering the input
     * @param input the task line previously loaded into the file
     * @return the deadline task description
     */
    public static String getDeadlineTask(String input) {
        int dateIndex = input.indexOf("(by: ");
        return input.substring(16, dateIndex - 1);
    }

    /**
     * Returns deadline task date from filtering the input
     * @param input the task line previously loaded into the file
     * @return the deadline task date
     */
    public static String getDeadlineDate(String input) {
        int dateIndex = input.indexOf("(by: ");
        return input.substring(dateIndex + 5, dateIndex + 15);
    }

    /**
     * Returns event task description from filtering the input
     * @param input the task line previously loaded into the file
     * @return the event task description
     */
    public static String getEventTask(String input) {
        int dateIndex = input.indexOf("(at: ");
        return input.substring(16, dateIndex - 1);
    }

    /**
     * Returns event task date from filtering the input
     * @param input the task line previously loaded into the file
     * @return the event task date
     */
    public static String getEventDate(String input) {
        int dateIndex = input.indexOf("(at: ");
        return input.substring(dateIndex + 5, input.length() - 2);
    }

    public static void checkIfTaskDone(String storedTaskString) {
        if (storedTaskString.substring(4).startsWith("C")) {
            setDone(updatedTaskList.get(updatedTaskList.size()-1));
        }
    }

    /**
     * Adds task into the task list
     * @param task to be added into the task list
     */
    public static void addTaskToList(Task task) {
        updatedTaskList.add(task);
    }

    /**
     * Creates an event task and adds it to the task list
     * @param eventTask event task description
     * @param date of which the event occurs
     */
    public static void createEventTask(String eventTask, String date) {
        Task task = new Event(eventTask, date);
        addTaskToList(task);
    }


    /**
     * Creates an deadline task and adds it to the task list
     * @param deadlineTask deadline task description
     * @param deadline of which the task is supposed to be completed by
     */
    public static void createDeadlineTask(String deadlineTask, LocalDate deadline) {
        Task task = new Deadline(deadlineTask, deadline);
        addTaskToList(task);
    }

    /**
     * Creates a todo task and adds it to the task list
     * @param todoTask todo task description
     */
    public static void createTodoTask(String todoTask) {
        Task task = new Todo(todoTask);
        addTaskToList(task);
    }

    /**
     * Mark task as done
     * @param task to be marked as done
     */
    public static void setDone(Task task) {
        task.isDone = true;
    }

    /**
     * Deletes the task from the task list
     * @param taskNumber of the task (in the task list) that is to be deleted
     */
    public static void deleteTask(int taskNumber) {
        updatedTaskList.remove(taskNumber - 1);
    }

    public static ArrayList<String> findMatchingTasks(String keyword) {
        ArrayList<String> matchedTasks = new ArrayList<>();
        for (int i = 0; i < loadedList.size(); i++) {
            String currentTask = loadedList.get(i);
            if (currentTask.indexOf(keyword) != -1) {
                matchedTasks.add(currentTask);
            }
        }
        return matchedTasks;
    }

}


