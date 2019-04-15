package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;
import seedu.address.commons.util.ModuleTree;
import seedu.address.model.limits.SemesterLimit;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.CapAverage;
import seedu.address.model.moduletaken.Grade;
import seedu.address.model.moduletaken.Hour;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.moduletaken.SemesterLimitList;
import seedu.address.model.moduletaken.UniqueModuleTakenList;
import seedu.address.model.tag.Tag;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameModuleTaken comparison)
 */
public class GradTrak implements ReadOnlyGradTrak {

    private static final int NUM_SEMS = 10;

    /* The following accounts for some modules with 'A' level prerequisites */
    private static final String A_LEVEL_REGEX =
            "CS1231|MA1521|MA1101R|PC1141|PC1142|PC1143|PC1144|PC1431|PC1432|"
            + "CM1121|CM1401|CM1402|CM1501|CM1502|"
            + "LSM1102|LSM1105|LSM1106|LSM1301|LSM1302|";

    private Semester currentSemester;
    private final SemesterLimitList semesterLimitList;
    private final UniqueModuleTakenList modulesTaken;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        modulesTaken = new UniqueModuleTakenList();
        semesterLimitList = new SemesterLimitList();
    }

    public GradTrak() {
        setSemesterLimits(getSampleSemesterLimits());
        setCurrentSemester(Semester.Y1S1);
    }

    /**
     * Creates an GradTrak using the ModulesTaken in the {@code toBeCopied}
     */
    public GradTrak(ReadOnlyGradTrak toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    /**
     * returns an initialized a gradtrak with default data
     */
    public static GradTrak initializeDefaults() {
        GradTrak defaultGradTrak = new GradTrak();
        defaultGradTrak.setModulesTaken(getSampleModulesTaken());
        defaultGradTrak.setSemesterLimits(getSampleSemesterLimits());
        defaultGradTrak.setCurrentSemester(Semester.Y1S1);
        return defaultGradTrak;
    }

    public static List<ModuleTaken> getSampleModulesTaken() {
        List<ModuleTaken> modulesTakenList = new ArrayList<>();
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("CS1010"), Semester.valueOf("Y1S1"),
                Grade.valueOf("B"), Grade.valueOf("B"), getTagSet("friends")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("CS2040"), Semester.valueOf("Y1S1"),
                Grade.valueOf("B_PLUS"), Grade.valueOf("B_PLUS"), getTagSet("programming", "friends")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("CS2030"), Semester.valueOf("Y1S2"),
                Grade.valueOf("B"), Grade.valueOf("B"), getTagSet("programming", "friends")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("MA1521"), Semester.valueOf("Y3S1"),
                Grade.valueOf("C"), Grade.valueOf("A"), getTagSet("math")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("CS2101"), Semester.valueOf("Y3S1"),
                Grade.valueOf("C"), Grade.valueOf("A"), getTagSet("communication")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("CS2103T"), Semester.valueOf("Y3S1"),
                Grade.valueOf("C"), Grade.valueOf("A"), getTagSet("programming")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("GER1000"), Semester.valueOf("Y3S2"),
                Grade.valueOf("C"), Grade.valueOf("A"), getTagSet("GEM")));
        modulesTakenList.add(new ModuleTaken(new ModuleInfoCode("LSM1301"), Semester.valueOf("Y4S1"),
                Grade.valueOf("C"), Grade.valueOf("A"), getTagSet("friends")));
        return modulesTakenList;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

    public static List<SemesterLimit> getSampleSemesterLimits() {
        List<SemesterLimit> semList = new ArrayList<>();
        for (int i = 0; i < NUM_SEMS; i++) {
            semList.add(new SemesterLimit(new CapAverage(2.0), new CapAverage(5.0), new Hour("5.0"), new Hour("9.0"),
                    new Hour("2.5"), new Hour("5.0"), new Hour("2.0"), new Hour("5.0"), new Hour("2.0"),
                    new Hour("5.0"), new Hour("6.0"), new Hour("10.0")));
        }
        return semList;
    }

    public Semester getCurrentSemester() {
        return currentSemester;
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the moduleTaken list with {@code ModuleTaken}.
     * {@code ModuleTaken} must not contain duplicate modulesTaken.
     */
    public void setModulesTaken(List<ModuleTaken> modulesTaken) {
        this.modulesTaken.setPersons(modulesTaken);
        indicateModified();
    }

    /**
     * Replaces the contents of the Semester Limit list with {@code semesterLimits}.
     */
    public void setSemesterLimits(List<SemesterLimit> semesterLimits) {
        this.semesterLimitList.setSemesterLimits(semesterLimits);
        indicateModified();
    }

    /**
     * Resets the existing data of this {@code GradTrak} with {@code newData}.
     */
    public void resetData(ReadOnlyGradTrak newData) {
        requireNonNull(newData);

        setModulesTaken(newData.getModulesTakenList());
        setSemesterLimits(newData.getSemesterLimitList());
        setCurrentSemester(newData.getCurrentSemester());
    }

    //// moduleTaken-level operations

    /**
     * Returns true if a {@code ModuleTaken} in GradTrak has the same identity as the given {@code ModuleTaken}.
     */
    public boolean hasModuleTaken(ModuleTaken moduleTaken) {
        requireNonNull(moduleTaken);
        return modulesTaken.contains(moduleTaken);
    }

    /**
     * Adds a moduleTaken to GradTrak.
     * The moduleTaken must not already exist in the GradTrak.
     */
    public void addModuleTaken(ModuleTaken p) {
        modulesTaken.add(p);
        indicateModified();
    }

    /**
     * Replaces the given moduleTaken {@code target} in the list with {@code editedModuleTaken}.
     * {@code target} must exist in the address book.
     * The moduleTaken identity of {@code editedModuleTaken} must not be the same as another
     * existing moduleTaken in the address book.
     */
    public void setModuleTaken(ModuleTaken target, ModuleTaken editedModuleTaken) {
        requireNonNull(editedModuleTaken);

        modulesTaken.setPerson(target, editedModuleTaken);
        indicateModified();
    }

    /**
     * Replaces the given index of semester limit with {@code editedSemesterLimit}.
     */
    public void setSemesterLimit(int index, SemesterLimit editedSemesterLimit) {
        requireNonNull(editedSemesterLimit);

        semesterLimitList.setSemesterLimit(index, editedSemesterLimit);
        indicateModified();
    }

    /**
     * Replaces the given index of semester limit with {@code editedSemesterLimit}.
     */
    public void setCurrentSemester(Semester semester) {
        requireNonNull(semester);

        this.currentSemester = semester;
        indicateModified();
    }

    /**
     * Removes {@code key} from this {@code GradTrak}.
     * {@code key} must exist in the address book.
     */
    public void removeModuleTaken(ModuleTaken key) {
        modulesTaken.remove(key);
        indicateModified();
    }

    @Override
    public List<ModuleInfoCode> getNonFailedCodeList() {
        List<ModuleInfoCode> codeList = new ArrayList<>();
        for (ModuleTaken moduleTaken : getModulesTakenList()) {
            if (!moduleTaken.isFailed(currentSemester)) {
                codeList.add(moduleTaken.getModuleInfoCode());
            }
        }

        return codeList;
    }

    @Override
    public ArrayList<String> getMissingPrerequisites(ModuleTree moduleTree) {
        ArrayList<String> codeStringList = new ArrayList<>();
        if (moduleTree.getModuleCode().matches(A_LEVEL_REGEX)) {
            return codeStringList;
        }
        for (ModuleInfoCode code : getNonFailedCodeList()) {
            codeStringList.add(code.toString());
        }

        return moduleTree.checkPrerequisites(codeStringList);
    }

    @Override
    public ArrayList<String> getMissingPrerequisitesWithoutModule(ModuleTree moduleTree, ModuleTaken moduleTaken) {
        ArrayList<String> codeStringList = new ArrayList<>();
        if (moduleTree.getModuleCode().matches(A_LEVEL_REGEX)) {
            return codeStringList;
        }
        for (ModuleInfoCode code : getNonFailedCodeList()) {
            if (!code.equals(moduleTaken.getModuleInfoCode())) {
                codeStringList.add(code.toString());
            }
        }

        return moduleTree.checkPrerequisites(codeStringList);
    }

    @Override
    public ArrayList<String> getMissingPrerequisitesWithEditedModule(ModuleTree moduleTree,
                                                                     ModuleTaken moduleTakenToEdit,
                                                                     ModuleTaken editedModuleTaken) {
        ArrayList<String> codeStringList = new ArrayList<>();
        if (moduleTree.getModuleCode().matches(A_LEVEL_REGEX)) {
            return codeStringList;
        }
        for (ModuleInfoCode code : getNonFailedCodeList()) {
            if (!code.equals(moduleTakenToEdit.getModuleInfoCode())) {
                codeStringList.add(code.toString());
            } else {
                codeStringList.add(editedModuleTaken.getModuleInfoCode().toString());
            }
        }

        return moduleTree.checkPrerequisites(codeStringList);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        invalidationListenerManager.addListener(listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        invalidationListenerManager.removeListener(listener);
    }

    /**
     * Notifies listeners that the address book has been modified.
     */
    protected void indicateModified() {
        invalidationListenerManager.callListeners(this);
    }

    //// util methods

    @Override
    public String toString() {
        return modulesTaken.asUnmodifiableObservableList().size() + " modulesTaken";
        // TODO: refine later
    }

    @Override
    public ObservableList<ModuleTaken> getModulesTakenList() {
        return modulesTaken.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<SemesterLimit> getSemesterLimitList() {
        return semesterLimitList.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof GradTrak // instanceof handles nulls
                && modulesTaken.equals(((GradTrak) other).modulesTaken));
    }

    @Override
    public int hashCode() {
        return modulesTaken.hashCode();
    }
}
