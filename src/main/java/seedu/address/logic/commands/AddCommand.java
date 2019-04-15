package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPECTED_MAX_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPECTED_MIN_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_INFO_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.Messages;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Workload;

/**
 * Adds a moduleTaken to the GradTrak.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a moduleTaken to GradTrak. "
            + "Parameters: "
            + PREFIX_MODULE_INFO_CODE + "ModuleInfoCode "
            + PREFIX_SEMESTER + "SEMESTER "
            + PREFIX_EXPECTED_MIN_GRADE + "EXPECTED MIN GRADE "
            + PREFIX_EXPECTED_MAX_GRADE + "EXPECTED MAX GRADE "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_MODULE_INFO_CODE + "CS2103T "
            + PREFIX_SEMESTER + "Y3S1 "
            + PREFIX_EXPECTED_MIN_GRADE + "D "
            + PREFIX_EXPECTED_MAX_GRADE + "A "
            + PREFIX_TAG + "Software "
            + PREFIX_TAG + "OOP";

    public static final String MESSAGE_SUCCESS = "New moduleTaken added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This moduleTaken already exists in the address book";
    public static final String MESSAGE_PREREQ_MISSING = "Prerequisites not satisfied";

    private final ModuleTaken toAdd;

    /**
     * Creates an AddCommand to add the specified {@code ModuleTaken}
     */
    public AddCommand(ModuleTaken moduleTaken) {
        requireNonNull(moduleTaken);
        toAdd = moduleTaken;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);

        ModuleInfoList moduleInfoList = model.getModuleInfoList();
        ModuleInfo moduleInfo = moduleInfoList.getModule(toAdd.getModuleInfoCode().value);

        if (!moduleInfoList.isEmpty()) {
            if (moduleInfo == null) {
                throw new CommandException(Messages.MESSAGE_MODULE_DOES_NOT_EXIST);
            }

        }

        if (model.hasModuleTaken(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        if (!model.isEligibleModuleTaken(toAdd)) {
            throw new CommandException(MESSAGE_PREREQ_MISSING);
        }

        if (toAdd.getExpectedMinGrade().getGradePoint()
                > toAdd.getExpectedMaxGrade().getGradePoint()) {
            throw new CommandException(Messages.MESSAGE_GRADES_OUT_OF_ORDER);
        }

        if (!toAdd.getExpectedMaxGrade().isCountedInCap()) {
            throw new CommandException(Messages.MESSAGE_MAX_GRADE_MUST_BE_COUNTED);
        }

        if (!toAdd.getExpectedMinGrade().equals(toAdd.getExpectedMaxGrade())
                && toAdd.getSemester().getIndex() < model.getCurrentSemester().getIndex()) {
            throw new CommandException(Messages.MESSAGE_GRADES_NOT_FINALIZED_BEFORE_SEMESTER);
        }

        if (moduleInfo != null) {
            toAdd.setWorkload(new Workload(moduleInfo.getModuleInfoWorkload()));
        }

        model.addModuleTaken(toAdd);
        model.commitGradTrak();
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddCommand // instanceof handles nulls
                && toAdd.equals(((AddCommand) other).toAdd));
    }
}
