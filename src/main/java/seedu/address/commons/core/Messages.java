package seedu.address.commons.core;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format %1$s\n";
    public static final String MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX = "The index provided is invalid";
    public static final String MESSAGE_MODULE_DOES_NOT_EXIST = "Module does not exist";
    public static final String MESSAGE_INVALID_SEMESTER_LIMIT = "The semester provided is invalid";
    public static final String MESSAGE_GRADES_NOT_FINALIZED_BEFORE_SEMESTER =
            "Grades before current semester must be finalized";
    public static final String MESSAGE_GRADES_OUT_OF_ORDER = "Grades are out of order";
    public static final String MESSAGE_MAX_GRADE_MUST_BE_COUNTED = "Max grade of module must be countable in CAP";
    public static final String MESSAGE_CAP_LIMIT_OUT_OF_ORDER = "CAP limit is out of order";
    public static final String MESSAGE_LECTURE_HOUR_LIMIT_OUT_OF_ORDER = "Lecture hour is out of order";
    public static final String MESSAGE_TUTORIAL_HOUR_LIMIT_OUT_OF_ORDER = "Tutorial hour is out of order";
    public static final String MESSAGE_LAB_HOUR_LIMIT_OUT_OF_ORDER = "Lab hour is out of order";
    public static final String MESSAGE_PROJECT_HOUR_LIMIT_OUT_OF_ORDER = "Project hour is out of order";
    public static final String MESSAGE_PREPARATION_HOUR_LIMIT_OUT_OF_ORDER = "Preparation hour limit is out of order";
    public static final String MESSAGE_MODULETAKEN_LISTED_OVERVIEW = "GradTrak modules found: %1$d";
    public static final String MESSAGE_PREREQUISITE_VIOLATED =
            "Failed, this module is a prerequisite for other modules";

}
