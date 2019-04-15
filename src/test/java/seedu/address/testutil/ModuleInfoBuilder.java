package seedu.address.testutil;

import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduleinfo.ModuleInfoCredits;
import seedu.address.model.moduleinfo.ModuleInfoDepartment;
import seedu.address.model.moduleinfo.ModuleInfoDescription;
import seedu.address.model.moduleinfo.ModuleInfoPrerequisites;
import seedu.address.model.moduleinfo.ModuleInfoTitle;
import seedu.address.model.moduleinfo.ModuleInfoWorkload;

/**
 * A utility class to help with building ModuleInfo objects.
 */
public class ModuleInfoBuilder {

    public static final String DEFAULT_MODULE_INFO_CODE = "CS2103T";
    public static final String DEFAULT_MODULE_INFO_TITLE = "Software Engineering";
    public static final double DEFAULT_MODULE_INFO_CREDITS = 4.0;
    public static final String DEFAULT_MODULE_INFO_DEPARTMENT = "Computer Science";
    public static final String DEFAULT_MODULE_INFO_DESCRIPTION = "Standard sentence for description of this module.";
    public static final String DEFAULT_MODULE_INFO_WORKLOAD = "1-2-3-4-5";
    public static final String DEFAULT_MODULE_INFO_PREREQUISITE = "(CS1020 or CS1020E or CS2020) or "
            + "(CS2030 and (CS2040 or CS2040C))";


    private ModuleInfoCode moduleinfocode;
    private ModuleInfoTitle moduleinfotitle;
    private ModuleInfoCredits moduleinfocredits;
    private ModuleInfoDescription moduleinfodescription;
    private ModuleInfoDepartment moduleinfodepartment;
    private ModuleInfoWorkload moduleinfoworkload;
    private ModuleInfoPrerequisites moduleinfoprerequisite;

    public ModuleInfoBuilder() {
        moduleinfocode = new ModuleInfoCode(DEFAULT_MODULE_INFO_CODE);
        moduleinfotitle = new ModuleInfoTitle(DEFAULT_MODULE_INFO_TITLE);
        moduleinfocredits = new ModuleInfoCredits(DEFAULT_MODULE_INFO_CREDITS);
        moduleinfodepartment = new ModuleInfoDepartment(DEFAULT_MODULE_INFO_DEPARTMENT);
        moduleinfodescription = new ModuleInfoDescription(DEFAULT_MODULE_INFO_DESCRIPTION);
        moduleinfoworkload = new ModuleInfoWorkload(DEFAULT_MODULE_INFO_WORKLOAD);
        moduleinfoprerequisite = new ModuleInfoPrerequisites(DEFAULT_MODULE_INFO_CODE,
                DEFAULT_MODULE_INFO_PREREQUISITE);
    }

    /**
     * Initializes the ModuleTakenBuilder with the data of {@code moduleTakenToCopy}.
     */
    public ModuleInfoBuilder(ModuleInfo moduleInfoCopy) {
        moduleinfocode = moduleInfoCopy.getModuleInfoCode();
        moduleinfotitle = moduleInfoCopy.getModuleInfoTitle();
        moduleinfocredits = moduleInfoCopy.getModuleInfoCredits();
        moduleinfodepartment = moduleInfoCopy.getModuleInfoDepartment();
        moduleinfodescription = moduleInfoCopy.getModuleInfoDescription();
        moduleinfoworkload = moduleInfoCopy.getModuleInfoWorkload();
        moduleinfoprerequisite = moduleInfoCopy.getModuleInfoPrerequisite();
    }

    /**
     * Sets the {@code code} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoCode(String moduleInfoCode) {
        this.moduleinfocode = new ModuleInfoCode(moduleInfoCode);
        return this;
    }

    /**
     * Sets the {@code title} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoTitle(String moduleInfoTitle) {
        this.moduleinfotitle = new ModuleInfoTitle(moduleInfoTitle);
        return this;
    }

    /**
     * Sets the {@code credits} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoCredits(double moduleInfoCredits) {
        this.moduleinfocredits = new ModuleInfoCredits(moduleInfoCredits);
        return this;
    }

    /**
     * Sets the {@code Department} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoDeparment(String moduleInfoDept) {
        this.moduleinfodepartment = new ModuleInfoDepartment(moduleInfoDept);
        return this;
    }

    /**
     * Sets the {@code description} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoDescription(String moduleInfodescrip) {
        this.moduleinfodescription = new ModuleInfoDescription(moduleInfodescrip);
        return this;
    }

    /**
     * Sets the {@code workload} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoWorkload(String moduleInfoworkload) {
        this.moduleinfoworkload = new ModuleInfoWorkload(moduleInfoworkload);
        return this;
    }

    /**
     * Sets the {@code prerequisite} of the {@code ModuleInfo} being built.
     */
    public ModuleInfoBuilder withModuleInfoPrerquisite(String moduleInfoPrereq, String moduleInfoCode) {
        this.moduleinfoprerequisite = new ModuleInfoPrerequisites(moduleInfoCode, moduleInfoPrereq);
        return this;
    }

    /**
     * Builds a moduleInfo with all the fields in the {@code ModuleTakenBuilder}
     */
    public ModuleInfo build() {
        return new ModuleInfo(moduleinfocode.toString(), moduleinfotitle.toString(), moduleinfocredits.getCredits(),
                moduleinfodescription.toString(), moduleinfoworkload.toString(), "empty",
                moduleinfodepartment.toString(), moduleinfoprerequisite.toString());
    }
}
