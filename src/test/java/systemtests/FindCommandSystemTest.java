package systemtests;

import static seedu.address.commons.core.Messages.MESSAGE_MODULETAKEN_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.FINISHED_STATUS_FALSE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_FINISHED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_INFO_CODE;

import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.testutil.TypicalModuleTaken.CS1010S;
import static seedu.address.testutil.TypicalModuleTaken.CS1010X;
import static seedu.address.testutil.TypicalModuleTaken.CS2101;
import static seedu.address.testutil.TypicalModuleTaken.CS2103T;
import static seedu.address.testutil.TypicalModuleTaken.GER1000;
import static seedu.address.testutil.TypicalModuleTaken.KEYWORD_MATCHING_CS2103T;
import static seedu.address.testutil.TypicalModuleTaken.LSM1301;
import static seedu.address.testutil.TypicalModuleTaken.MA1521;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.logic.commands.FindCommand;
import seedu.address.model.Model;

public class FindCommandSystemTest extends GradTrakSystemTest {

    /* Default current semester is Y1S1 */
    @Test
    public void find_codeOnly() {
        Model expectedModel = getModel();

        // Case: find moduleTaken in GradTrak by code, command with leading spaces and trailing spaces -> 1 found
        String command = "   " + FindCommand.COMMAND_WORD + " "
                + PREFIX_MODULE_INFO_CODE + KEYWORD_MATCHING_CS2103T + "   ";
        ModelHelper.setFilteredList(expectedModel, CS2103T);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        // Case: repeat previous find command -> 1 found
        command = FindCommand.COMMAND_WORD + " " + PREFIX_MODULE_INFO_CODE + KEYWORD_MATCHING_CS2103T;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        // Case: find non-existing ModuleInfoCode in GradTrak -> 0 found
        command = FindCommand.COMMAND_WORD + " " + PREFIX_MODULE_INFO_CODE + "abcdefg";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
    }

    @Test
    public void find_semesterOnly() {
        Model expectedModel = getModel();

        String command = FindCommand.COMMAND_WORD + " " + PREFIX_SEMESTER + "Y4S2";
        ModelHelper.setFilteredList(expectedModel, LSM1301);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }

    @Test
    public void find_gradeOnly() {
        Model expectedModel = getModel();

        String command = FindCommand.COMMAND_WORD + " " + PREFIX_GRADE + "A";
        ModelHelper.setFilteredList(expectedModel, CS2103T, CS2101, CS1010S, CS1010X, MA1521, LSM1301);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }

    @Test
    public void find_finishedStatusOnly() {
        Model expectedModel = getModel();

        String command = FindCommand.COMMAND_WORD + " " + PREFIX_FINISHED + FINISHED_STATUS_FALSE;
        ModelHelper.setFilteredList(expectedModel, CS2103T, CS2101, CS1010S, CS1010X, MA1521, LSM1301, GER1000);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }

    @Test
    public void find_multipleParameters() {
        Model expectedModel = getModel();

        String command = FindCommand.COMMAND_WORD + " " + PREFIX_MODULE_INFO_CODE + "CS " + PREFIX_SEMESTER + "Y3S2 "
                + PREFIX_GRADE + "A " + PREFIX_FINISHED + FINISHED_STATUS_FALSE;
        ModelHelper.setFilteredList(expectedModel, CS2101);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();
    }

    @Test
    public void find_emptyArgs() {
        String command = FindCommand.COMMAND_WORD;
        assertCommandFailure(command,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_MODULETAKEN_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code GradTrakSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see GradTrakSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) {
        String expectedResultMessage = String.format(
                MESSAGE_MODULETAKEN_LISTED_OVERVIEW, expectedModel.getFilteredModulesTakenList().size());
        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code GradTrakSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see GradTrakSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
