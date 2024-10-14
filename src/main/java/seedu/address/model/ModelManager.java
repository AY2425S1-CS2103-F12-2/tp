package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.State;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNumber;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;

    private final UserPrefs userPrefs;
    private final FilteredList<Student> filteredStudents;
    private final FilteredList<Group> filteredGroups;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredStudents = new FilteredList<>(this.addressBook.getStudentList());
        filteredGroups = new FilteredList<>(this.addressBook.getGroupList());
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setStateStudents() {
        this.userPrefs.setStateStudents();
        ;
    }

    @Override
    public void setStateGroups() {
        this.userPrefs.setStateGroups();
    }

    @Override
    public State getState() {
        return this.userPrefs.getState();
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Student student) {
        requireNonNull(student);
        return addressBook.hasStudent(student);
    }

    @Override
    public void deletePerson(Student target) {
        addressBook.removeStudent(target);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void addPerson(Student student) {
        addressBook.addStudent(student);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Student target, Student editedStudent) {
        requireAllNonNull(target, editedStudent);
        addressBook.setStudent(target, editedStudent);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return addressBook.hasGroup(group);
    }

    @Override
    public void deleteGroup(Group group) {
        addressBook.removeGroup(group);
        updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
    }

    @Override
    public void addGroup(Group group) {
        addressBook.addGroup(group);
        updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
    }

    @Override
    public void setGroup(Group target, Group editedGroup) {
        requireAllNonNull(target, editedGroup);
        addressBook.setGroup(target, editedGroup);
        updateFilteredGroupList(PREDICATE_SHOW_ALL_GROUPS);
    }

    @Override
    public boolean hasPersonInGroup(Student student, Group group) {
        requireAllNonNull(student, group);
        return addressBook.hasStudentInGroup(student, group);
    }

    @Override
    public void addPersonToGroup(Student student, Group group) {
        requireAllNonNull(student, group);
        addressBook.addStudentToGroup(student, group);
    }

    @Override
    public Student getPersonByNumber(StudentNumber studentNumber) {
        return addressBook.getStudentByNumber(studentNumber);
    }

    @Override
    public Group getGroupByName(GroupName groupName) {
        return addressBook.getGroupByName(groupName);
    }

    //=========== Filtered Student List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Student} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Student> getFilteredPersonList() {
        return filteredStudents;
    }

    @Override
    public ObservableList<Group> getFilteredGroupList() {
        return filteredGroups;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Student> predicate) {
        requireNonNull(predicate);
        filteredStudents.setPredicate(predicate);
    }

    @Override
    public void updateFilteredGroupList(Predicate<Group> predicate) {
        requireNonNull(predicate);
        filteredGroups.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
            && userPrefs.equals(otherModelManager.userPrefs)
            && filteredStudents.equals(otherModelManager.filteredStudents)
            && filteredGroups.equals(otherModelManager.filteredGroups);
    }

}
