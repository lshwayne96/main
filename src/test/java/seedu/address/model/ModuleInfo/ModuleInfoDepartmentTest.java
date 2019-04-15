package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.moduleinfo.ModuleInfoDepartment;
import seedu.address.testutil.Assert;

public class ModuleInfoDepartmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ModuleInfoDepartment(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidDepartment = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new ModuleInfoDepartment(invalidDepartment));
    }

    @Test
    public void isValidModuleInfoDepartment() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> ModuleInfoDepartment.isValidModuleInfoDepartment(null));

        //Invalid Department Names
        assertFalse(ModuleInfoDepartment.isValidModuleInfoDepartment("")); //empty String
        assertFalse(ModuleInfoDepartment.isValidModuleInfoDepartment(" ")); //only whitespaces
        assertFalse(ModuleInfoDepartment.isValidModuleInfoDepartment("111122323")); //only numbers
        assertFalse(ModuleInfoDepartment.isValidModuleInfoDepartment(" Department")); //starts with whitespace

        //Valid Deparment Names
        assertTrue(ModuleInfoDepartment.isValidModuleInfoDepartment("Mathematics")); //case insensitive
        assertTrue(ModuleInfoDepartment.isValidModuleInfoDepartment("MATHEMATHICS")); //only upper cases
        assertTrue(ModuleInfoDepartment.isValidModuleInfoDepartment("mathemathics")); //only lower case
        assertTrue(ModuleInfoDepartment.isValidModuleInfoDepartment("Department of Socioloagy")); //multiple words

    }

    @Test
    public void toStringTest() {
        ModuleInfoDepartment dept = new ModuleInfoDepartment("Mathematics");
        assertEquals("Mathematics", dept.toString());
    }

    @Test
    public void equals() {
        ModuleInfoDepartment depts = new ModuleInfoDepartment("Mathematics");
        ModuleInfoDepartment deptsCopy = depts;

        assertTrue(depts.equals(deptsCopy));

        ModuleInfoDepartment diffdepts = new ModuleInfoDepartment("Science");

        assertFalse(depts.equals(diffdepts));
    }

    @Test
    public void hashcode() {
        ModuleInfoDepartment depts = new ModuleInfoDepartment("Mathematics");
        ModuleInfoDepartment deptsCopy = depts;

        assertEquals(depts.hashCode(), deptsCopy.hashCode());
    }
}
