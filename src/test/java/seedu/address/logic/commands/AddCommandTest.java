package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.ClassForPrinting;
import seedu.address.model.EligibleModulePredicate;
import seedu.address.model.GradTrak;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyGradTrak;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserInfo;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseName;
import seedu.address.model.course.RequirementStatus;
import seedu.address.model.limits.SemesterLimit;

import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.recmodule.RecModule;
import seedu.address.storage.moduleinfostorage.ModuleInfoManager;
import seedu.address.testutil.ModuleTakenBuilder;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        ModuleTaken validModuleTaken = new ModuleTakenBuilder().build();

        CommandResult commandResult = new AddCommand(validModuleTaken).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validModuleTaken), commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validModuleTaken), modelStub.personsAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() throws Exception {
        ModuleTaken validModuleTaken = new ModuleTakenBuilder().build();
        AddCommand addCommand = new AddCommand(validModuleTaken);
        ModelStub modelStub = new ModelStubWithPerson(validModuleTaken);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_PERSON);
        addCommand.execute(modelStub, commandHistory);
    }

    @Test
    public void equals() {
        ModuleTaken cs2103t = new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build();
        ModuleTaken cs1010 = new ModuleTakenBuilder().withModuleInfoCode("CS1010").build();
        AddCommand addCS2103T = new AddCommand(cs2103t);
        AddCommand addCS1010 = new AddCommand(cs1010);

        // same object -> returns true
        assertTrue(addCS2103T.equals(addCS2103T));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(cs2103t);
        assertTrue(addCS2103T.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addCS2103T.equals(1));

        // null -> returns false
        assertFalse(addCS2103T.equals(null));

        // different moduleTaken -> returns false
        assertFalse(addCS2103T.equals(addCS1010));
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        private final ModuleInfoList moduleInfoList;

        ModelStub() {
            ModuleInfoManager moduleInfoManager = new ModuleInfoManager();
            Optional<ModuleInfoList> list = Optional.empty();
            try {
                list = moduleInfoManager.readModuleInfoFile();
            } catch (DataConversionException dce) {
                System.err.println("Error reading json");
            }
            assert (list.isPresent());
            moduleInfoList = list.get();
        }

        @Override
        public void setCourse(CourseName course) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasCourse(CourseName courseName) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Course getCourse() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGradTrakFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addModuleTaken(ModuleTaken moduleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGradTrak(ReadOnlyGradTrak newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getGradTrakFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Semester getCurrentSemester() {
            return Semester.Y1S1;
        }

        @Override
        public boolean hasModuleTaken(ModuleTaken moduleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean isEligibleModuleTaken(ModuleTaken moduleTaken) {
            requireNonNull(moduleTaken);
            ModuleInfo moduleInfo = moduleInfoList.getModule(moduleTaken.getModuleInfoCode().value);
            return new EligibleModulePredicate(new GradTrak()).test(moduleInfo);
        }

        @Override
        public boolean canEditModuleTaken(ModuleTaken moduleTakenToEdit, ModuleTaken editedModuleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canDeleteModuleTaken(ModuleTaken moduleTaken) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void deleteModuleTaken(ModuleTaken target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setModuleTaken(ModuleTaken target, ModuleTaken editedModuleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyGradTrak getGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSemesterLimit(int index, SemesterLimit editedSemesterLimit) {
            throw new AssertionError("This method should not be called.");
        }


        @Override
        public void setCurrentSemester(Semester semester) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<SemesterLimit> getSemesterLimitList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<ModuleTaken> getFilteredModulesTakenList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredModulesTakenList(Predicate<ModuleTaken> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ClassForPrinting checkLimit(ModuleInfoList moduleInfoList) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void undoGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void redoGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitGradTrak() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyProperty<ClassForPrinting> selectedClassForPrintingProperty() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ModuleTaken getSelectedClassForPrinting() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setSelectedClassForPrinting(ClassForPrinting moduleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<ModuleInfo> getDisplayList() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public void updateDisplayList(Predicate<ModuleInfo> predicate) {
            throw new AssertionError("This method should not be called");
        }

        @Override

        public void setSelectedModuleInfo (ModuleInfo moduleInfo) {
            throw new AssertionError("This method should not be called");
        }

        public ObservableList<RecModule> getRecModuleListSorted() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ReadOnlyProperty<ModuleInfo> selectedModuleInfoProperty() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ModuleInfoList getModuleInfoList() {
            return moduleInfoList;
        }

        public void updateRecModuleList() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ObservableList<RequirementStatus> getRequirementStatusList() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ModuleInfo getSelectedModuleInfo() {
            throw new AssertionError("This method should not be called");
        }


        @Override
        public UserInfo getUserInfo() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public ObservableList<ModuleInfoCode> getModuleInfoCodeList() {
            throw new AssertionError("This method should not be called");
        }

        @Override
        public void updateRequirementStatusList() {
            throw new AssertionError("This method should not be called");
        }
    }

    /**
     * A Model stub that contains a single moduleTaken.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final ModuleTaken moduleTaken;

        ModelStubWithPerson(ModuleTaken moduleTaken) {
            requireNonNull(moduleTaken);
            this.moduleTaken = moduleTaken;
        }

        @Override
        public boolean hasModuleTaken(ModuleTaken moduleTaken) {
            requireNonNull(moduleTaken);
            return this.moduleTaken.isSameModuleTaken(moduleTaken);
        }
    }

    /**
     * A Model stub that always accept the moduleTaken being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<ModuleTaken> personsAdded = new ArrayList<>();

        @Override
        public boolean hasModuleTaken(ModuleTaken moduleTaken) {
            requireNonNull(moduleTaken);
            return personsAdded.stream().anyMatch(moduleTaken::isSameModuleTaken);
        }

        @Override
        public void addModuleTaken(ModuleTaken moduleTaken) {
            requireNonNull(moduleTaken);
            personsAdded.add(moduleTaken);
        }

        @Override
        public void commitGradTrak() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyGradTrak getGradTrak() {
            return new GradTrak();
        }
    }
}
