package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.moduleinfo.ModuleInfoWorkload;
import seedu.address.testutil.Assert;

public class ModuleInfoWorkloadTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ModuleInfoWorkload(null));
    }

    @Test
    public void isValidModuleInfoWorkLoad() {
        // null Workload
        Assert.assertThrows(NullPointerException.class, () -> ModuleInfoWorkload.isValidModuleInfoWorkload(null));

        //Invalid ModuleInfoWorkload
        assertFalse(ModuleInfoWorkload.isValidModuleInfoWorkload("")); //empty String
        assertFalse(ModuleInfoWorkload.isValidModuleInfoWorkload(" ")); //only whitespace
        assertFalse(ModuleInfoWorkload.isValidModuleInfoWorkload("words")); //only words
        assertFalse(ModuleInfoWorkload.isValidModuleInfoWorkload("11231")); //only numbers
        assertFalse(ModuleInfoWorkload.isValidModuleInfoWorkload("1-2-3")); //incomplete workload data

        //Valid ModuleInfoWorkload
        assertTrue(ModuleInfoWorkload.isValidModuleInfoWorkload("1-2-3-4-5")); //Only integers
        assertTrue(ModuleInfoWorkload.isValidModuleInfoWorkload("1.0-2.0-3.1-4.7-5.5")); //Only double
        assertTrue(ModuleInfoWorkload.isValidModuleInfoWorkload("0-0-0-0-0")); //All Zeros
        assertTrue(ModuleInfoWorkload.isValidModuleInfoWorkload("1.0-1.0-2.0-4-5")); //Mix of int and double
    }

    @Test
    public void toStringTest() {
        ModuleInfoWorkload workload = new ModuleInfoWorkload("1-2-3-4-5");
        assertEquals("1-2-3-4-5", workload.toString());
    }

    @Test
    public void getter_methods_test() {
        ModuleInfoWorkload workload = new ModuleInfoWorkload("1-2-3-4-5");

        assertEquals(1.0, workload.getLecture(), 0);
        assertEquals(2.0, workload.getTutorial(), 0);
        assertEquals(3.0, workload.getLab(), 0);
        assertEquals(4.0, workload.getProject(), 0);
        assertEquals(5.0, workload.getPreparation(), 0);
    }

    @Test
    public void splitWorkload() {
        ModuleInfoWorkload workload = new ModuleInfoWorkload("1-2-3-4-5");

        String[] testArray = {"1", "2", "3", "4", "5"};

        assertArrayEquals(testArray, workload.splitWorkload(workload.toString()));
    }

    @Test
    public void equals() {
        ModuleInfoWorkload descrip = new ModuleInfoWorkload("5-4-3-2-1");
        ModuleInfoWorkload descripCopy = descrip;

        assertTrue(descrip.equals(descripCopy));

        ModuleInfoWorkload diffdescrip = new ModuleInfoWorkload("1-1-1-1-1");

        assertFalse(descrip.equals(diffdescrip));
    }

    @Test
    public void hashcode() {
        ModuleInfoWorkload descrip = new ModuleInfoWorkload("5-4-3-2-1");
        ModuleInfoWorkload descripCopy = descrip;

        assertEquals(descrip.hashCode(), descripCopy.hashCode());
    }

}
