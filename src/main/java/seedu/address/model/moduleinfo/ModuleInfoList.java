package seedu.address.model.moduleinfo;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.util.InvalidationListenerManager;

/**
 * Wraps all data at the Module Info List level
 * Duplicates are not allowed (by comparison)
 */
public class ModuleInfoList {

    private final List<ModuleInfo> moduleInfoList;
    private final InvalidationListenerManager invalidationListenerManager = new InvalidationListenerManager();

    {
        moduleInfoList = new ArrayList<>();
    }

    public ModuleInfoList() {}

    /**
     * Adds Module to the List of all the modules
     * @param module
     */
    public void addModuleInfo(ModuleInfo module) {
        this.moduleInfoList.add(module);
        //System.out.println("new module added:" + module.getCodeString());
    }

    /**
     * Returns Module which has the same module code
     * @param code
     * @return ModuleInfo
     */
    public ModuleInfo getModule(String code) {
        ModuleInfo module = null;
        for (int i = 0; i < moduleInfoList.size(); i++) {
            if (moduleInfoList.get(i).getCodeString().equals(code)) {
                module = moduleInfoList.get(i);
            }
        }
        return module;
    }

    public ObservableList<ModuleInfo> getObservableList() {
        ObservableList<ModuleInfo> observableList = FXCollections.observableArrayList(moduleInfoList);
        return observableList;
    }
}
