package seedu.address.logic.parser.addcommands;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.addcommands.AddExistingTaskToGroupCommand;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.Prefix;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupName;

/**
 * Parses input arguments and creates a new AddExistingTaskToGroupCommand object.
 */
public class AddExistingTaskToGroupCommandParser implements Parser<AddExistingTaskToGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddExistingTaskToGroupCommand
     * and returns an AddExistingTaskToGroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddExistingTaskToGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_GROUP_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_INDEX, PREFIX_GROUP_NAME) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                AddExistingTaskToGroupCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX);
        Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
        List<GroupName> groupNameList = new ArrayList<>();
        for (String s : argMultimap.getAllValues(PREFIX_GROUP_NAME)) {
            groupNameList.add(ParserUtil.parseGroupName(s));
        }

        return new AddExistingTaskToGroupCommand(index, groupNameList);

    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
