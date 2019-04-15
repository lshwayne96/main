package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MAX_GRADE_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MAX_GRADE_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MIN_GRADE_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EXPECTED_MIN_GRADE_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LECTURE_HOUR_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LECTURE_HOUR_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_INFO_CODE_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_MODULE_INFO_CODE_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SEMESTER_CS1010;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SEMESTER_CS2103T;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.model.GradTrak;
import seedu.address.model.limits.SemesterLimit;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.CapAverage;
import seedu.address.model.moduletaken.Hour;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;

/**
 * A utility class containing a list of {@code ModuleTaken} objects to be used in tests.
 */
public class TypicalModuleTaken {

    public static final ModuleTaken CS2103T = new ModuleTakenBuilder().withModuleInfoCode("CS2103T")
            .withExpectedMinGrade("F").withExpectedMaxGrade("A").withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0")
            .withSemester("Y1S2")
            .withTags("friends").build();
    public static final ModuleTaken CS2101 = new ModuleTakenBuilder().withModuleInfoCode("CS2101")
            .withExpectedMinGrade("D")
            .withExpectedMaxGrade("A").withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").withSemester("Y3S2")
            .withTags("owesMoney", "friends").build();
    public static final ModuleTaken CS1010S = new ModuleTakenBuilder()
            .withModuleInfoCode("CS1010S")
            .withSemester("Y5S2")
            .withExpectedMinGrade("F")
            .withExpectedMaxGrade("A")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").build();
    public static final ModuleTaken CS1010X = new ModuleTakenBuilder()
            .withModuleInfoCode("CS1010X")
            .withSemester("Y5S2")
            .withExpectedMinGrade("F")
            .withExpectedMaxGrade("A")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0")
            .withTags("friends").build();
    public static final ModuleTaken MA1521 = new ModuleTakenBuilder()
            .withModuleInfoCode("MA1521")
            .withSemester("Y3S1")
            .withExpectedMinGrade("C")
            .withExpectedMaxGrade("A")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").build();
    public static final ModuleTaken LSM1301 = new ModuleTakenBuilder()
            .withModuleInfoCode("LSM1301")
            .withSemester("Y4S2")
            .withExpectedMinGrade("D")
            .withExpectedMaxGrade("A")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").build();
    public static final ModuleTaken GER1000 = new ModuleTakenBuilder()
            .withModuleInfoCode("GER1000")
            .withSemester("Y4S1")
            .withExpectedMinGrade("C")
            .withExpectedMaxGrade("B")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").build();

    // Manually added
    public static final ModuleTaken CS2030 = new ModuleTakenBuilder()
            .withModuleInfoCode("CS2030")
            .withSemester("Y1S1")
            .withExpectedMinGrade("B")
            .withExpectedMaxGrade("A")
            .withLectureHour("0")
            .withTutorialHour("0")
            .withLabHour("0")
            .withProjectHour("0")
            .withPreparationHour("0").build();
    public static final ModuleTaken CS2040 = new ModuleTakenBuilder()
            .withModuleInfoCode("CS2040")
            .withSemester("Y3S2")
            .withExpectedMinGrade("D")
            .withExpectedMaxGrade("A")
            .withLectureHour("0").build();

