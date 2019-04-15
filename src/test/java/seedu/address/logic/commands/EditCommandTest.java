package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.DESC_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_INFO_CODE_CS1010;
//import static seedu.address.logic.commands.CommandTestUtil.VALID_SEMESTER_CS1010;
//import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
//import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
//import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
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
import seedu.address.logic.commands.EditCommand.EditModuleTakenDescriptor;
//import seedu.address.model.GradTrak;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.storage.moduleinfostorage.ModuleInfoManager;
import seedu.address.testutil.EditModuleTakenDescriptorBuilder;
//import seedu.address.testutil.ModuleTakenBuilder;

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class EditCommandTest {

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

    /*
    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        ModuleTaken editedModuleTaken = new ModuleTakenBuilder().build();
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder(editedModuleTaken).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedModuleTaken);

        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.setModuleTaken(model.getFilteredModulesTakenList().get(0), editedModuleTaken);
        expectedModel.updateFilteredModulesTakenList(PREDICATE_SHOW_ALL_PERSONS);
        expectedModel.commitGradTrak();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredModulesTakenList().size());
        ModuleTaken lastModuleTaken = model.getFilteredModulesTakenList().get(indexLastPerson.getZeroBased());

        ModuleTakenBuilder personInList = new ModuleTakenBuilder(lastModuleTaken);
        ModuleTaken editedModuleTaken = personInList.withModuleInfoCode(VALID_MODULE_INFO_CODE_CS1010)
                .withSemester(VALID_SEMESTER_CS1010)
                .withTags(VALID_TAG_HUSBAND).build();

        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder()
                .withName(VALID_MODULE_INFO_CODE_CS1010)
                .withSemester(VALID_SEMESTER_CS1010).withTags(VALID_TAG_HUSBAND).build();
        EditCommand editCommand = new EditCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedModuleTaken);

        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.setModuleTaken(lastModuleTaken, editedModuleTaken);
        expectedModel.commitGradTrak();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }


    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditModuleTakenDescriptor());
        ModuleTaken editedModuleTaken = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedModuleTaken);

        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.commitGradTrak();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        ModuleTaken moduleTakenInFilteredList = model.getFilteredModulesTakenList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        ModuleTaken editedModuleTaken = new ModuleTakenBuilder(moduleTakenInFilteredList)
                .withModuleInfoCode(VALID_MODULE_INFO_CODE_CS1010).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditModuleTakenDescriptorBuilder().withName(VALID_MODULE_INFO_CODE_CS1010).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS, editedModuleTaken);

        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.setModuleTaken(model.getFilteredModulesTakenList().get(0), editedModuleTaken);
        expectedModel.commitGradTrak();

        assertCommandSuccess(editCommand, model, commandHistory, expectedMessage, expectedModel);
    }
    */

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        ModuleTaken firstModuleTaken = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder(firstModuleTaken).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit moduleTaken in filtered list into a duplicate in address book
        ModuleTaken moduleTakenInList = model.getGradTrak().getModulesTakenList()
                .get(INDEX_SECOND_PERSON.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON,
                new EditModuleTakenDescriptorBuilder(moduleTakenInList).build());

        assertCommandFailure(editCommand, model, commandHistory, EditCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModulesTakenList().size() + 1);
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder()
                .withName(VALID_MODULE_INFO_CODE_CS1010).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getGradTrak().getModulesTakenList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditModuleTakenDescriptorBuilder().withName(VALID_MODULE_INFO_CODE_CS1010).build());

        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
    }

    /*
    @Test
    public void executeUndoRedo_validIndexUnfilteredList_success() throws Exception {
        ModuleTaken editedModuleTaken = new ModuleTakenBuilder().build();
        ModuleTaken moduleTakenToEdit = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder(editedModuleTaken).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());
        expectedModel.setModuleTaken(moduleTakenToEdit, editedModuleTaken);
        expectedModel.commitGradTrak();

        // edit -> first moduleTaken edited
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered moduleTaken list to show all persons
        expectedModel.undoGradTrak();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        // redo -> same first moduleTaken edited again
        expectedModel.redoGradTrak();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
    */

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredModulesTakenList().size() + 1);
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder()
                .withName(VALID_MODULE_INFO_CODE_CS1010).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        // execution failed -> address book state not added into model
        assertCommandFailure(editCommand, model, commandHistory, Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);

        // single address book state in model -> undoCommand and redoCommand fail
        assertCommandFailure(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * 1. Edits a {@code ModuleTaken} from a filtered list.
     * 2. Undo the edit.
     * 3. The unfiltered list should be shown now. Verify that the index of the previously edited moduleTaken in the
     * unfiltered list is different from the index at the filtered list.
     * 4. Redo the edit. This ensures {@code RedoCommand} edits the moduleTaken object regardless of indexing.
     */
    /*
    @Test
    public void executeUndoRedo_validIndexFilteredList_samePersonEdited() throws Exception {
        ModuleTaken editedModuleTaken = new ModuleTakenBuilder().build();
        EditModuleTakenDescriptor descriptor = new EditModuleTakenDescriptorBuilder(editedModuleTaken).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, descriptor);
        Model expectedModel = new ModelManager(new GradTrak(model.getGradTrak()), new UserPrefs(),
                                               moduleInfoList, new CourseList(), new UserInfo());

        showPersonAtIndex(model, INDEX_SECOND_PERSON);
        ModuleTaken moduleTakenToEdit = model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased());
        expectedModel.setModuleTaken(moduleTakenToEdit, editedModuleTaken);
        expectedModel.commitGradTrak();

        // edit -> edits second moduleTaken in unfiltered moduleTaken list / first moduleTaken in
        // filtered moduleTaken list
        editCommand.execute(model, commandHistory);

        // undo -> reverts addressbook back to previous state and filtered moduleTaken list to show all persons
        expectedModel.undoGradTrak();
        assertCommandSuccess(new UndoCommand(), model, commandHistory, UndoCommand.MESSAGE_SUCCESS, expectedModel);

        assertNotEquals(model.getFilteredModulesTakenList().get(INDEX_FIRST_PERSON.getZeroBased()), moduleTakenToEdit);
        // redo -> edits same second moduleTaken in unfiltered moduleTaken list
        expectedModel.redoGradTrak();
        assertCommandSuccess(new RedoCommand(), model, commandHistory, RedoCommand.MESSAGE_SUCCESS, expectedModel);
    }
*/
    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_PERSON, DESC_CS2103T);

        // same values -> returns true
        EditModuleTakenDescriptor copyDescriptor = new EditModuleTakenDescriptor(DESC_CS2103T);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_PERSON, DESC_CS2103T)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_PERSON, DESC_CS1010)));
    }

}
