package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.function.Predicate;

import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduleinfo.ModuleInfoPrerequisites;

/**
 * Tests if a module can be read.
 */
public class EligibleModulePredicate implements Predicate<ModuleInfo> {

    private final ReadOnlyGradTrak gradTrak;

    public EligibleModulePredicate(ReadOnlyGradTrak gradTrak) {
        this.gradTrak = gradTrak;
    }

    @Override
    public boolean test(ModuleInfo moduleInfo) {
        requireNonNull(moduleInfo);
        ModuleInfoCode code = moduleInfo.getModuleInfoCode();
        ModuleInfoPrerequisites prerequisites = moduleInfo.getModuleInfoPrerequisite();

        boolean isAdded = gradTrak.getNonFailedCodeList().contains(code);
        boolean isPrereqSatisfied = gradTrak.getMissingPrerequisites(prerequisites.getModuleTree()).isEmpty();

        return !isAdded && isPrereqSatisfied;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EligibleModulePredicate // instanceof handles nulls
                && gradTrak.equals(((EligibleModulePredicate) other).gradTrak));
    }
}
