package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import seedu.address.logic.commands.DisplaymodCommand;
import seedu.address.model.moduleinfo.CodeContainsKeywordsPredicate;


public class DisplaymodCommandPaserTest {
    private DisplaymodCommandParser parser = new DisplaymodCommandParser();

    @Test
    public void parse_emptyArgs_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_empty_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "cs", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidInput_failure() {
        String invalidInput = " c/C1010";
        assertParseFailure(parser, invalidInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
        invalidInput = " c/CS1010 CS2103T";
        assertParseFailure(parser, invalidInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
        invalidInput = " n/software Engineering";
        assertParseFailure(parser, invalidInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validinput_test() {
        String validSingleCodeInput = " c/CS1010";
        CodeContainsKeywordsPredicate predicates = new CodeContainsKeywordsPredicate(Collections.singletonList(
                "CS1010"));
        DisplaymodCommand expectedCmd = new DisplaymodCommand(predicates);
        assertParseSuccess(parser, validSingleCodeInput, expectedCmd);

        String validMultiCodeInput = " c/CS1010,CS2103T";
        String[] validList = {"CS1010", "CS2103T"};
        predicates = new CodeContainsKeywordsPredicate(Arrays.asList(validList));
        expectedCmd = new DisplaymodCommand(predicates);
        assertParseSuccess(parser, validMultiCodeInput, expectedCmd);

        String validSingleWordInput = " n/Software";
        predicates = new CodeContainsKeywordsPredicate(Collections.singletonList(
                "Software"));
        expectedCmd = new DisplaymodCommand(predicates);
        assertParseSuccess(parser, validSingleWordInput, expectedCmd);

        String validMultiWordInput = " n/Software+Engineering";
        predicates = new CodeContainsKeywordsPredicate(Collections.singletonList("Software+Engineering"));
        expectedCmd = new DisplaymodCommand(predicates);
        assertParseSuccess(parser, validMultiWordInput, expectedCmd);
    }

    @Test
    public void parse_invalidcode_failture() {
        String invalidInput = "C1010";
        assertParseFailure(parser, invalidInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidword_failure() {
        String invalidInput = "@";
        assertParseFailure(parser, invalidInput, String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DisplaymodCommand.MESSAGE_USAGE));
    }
}
