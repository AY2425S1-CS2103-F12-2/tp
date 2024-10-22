package seedu.address.logic.parser.editcommands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_ILLEGAL_PREFIX_USED;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.ALL_PREFIX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.logic.commands.addcommands.AddGroupCommand;
import seedu.address.logic.commands.editcommands.EditGroupCommand;
import seedu.address.logic.commands.editcommands.EditGroupCommand.EditGroupDescriptor;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupName;

/**
 * Parses input arguments and creates a new EditGroupCommand object
 */
public class EditGroupCommandParser implements Parser<EditGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditGroupCommand
     * and returns an EditGroupCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditGroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        List<Prefix> allowedPrefix = new ArrayList<Prefix>(Arrays.asList(PREFIX_GROUP_NAME));
        List<Prefix> invalidPrefixes = ALL_PREFIX;
        invalidPrefixes.removeAll(allowedPrefix);
        if (!containsInvalidPrefix(args, invalidPrefixes)) {
            throw new ParseException(MESSAGE_ILLEGAL_PREFIX_USED + "\n" + EditGroupCommand.MESSAGE_USAGE);
        }

        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_GROUP_NAME);
        if (!arePrefixesPresent(argMultimap, PREFIX_GROUP_NAME)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddGroupCommand.MESSAGE_USAGE));
        }

        List<String> groupNames = argMultimap.getAllValues(PREFIX_GROUP_NAME);
        if (groupNames.size() > 2) {
            throw new ParseException(String.format(EditGroupCommand.MESSAGE_INVALID_COMMAND_FORMAT, groupNames.size()));
        }
        if (groupNames.size() == 1) {
            throw new ParseException(EditGroupCommand.MESSAGE_NOT_EDITED);
        }

        EditGroupDescriptor editGroupDescriptor = new EditGroupDescriptor();
        GroupName originalGroupName = ParserUtil.parseGroupName(groupNames.get(0));
        GroupName updatedGroupName = ParserUtil.parseGroupName(groupNames.get(1));
        editGroupDescriptor.setGroupName(updatedGroupName);
        return new EditGroupCommand(originalGroupName, editGroupDescriptor);
    }
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    private boolean containsInvalidPrefix(String arg, List<Prefix> invalidPreFixes) {
        for (Prefix prefix : invalidPreFixes) {
            if (arg.contains(prefix.getPrefix())) {
                return false;
            }
        }
        return true;
    }
}
