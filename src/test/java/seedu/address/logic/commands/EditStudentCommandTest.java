package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditStudentCommand.EditPersonDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.student.Email;
import seedu.address.model.student.Name;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNumber;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;


public class EditStudentCommandTest {
    private Model model;
    private Student studentToEdit;
    private Student editedStudent;
    private EditPersonDescriptor editPersonDescriptor;

    private final StudentNumber validStudentNumber = new StudentNumber("A0123456P");
    private final StudentNumber nonExistentStudentNumber = new StudentNumber("A0999999K");
    @BeforeEach
    public void setUp() {
        model = new ModelManager();
        studentToEdit = new Student(new Name("Amy"),
            new Email("amy@u.nus.edu"),
            new HashSet<Tag>(),
            validStudentNumber);
        model.addPerson(studentToEdit);
        Set<Tag> tags = new HashSet<Tag>();
        tags.add(new Tag("GoodAtUI"));
        editedStudent = new Student(new Name("Bob"),
            new Email("bob@u.nus.edu"),
            tags,
            validStudentNumber);
        editPersonDescriptor = new EditPersonDescriptor();
        editPersonDescriptor.setName(new Name("Bob"));
        editPersonDescriptor.setTags(tags);
        editPersonDescriptor.setEmail(new Email("bob@u.nus.edu"));
    }
    @Test
    public void execute_studentEdited_success() throws CommandException {
        EditStudentCommand command = new EditStudentCommand(validStudentNumber, editPersonDescriptor);
        CommandResult result = command.execute(model);
        assertEquals(String.format(EditStudentCommand.MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedStudent)),
            result.getFeedbackToUser());
        assertEquals(editedStudent, model.getPersonByNumber(validStudentNumber));
    }

    @Test
    public void execute_studentNotFound_throwsCommandException() {
        EditStudentCommand command = new EditStudentCommand(nonExistentStudentNumber, editPersonDescriptor);
        assertThrows(CommandException.class, ()->command.execute(model), EditStudentCommand.MESSAGE_STUDENT_NOT_FOUND);
    }
    @Test
    public void equals() {
        StudentNumber studentNumberOne = new StudentNumber("A0123456P");
        StudentNumber studentNumberTwo = new StudentNumber("A0654321P");
        EditPersonDescriptor descriptorOne = new EditPersonDescriptor();
        descriptorOne.setName(new Name("John Doe"));
        descriptorOne.setEmail(new Email("e0000000@u.nus.edu"));
        EditPersonDescriptor descriptorTwo = new EditPersonDescriptor();
        descriptorTwo.setName(new Name("John Doe"));
        descriptorTwo.setEmail(new Email("e1111111@u.nus.edu"));

        EditStudentCommand commandOne = new EditStudentCommand(studentNumberOne, descriptorOne);
        EditStudentCommand commandTwo = new EditStudentCommand(studentNumberTwo, descriptorTwo);
        EditStudentCommand commandOneCopy = new EditStudentCommand(studentNumberOne, descriptorOne);
        assertTrue(commandOne.equals(commandOne));
        assertTrue(commandOne.equals(commandOneCopy));
        assertFalse(commandTwo.equals(commandOne));
        assertFalse(commandOne.equals(null));
        assertFalse(commandOne.equals(2));
    }
}
