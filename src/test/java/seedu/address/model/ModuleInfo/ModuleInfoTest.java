package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.testutil.ModuleInfoBuilder;

public class ModuleInfoTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void getter_string_methods() {
        ModuleInfo typicalModuleInfo = new ModuleInfoBuilder().build();

        assertEquals("CS2103T", typicalModuleInfo.getCodeString());
        assertEquals("Software Engineering", typicalModuleInfo.getTitleString());
        assertEquals(4.0, typicalModuleInfo.getCredits(), 0);
        assertEquals("Computer Science", typicalModuleInfo.getDepartmentString());
        assertEquals("Standard sentence for description of this module.", typicalModuleInfo.getDescriptionString());
        assertEquals("1-2-3-4-5", typicalModuleInfo.getWorkloadString());
        assertEquals("(CS1020 or CS1020E or CS2020) or (CS2030 and (CS2040 or CS2040C))",
                typicalModuleInfo.getPrerequisitesString());

    }

    @Test
    public void equals() {
        // same values -> returns true
        ModuleInfo typicalModule = new ModuleInfoBuilder().build();

        ModuleInfo typicalModuleCopy = new ModuleInfoBuilder(typicalModule).build();


        // same object -> returns true
        assertTrue(typicalModule.equals(typicalModule));

        // null -> returns false
        assertFalse(typicalModule.equals(null));

        // different type -> returns false
        assertFalse(typicalModule.equals(5));

        // different moduleTaken -> returns false
        ModuleInfo differentModule = new ModuleInfo("CS1010", "Programming Methodology", 4.0,
                "A totally different String sentence.", "1-1-1-1-1", "None",
                "Computing", "No prerequisites.");
        assertFalse(typicalModule.equals(differentModule));

        // different name -> returns false
        ModuleInfo editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoCode("CS1010").build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different semester -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoTitle("Programming Methodology").build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different email -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoCredits(1.0).build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different address -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoDeparment("School of Computing").build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different tags -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoDescription("A totally different String sentence.").build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different tags -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoWorkload("0-0-0-0-0").build();
        assertFalse(typicalModule.equals(editedTypicalModule));

        // different tags -> returns false
        editedTypicalModule = new ModuleInfoBuilder(typicalModule)
                .withModuleInfoPrerquisite("No prerquisites", typicalModule.getCodeString()).build();
        assertFalse(typicalModule.equals(editedTypicalModule));
    }
}
