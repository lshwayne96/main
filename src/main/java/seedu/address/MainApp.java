package seedu.address;

import static seedu.address.model.util.SampleCourse.getSampleCourseList;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.stage.Stage;
import seedu.address.commons.core.Config;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.Version;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.commons.util.ConfigUtil;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.GradTrak;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyGradTrak;
import seedu.address.model.UserInfo;
import seedu.address.model.UserPrefs;
import seedu.address.model.course.CourseList;
import seedu.address.model.moduleinfo.ModuleInfoList;
import seedu.address.storage.GradTrakStorage;
import seedu.address.storage.JsonGradTrakStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.Storage;
import seedu.address.storage.StorageManager;
import seedu.address.storage.UserInfoStorageManager;
import seedu.address.storage.UserPrefsStorage;
import seedu.address.storage.coursestorage.CourseManager;
import seedu.address.storage.moduleinfostorage.ModuleInfoManager;
import seedu.address.ui.Ui;
import seedu.address.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 1, 0, true);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected ModuleInfoManager moduleInfoManager;
    protected CourseManager courseManager;
    protected UserInfoStorageManager userInfoStorageManager;
    //protected Course manager;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing GradTrak ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        UserPrefs userPrefs = initPrefs(userPrefsStorage);
        GradTrakStorage gradTrakStorage = new JsonGradTrakStorage(userPrefs.getGradTrakFilePath());
        storage = new StorageManager(gradTrakStorage, userPrefsStorage);

        initLogging(config);

        //Other app data managers
        moduleInfoManager = new ModuleInfoManager();
        courseManager = new CourseManager();
        userInfoStorageManager = new UserInfoStorageManager();

        model = initModelManager(storage, userPrefs, moduleInfoManager,
                courseManager, userInfoStorageManager);

        logic = new LogicManager(model, storage, userInfoStorageManager);

        ui = new UiManager(logic);
    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s address book and {@code userPrefs}. <br>
     * An empty GradTrak will be used instead if {@code storage}'s GradTrak is not found,
     * or an empty GradTrak will be used instead if errors occur when reading {@code storage}'s GradTrak.
     */
    private Model initModelManager(Storage storage, UserPrefs userPrefs, ModuleInfoManager moduleInfoManager,
                                   CourseManager courseManager, UserInfoStorageManager userInfoStorageManager) {
        Optional<ReadOnlyGradTrak> gradTrakOptional;
        ReadOnlyGradTrak initialData;
        Optional<ModuleInfoList> allModulesOptional;
        ModuleInfoList allModules;
        Optional<CourseList> allCourseListOptional;
        CourseList allCourses;
        Optional<UserInfo> userInfoOptional;
        UserInfo userInfo;
        try {
            allCourseListOptional = courseManager.readCourseFile();
            if (!allCourseListOptional.isPresent()) {
                logger.info("File for all courses not found! Starting with sample course list");

            }
            userInfoOptional = userInfoStorageManager.readUserInfoFile();
            if (!userInfoOptional.isPresent()) {
                logger.info("File for user information not found! Starting with default user info file");
            }
            allModulesOptional = moduleInfoManager.readModuleInfoFile();
            if (!allModulesOptional.isPresent()) {
                logger.info("File for all module information not found! Starting with empty module List");
            }
            gradTrakOptional = storage.readGradTrak();
            if (!gradTrakOptional.isPresent()) {
                logger.info("Data file not found. Will be starting with a default GradTrak");
            }

            initialData = gradTrakOptional.orElseGet(GradTrak::initializeDefaults);

            //If unable to find the data file provide a blank Module Info List
            allModules = allModulesOptional.orElse(new ModuleInfoList());
            //If unable to find data file, provide default course list
            allCourses = allCourseListOptional.orElse(getSampleCourseList());
            userInfo = userInfoOptional.orElse(new UserInfo());
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty GradTrak");
            initialData = new GradTrak();
            allModules = new ModuleInfoList();
            allCourses = new CourseList();
            userInfo = new UserInfo();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty GradTrak");
            initialData = new GradTrak();
            allModules = new ModuleInfoList();
            allCourses = new CourseList();
            userInfo = new UserInfo();
        }

        return new ModelManager(initialData, userPrefs, allModules, allCourses, userInfo);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty GradTrak");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting GradTrak " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Address Book ] =============================");
        try {
            storage.saveUserPrefs(model.getUserPrefs());
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
