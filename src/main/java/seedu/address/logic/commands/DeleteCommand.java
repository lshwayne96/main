package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.moduletaken.ModuleTaken;

/**
 * Deletes a moduleTaken identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the moduleTaken identified by the index number used in the displayed moduleTaken list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted ModuleTaken: %1$s";

    private final Index targetIndex;

    public DeleteCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<ModuleTaken> lastShownList = model.getFilteredModulesTakenList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
        }

        ModuleTaken moduleTakenToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (model.canDeleteModuleTaken(moduleTakenToDelete)) {
            model.deleteModuleTaken(moduleTakenToDelete);
        } else {
            throw new CommandException(Messages.MESSAGE_PREREQUISITE_VIOLATED);
        }
        model.commitGradTrak();
        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, moduleTakenToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteCommand // instanceof handles nulls
                && targetIndex.equals(((DeleteCommand) other).targetIndex)); // state check
    }
}
