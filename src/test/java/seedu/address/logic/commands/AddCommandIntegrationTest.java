package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalModuleTaken.getTypicalGradTrak;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.testutil.ModuleTakenBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddCommand}.
 */
public class AddCommandIntegrationTest {

    private Model model;
    private CommandHistory commandHistory = new CommandHistory();

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalGradTrak(), new UserPrefs(), new ModuleInfoList(),
                new CourseList(), new UserInfo());
    }

    @Test
    public void execute_newPerson_success() {
        ModuleTaken validModuleTaken = new ModuleTakenBuilder().withModuleInfoCode("CS1010").build();

        Model expectedModel = new ModelManager(model.getGradTrak(), new UserPrefs(),
                new ModuleInfoList(), new CourseList(), new UserInfo());
        expectedModel.addModuleTaken(validModuleTaken);
        expectedModel.commitGradTrak();

        assertCommandSuccess(new AddCommand(validModuleTaken), model, commandHistory,
                String.format(AddCommand.MESSAGE_SUCCESS, validModuleTaken), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        ModuleTaken moduleTakenInList = model.getGradTrak().getModulesTakenList().get(0);
        assertCommandFailure(new AddCommand(moduleTakenInList), model, commandHistory,
                AddCommand.MESSAGE_DUPLICATE_PERSON);
    }

}
