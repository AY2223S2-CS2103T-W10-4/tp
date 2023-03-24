package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_TUTEES;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tutee.*;
import seedu.address.model.tutee.Tutee;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing tutee in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the tutee identified "
            + "by the index number used in the displayed tutee list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited Tutee: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This tutee already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the tutee in the filtered tutee list to edit
     * @param editPersonDescriptor details to edit the tutee with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Tutee> lastShownList = model.getFilteredTuteeList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Tutee tuteeToEdit = lastShownList.get(index.getZeroBased());
        Tutee editedTutee = createEditedPerson(tuteeToEdit, editPersonDescriptor);

        if (!tuteeToEdit.isSamePerson(editedTutee) && model.hasTutee(editedTutee)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setTutee(tuteeToEdit, editedTutee);
        model.updateFilteredTuteeList(PREDICATE_SHOW_ALL_TUTEES);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedTutee));
    }

    /**
     * Creates and returns a {@code Tutee} with the details of {@code tuteeToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    static Tutee createEditedPerson(Tutee tuteeToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert tuteeToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(tuteeToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(tuteeToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(tuteeToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(tuteeToEdit.getAddress());
        Remark updatedRemark = tuteeToEdit.getRemark(); // edit command does not allow editing remarks
        Subject updatedSubject = editPersonDescriptor.getSubject().orElse(tuteeToEdit.getSubject());
        Schedule updatedSchedule = editPersonDescriptor.getSchedule().orElse(tuteeToEdit.getSchedule());
        StartTime updatedStartTime = editPersonDescriptor.getStartTime().orElse(tuteeToEdit.getStartTime());
        EndTime updatedEndTime = editPersonDescriptor.getEndTime().orElse(tuteeToEdit.getEndTime());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(tuteeToEdit.getTags());

        return new Tutee(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedRemark, updatedSubject
                ,updatedSchedule, updatedStartTime, updatedEndTime, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the tutee with. Each non-empty field value will replace the
     * corresponding field value of the tutee.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Subject subject;
        private Schedule schedule;
        private StartTime startTime;
        private EndTime endTime;
        private Set<Tag> tags;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setSubject(toCopy.subject);
            setSchedule(toCopy.schedule);
            setStartTime(toCopy.startTime);
            setEndTime(toCopy.endTime);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        public void setSubject(Subject subject) { this.subject = subject; }

        public Optional<Subject> getSubject() { return Optional.ofNullable(subject); }

        public void setSchedule(Schedule schedule) { this.schedule = schedule; }

        public Optional<Schedule> getSchedule() { return Optional.ofNullable(schedule); }

        public void setStartTime(StartTime startTime) { this.startTime = startTime; }

        public Optional<StartTime> getStartTime() { return Optional.ofNullable(startTime); }

        public void setEndTime(EndTime endTime) { this.endTime = endTime; }

        public Optional<EndTime> getEndTime() { return Optional.ofNullable(endTime); }

        /**
         * Sets {@code tags} EndTime this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getSubject().equals(e.getSubject())
                    && getSchedule().equals(e.getSchedule())
                    && getStartTime().equals(e.getStartTime())
                    && getEndTime().equals(e.getEndTime())
                    && getTags().equals(e.getTags());
        }
    }
}
