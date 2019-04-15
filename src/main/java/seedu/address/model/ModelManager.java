package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ModuleTree;
import seedu.address.model.course.Course;
import seedu.address.model.course.CourseList;
import seedu.address.model.course.CourseName;
import seedu.address.model.course.RequirementStatus;
import seedu.address.model.course.RequirementStatusList;
import seedu.address.model.limits.LimitChecker;
import seedu.address.model.limits.SemesterLimit;
import seedu.address.model.moduleinfo.CodeContainsKeywordsPredicate;

import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.moduletaken.exceptions.ModuleTakenNotFoundException;
import seedu.address.model.recmodule.RecModule;
import seedu.address.model.recmodule.RecModuleComparator;
import seedu.address.model.recmodule.RecModulePredicate;

/**
 * Represents the in-memory model of the GradTrak data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private Course course;

    private final VersionedGradTrak versionedGradTrak;
    private final UserPrefs userPrefs;
    private final FilteredList<ModuleTaken> filteredModulesTaken;
    private final SimpleObjectProperty<ClassForPrinting> selectedClassForPrinting = new SimpleObjectProperty<>();

    //Model Information List for Model Manager to have Module Info List and list to be printed for displaymod
    private final ObservableList<ModuleInfo> allModules;
    private final FilteredList<ModuleInfo> displayList;
    private final ModuleInfoList moduleInfoList;
    private final SimpleObjectProperty<ModuleInfo> selectedModuleInfo = new SimpleObjectProperty<>();

    private final ObservableList<Course> allCourses;
    private final CourseList courseList;
    private final RequirementStatusList requirementStatusList;

    private final FilteredList<RecModule> recModuleList;
    private final SortedList<RecModule> recModuleListSorted;

    //TODO: Interaction with user Info
    private final UserInfo userInfo;
    /**
     * Initializes a ModelManager with the given GradTrak and userPrefs.
     */
    public ModelManager(ReadOnlyGradTrak gradTrak, UserPrefs userPrefs,
                        ModuleInfoList moduleInfoList, CourseList allCourses,
                        UserInfo userInfo) {
        super();
        requireAllNonNull(gradTrak, userPrefs, moduleInfoList);

        logger.fine("Initializing with Gradtrak: " + gradTrak + " and user prefs " + userPrefs);

        versionedGradTrak = new VersionedGradTrak(gradTrak);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredModulesTaken = new FilteredList<>(versionedGradTrak.getModulesTakenList());
        filteredModulesTaken.addListener(this::ensureSelectedModuleTakenIsValid);

        //Get an non Modifiable List of all modules and use a filtered list based on that to search for modules
        this.allModules = moduleInfoList.getObservableList();
        this.displayList = new FilteredList<>(this.allModules);
        this.moduleInfoList = moduleInfoList;
        updateDisplayList(new CodeContainsKeywordsPredicate(null));

        // Initialise list of RecModule
        this.recModuleList = new FilteredList<>(getObservableRecModuleList(allModules));
        this.recModuleListSorted = new SortedList<>(recModuleList);

        //Get a non-modifiable list of all courses
        this.allCourses = allCourses.getObservableList();
        this.courseList = allCourses;

        //Get user info file that can be modified
        this.userInfo = userInfo;
        //TODO: interaction for setting course in user info
        //for now default course will be Computer Science Algorithms
        this.course = userInfo.getCourse();
        this.requirementStatusList = new RequirementStatusList();
        requirementStatusList.updateRequirementStatus(course,
                versionedGradTrak.getModulesTakenList()
                        .stream()
                        .map(ModuleTaken::getModuleInfoCode)
                        .collect(Collectors.toList()));
    }

    public ModelManager() {
        this(new GradTrak(), new UserPrefs(), new ModuleInfoList(), new CourseList(), new UserInfo());
    }

    //=========== UserPrefs ==================================================================================
    @Override
    public void setCourse(CourseName courseName) {
        requireNonNull(courseName);
        course = courseList.getCourse(courseName);
        requirementStatusList.updateRequirementStatus(course, getModuleInfoCodeList());
        userInfo.setCourse(course);
    }

    @Override
    public boolean hasCourse(CourseName courseName) {
        requireNonNull(courseName);
        course = courseList.getCourse(courseName);
        if (course == null) {
            return false;
        }
        return true;
    }

    @Override
    public Course getCourse() {
        return course;
    }

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getGradTrakFilePath() {
        return userPrefs.getGradTrakFilePath();
    }

    @Override
    public void setGradTrakFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setGradTrakFilePath(addressBookFilePath);
    }

    //=========== GradTrak ================================================================================

    @Override
    public void setGradTrak(ReadOnlyGradTrak addressBook) {
        versionedGradTrak.resetData(addressBook);
    }

    @Override
    public ReadOnlyGradTrak getGradTrak() {
        return versionedGradTrak;
    }

    @Override
    public Semester getCurrentSemester() {
        return versionedGradTrak.getCurrentSemester();
    }

    @Override
    public boolean hasModuleTaken(ModuleTaken moduleTaken) {
        requireNonNull(moduleTaken);
        return versionedGradTrak.hasModuleTaken(moduleTaken);
    }

    @Override
    public boolean isEligibleModuleTaken(ModuleTaken moduleTaken) {
        requireNonNull(moduleTaken);
        if (moduleInfoList.isEmpty()) { //cannot check if not module info
            return true;
        }
        ModuleInfo moduleInfo = moduleInfoList.getModule(moduleTaken.getModuleInfoCode().value);
        return new EligibleModulePredicate(versionedGradTrak).test(moduleInfo);
    }

    @Override
    public boolean canEditModuleTaken(ModuleTaken moduleTakenToEdit, ModuleTaken editedModuleTaken) {
        requireNonNull(moduleTakenToEdit);
        requireNonNull(editedModuleTaken);
        for (ModuleTaken module : versionedGradTrak.getModulesTakenList()) {
            if (module.equals(moduleTakenToEdit)) {
                continue;
            }
            ModuleInfo moduleInfo = moduleInfoList.getModule(module.getModuleInfoCode().value);
            ModuleTree moduleTree = moduleInfo.getModuleInfoPrerequisite().getModuleTree();
            if (!versionedGradTrak.getMissingPrerequisitesWithEditedModule(
                    moduleTree, moduleTakenToEdit, editedModuleTaken).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean canDeleteModuleTaken(ModuleTaken toDelete) {
        requireNonNull(toDelete);
        for (ModuleTaken module : versionedGradTrak.getModulesTakenList()) {
            if (module.equals(toDelete)) {
                continue;
            }
            ModuleInfo moduleInfo = moduleInfoList.getModule(module.getModuleInfoCode().value);
            ModuleTree moduleTree = moduleInfo.getModuleInfoPrerequisite().getModuleTree();
            if (!versionedGradTrak.getMissingPrerequisitesWithoutModule(moduleTree, toDelete).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void deleteModuleTaken(ModuleTaken target) {
        versionedGradTrak.removeModuleTaken(target);
    }

    @Override
    public void addModuleTaken(ModuleTaken moduleTaken) {
        versionedGradTrak.addModuleTaken(moduleTaken);
        updateFilteredModulesTakenList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setModuleTaken(ModuleTaken target, ModuleTaken editedModuleTaken) {
        requireAllNonNull(target, editedModuleTaken);
        versionedGradTrak.setModuleTaken(target, editedModuleTaken);
    }

    @Override
    public void setSemesterLimit(int index, SemesterLimit editedSemesterLimit) {
        requireAllNonNull(index, editedSemesterLimit);
        versionedGradTrak.setSemesterLimit(index, editedSemesterLimit);
    }

    @Override
    public void setCurrentSemester(Semester semester) {
        requireAllNonNull(semester);
        versionedGradTrak.setCurrentSemester(semester);
    }

    //=========== Filtered ModuleTaken List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code ModuleTaken} backed by the internal list of
     * {@code versionedGradTrak}
     */
    @Override
    public ObservableList<ModuleTaken> getFilteredModulesTakenList() {
        return filteredModulesTaken;
    }

    /**
     * Returns an unmodifiable view of the list of {@code SemesterLimit} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<SemesterLimit> getSemesterLimitList() {
        return versionedGradTrak.getSemesterLimitList();
    }

    @Override
    public void updateFilteredModulesTakenList(Predicate<ModuleTaken> predicate) {
        requireNonNull(predicate);
        filteredModulesTaken.setPredicate(predicate);
    }

    /**
     * Returns a LimitChecker with the generated html string
     * that shows where their CAP and workload limits are violated.
     */
    @Override
    public ClassForPrinting checkLimit(ModuleInfoList moduleInfoList) {
        return new LimitChecker(getCurrentSemester(), getSemesterLimitList(),
                getFilteredModulesTakenList(), moduleInfoList);
    }

    //=========== Undo/Redo =================================================================================

    @Override
    public boolean canUndoGradTrak() {
        return versionedGradTrak.canUndo();
    }

    @Override
    public boolean canRedoGradTrak() {
        return versionedGradTrak.canRedo();
    }

    @Override
    public void undoGradTrak() {
        versionedGradTrak.undo();
    }

    @Override
    public void redoGradTrak() {
        versionedGradTrak.redo();
    }

    @Override
    public void commitGradTrak() {
        versionedGradTrak.commit();
    }

    //=========== Selected moduleTaken ===========================================================================

    @Override
    public ReadOnlyProperty<ClassForPrinting> selectedClassForPrintingProperty() {
        return selectedClassForPrinting;
    }

    @Override
    public ClassForPrinting getSelectedClassForPrinting() {
        return selectedClassForPrinting.getValue();
    }

    @Override
    public void setSelectedClassForPrinting(ClassForPrinting classForPrinting) {
        if (classForPrinting != null && classForPrinting instanceof ModuleTaken
                && !filteredModulesTaken.contains(classForPrinting)) {
            throw new ModuleTakenNotFoundException();
        }
        selectedClassForPrinting.setValue(classForPrinting);
    }

    //=========== Module Info List ===========================================================================

    @Override
    public ObservableList<ModuleInfo> getDisplayList() {
        return this.displayList;
    }

    @Override
    public void updateDisplayList(Predicate<ModuleInfo> predicate) {
        requireNonNull(predicate);
        displayList.setPredicate(predicate);
    }

    @Override
    public ReadOnlyProperty<ModuleInfo> selectedModuleInfoProperty() {
        return selectedModuleInfo;
    }

    @Override
    public ModuleInfoList getModuleInfoList() {
        return moduleInfoList;
    }

    @Override
    public ModuleInfo getSelectedModuleInfo() {
        return selectedModuleInfo.getValue();
    }

    @Override
    public void setSelectedModuleInfo(ModuleInfo moduleInfo) {
        if (moduleInfo != null && !displayList.contains(moduleInfo)) {
            //temp solution TODO: HERE!!!
            throw new ModuleTakenNotFoundException();
        }
        selectedModuleInfo.setValue(moduleInfo);
    }

    //=========== Module recommendation ===========================================================================
    @Override
    public ObservableList<RecModule> getRecModuleListSorted() {
        return recModuleListSorted;
    }

    @Override
    public void updateRecModuleList() {
        recModuleList.setPredicate(new RecModulePredicate(course, versionedGradTrak));
        recModuleListSorted.setComparator(new RecModuleComparator());
    }

    /**
     * Generates a List of {@code RecModule} from a List of {@code ModuleInfo}
     * for initialisation in {@code ModelManager}.
     * @param moduleInfoList The List of {@code ModuleInfo}.
     * @return A List of {@code RecModule}.
     */
    private static ObservableList<RecModule> getObservableRecModuleList(ObservableList<ModuleInfo> moduleInfoList) {
        ArrayList<RecModule> recModuleList = new ArrayList<>();
        for (ModuleInfo moduleInfo : moduleInfoList) {
            recModuleList.add(new RecModule(moduleInfo));
        }

        return FXCollections.observableArrayList(recModuleList);
    }

    /**
     * Ensures {@code selectedClassForPrinting} is a valid moduleTaken in {@code filteredModulesTaken}.
     */
    private void ensureSelectedModuleTakenIsValid(ListChangeListener.Change<? extends ModuleTaken> change) {
        while (change.next()) {
            if (selectedClassForPrinting.getValue() == null
                    || !(selectedClassForPrinting.getValue() instanceof ModuleTaken)) {
                // null is always a valid selected moduleTaken, so we do not need to check that it is valid anymore.
                return;
            }

            boolean wasSelectedModuleTakenReplaced = change.wasReplaced()
                    && change.getAddedSize() == change.getRemovedSize()
                    && change.getRemoved().contains(selectedClassForPrinting.getValue());
            if (wasSelectedModuleTakenReplaced) {
                // Update selectedClassForPrinting to its new value.
                int index = change.getRemoved().indexOf(selectedClassForPrinting.getValue());
                selectedClassForPrinting.setValue(change.getAddedSubList().get(index));
                continue;
            }

            boolean wasSelectedPersonRemoved = change.getRemoved().stream()
                    .anyMatch(removedPerson
                        -> ((ModuleTaken) selectedClassForPrinting.getValue()).isSameModuleTaken(removedPerson));
            if (wasSelectedPersonRemoved) {
                // Select the moduleTaken that came before it in the list,
                // or clear the selection if there is no such moduleTaken.
                selectedClassForPrinting.setValue(
                        change.getFrom() > 0 ? change.getList().get(change.getFrom() - 1) : null);
            }
        }
    }
    //=========== Display completed requirement =======================================================================
    @Override
    public ObservableList<RequirementStatus> getRequirementStatusList() {
        updateRequirementStatusList();
        return this.requirementStatusList.getRequirementStatusList();
    }

    /**
     * Updates the requirement status in requirement status list with the latest module taken information
     */
    public void updateRequirementStatusList() {
        requirementStatusList.updateRequirementStatus(
                versionedGradTrak.getNonFailedCodeList());
    }

    @Override
    public UserInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public ObservableList<ModuleInfoCode> getModuleInfoCodeList() {
        return FXCollections.observableArrayList(
                versionedGradTrak.getModulesTakenList()
                        .stream()
                        .map(ModuleTaken::getModuleInfoCode)
                        .collect(Collectors.toList()));
    }


    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof ModelManager)) {
            return false;
        }
        // state check
        ModelManager other = (ModelManager) obj;
        return versionedGradTrak.equals(other.versionedGradTrak)
                && userPrefs.equals(other.userPrefs)
                && filteredModulesTaken.equals(other.filteredModulesTaken)
                && Objects.equals(selectedClassForPrinting.get(), other.selectedClassForPrinting.get())
                && recModuleListSorted.equals(other.recModuleListSorted)
                && course.equals(other.course)
                && requirementStatusList.equals(other.requirementStatusList)
                && this.userInfo.equals(other.userInfo);
    }
}
