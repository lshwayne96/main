package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.commons.core.Messages.MESSAGE_MODULETAKEN_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.FINISHED_STATUS_FALSE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MIN_GRADE_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_INFO_CODE_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_INFO_CODE_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SEMESTER_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.parser.ParserUtil.FINISHED_STATUS_TRUE;
import static seedu.address.testutil.TypicalModuleTaken.CS1010S;
import static seedu.address.testutil.TypicalModuleTaken.CS1010X;
import static seedu.address.testutil.TypicalModuleTaken.GER1000;
import static seedu.address.testutil.TypicalModuleTaken.KEYWORD_MATCHING_CS;
import static seedu.address.testutil.TypicalModuleTaken.KEYWORD_MATCHING_CS1010S;
import static seedu.address.testutil.TypicalModuleTaken.KEYWORD_MATCHING_CS2103T;
import static seedu.address.testutil.TypicalModuleTaken.MA1521;
import static seedu.address.testutil.TypicalModuleTaken.getTypicalGradTrak;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.FindCommand.FindModuleDescriptor;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.FindModulePredicate;
import seedu.address.model.moduletaken.Semester;
import seedu.address.testutil.FindModuleDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {

    private Model model = new ModelManager(getTypicalGradTrak(), new UserPrefs(),
            new ModuleInfoList(), new CourseList(), new UserInfo());
    private Model expectedModel = new ModelManager(getTypicalGradTrak(), new UserPrefs(),
            new ModuleInfoList(), new CourseList(), new UserInfo());
    private CommandHistory commandHistory = new CommandHistory();

    /* Default current semester is Y1S1 */
    @Test
    public void equals() {
        FindModuleDescriptor fd1 = new FindModuleDescriptorBuilder().withCode(VALID_MODULE_INFO_CODE_CS2103T).build();
        FindModuleDescriptor fd2 = new FindModuleDescriptorBuilder().withCode(VALID_MODULE_INFO_CODE_CS1010).build();
        FindModuleDescriptor fd3 = new FindModuleDescriptorBuilder().withCode(VALID_MODULE_INFO_CODE_CS2103T)
                .withSemester(VALID_SEMESTER_CS2103T).withGrade(VALID_EXPECTED_MIN_GRADE_CS2103T).build();
        FindModuleDescriptor fd4 = new FindModuleDescriptorBuilder().withCode("CS").build();
        FindModuleDescriptor fd5 = new FindModuleDescriptorBuilder().withCode("cs").build();

        FindCommand fc1 = new FindCommand(fd1);
        FindCommand fc2 = new FindCommand(fd2);
        FindCommand fc3 = new FindCommand(fd3);
        FindCommand fc4 = new FindCommand(fd4);
        FindCommand fc5 = new FindCommand(fd5);

        // same object -> returns true
        assertTrue(fc1.equals(fc1));

        // same values -> returns true
        FindCommand fc1Copy = new FindCommand(fd1);
        assertTrue(fc1.equals(fc1Copy));

        // same values (ignoring case) -> returns true
        assertTrue(fc4.equals(fc5));

        // different types -> returns false
        assertFalse(fc1.equals(1));

        // null -> returns false
        assertFalse(fc1.equals(null));

        // different code -> returns false
        assertFalse(fc1.equals(fc2));

        // different number of parameters -> returns false
        assertFalse(fc1.equals(fc3));
    }

    @Test
    public void execute_nonExistingCode_noModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 0);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withCode("2103cs").build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_codeOnly_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 2);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withCode(KEYWORD_MATCHING_CS).build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());
        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS1010S, CS1010X), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_semesterOnly_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 2);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withSemester("Y5S2").build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS1010S, CS1010X), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_gradeOnly_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 2);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withGrade("F").build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS1010S, CS1010X), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_finishedStatusOnly_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 2);
        model.setCurrentSemester(Semester.Y4S2);
        expectedModel.setCurrentSemester(Semester.Y4S2);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder()
                .withFinishedStatus(FINISHED_STATUS_TRUE).build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(MA1521, GER1000), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_multipleParameters_noModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 0);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withCode(KEYWORD_MATCHING_CS2103T)
                .withSemester("Y1S1").withGrade("A").withFinishedStatus(FINISHED_STATUS_TRUE).build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_multipleParameters_multipleModulesFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 2);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withSemester("Y5S2")
                .withGrade("D").withFinishedStatus(FINISHED_STATUS_FALSE).build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);
        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS1010S, CS1010X), model.getFilteredModulesTakenList());
    }

    @Test
    public void execute_multipleParameters_oneModuleFound() {
        String expectedMessage = String.format(MESSAGE_MODULETAKEN_LISTED_OVERVIEW, 1);

        FindModuleDescriptor descriptor = new FindModuleDescriptorBuilder().withCode(KEYWORD_MATCHING_CS1010S)
                .withSemester("Y5S2").withFinishedStatus(FINISHED_STATUS_FALSE).build();
        FindModulePredicate predicate = new FindModulePredicate(descriptor, model.getCurrentSemester());

        FindCommand command = new FindCommand(descriptor);
        expectedModel.updateFilteredModulesTakenList(predicate);

        assertCommandSuccess(command, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CS1010S), model.getFilteredModulesTakenList());
    }
}
