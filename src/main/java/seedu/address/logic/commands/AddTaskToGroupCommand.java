package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_DEADLINE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TASK_NAME;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.task.Deadline;
import seedu.address.model.task.Task;
import seedu.address.model.task.TaskName;

/**
 * Adds a task to a group.
 */
public class AddTaskToGroupCommand extends Command {

    public static final String COMMAND_WORD = "add_task_grp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to a group. "
            + "Parameters: "
            + PREFIX_TASK_NAME + "TASK_NAME "
            + PREFIX_TASK_DEADLINE + "TASK_DATE "
            + PREFIX_GROUP_NAME + "GROUP_NAME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TASK_NAME + "Complete this task "
            + PREFIX_TASK_DEADLINE + "2024-01-01 1300 "
            + PREFIX_GROUP_NAME + "Team 1";

    public static final String MESSAGE_SUCCESS = "Added task: %1$s to %2$s";

    public static final String MESSAGE_DUPLICATE_TASK_IN_GROUP = "This task is already in the group";

    public static final String GROUP_NOT_FOUND = "Group not found!";

    private final TaskName taskName;

    private final Deadline deadline;

    private final GroupName toAddInto;

    /**
     * Creates an AddStudentToGroupCommand to add the specified {@code Student} to the specified {@code Group}
     */
    public AddTaskToGroupCommand(TaskName taskName, Deadline deadline, GroupName groupName) {
        requireNonNull(taskName);
        requireNonNull(groupName);
        this.taskName = taskName;
        this.deadline = deadline;
        this.toAddInto = groupName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (!model.containsGroupName(toAddInto)) {
            throw new CommandException(GROUP_NOT_FOUND);
        }

        Task task = new Task(taskName, deadline);
        Group group = model.getGroupByName(toAddInto);

        if (model.hasTaskInGroup(task, group)) {
            throw new CommandException(MESSAGE_DUPLICATE_TASK_IN_GROUP);
        }

        model.addTaskToGroup(task, group);
        if (!model.hasTask(task)) {
            model.addTask(task);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, task.getTaskName().toString(),
                Messages.format(group)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AddTaskToGroupCommand)) {
            return false;
        }

        AddTaskToGroupCommand otherAddTaskToGroupCommand = (AddTaskToGroupCommand) other;
        return taskName.equals(otherAddTaskToGroupCommand.taskName)
                && deadline.equals(otherAddTaskToGroupCommand.deadline)
                && toAddInto.equals(otherAddTaskToGroupCommand.toAddInto);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("taskName", taskName)
                .add("deadline", deadline)
                .add("toAddInto", toAddInto)
                .toString();
    }
}
