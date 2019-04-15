package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.Grade;
import seedu.address.model.moduletaken.Hour;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.moduletaken.Workload;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building ModuleTaken objects.
 */
public class ModuleTakenBuilder {

    public static final String DEFAULT_MODULE_INFO_CODE = "CS1010S";
    public static final String DEFAULT_SEMESTER = "Y5S2";
    public static final String DEFAULT_EXPECTED_MIN_GRADE = "F";
    public static final String DEFAULT_EXPECTED_MAX_GRADE = "A";
    public static final String DEFAULT_LECTURE_HOUR = "0";
    public static final String DEFAULT_TUTORIAL_HOUR = "0";
    public static final String DEFAULT_LAB_HOUR = "0";
    public static final String DEFAULT_PROJECT_HOUR = "0";
    public static final String DEFAULT_PREPARATION_HOUR = "0";

    private ModuleInfoCode moduleInfoCode;
    private Semester semester;
    private Grade expectedMinGrade;
    private Grade expectedMaxGrade;
    private Hour lectureHour;
    private Hour tutorialHour;
    private Hour labHour;
    private Hour projectHour;
    private Hour preparationHour;
    private Set<Tag> tags;

    public ModuleTakenBuilder() {
        moduleInfoCode = new ModuleInfoCode(DEFAULT_MODULE_INFO_CODE);
        semester = Semester.valueOf(DEFAULT_SEMESTER);
        expectedMinGrade = Grade.valueOf(DEFAULT_EXPECTED_MIN_GRADE);
        expectedMaxGrade = Grade.valueOf(DEFAULT_EXPECTED_MAX_GRADE);
        lectureHour = new Hour(DEFAULT_LECTURE_HOUR);
        tutorialHour = new Hour(DEFAULT_TUTORIAL_HOUR);
        labHour = new Hour(DEFAULT_LAB_HOUR);
        projectHour = new Hour(DEFAULT_PROJECT_HOUR);
        preparationHour = new Hour(DEFAULT_PREPARATION_HOUR);
        tags = new HashSet<>();
    }

    /**
     * Initializes the ModuleTakenBuilder with the data of {@code moduleTakenToCopy}.
     */
    public ModuleTakenBuilder(ModuleTaken moduleTakenToCopy) {
        moduleInfoCode = moduleTakenToCopy.getModuleInfoCode();
        semester = moduleTakenToCopy.getSemester();
        expectedMinGrade = moduleTakenToCopy.getExpectedMinGrade();
        expectedMaxGrade = moduleTakenToCopy.getExpectedMaxGrade();
        lectureHour = moduleTakenToCopy.getLectureHour();
        tutorialHour = moduleTakenToCopy.getTutorialHour();
        labHour = moduleTakenToCopy.getLabHour();
        projectHour = moduleTakenToCopy.getProjectHour();
        preparationHour = moduleTakenToCopy.getPreparationHour();
        tags = new HashSet<>(moduleTakenToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withModuleInfoCode(String moduleInfoCode) {
        this.moduleInfoCode = new ModuleInfoCode(moduleInfoCode);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Grade} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withExpectedMaxGrade(String expectedMaxGrade) {
        this.expectedMaxGrade = Grade.valueOf(expectedMaxGrade);
        return this;
    }

    /**
     * Sets the {@code Semester} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withSemester(String semester) {
        this.semester = Semester.valueOf(semester);
        return this;
    }

    /**
     * Sets the {@code Grade} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withExpectedMinGrade(String expectedMinGrade) {
        this.expectedMinGrade = Grade.valueOf(expectedMinGrade);
        return this;
    }

    /**
     * Sets the {@code Hour} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withLectureHour(String lectureHour) {
        this.lectureHour = new Hour(lectureHour);
        return this;
    }

    /**
     * Sets the {@code Hour} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withTutorialHour(String tutorialHour) {
        this.tutorialHour = new Hour(tutorialHour);
        return this;
    }

    /**
     * Sets the {@code Hour} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withLabHour(String labHour) {
        this.labHour = new Hour(labHour);
        return this;
    }

    /**
     * Sets the {@code Hour} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withProjectHour(String projectHour) {
        this.projectHour = new Hour(projectHour);
        return this;
    }

    /**
     * Sets the {@code Hour} of the {@code ModuleTaken} being built.
     */
    public ModuleTakenBuilder withPreparationHour(String preparationHour) {
        this.preparationHour = new Hour(preparationHour);
        return this;
    }

    /**
     * Builds a module taken with all the fields in the {@code ModuleTakenBuilder}
     */
    public ModuleTaken build() {
        return new ModuleTaken(moduleInfoCode, semester, expectedMinGrade, expectedMaxGrade,
                new Workload(lectureHour, tutorialHour, labHour, projectHour, preparationHour), tags);
    }

}
