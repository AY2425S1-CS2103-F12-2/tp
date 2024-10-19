package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.VersionHistory;

/**
 * Adds a group to the address book.
 */
public class ListStudentCommand extends Command {

    public static final String COMMAND_WORD = "list_s";
    public static final String COMMAND_WORD_ALIS = "ls";
    public static final int LIST_STUDENT_MARKER = 0;

    public static final String MESSAGE_USAGE = COMMAND_WORD + "/" + COMMAND_WORD_ALIS
        + ": Lists all students.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all students";

    /**
     * Creates an ListStudentCommand to add the specified {@code Group}
     */
    public ListStudentCommand() {
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.setStateStudents();
        return new CommandResult(MESSAGE_SUCCESS, LIST_STUDENT_MARKER);
    }

    @Override
    public VersionHistory updateVersionHistory(VersionHistory versionHistory, Model model) {
        return versionHistory;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .toString();
    }
}
