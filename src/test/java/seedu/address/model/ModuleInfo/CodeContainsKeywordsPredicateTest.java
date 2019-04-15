package seedu.address.model.ModuleInfo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.address.model.moduleinfo.CodeContainsKeywordsPredicate;
import seedu.address.model.moduleinfo.ModuleInfo;
import seedu.address.testutil.ModuleInfoBuilder;


public class CodeContainsKeywordsPredicateTest {

    @Test
    public void predicateSplitterTest() {
        List<String> testPredicateKeywordsList = Collections.singletonList("keywords+are+split");
        CodeContainsKeywordsPredicate testPredicate = new CodeContainsKeywordsPredicate(testPredicateKeywordsList);
        String[] expectedArray = {"keywords", "are", "split"};
        assertArrayEquals(expectedArray, testPredicate.splitPredicate(testPredicateKeywordsList.get(0)));

    }

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        CodeContainsKeywordsPredicate firstPredicate = new CodeContainsKeywordsPredicate(firstPredicateKeywordList);
        CodeContainsKeywordsPredicate secondPredicate = new CodeContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        CodeContainsKeywordsPredicate firstPredicateCopy = new CodeContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different moduleTaken -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void combinationSearchTest() {
        CodeContainsKeywordsPredicate testPredicate = new CodeContainsKeywordsPredicate(null);
        ModuleInfo typicalModule = new ModuleInfoBuilder().build();

        String[] correctKeywords = {"Software", "Engineering"};
        assertTrue(testPredicate.combinationSearch(correctKeywords, typicalModule));

        String[] correctKeyword = {"Software"};
        assertTrue(testPredicate.combinationSearch(correctKeyword, typicalModule));

        String[] incorrectKeyword = {"Programming"};
        assertFalse(testPredicate.combinationSearch(incorrectKeyword, typicalModule));

        String[] incorrectKeywords = {"Programming", "Methodology"};
        assertFalse(testPredicate.combinationSearch(incorrectKeywords, typicalModule));
    }

    @Test
    public void test() {
        List<String> keywords = Collections.singletonList("CS2103T");
        CodeContainsKeywordsPredicate testPredicate = new CodeContainsKeywordsPredicate(keywords);

        ModuleInfo typicalModule = new ModuleInfoBuilder().build();

        assertTrue(testPredicate.test(typicalModule));

        keywords = Collections.singletonList("Software+Engineering");
        testPredicate = new CodeContainsKeywordsPredicate(keywords);
        assertTrue(testPredicate.test(typicalModule));

        keywords = Collections.singletonList("Engineering");
        testPredicate = new CodeContainsKeywordsPredicate(keywords);
        assertTrue(testPredicate.test(typicalModule));

        keywords = Collections.singletonList("Programming");
        testPredicate = new CodeContainsKeywordsPredicate(keywords);
        assertFalse(testPredicate.test(typicalModule));
    }
}
