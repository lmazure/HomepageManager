package data.jsongenerator.test;


import org.junit.jupiter.api.Assertions;

import java.io.File;

import org.junit.jupiter.api.Test;

import data.jsongenerator.Article;
import data.jsongenerator.ArticleComparator;
import data.jsongenerator.Link;

class ArticleComparatorTest {

    @Test
    void compareSimpleStrings() {

        // --- arrange ---
        final String[] formats = { "HTML" };
        final String[] languages = { "HTML" };
        final Article data1 = new Article(new File(""), 2000, 1, 1);
        data1.addLink(new Link(data1, "aa", null, "url", null, null, formats, languages, null, null, null) );
        final Article data2 = new Article(new File(""), 2000, 1, 1);
        data2.addLink(new Link(data2, "ab", null, "url", null, null, formats, languages, null, null, null) );
        final ArticleComparator comparator = new ArticleComparator();
        
        // --- act ---
        final int result = comparator.compare(data1, data2); 
        
        // --- assert ---
        Assertions.assertTrue(result < 0);
    }

    @Test
    void compareStringsWithSpace() {
        
        // --- arrange ---
        final String[] formats = { "HTML" };
        final String[] languages = { "HTML" };
        final Article data1 = new Article(new File(""), 2000, 1, 1);
        data1.addLink(new Link(data1, "a a", null, "url", null, null, formats, languages, null, null, null) );
        final Article data2 = new Article(new File(""), 2000, 1, 1);
        data2.addLink(new Link(data2, "aa", null, "url", null, null, formats, languages, null, null, null) );
        final ArticleComparator comparator = new ArticleComparator();
        
        // --- act ---
        final int result = comparator.compare(data1, data2); 
        
        // --- assert ---
        Assertions.assertTrue(result < 0);
    }

    @Test
    void compareStringsWithSpaceAndSpecialLetter() {
        
        // --- arrange ---
        final String[] formats = { "HTML" };
        final String[] languages = { "HTML" };
        final Article data1 = new Article(new File(""), 2000, 1, 1);
        data1.addLink(new Link(data1, "a a", null, "url", null, null, formats, languages, null, null, null) );
        final Article data2 = new Article(new File(""), 2000, 1, 1);
        data2.addLink(new Link(data2, "aØ", null, "url", null, null, formats, languages, null, null, null) );
        final ArticleComparator comparator = new ArticleComparator();
        
        // --- act ---
        final int result = comparator.compare(data1, data2); 
        
        // --- assert ---
        Assertions.assertTrue(result < 0);
    }
}