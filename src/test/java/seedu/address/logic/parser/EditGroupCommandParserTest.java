package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NAME;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.editcommands.EditGroupCommand;
import seedu.address.logic.commands.editcommands.EditGroupCommand.EditGroupDescriptor;
import seedu.address.logic.parser.editcommands.EditGroupCommandParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupName;

public class EditGroupCommandParserTest {
    private EditGroupCommandParser parser = new EditGroupCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws ParseException {
        String userInput = " " + PREFIX_GROUP_NAME + "OriginalGroup " + PREFIX_GROUP_NAME + "UpdatedGroup";
        EditGroupDescriptor descriptor = new EditGroupDescriptor();
        descriptor.setGroupName(new GroupName("UpdatedGroup"));
        EditGroupCommand expectedCommand = new EditGroupCommand(new GroupName("OriginalGroup"), descriptor);

        assertEquals(expectedCommand, parser.parse(userInput));
    }
    @Test
    public void parse_moreThanTwoFields_failure() {
        String userInput = " " + PREFIX_GROUP_NAME + "OriginalGroup " + PREFIX_GROUP_NAME + "NewGroup "
            + PREFIX_GROUP_NAME + "ExtraGroup";
        assertThrows(ParseException.class, () -> parser.parse(userInput),
            String.format(EditGroupCommand.MESSAGE_INVALID_COMMAND_FORMAT, 3));
    }
    @Test
    public void parse_noFieldEdited_failure() {
        String userInput = " " + PREFIX_GROUP_NAME + "OriginalGroup";
        assertThrows(ParseException.class, () -> parser.parse(userInput), EditGroupCommand.MESSAGE_NOT_EDITED);
    }
    @Test
    public void parse_invalidPrefix_failure() {
        String userInput = " " + PREFIX_GROUP_NAME + "OriginalGroup" + PREFIX_STUDENT_NAME + "Student Name";
        assertThrows(ParseException.class, ()-> parser.parse(userInput),
            String.format(Messages.MESSAGE_ILLEGAL_PREFIX_USED, EditGroupCommand.MESSAGE_USAGE));
    }
}
