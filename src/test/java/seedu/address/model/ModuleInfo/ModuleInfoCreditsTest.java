package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.moduleinfo.ModuleInfoCredits;
import seedu.address.testutil.Assert;

public class ModuleInfoCreditsTest {
    @Test
    public void constructor_double_throwsIllegalArgumentException() {
        double credits = 200.0; // not a proper value for credits
        Assert.assertThrows(IllegalArgumentException.class, () -> new ModuleInfoCredits(credits));
    }

    @Test
    public void isValidModuleInfoCredits() {
        //invalid credit values
        assertFalse(ModuleInfoCredits.isValidModuleInfoCredits(-1.0));
        assertFalse(ModuleInfoCredits.isValidModuleInfoCredits(-100.0));
        assertFalse(ModuleInfoCredits.isValidModuleInfoCredits(41));
        assertFalse(ModuleInfoCredits.isValidModuleInfoCredits(400));

        //valid credit values
        assertTrue(ModuleInfoCredits.isValidModuleInfoCredits(0));
        assertTrue(ModuleInfoCredits.isValidModuleInfoCredits(40));
        assertTrue(ModuleInfoCredits.isValidModuleInfoCredits(10));
        assertTrue(ModuleInfoCredits.isValidModuleInfoCredits(2.5));
    }

    @Test
    public void toStringTest() {
        ModuleInfoCredits title = new ModuleInfoCredits(4.0);
        assertEquals("4.0", title.toString());
    }

    @Test
    public void equals() {
        ModuleInfoCredits credits = new ModuleInfoCredits(4.0);
        ModuleInfoCredits creditsCopy = credits;

        assertTrue(credits.equals(creditsCopy));

        ModuleInfoCredits diffCredits = new ModuleInfoCredits(1.0);

        assertFalse(credits.equals(diffCredits));
    }

    @Test
    public void hashcode() {
        ModuleInfoCredits credits = new ModuleInfoCredits(4.0);
        ModuleInfoCredits creditsCopy = credits;

        assertEquals(credits.hashCode(), creditsCopy.hashCode());
    }

}
