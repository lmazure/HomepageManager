package fr.mazure.homepagemanager.data.jsongenerator.test;

import java.io.File;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.jsongenerator.Article;
import fr.mazure.homepagemanager.data.jsongenerator.ArticleComparator;
import fr.mazure.homepagemanager.data.jsongenerator.Link;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;

/**
 *
 */
class ArticleComparatorTest {

    @SuppressWarnings("static-method")
    @Test
    void compareSimpleStrings() {

        // --- arrange ---
        final LinkFormat[] formats = { LinkFormat.HTML };
        final Locale[] languages = { Locale.FRENCH };
        final Article data1 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data1.addLink(new Link("aa", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final Article data2 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data2.addLink(new Link("ab", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final ArticleComparator comparator = new ArticleComparator();

        // --- act ---
        final int result = comparator.compare(data1, data2);

        // --- assert ---
        Assertions.assertTrue(result < 0);
    }

    @SuppressWarnings("static-method")
    @Test
    void compareStringsWithSpace() {

        // --- arrange ---
        final LinkFormat[] formats = { LinkFormat.HTML };
        final Locale[] languages = { Locale.FRENCH };
        final Article data1 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data1.addLink(new Link("a a", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final Article data2 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data2.addLink(new Link("aa", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final ArticleComparator comparator = new ArticleComparator();

        // --- act ---
        final int result = comparator.compare(data1, data2);

        // --- assert ---
        Assertions.assertTrue(result < 0);
    }

    @SuppressWarnings("static-method")
    @Test
    void compareStringsWithSpaceAndSpecialLetter() {

        // --- arrange ---
        final LinkFormat[] formats = { LinkFormat.HTML };
        final Locale[] languages = { Locale.FRENCH };
        final Article data1 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data1.addLink(new Link("a a", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final Article data2 = new Article(new File(""), Optional.of(LocalDate.of(2000, 1, 1)));
        data2.addLink(new Link("aØ", null, "url", null, null, formats, languages, Optional.empty(), Optional.empty(), Optional.empty()));
        final ArticleComparator comparator = new ArticleComparator();

        // --- act ---
        final int result = comparator.compare(data1, data2);

        // --- assert ---
        Assertions.assertTrue(result < 0);
    }
}