    public static final SemesterLimit Y1S1 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y1S2 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y2S1 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y2S2 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y3S1 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y3S2 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y4S1 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y4S2 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y5S1 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));
    public static final SemesterLimit Y5S2 = new SemesterLimit(new CapAverage(1), new CapAverage(5), new Hour("4"),
            new Hour("8"), new Hour("2"), new Hour("5"), new Hour("2"), new Hour("5"),
            new Hour("2"), new Hour("5"), new Hour("6"), new Hour("10"));

    // Manually added - ModuleTaken's details found in {@code CommandTestUtil}
    public static final ModuleTaken DEFAULT_MODULE_CS2103T = new ModuleTakenBuilder()
            .withModuleInfoCode(VALID_MODULE_INFO_CODE_CS2103T)
            .withSemester(VALID_SEMESTER_CS2103T)
            .withExpectedMinGrade(VALID_EXPECTED_MIN_GRADE_CS2103T)
            .withExpectedMaxGrade(VALID_EXPECTED_MAX_GRADE_CS2103T)
            .withLectureHour(VALID_LECTURE_HOUR_CS2103T)
            .withTags(VALID_TAG_FRIEND).build();
    public static final ModuleTaken DEFAULT_MODULE_CS1010 = new ModuleTakenBuilder()
            .withModuleInfoCode(VALID_MODULE_INFO_CODE_CS1010)
            .withSemester(VALID_SEMESTER_CS1010)
            .withExpectedMinGrade(VALID_EXPECTED_MIN_GRADE_CS1010)
            .withExpectedMaxGrade(VALID_EXPECTED_MAX_GRADE_CS1010)
            .withLectureHour(VALID_LECTURE_HOUR_CS1010)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final String KEYWORD_MATCHING_CS2103T = "CS2103T";
    public static final String KEYWORD_MATCHING_CS1010S = "CS1010S";
    public static final String KEYWORD_MATCHING_MA1521 = "MA1521";
    public static final String KEYWORD_MATCHING_CS = "cs";
    public static final String KEYWORD_MATCHING_MA = "ma";
    public static final String KEYWORD_MATCHING_GE = "ge";

    private TypicalModuleTaken() {} // prevents instantiation

    /**
     * Returns an {@code GradTrak} with all the typical persons.
     */
    public static GradTrak getTypicalGradTrak() {
        GradTrak gt = new GradTrak();
        for (ModuleTaken moduleTaken : getTypicalModulesTaken()) {
            gt.addModuleTaken(moduleTaken);
        }
        List<SemesterLimit> semList = new ArrayList<>();
        for (SemesterLimit semesterLimit : getTypicalSemesterLimits()) {
            semList.add(semesterLimit);
        }
        gt.setSemesterLimits(semList);
        gt.setCurrentSemester(Semester.Y1S1);

        return gt;
    }

    public static List<ModuleTaken> getTypicalModulesTaken() {
        return new ArrayList<>(Arrays.asList(CS1010S, CS1010X, MA1521, LSM1301, GER1000));
    }

    public static List<SemesterLimit> getTypicalSemesterLimits() {
        return new ArrayList<>(Arrays.asList(Y1S1, Y1S2, Y2S1, Y2S2, Y3S1, Y3S2, Y4S1, Y4S2, Y5S1, Y5S2));
    }

    public static List<ModuleInfoCode> getTypicalModulesInfoCodes() {
        List<ModuleInfoCode> list = getTypicalModulesTaken()
                .stream().map(ModuleTaken::getModuleInfoCode).collect(Collectors.toList());
        list.addAll(List.of(new ModuleInfoCode("GEQ1000"), new ModuleInfoCode("GEH1000"), new ModuleInfoCode("GES1000"),
                new ModuleInfoCode("GET1000")));
        return list;
    }

    public static List<ModuleTaken> getGeList() {
        List<ModuleTaken> geList = new ArrayList<>();

        geList.add(new ModuleTakenBuilder().withModuleInfoCode("GEH1001").build());
        geList.add(new ModuleTakenBuilder().withModuleInfoCode("GEQ1000").build());
        geList.add(new ModuleTakenBuilder().withModuleInfoCode("GER1000").build());
        geList.add(new ModuleTakenBuilder().withModuleInfoCode("GES1002").build());
        geList.add(new ModuleTakenBuilder().withModuleInfoCode("GET1001").build());

        return geList;
    }

    public static List<ModuleTaken> getCoreList() {
        List<ModuleTaken> coreList = new ArrayList<>();

        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS1101S").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS1231").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2030").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2040").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2100").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2103T").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2105").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2106").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3230").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("IS1103").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2101").build());
        coreList.add(new ModuleTakenBuilder().withModuleInfoCode("ES2660").build());

        return coreList;
    }

    public static List<ModuleTaken> getAlgoBdList() {
        List<ModuleTaken> algoBdList = new ArrayList<>();

        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3236").build());
        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4231").build());
        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3233").build());
        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2107").build());
        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4257").build());
        algoBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4268").build());

        return algoBdList;
    }

    public static List<ModuleTaken> getAiBdList() {
        List<ModuleTaken> aiBdList = new ArrayList<>();

        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3243").build());
        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3244").build());
        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4243").build());
        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4231").build());
        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2107").build());
        aiBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4257").build());

        return aiBdList;
    }

    public static List<ModuleTaken> getSeBdList() {
        List<ModuleTaken> seBdList = new ArrayList<>();

        seBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3219").build());
        seBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4211").build());
        seBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4231").build());
        seBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS2107").build());
        seBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS4257").build());

        return seBdList;
    }

    public static List<ModuleTaken> getIeList() {
        List<ModuleTaken> ieList = new ArrayList<>();

        ieList.add(new ModuleTakenBuilder().withModuleInfoCode("CP3200").build());
        ieList.add(new ModuleTakenBuilder().withModuleInfoCode("CP3202").build());

        return ieList;
    }

    public static List<ModuleTaken> getProjectBdList() {
        List<ModuleTaken> projectBdList = new ArrayList<>();

        projectBdList.add(new ModuleTakenBuilder().withModuleInfoCode("CS3203").build());

        return projectBdList;
    }

    public static List<ModuleTaken> getMathScienceList() {
        List<ModuleTaken> mathScienceList = new ArrayList<>();

        mathScienceList.add(new ModuleTakenBuilder().withModuleInfoCode("MA1521").build());
        mathScienceList.add(new ModuleTakenBuilder().withModuleInfoCode("MA1101R").build());
        mathScienceList.add(new ModuleTakenBuilder().withModuleInfoCode("ST2334").build());
        mathScienceList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1221").build());

        return mathScienceList;
    }

    public static List<ModuleTaken> getUeList() {
        List<ModuleTaken> ueList = new ArrayList<>();

        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1141").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1144").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("LAJ1201").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("LAJ2201").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1142").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1143").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC3232").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC4230").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("LAJ3201").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("LAJ3202").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1222").build());
        ueList.add(new ModuleTakenBuilder().withModuleInfoCode("PC1322").build());

        return ueList;
    }

    public static List<ModuleTaken> getFullAlgoList() {
        return Stream.of(getGeList(), getCoreList(), getAlgoBdList(), getIeList(), getProjectBdList(),
                getMathScienceList(), getUeList())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<ModuleTaken> getFullAiList() {
        return Stream.of(getGeList(), getCoreList(), getAiBdList(), getIeList(), getProjectBdList(),
                getMathScienceList(), getUeList())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static List<ModuleTaken> getFullSeList() {
        return Stream.of(getGeList(), getCoreList(), getSeBdList(), getIeList(), getProjectBdList(),
                getMathScienceList(), getUeList())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
