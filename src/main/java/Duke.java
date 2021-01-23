import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a Personal Assistant Chatbot named Duke that
 * helps users to keep track of things
 * @author Damith C. Rajapakse, Jeffry Lum, Vanessa Tay
 */
public class Duke {
    public static ArrayList<Task> taskList = new ArrayList<>();
    public static final String FILE_PATH_SAVED_TASKS = "./savedTasks.txt";

    /**
     * Greets the user and begins listening to user commands
     * @param args supplies command-line arguments to the Chatbot
     */
    public static void main(String[] args) throws DukeException {
        greetUser();
        try {
            Storage.loadTaskList(FILE_PATH_SAVED_TASKS);
            prepareTaskList(Storage.storedList);
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("Creating file at ./savedTasks.txt\n");
        }
        listenToUserCommand();
    }

    public static void prepareTaskList(ArrayList<String> list) throws DukeException {
        for(int i = 0; i < list.size(); i++) {
            String taskInList = list.get(i);
            if (taskInList.startsWith("[T]")) {
               String task = taskInList.substring(16);
               Task createTask = new Todo(task);
                if(taskInList.substring(4).startsWith("C")) {
                    createTask.isDone = true;
                }
               taskList.add(createTask);
            } else if (taskInList.startsWith("[D]")) {
                int date = taskInList.indexOf("(by: ");
                String task = taskInList.substring(16, date);
                String by = taskInList.substring(date);
                Task createTask = new Deadline(task, by);
                if(taskInList.substring(4).startsWith("C")) {
                    createTask.isDone = true;
                }
                taskList.add(createTask);
            } else if (taskInList.startsWith("[E]")) {
                int date = taskInList.indexOf("(at: ");
                String task = taskInList.substring(16, date);
                String at = taskInList.substring(date);
                Task createTask = new Deadline(task, at);
                if(taskInList.substring(4).startsWith("C")) {
                    createTask.isDone = true;
                }
                taskList.add(createTask);
            } else {
                throw new DukeException("Invalid task.");
            }
        }
    }

    public static void greetUser() {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";
        System.out.println("Hello from\n" + logo + "\nHow can I help?\n");
    }

    /**
     * Takes in user's command line arguments and treats them accordingly.
     * Chatbot Duke will end the session when user inputs "bye".
     */
    public static void listenToUserCommand() throws DukeException {
        Scanner sc = new Scanner(System.in);
        while(true) {
            String input = sc.nextLine();
            if (input.equals("bye")) {
                stopListeningToCommand();
                break;
            } else if (input.equals("list")) {
                displayUserCommands();
            } else if (input.startsWith("done ")) {
                markTaskDone(input);
            } else if (input.startsWith("todo ")) {
                if (input.split(" ").length == 1) {
                    throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
                }
                createTodoTask(input);
                updateTaskList();
            } else if (input.startsWith("event ")) {
                createEventTask(input);
                updateTaskList();
            } else if (input.startsWith("deadline ")) {
                createDeadlineTask(input);
                updateTaskList();
            } else if (input.startsWith("delete ")) {
                deleteTask(input);
            } else {
                throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
            Storage.storeTaskList(FILE_PATH_SAVED_TASKS);
        }
    }

    /**
     * Chatbot deletes the task from the task list
     * @param input referring to the delete command-line input supplied by the user
     */
    public static void deleteTask(String input) {
        int taskNumber = Integer.parseInt(input.substring(7));
        Task taskDeleted = taskList.get(taskNumber-1);
        System.out.println("Noted. I've removed this task: ");
        taskList.remove(taskNumber-1);
        System.out.println(taskDeleted);
        updateTaskList();
    }

    /**
     * Chatbot prints out a farewell message before ending the session
     */
    public static void stopListeningToCommand() {
        System.out.println("Bye. Hope to see you soon!\n");
    }

    /**
     * Chatbot prints out line-by-line all of the user's tasks
     * stored in the task list in that given session.
     */
    public static void displayUserCommands() {
        System.out.println("Here are your tasks!");
        for(int i = 1; i <= taskList.size(); i++) {
            Task task = taskList.get(i-1);
            System.out.print(i + ".");
            System.out.println(task);
        }
        System.out.println();
    }

    /**
     * Mark task as done and notify user of that change
     * @param input supplied from the user's command-line input to mark a task as done
     */
    public static void markTaskDone(String input) {
        int taskNumber = Integer.parseInt(input.substring(5));
        Task task = taskList.get(taskNumber - 1);
        task.isDone = true;
        System.out.println("Nice! I've marked this task as done:");
        System.out.println(task);
        System.out.println();
    }

    /**
     * creates a todo task and adds it to the task list
     * @param input supplied from user's command-line input to add a todo task
     */
    public static void createTodoTask(String input) {
        String todoTask = input.substring(5);
        Task task = new Todo(todoTask);
        addTaskToList(task);
    }

    /**
     * creates an event task and adds it to the task list
     * @param input supplied from user's command-line input to add an event task
     */
    public static void createEventTask(String input) {
        String[] taskAndDate = input.substring(6).split("/");
        String eventTask = taskAndDate[0];
        String date = taskAndDate[1].substring(3);
        Task task = new Event(eventTask, date);
        addTaskToList(task);
    }

    /**
     * creates a deadline task and adds it to the task list
     * @param input supplied from user's command-line input to add a deadline task
     */
    public static void createDeadlineTask(String input) {
        String[] taskAndDate = input.substring(9).split("/");
        String deadlineTask = taskAndDate[0];
        String deadline = taskAndDate[1].substring(3);
        Task task = new Deadline(deadlineTask, deadline);
        addTaskToList(task);
    }

    /**
     * adds task into the task list
     * @param task to be added into the task list
     */
    public static void addTaskToList(Task task) {
        taskList.add(task);
        System.out.println("added: " + task);
    }

    /**
     * prints out the number of current tasks in the task list
     */
    public static void updateTaskList() {
        int numTasks = taskList.size();
        System.out.println("you have " + numTasks + " tasks in your list");
        System.out.println();
    }

}
