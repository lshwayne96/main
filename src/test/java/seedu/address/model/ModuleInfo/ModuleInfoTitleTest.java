package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.address.model.moduleinfo.ModuleInfoTitle;
import seedu.address.testutil.Assert;

public class ModuleInfoTitleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new ModuleInfoTitle(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new ModuleInfoTitle(invalidName));
    }

    @Test
    public void isValidModuleInfoTitle() {
        // null name
        Assert.assertThrows(NullPointerException.class, () -> ModuleInfoTitle.isValidModuleInfoTitle(null));

        //Invalid Title
        assertFalse(ModuleInfoTitle.isValidModuleInfoTitle("")); //empty String
        assertFalse(ModuleInfoTitle.isValidModuleInfoTitle(" ")); //whitespace
        assertFalse(ModuleInfoTitle.isValidModuleInfoTitle(" word")); //whitespace followed by word

        //Valid Title
        assertTrue(ModuleInfoTitle.isValidModuleInfoTitle("Title")); //Single Word
        assertTrue(ModuleInfoTitle.isValidModuleInfoTitle("Title Title Tile")); //Multiple Words
        assertTrue(ModuleInfoTitle.isValidModuleInfoTitle("TITLE")); //All in upper case
        assertTrue(ModuleInfoTitle.isValidModuleInfoTitle("title")); //all in lower case

    }

    @Test
    public void toStringTest() {
        ModuleInfoTitle title = new ModuleInfoTitle("Software Engineering");
        assertEquals("Software Engineering", title.toString());
    }

    @Test
    public void equals() {
        ModuleInfoTitle descrip = new ModuleInfoTitle("Software Engineering");
        ModuleInfoTitle descripCopy = descrip;

        assertTrue(descrip.equals(descripCopy));

        ModuleInfoTitle diffdescrip = new ModuleInfoTitle("Programming methodology");

        assertFalse(descrip.equals(diffdescrip));
    }

    @Test
    public void hashcode() {
        ModuleInfoTitle descrip = new ModuleInfoTitle("Software Engineering");
        ModuleInfoTitle descripCopy = descrip;

        assertEquals(descrip.hashCode(), descripCopy.hashCode());
    }

}
