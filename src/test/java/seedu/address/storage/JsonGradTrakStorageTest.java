package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.address.testutil.TypicalModuleTaken.CS1010X;
import static seedu.address.testutil.TypicalModuleTaken.CS2030;
import static seedu.address.testutil.TypicalModuleTaken.CS2040;
import static seedu.address.testutil.TypicalModuleTaken.getTypicalGradTrak;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.GradTrak;
import seedu.address.model.ReadOnlyGradTrak;

public class JsonGradTrakStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonGradTrakStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readGradTrak_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readGradTrak(null);
    }

    private java.util.Optional<ReadOnlyGradTrak> readGradTrak(String filePath) throws Exception {
        return new JsonGradTrakStorage(Paths.get(filePath)).readGradTrak(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readGradTrak("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readGradTrak("notJsonFormat.json");

        // IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
        // That means you should not have more than one exception test in one method
    }

    @Test
    public void readAddressBook_invalidPersonAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readGradTrak("ivalidModuleTaken.json");
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readGradTrak("invalidAndValidModulesTaken.json");
    }

    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempAddressBook.json");
        GradTrak original = getTypicalGradTrak();
        JsonGradTrakStorage jsonAddressBookStorage = new JsonGradTrakStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveGradTrak(original, filePath);
        ReadOnlyGradTrak readBack = jsonAddressBookStorage.readGradTrak(filePath).get();
        assertEquals(original, new GradTrak(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addModuleTaken(CS2030);
        original.removeModuleTaken(CS1010X);
        jsonAddressBookStorage.saveGradTrak(original, filePath);
        readBack = jsonAddressBookStorage.readGradTrak(filePath).get();
        assertEquals(original, new GradTrak(readBack));

        // Save and read without specifying file path
        original.addModuleTaken(CS2040);
        jsonAddressBookStorage.saveGradTrak(original); // file path not specified
        readBack = jsonAddressBookStorage.readGradTrak().get(); // file path not specified
        assertEquals(original, new GradTrak(readBack));

    }

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(null, "SomeFile.json");
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyGradTrak addressBook, String filePath) {
        try {
            new JsonGradTrakStorage(Paths.get(filePath))
                    .saveGradTrak(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        saveAddressBook(new GradTrak(), null);
    }
}
