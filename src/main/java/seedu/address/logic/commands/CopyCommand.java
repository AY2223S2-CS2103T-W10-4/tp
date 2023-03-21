package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STARTTIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ENDTIME;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.tutee.Tutee;

public class CopyCommand extends Command {

    public static final String COMMAND_WORD = "copy";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an existing tutee to the system. "
            + "Parameters: INDEX (must be a positive integer)"
            + PREFIX_SUBJECT + "SUBJECT "
            + PREFIX_SCHEDULE + "SCHEDULE "
            + PREFIX_STARTTIME + "STARTTIME "
            + PREFIX_ENDTIME + "ENDTIME \n"
            + "Example: " + COMMAND_WORD + " 1"
            + PREFIX_SUBJECT + "English "
            + PREFIX_SCHEDULE + "monday "
            + PREFIX_STARTTIME + "08:30 "
            + PREFIX_ENDTIME + "10:30 ";

    public static final String MESSAGE_SUCCESS = "New lessons for tutee added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This tutee already has such lessons in the address book";

    private final Index targetIndex;

    private final Tutee toCopy;

    /**
     * Creates a CopyCommand to copy the specified {@code Tutee}
     */
    public CopyCommand(Index targetIndex, Tutee tutee) {
        requireNonNull(tutee);
        this.toCopy = tutee;
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException{
        requireNonNull(model);
        if (model.hasTutee(toCopy)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
        List<Tutee> lastShownList = model.getFilteredTuteeList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Tutee tuteeToCopy = lastShownList.get(targetIndex.getZeroBased());

        model.addTutee(tuteeToCopy);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toCopy));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof CopyCommand // instanceof handles nulls
                && targetIndex.equals(((CopyCommand) other).targetIndex)); // state check
    }
}
