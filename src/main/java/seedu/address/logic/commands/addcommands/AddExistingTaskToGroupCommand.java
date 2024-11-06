package seedu.address.logic.commands.addcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.ListMarkers.LIST_GROUP_MARKER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_GROUPS;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.VersionHistory;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.task.Task;

/**
 * Adds an existing task to a set of groups.
 */
public class AddExistingTaskToGroupCommand extends Command {

    public static final String COMMAND_WORD = "add_et_g";
    public static final String COMMAND_WORD_ALIAS = "aetg";

    public static final String MESSAGE_USAGE = COMMAND_WORD + "/" + COMMAND_WORD_ALIAS
        + ": Adds an existing task to the group(s).\n"
        + "Parameters: "
        + PREFIX_INDEX + "INDEX (must be a positive integer) "
        + PREFIX_GROUP_NAME + "GROUP_NAME "
        + "[" + PREFIX_GROUP_NAME + "GROUP_NAME]...\n"
        + "Example: " + COMMAND_WORD
        + PREFIX_INDEX + "1 "
        + PREFIX_GROUP_NAME + "CS2103T-F12-2"
        + PREFIX_GROUP_NAME + "CS2103-F13-3";
    public static final String MESSAGE_SUCCESS = "Added task (%1$s) to the following groups:\n%2$s";
    public static final String GROUP_HAS_TASK = "%1$s already has the task.";
    public static final String GROUP_NOT_FOUND = "Group with name %1$s does not exist.";
    public static final String MESSAGE_DUPLICATE_GROUP = "Duplicate group(s) entered, only 1 will be added:";

    private final Index targetIndex;
    private final List<GroupName> groupNames;

    /**
     * Creates an AddExistingTaskToGroupCommand to add the specified {@code Task} at the given {@code Index}
     * to the specified set of {@code Group}s.
     */
    public AddExistingTaskToGroupCommand(Index targetIndex, List<GroupName> groupNames) {
        requireNonNull(targetIndex);
        requireNonNull(groupNames);
        this.targetIndex = targetIndex;
        this.groupNames = groupNames;
    }


    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Task> lastShownList = model.getFilteredTaskList();
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_DISPLAYED_INDEX);
        }
        Task taskToAdd = lastShownList.get(targetIndex.getZeroBased());
        StringBuilder groupsAdded = new StringBuilder();
        Stream<GroupName> checkForDuplicates =
            groupNames.stream().filter(x -> Collections.frequency(groupNames, x) > 1).distinct();
        long numDuplicates =
            groupNames.stream().filter(x -> Collections.frequency(groupNames, x) > 1).distinct().count();
        String duplicateMessage = checkForDuplicates.map(GroupName::getGroupName).reduce(
            MESSAGE_DUPLICATE_GROUP, (x, y) -> x + "\n" + y
        );
        List<GroupName> noDuplicateGroupNameList = groupNames.stream().distinct().toList();
        for (GroupName groupName : noDuplicateGroupNameList) {
            Group groupToAdd = model.getGroupByName(groupName);
            // check that group exists
            if (groupToAdd == null) {
                throw new CommandException(String.format(GROUP_NOT_FOUND, groupName));
            }
            // check that group doesn't have task
            if (model.hasTaskInGroup(taskToAdd, groupToAdd)) {
                throw new CommandException(String.format(GROUP_HAS_TASK, groupName));
            }
            model.addTaskToGroup(taskToAdd, groupToAdd);
            model.increaseGroupWithTask(taskToAdd);
            groupsAdded.append(groupToAdd.getGroupName()).append("\n");
        }
        model.updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
        if (numDuplicates > 0) {
            return new CommandResult(duplicateMessage + "\n"
                + String.format(MESSAGE_SUCCESS, taskToAdd.getTaskName().getTaskName(), groupsAdded)
                , LIST_GROUP_MARKER);
        }
        return new CommandResult(String.format(MESSAGE_SUCCESS, taskToAdd.getTaskName().getTaskName(),
            groupsAdded), LIST_GROUP_MARKER);
    }

    @Override
    public VersionHistory updateVersionHistory(VersionHistory versionHistory, Model model) throws CommandException {
        versionHistory.addVersion(model);
        return versionHistory;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddExistingTaskToGroupCommand)) {
            return false;
        }

        AddExistingTaskToGroupCommand otherAddExistingTaskToGroupCommand = (AddExistingTaskToGroupCommand) other;
        return targetIndex.equals(otherAddExistingTaskToGroupCommand.targetIndex)
            && groupNames.equals(otherAddExistingTaskToGroupCommand.groupNames);
    }
}
