package seedu.address.model.student;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.group.GroupName;
import seedu.address.model.tag.Tag;

/**
 * Represents a Student in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Student {

    // Identity fields
    private final Name name;
    private final Email email;
    private final StudentNumber studentNumber;

    // Data fields
    private final Set<Tag> tags = new HashSet<>();
    private final Optional<GroupName> groupName;

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Email email, Set<Tag> tags, StudentNumber studentNumber) {
        requireAllNonNull(name, email, tags, studentNumber);
        this.name = name;
        this.email = email;
        this.tags.addAll(tags);
        this.studentNumber = studentNumber;
        this.groupName = Optional.empty();
    }

    /**
     * Every field must be present and not null.
     */
    public Student(Name name, Email email, Set<Tag> tags, StudentNumber studentNumber, Optional<GroupName> groupName) {
        requireAllNonNull(name, email, tags, studentNumber, groupName);
        this.name = name;
        this.email = email;
        this.tags.addAll(tags);
        this.groupName = groupName;
        this.studentNumber = studentNumber;
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public StudentNumber getStudentNumber() {
        return studentNumber;
    }

    public Optional<GroupName> getGroupName() {
        return groupName;
    }


    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Student otherStudent) {
        if (otherStudent == this) {
            return true;
        }

        return otherStudent != null
            && otherStudent.getStudentNumber().equals(getStudentNumber());

    }

    /**
     * Returns a copy of student which has no group
     */
    public Student removeGroup() {
        return new Student(name, email, tags, studentNumber, Optional.empty());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Student)) {
            return false;
        }

        Student otherStudent = (Student) other;
        return name.equals(otherStudent.name)
            && email.equals(otherStudent.email)
            && tags.equals(otherStudent.tags)
            && studentNumber.equals(otherStudent.studentNumber);
    }

    public Student setStudentGroup(GroupName groupName) {
        return new Student(name, email, tags, studentNumber, Optional.<GroupName>of(groupName));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, email, studentNumber, tags);
    }

    /**
     * Returns Student in an easy to read format for listing in Group cards.
     */
    public String toDisplayString() {
        return new StringBuilder().append(name).append(" [").append(studentNumber).append(", ").append(email).append(
            "]").toString();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("name", name)
            .add("email", email)
            .add("tags", tags)
            .add("student number", studentNumber)
            .add("group", groupName.orElse(null))
            .toString();
    }

}
