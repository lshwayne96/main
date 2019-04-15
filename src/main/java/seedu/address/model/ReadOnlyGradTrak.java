package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import seedu.address.commons.util.ModuleTree;
import seedu.address.model.limits.SemesterLimit;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;

/**
 * Unmodifiable view of an Graduation Tracker/GradTrak
 */
public interface ReadOnlyGradTrak extends Observable {

    /**
     * Returns an unmodifiable view of the persons list.
     * This list will not contain any duplicate persons.
     */
    ObservableList<ModuleTaken> getModulesTakenList();

    /**
     * Returns an unmodifiable view of the sem limit list.
     */
    ObservableList<SemesterLimit> getSemesterLimitList();

    /**
     * Returns the current semester.
     */
    Semester getCurrentSemester();

    /**
     * Returns a {@code List} of {@code ModuleInfoCode} representing non-failed {@code ModuleTaken}.
     * @return a {@code List} of {@code ModuleInfoCode} representing non-failed {@code ModuleTaken}.
     */
    List<ModuleInfoCode> getNonFailedCodeList();

    /**
     * Generates an {@code ArrayList} of {@code String} of missing prerequisites in this {@code GradTrak},
     * given the {@code ModuleTree} of the module.
     * @param moduleTree The {@code ModuleTree} of the module whose missing prerequisites are being generated.
     * @return an {@code ArrayList} of {@code String} of missing prerequisites.
     */
    ArrayList<String> getMissingPrerequisites(ModuleTree moduleTree);

    /**
     * Generates an {@code ArrayList} of {@code String} of missing prerequisites in a {@code GradTrak}
     * excluding the specified {@code ModuleTaken}, given the {@code ModuleTree} of the module.
     * @param moduleTree The {@code ModuleTree} of the module whose missing prerequisites are being generated.
     * @param moduleTaken The {@code ModuleTaken} to exclude.
     * @return an {@code ArrayList} of {@code String} of missing prerequisites.
     */
    ArrayList<String> getMissingPrerequisitesWithoutModule(ModuleTree moduleTree, ModuleTaken moduleTaken);

    /**
     * Generates an {@code ArrayList} of {@code String} of missing prerequisites in a {@code GradTrak}
     * excluding the specified {@code moduleTakenToEdit} but including the specified {@code editedModuleTaken},
     * given the {@code ModuleTree} of the module.
     * @param moduleTree The {@code ModuleTree} of the module whose missing prerequisites are being generated.
     * @param moduleTakenToEdit The {@code ModuleTaken} to exclude.
     * @param editedModuleTaken The {@code ModuleTaken} to include.
     * @return an {@code ArrayList} of {@code String} of missing prerequisites.
     */
    ArrayList<String> getMissingPrerequisitesWithEditedModule(ModuleTree moduleTree, ModuleTaken moduleTakenToEdit,
                                                              ModuleTaken editedModuleTaken);
}
