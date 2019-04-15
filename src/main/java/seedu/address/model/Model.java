package seedu.address.model;

import java.nio.file.Path;

import java.util.function.Predicate;

import javafx.beans.property.ReadOnlyProperty;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
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

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<ModuleTaken> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces course data with the data in {@code Model}.
     */
    void setCourse(CourseName courseName);

    /**
     * Checks whether model has course with course name {@code course}
     */
    boolean hasCourse(CourseName courseName);

    /**
     * Returns the course data.
     */
    Course getCourse();

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' GradTrak file path.
     */
    Path getGradTrakFilePath();

    /**
     * Sets the user prefs' GradTrak file path.
     */
    void setGradTrakFilePath(Path addressBookFilePath);

    /**
     * Replaces GradTrak data with the data in {@code addressBook}.
     */
    void setGradTrak(ReadOnlyGradTrak addressBook);

    /** Returns the GradTrak */
    ReadOnlyGradTrak getGradTrak();

    /** Returns the current Semester */
    Semester getCurrentSemester();

    /**
     * Returns true if a moduleTaken with the same identity as {@code moduleTaken} exists in the GradTrak .
     */
    boolean hasModuleTaken(ModuleTaken moduleTaken);

    /**
     * Returns true if a moduleTaken has all its prerequisites satisfied.
     */
    boolean isEligibleModuleTaken(ModuleTaken moduleTaken);

    /**
     * Returns true if editing the moduleTaken will not cause other prerequisites to be unsatisfied
     */
    boolean canEditModuleTaken(ModuleTaken moduleTakenToEdit, ModuleTaken editedModuleTaken);

    /**
     * Returns true if deleting the moduleTaken will not cause other prerequisites to be unsatisfied
     */
    boolean canDeleteModuleTaken(ModuleTaken moduleTaken);

    /**
     * Deletes the given moduleTaken.
     * The moduleTaken must exist in the GradTrak .
     */
    void deleteModuleTaken(ModuleTaken target);

    /**
     * Adds the given moduleTaken.
     * {@code moduleTaken} must not already exist in the GradTrak .
     */
    void addModuleTaken(ModuleTaken moduleTaken);

    /**
     * Replaces the given moduleTaken {@code target} with {@code editedModuleTaken}.
     * {@code target} must exist in the GradTrak .
     * The moduleTaken identity of {@code editedModuleTaken} must not be the same as another
     * existing moduleTaken in the GradTrak .
     */
    void setModuleTaken(ModuleTaken target, ModuleTaken editedModuleTaken);

    /**
     * Replaces the semester limit at the given index with {@code editedSemesterLimit}.
     */
    void setSemesterLimit(int index, SemesterLimit editedSemesterLimit);

    /**
     * Replaces the current semester with the given semester.
     */
    void setCurrentSemester(Semester semester);

    /** Returns an unmodifiable view of the filtered moduleTaken list */
    ObservableList<ModuleTaken> getFilteredModulesTakenList();

    /** Returns an unmodifiable view of the SemesterLimit list */
    ObservableList<SemesterLimit> getSemesterLimitList();

    /**
     * Updates the filter of the filtered moduleTaken list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredModulesTakenList(Predicate<ModuleTaken> predicate);

    /**
     * Returns a printable LimitChecker with generated html string that indicates if the CAP and workload limits
     * set by the user for every semester have been violated based the modules taken in their plan.
     */
    ClassForPrinting checkLimit(ModuleInfoList moduleInfoList);

    /**
     * Returns true if the model has previous GradTrak states to restore.
     */
    boolean canUndoGradTrak();

    /**
     * Returns true if the model has undone GradTrak states to restore.
     */
    boolean canRedoGradTrak();

    /**
     * Restores the model's GradTrak to its previous state.
     */
    void undoGradTrak();

    /**
     * Restores the model's GradTrak to its previously undone state.
     */
    void redoGradTrak();

    /**
     * Saves the current GradTrak state for undo/redo.
     */
    void commitGradTrak();

    /**
     * Selected moduleTaken in the filtered moduleTaken list.
     * null if no moduleTaken is selected.
     */
    ReadOnlyProperty<ClassForPrinting> selectedClassForPrintingProperty();

    /**
     * Returns the selected moduleTaken in the filtered moduleTaken list.
     * null if no moduleTaken is selected.
     */
    ClassForPrinting getSelectedClassForPrinting();

    /**
     * Sets the selected moduleTaken in the filtered moduleTaken list.
     */
    void setSelectedClassForPrinting(ClassForPrinting classForPrinting);

    /**
     * Returns an Observable list of all module information from storage
     * @return Observable list of ModuleInfo based on predicate set
     */
    ObservableList<ModuleInfo> getDisplayList();

    /**
     * Updates the filtered list based on the predicate provided by user input
     * @param predicate
     */
    void updateDisplayList(Predicate<ModuleInfo> predicate);

    /**
     * Updates the filtered list based on the predicate provided by user input
     * @return Observable list of ModuleInf
     */
    ReadOnlyProperty<ModuleInfo> selectedModuleInfoProperty();

    /**
     * Gets the list of module info to be searchable by module info code.
     * @return ModuleInfoList
     */
    ModuleInfoList getModuleInfoList();

    /**
     * Updates the filtered list based on the predicate provided by user input
     * @return ModuleInfo
     */
    ModuleInfo getSelectedModuleInfo();

    /**
     * Updates the filtered list based on the predicate provided by user input
     * @param moduleInfo
     */
    void setSelectedModuleInfo(ModuleInfo moduleInfo);


    /**
     * Returns an unmodifiable view of the recommended module list.
     */
    ObservableList<RecModule> getRecModuleListSorted();

    /**
     * Updates the recommended module list.
     */
    void updateRecModuleList();

    /**
     * Returns an unmodifiable view of the filtered course requirement list
     */
    ObservableList<RequirementStatus> getRequirementStatusList();

    /**
     * Updates requirement status list of model
     */
    void updateRequirementStatusList();

    /**
     * Returns a copy of the user info.
     */
    UserInfo getUserInfo();

    /**
     * Returns an unmodifiable view of the moduleInfoCode list in GradTrak
     */
    ObservableList<ModuleInfoCode> getModuleInfoCodeList();

}
