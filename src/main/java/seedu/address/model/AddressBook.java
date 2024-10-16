package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupName;
import seedu.address.model.group.UniqueGroupList;
import seedu.address.model.student.Student;
import seedu.address.model.student.StudentNumber;
import seedu.address.model.student.UniqueStudentList;
import seedu.address.model.task.Task;
import seedu.address.model.task.UniqueTaskList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueStudentList students;
    private final UniqueGroupList groups;
    private final UniqueTaskList tasks;

    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        students = new UniqueStudentList();
        groups = new UniqueGroupList();
        tasks = new UniqueTaskList();
    }

    public AddressBook() {
    }

    /**
     * Creates an AddressBook using the Persons in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the student list with {@code students}.
     * {@code students} must not contain duplicate students.
     */
    public void setStudents(List<Student> students) {
        this.students.setPersons(students);
    }

    /**
     * Replaces the contents of the student list with {@code groups}.
     * {@code groups} must not contain duplicate students.
     */
    public void setGroups(List<Group> groups) {
        this.groups.setGroups(groups);
    }

    /**
     * Replaces the contents of the task list with {@code tasks}.
     * {@code tasks} must not contain duplicate tasks.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.setTasks(tasks);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setStudents(newData.getStudentList());
        setGroups(newData.getGroupList());
        setTasks(newData.getTaskList());
    }

    //// student-level operations

    /**
     * Returns true if a student with the same identity as {@code student} exists in the address book.
     */
    public boolean hasStudent(Student student) {
        requireNonNull(student);
        return students.contains(student);
    }

    /**
     * Adds a student to the address book.
     * The student must not already exist in the address book.
     */
    public void addStudent(Student p) {
        students.add(p);
    }

    /**
     * Replaces the given student {@code target} in the list with {@code editedStudent}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the address
     * book.
     */
    public void setStudent(Student target, Student editedStudent) {
        requireNonNull(editedStudent);

        students.setPerson(target, editedStudent);
    }

    /**
     * Returns true if a student with the same identity as {@code student} exists in the group
     * with the same identity as {@code group}.
     */
    public boolean hasStudentInGroup(Student student, Group group) {
        requireNonNull(student);
        requireNonNull(group);
        return group.getStudents().contains(student);
    }

    /**
     * Returns true if a task with the same identity as {@code task} exists in the group
     * with the same identity as {@code group}.
     */
    public boolean hasTaskInGroup(Task task, Group group) {
        requireNonNull(task);
        requireNonNull(group);
        return group.getTasks().contains(task);
    }

    /**
     * Adds {@code student} to {@code group}.
     * {@code student} and {@code group} must exist in the address book.
     */
    public void addStudentToGroup(Student student, Group group) {
        student.setStudentGroup(group.getGroupName());
        group.add(student);
    }

    /**
     * Adds {@code task} to {@code group}.
     * {@code group} must exist in the address book.
     */
    public void addTaskToGroup(Task task, Group group) {
        group.addTask(task);
    }

    /**
     * Adds {@code task} to {@code addressbook}.
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    public Student getStudentByNumber(StudentNumber studentNumber) {
        return students.getStudentByNumber(studentNumber);
    }

    public Group getGroupByName(GroupName groupName) {
        return groups.getGroupByName(groupName);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeStudent(Student key) {
        students.remove(key);
    }

    /**
     * Returns true if a group with the same identity as {@code group} exists in the address book.
     */
    public boolean hasGroup(Group group) {
        requireNonNull(group);
        return groups.contains(group);
    }

    /**
     * Returns true if a group with the same group name as {@code groupName} exists in the address book.
     */
    public boolean containsGroupName(GroupName groupName) {
        requireNonNull(groupName);
        return groups.containsGroupWithName(groupName);
    }

    /**
     * Adds a group to the address book.
     * The group must not already exist in the address book.
     */
    public void addGroup(Group p) {
        groups.add(p);
    }

    public void removeGroup(Group groupToBeDeleted) {
        groups.remove(groupToBeDeleted);
    }

    /**
     * Replaces the given group {@code target} in the list with {@code editedGroup}.
     * {@code target} must exist in the address book.
     * The student identity of {@code editedStudent} must not be the same as another existing student in the address
     * book.
     */
    public void setGroup(Group target, Group editedGroup) {
        requireNonNull(editedGroup);

        groups.setGroup(target, editedGroup);
    }

    public Group findGroupByName(GroupName groupName) {
        return groups.findGroupByName(groupName);
    }

    /**
     * Deletes the student {@code student} from the given group {@code group}
     */
    public void deleteStudentFromGroup(Group group, Student student) {
        requireNonNull(group);
        requireNonNull(student);
        group.delete(student);
        students.setPerson(student, student.removeGroup());
    }

    /**
     * Checks if the specified task currently exists.
     * @param task  The task in particular.
     * @return      Returns true if a task with the same identity as {@code task} exists in the address book.
     */
    public boolean hasTask(Task task) {
        requireNonNull(task);
        return tasks.contains(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    public void deleteTaskFromGroup(Task task, Group group) {
        group.deleteTask(task);
    }

    public void setTask(Task target, Task editedTask) {
        requireNonNull(editedTask);

        tasks.setTask(target, editedTask);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("students", students)
            .toString();
    }

    @Override
    public ObservableList<Student> getStudentList() {
        return students.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Group> getGroupList() {
        return groups.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return students.equals(otherAddressBook.students)
                && groups.equals(otherAddressBook.groups)
                && tasks.equals(otherAddressBook.tasks);
    }

    @Override
    public int hashCode() {
        return students.hashCode();
    }
}
