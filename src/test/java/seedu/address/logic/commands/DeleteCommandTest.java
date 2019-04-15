package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalModuleTaken.getTypicalGradTrak;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.storage.moduleinfostorage.ModuleInfoManager;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();
    private ModuleInfoList moduleInfoList;

    @Before
    public void setUp() {
        ModuleInfoManager moduleInfoManager = new ModuleInfoManager();
        Optional<ModuleInfoList> list = Optional.empty();
        try {
            list = moduleInfoManager.readModuleInfoFile();
        } catch (DataConversionException dce) {
            System.err.println("Error reading json");
        }
        assert (list.isPresent());
        moduleInfoList = list.get();

        model = new ModelManager(getTypicalGradTrak(), new UserPrefs(),
                moduleInfoList, new CourseList(), new UserInfo());
    }

    @Test
    public void execute_validIndexUnfilteredList_success() {
        ModuleTaken moduleTakenToDelete = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, moduleTakenToDelete);

        ModelManager expectedModel = new ModelManager(model.getGradTrak(), new UserPrefs(),
                moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.deleteModuleTaken(moduleTakenToDelete);
        expectedModel.commitGradTrak();

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModulesTakenList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ModuleTaken moduleTakenToDelete = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS, moduleTakenToDelete);

        Model expectedModel = new ModelManager(model.getGradTrak(), new UserPrefs(),
                moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.deleteModuleTaken(moduleTakenToDelete);
        expectedModel.commitGradTrak();
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getGradTrak().getModulesTakenList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        ModuleTaken moduleTakenToDelete = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getGradTrak(), new UserPrefs(),
                moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.deleteModuleTaken(moduleTakenToDelete);
        expectedModel.commitGradTrak();

        // delete -> first moduleTaken deleted
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered moduleTaken list to show all persons
        expectedModel.undoGradTrak();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first moduleTaken deleted again
        expectedModel.redoGradTrak();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModulesTakenList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        // execution failed -> address book state not added into model
        assertCommandFailure(deleteCommand, model, commandHistory,
                Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Deletes a {@code ModuleTaken} from a filtered list.
     * 2. Undo the deletion.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously deleted moduleTaken in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the deletion. This ensures {@code RedoCommand} deletes the moduleTaken object regardless of indexing.
     */
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonDeleted() throws Exception {
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        Model expectedModel = new ModelManager(model.getGradTrak(), new UserPrefs(),
                moduleInfoList, new CourseList(), new UserInfo());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        ModuleTaken moduleTakenToDelete = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.deleteModuleTaken(moduleTakenToDelete);
        expectedModel.commitGradTrak();

        // delete -> deletes second moduleTaken in unfiltered moduleTaken list / first moduleTaken in
        // filtered moduleTaken list
        deleteCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered moduleTaken list to show all persons
        expectedModel.undoGradTrak();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(moduleTakenToDelete,
                model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased()));
        // redo -> deletes same second moduleTaken in unfiltered moduleTaken list
        expectedModel.redoGradTrak();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different moduleTaken -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredModulesTakenList(p -> false);

        assertTrue(model.getFilteredModulesTakenList().isEmpty());
    }
}
