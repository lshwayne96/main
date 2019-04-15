package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MAX_GRADE_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalModuleTaken.CS2103T;
import static seedu.address.testutil.TypicalModuleTaken.getTypicalGradTrak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ModuleTree;
import seedu.address.model.limits.SemesterLimit;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.moduletaken.exceptions.DuplicateModuleTakenException;
import seedu.address.testutil.ModuleTakenBuilder;

public class GradTrakTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final GradTrak gradTrak = new GradTrak();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), gradTrak.getModulesTakenList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        gradTrak.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        GradTrak newData = getTypicalGradTrak();
        gradTrak.resetData(newData);
        assertEquals(newData, gradTrak);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two modulesTaken with the same identity fields
        ModuleTaken editedAlice = new ModuleTakenBuilder(CS2103T)
                .withExpectedMaxGrade(VALID_EXPECTED_MAX_GRADE_CS1010)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        List<ModuleTaken> newModuleTakens = Arrays.asList(CS2103T, editedAlice);
        GradTrakStub newData = new GradTrakStub(newModuleTakens);

        thrown.expect(DuplicateModuleTakenException.class);
        gradTrak.resetData(newData);
    }

    @Test
    public void hasModuleTaken_nullModuleTaken_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        gradTrak.hasModuleTaken(null);
    }

    @Test
    public void hasModuleTaken_moduleTakenNotInGradTrak_returnsFalse() {
        assertFalse(gradTrak.hasModuleTaken(CS2103T));
    }

    @Test
    public void hasModuleTaken_moduleTakenInGradTrak_returnsTrue() {
        gradTrak.addModuleTaken(CS2103T);
        assertTrue(gradTrak.hasModuleTaken(CS2103T));
    }

    @Test
    public void hasModuleTaken_moduleTakenWithSameField_returnsTrue() {
        gradTrak.addModuleTaken(CS2103T);
        ModuleTaken editedAlice = new ModuleTakenBuilder(CS2103T)
                .withExpectedMaxGrade(VALID_EXPECTED_MAX_GRADE_CS1010)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(gradTrak.hasModuleTaken(editedAlice));
    }

    @Test
    public void getModuleTakenList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        gradTrak.getModulesTakenList().remove(0);
    }

    @Test
    public void addListener_withInvalidationListener_listenerAdded() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        gradTrak.addListener(listener);
        gradTrak.addModuleTaken(CS2103T);
        assertEquals(1, counter.get());
    }

    @Test
    public void removeListener_withInvalidationListener_listenerRemoved() {
        SimpleIntegerProperty counter = new SimpleIntegerProperty();
        InvalidationListener listener = observable -> counter.set(counter.get() + 1);
        gradTrak.addListener(listener);
        gradTrak.removeListener(listener);
        gradTrak.addModuleTaken(CS2103T);
        assertEquals(0, counter.get());
    }

    /**
     * A stub ReadOnlyGradTrak whose modulesTaken list can violate interface constraints.
     */
    private static class GradTrakStub implements ReadOnlyGradTrak {
        private final ObservableList<ModuleTaken> modulesTaken = FXCollections.observableArrayList();

        GradTrakStub(Collection<ModuleTaken> modulesTaken) {
            this.modulesTaken.setAll(modulesTaken);
        }

        @Override
        public ObservableList<ModuleTaken> getModulesTakenList() {
            return modulesTaken;
        }

        @Override
        public ObservableList<SemesterLimit> getSemesterLimitList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Semester getCurrentSemester() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void removeListener(InvalidationListener listener) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public List<ModuleInfoCode> getNonFailedCodeList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ArrayList<String> getMissingPrerequisites(ModuleTree moduleTree) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ArrayList<String> getMissingPrerequisitesWithoutModule(ModuleTree moduleTree, ModuleTaken moduleTaken) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ArrayList<String> getMissingPrerequisitesWithEditedModule(ModuleTree moduleTree,
                                                                         ModuleTaken moduleTakenToEdit,
                                                                         ModuleTaken editedModuleTaken) {
            throw new AssertionError("This method should not be called.");
        }
    }
}
