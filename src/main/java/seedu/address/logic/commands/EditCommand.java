package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPECTED_MAX_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXPECTED_MIN_GRADE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LECTURE_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODULE_INFO_CODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PREPARATION_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PROJECT_HOUR;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SEMESTER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TUTORIAL_HOUR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.model.moduleinfo.ModuleInfoCode;
import seedu.address.model.moduletaken.Grade;
import seedu.address.model.moduletaken.Hour;
import seedu.address.model.moduletaken.ModuleTaken;
import seedu.address.model.moduletaken.Semester;
import seedu.address.model.moduletaken.Workload;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing moduleTaken in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the "
            + "moduleTaken identified "
            + "by the index number used in the displayed moduleTaken list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_MODULE_INFO_CODE + "NAME] "
            + "[" + PREFIX_SEMESTER + "SEMESTER] "
            + "[" + PREFIX_EXPECTED_MIN_GRADE + "EXPECTED MIN GRADE] "
            + "[" + PREFIX_EXPECTED_MAX_GRADE + "EXPECTED MAX GRADE] "
            + "[" + PREFIX_LECTURE_HOUR + "LECTURE HOUR] "
            + "[" + PREFIX_TUTORIAL_HOUR + "TUTORIAL HOUR] "
            + "[" + PREFIX_LAB_HOUR + "LAB HOUR] "
            + "[" + PREFIX_PROJECT_HOUR + "PROJECT HOUR] "
            + "[" + PREFIX_PREPARATION_HOUR + "PREPARATION HOUR] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SEMESTER + "Y3S1 "
            + PREFIX_EXPECTED_MIN_GRADE + "B";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited ModuleTaken: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This moduleTaken already exists in the address book.";

    private final Index index;
    private final EditModuleTakenDescriptor editPersonDescriptor;

    /**
     * @param index of the moduleTaken in the filtered moduleTaken list to edit
     * @param editPersonDescriptor details to edit the moduleTaken with
     */
    public EditCommand(Index index, EditModuleTakenDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditModuleTakenDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException {
        requireNonNull(model);
        List<ModuleTaken> lastShownList = model.getFilteredModulesTakenList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_MODULETAKEN_DISPLAYED_INDEX);
        }

        ModuleTaken moduleTakenToEdit = lastShownList.get(index.getZeroBased());
        ModuleTaken editedModuleTaken = createEditedPerson(moduleTakenToEdit, editPersonDescriptor);

        if (!moduleTakenToEdit.getModuleInfoCode().equals(editedModuleTaken.getModuleInfoCode())
                && !model.canEditModuleTaken(moduleTakenToEdit, editedModuleTaken)) {
            throw new CommandException(Messages.MESSAGE_PREREQUISITE_VIOLATED);
        }

        if (editedModuleTaken.getExpectedMinGrade().getGradePoint()
                > editedModuleTaken.getExpectedMaxGrade().getGradePoint()) {
            throw new CommandException(Messages.MESSAGE_GRADES_OUT_OF_ORDER);
        }

        if ((editPersonDescriptor.getExpectedMinGrade().isPresent()
                || editPersonDescriptor.getExpectedMaxGrade().isPresent())
                && editedModuleTaken.getSemester().getIndex() < model.getCurrentSemester().getIndex()) {
            throw new CommandException(Messages.MESSAGE_GRADES_NOT_FINALIZED_BEFORE_SEMESTER);
        }

        if (!editedModuleTaken.getExpectedMaxGrade().isCountedInCap()) {
            throw new CommandException(Messages.MESSAGE_MAX_GRADE_MUST_BE_COUNTED);
        }

        if (!moduleTakenToEdit.isSameModuleTaken(editedModuleTaken) && model.hasModuleTaken(editedModuleTaken)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        ModuleInfo moduleInfo = model.getModuleInfoList().getModule(editedModuleTaken.getModuleInfoCode().value);

        if (moduleInfo == null) {
            throw new CommandException(Messages.MESSAGE_MODULE_DOES_NOT_EXIST);
        }

        editedModuleTaken.setWorkload(new Workload(moduleInfo.getModuleInfoWorkload()));

        model.setModuleTaken(moduleTakenToEdit, editedModuleTaken);
        model.updateFilteredModulesTakenList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitGradTrak();
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, editedModuleTaken));
    }

    /**
     * Creates and returns a {@code ModuleTaken} with the details of {@code moduleTakenToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static ModuleTaken createEditedPerson(ModuleTaken moduleTakenToEdit,
                                                  EditModuleTakenDescriptor editPersonDescriptor) {
        assert moduleTakenToEdit != null;

        ModuleInfoCode updatedName = editPersonDescriptor.getModuleInfoCode()
                .orElse(moduleTakenToEdit.getModuleInfoCode());
        Semester updatedSemester = editPersonDescriptor.getSemester().orElse(moduleTakenToEdit.getSemester());
        Grade updatedExpectedMinGrade = editPersonDescriptor
                .getExpectedMinGrade().orElse(moduleTakenToEdit.getExpectedMinGrade());
        Grade updatedExpectedMaxGrade = editPersonDescriptor
                .getExpectedMaxGrade().orElse(moduleTakenToEdit.getExpectedMaxGrade());
        Hour updatedLectureHour = editPersonDescriptor
                .getLectureHour().orElse(moduleTakenToEdit.getLectureHour());
        Hour updatedTutorialHour = editPersonDescriptor
                .getTutorialHour().orElse(moduleTakenToEdit.getTutorialHour());
        Hour updatedLabHour = editPersonDescriptor
                .getLabHour().orElse(moduleTakenToEdit.getLabHour());
        Hour updatedProjectHour = editPersonDescriptor
                .getProjectHour().orElse(moduleTakenToEdit.getProjectHour());
        Hour updatedPreparationHour = editPersonDescriptor
                .getPreparationHour().orElse(moduleTakenToEdit.getPreparationHour());
        Workload updatedWorkload = new Workload(updatedLectureHour, updatedTutorialHour, updatedLabHour,
                updatedProjectHour, updatedPreparationHour);
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(moduleTakenToEdit.getTags());

        return new ModuleTaken(updatedName, updatedSemester, updatedExpectedMinGrade, updatedExpectedMaxGrade,
                updatedWorkload, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the moduleTaken with. Each non-empty field value will replace the
     * corresponding field value of the moduleTaken.
     */
    public static class EditModuleTakenDescriptor {
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

        public EditModuleTakenDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditModuleTakenDescriptor(EditModuleTakenDescriptor toCopy) {
            setModuleInfoCode(toCopy.moduleInfoCode);
            setSemester(toCopy.semester);
            setExpectedMinGrade(toCopy.expectedMinGrade);
            setExpectedMaxGrade(toCopy.expectedMaxGrade);
            setLectureHour(toCopy.lectureHour);
            setTutorialHour(toCopy.tutorialHour);
            setLabHour(toCopy.labHour);
            setProjectHour(toCopy.projectHour);
            setPreparationHour(toCopy.preparationHour);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(moduleInfoCode, semester, expectedMinGrade, expectedMaxGrade, tags);
        }

        public void setModuleInfoCode(ModuleInfoCode moduleInfoCode) {
            this.moduleInfoCode = moduleInfoCode;
        }

        public Optional<ModuleInfoCode> getModuleInfoCode() {
            return Optional.ofNullable(moduleInfoCode);
        }

        public void setSemester(Semester semester) {
            this.semester = semester;
        }

        public Optional<Semester> getSemester() {
            return Optional.ofNullable(semester);
        }

        public void setExpectedMinGrade(Grade expectedMinGrade) {
            this.expectedMinGrade = expectedMinGrade;
        }

        public Optional<Grade> getExpectedMinGrade() {
            return Optional.ofNullable(expectedMinGrade);
        }

        public void setExpectedMaxGrade(Grade expectedMaxGrade) {
            this.expectedMaxGrade = expectedMaxGrade;
        }

        public Optional<Grade> getExpectedMaxGrade() {
            return Optional.ofNullable(expectedMaxGrade);
        }

        public void setLectureHour(Hour lectureHour) {
            this.lectureHour = lectureHour;
        }

        public Optional<Hour> getLectureHour() {
            return Optional.ofNullable(lectureHour);
        }

        public void setTutorialHour(Hour tutorialHour) {
            this.tutorialHour = tutorialHour;
        }

        public Optional<Hour> getTutorialHour() {
            return Optional.ofNullable(tutorialHour);
        }

        public void setLabHour(Hour labHour) {
            this.labHour = labHour;
        }

        public Optional<Hour> getLabHour() {
            return Optional.ofNullable(labHour);
        }

        public void setProjectHour(Hour projectHour) {
            this.projectHour = projectHour;
        }

        public Optional<Hour> getProjectHour() {
            return Optional.ofNullable(projectHour);
        }

        public void setPreparationHour(Hour preparationHour) {
            this.preparationHour = preparationHour;
        }

        public Optional<Hour> getPreparationHour() {
            return Optional.ofNullable(preparationHour);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditModuleTakenDescriptor)) {
                return false;
            }

            // state check
            EditModuleTakenDescriptor e = (EditModuleTakenDescriptor) other;

            return getModuleInfoCode().equals(e.getModuleInfoCode())
                    && getSemester().equals(e.getSemester())
                    && getExpectedMinGrade().equals(e.getExpectedMinGrade())
                    && getExpectedMaxGrade().equals(e.getExpectedMaxGrade())
                    && getLectureHour().equals(e.getLectureHour())
                    && getTutorialHour().equals(e.getTutorialHour())
                    && getLabHour().equals(e.getLabHour())
                    && getProjectHour().equals(e.getProjectHour())
                    && getPreparationHour().equals(e.getPreparationHour())
                    && getTags().equals(e.getTags());
        }
    }
}
