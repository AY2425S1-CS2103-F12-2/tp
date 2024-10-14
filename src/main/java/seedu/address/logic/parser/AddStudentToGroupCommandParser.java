package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STUDENT_NUMBER;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddStudentToGroupCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.group.GroupName;
import seedu.address.model.student.StudentNumber;

/**
 * Parses input arguments and creates a new AddStudentToGroupCommand object.
 */
public class AddStudentToGroupCommandParser implements Parser<AddStudentToGroupCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddStudentToGroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddStudentToGroupCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_STUDENT_NUMBER, PREFIX_GROUP_NAME);

        if (!arePrefixesPresent(argMultimap, PREFIX_STUDENT_NUMBER, PREFIX_GROUP_NAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddStudentToGroupCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_STUDENT_NUMBER, PREFIX_GROUP_NAME);
        StudentNumber studentNumber = ParserUtil.parseStudentNumber(argMultimap.getValue(PREFIX_STUDENT_NUMBER).get());
        GroupName groupName = ParserUtil.parseGroupName(argMultimap.getValue(PREFIX_GROUP_NAME).get());

        return new AddStudentToGroupCommand(studentNumber, groupName);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
