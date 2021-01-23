import java.time.LocalDate;

/**
 * Makes sense of the user command
 */
public class Parser {

    /**
     * Responds to the user command accordingly
     * @param input  command-line input supplied by the user
     * @param ui  user interface in charge of printing out responses to the user
     * @throws DukeException  if user command is invalid
     */
    public static void handleUserCommand(String input, Ui ui) throws DukeException {
            if (input.equals("bye")) {
                ui.showGoodbyeMessage();
                Duke.sayBye = true;
            } else if (input.equals("list")) {
                ui.showTaskList();
            } else if (input.startsWith("done ")) {
                int taskNumber = Integer.parseInt(input.substring(5));
                Task task = TaskList.taskList.get(taskNumber - 1);
                TaskList.markTaskDone(task);
                ui.showTaskDone(task);
            } else if (input.startsWith("todo ")) {
                if (input.split(" ").length == 1) {
                    throw new DukeException("OOPS!!! The description of a todo cannot be empty.");
                }
                String todoTask = getTodoTask(input);
                TaskList.createTodoTask(todoTask);
                ui.showTaskAdded();
                ui.showNumberOfTasks();
            } else if (input.startsWith("event ")) {
                String eventTask = getEventTask(input);
                String date = getEventDate(input);
                TaskList.createEventTask(eventTask, date);
                ui.showTaskAdded();
                ui.showNumberOfTasks();
            } else if (input.startsWith("deadline ")) {
                String deadlineTask = getDeadlineTask(input);
                LocalDate deadline = getDeadlineDate(input);
                TaskList.createDeadlineTask(deadlineTask, deadline);
                ui.showTaskAdded();
                ui.showNumberOfTasks();
            } else if (input.startsWith("delete ")) {
                int taskNumber = Integer.parseInt(input.substring(7));
                Task taskDeleted = TaskList.taskList.get(taskNumber-1);
                TaskList.deleteTask(taskNumber);
                ui.showTaskDeleted(taskDeleted);
                ui.showTaskAdded();
                ui.showNumberOfTasks();
            } else {
                throw new DukeException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
    }

    /**
     * Returns the todo task description upon filtering the input
     * @param input  supplied by the user command
     * @return  todo task description
     */
    public static String getTodoTask(String input) {
        return input.substring(5);
    }

    /**
     * Returns the event task description upon filtering the input
     * @param input  supplied by the user command
     * @return  event task description
     */
    public static String getEventTask(String input) {
        return input.substring(6).split("/")[0];
    }

    /**
     * Returns the event date upon filtering the input
     * @param input  supplied by the user command
     * @return  event date
     */
    public static String getEventDate(String input) {
        return input.substring(6).split("/")[1].substring(3);
    }

    /**
     * Returns the deadline task description upon filtering the input
     * @param input  supplied by the user command
     * @return  deadline task description
     */
    public static String getDeadlineTask(String input) {
        return input.substring(9).split("/",2)[0];
    }

    /**
     * Returns the deadline date upon filtering the input
     * @param input  supplied by the user command
     * @return  deadline date
     */
    public static LocalDate getDeadlineDate(String input) {
        return LocalDate.parse(input.substring(9).split("/",2)[1].substring(3)
                .replaceAll("/","-"));
    }



}