package fr.mazure.homepagemanager.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * Tools for managing strings
 */
public class StringHelper {

    private static final Set<String> englishWords = new HashSet<>(Arrays.asList(
            "a", "an",
            "the",
            "this", "that", "these",
            "some", "many",
            "all",
            "not",
            "is", "are", "was", "were",
            "at",
            "of",
            "and", "or",
            "before",
            "after", "behind",
            "since",
            "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december",
            "by",
            "where",
            "in",
            "on",
            "for",
            "more",
            "less",
            "small",
            "big",
            "new",
            "map",
            "sky",
            "history", "story",
            "iron", "group", "subject",
            "japan"
        ));

    private static final Set<String> frenchWords = new HashSet<>(Arrays.asList(
            "un", "une",
            "le", "la", "les",
            "ces", "cet", "cette",
            "des", "plusieurs", "nombreux",
            "tous", "toutes",
            "pas",
            "est", "sont", "était", "étaient",
            "à",
            "de",
            "et", "ou",
            "avant", "devant",
            "après", "derrière",
            "depuis",
            "janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre", "octobre", "novembre", "décembre",
            "par",
            "où",
            "sur",
            "dans", "en",
            "pour",
            "plus",
            "moins",
            "petit", "petite", "petits", "petites",
            "grand", "grande", "grands", "grandes",
            "nouveau", "nouveaux", "nouvelle", "nouvelles",
            "carte",
            "ciel",
            "histoire", "groupe", "sujet",
            "fer",
            "japon"
        ));

    /**
     * Guess the langage of s string
     * @param text string
     * @return guessed language, Optional.empty if guess failure
     */
    public static Optional<Locale> guessLanguage(final String text) {

        final String[] words = text.split("\\W");

        int french = 0;
        int english = 0;

        for (final String word: words) {
            final String w = word.toLowerCase();
            if (englishWords.contains(w)) {
                english++;
            } else if (frenchWords.contains(w)) {
                french++;
            }
        }

        if (english > french) {
            return Optional.of(Locale.ENGLISH);
        }

        if (english < french) {
            return Optional.of(Locale.FRENCH);
        }

        return Optional.empty();
    }

    /**
     * search a substring in a string
     *
     * @param string string which is looked in
     * @param searchedString substring which is looked for
     * @param ignoreCase indicate if casing should be ignored
     * @param ignoreSpaceType indicate if type of space should be ignored
     * @return index of the substring in the string
     */
    public static int generalizedIndex(final String string,
                                       final String searchedString,
                                       final boolean ignoreCase,
                                       final boolean ignoreSpaceType) {
        final String s1 = ignoreCase ? string.toUpperCase() : string;
        final String ss1 = ignoreCase ? searchedString.toUpperCase() : searchedString;
        final String s2 = ignoreSpaceType ? normalizeSpace(s1) : s1;
        final String ss2 = ignoreSpaceType ? normalizeSpace(ss1) : ss1;
        return s2.indexOf(ss2);
    }

    /**
     * Replace the space characters by ' '
     *
     * @param string input string
     * @return normalized string
     */
    private static String normalizeSpace(final String string) {
        final int length = string.length();
        final StringBuilder builder = new StringBuilder(length);
        for (int offset = 0; offset < length;) {
            final int codepoint = string.codePointAt(offset);
            final int normalizedCodepoint = Character.isSpaceChar(codepoint) ? ' ' : codepoint;
            builder.appendCodePoint(normalizedCodepoint);
            offset += Character.charCount(codepoint);
        }
        return builder.toString();
    }

    /**
     * EXplain the difference between two strings
     *
     * @param str1 first string
     * @param str2 second string
     * @return explanation of the difference between two strings, null is the two strings are equal
     */
    public static String compareAndExplainDifference(final String str1,
                                                     final String str2) {
        if (str1.equals(str2)) {
            return null;
        }
        final int length1 = str1.length();
        final int length2 = str2.length();
        final StringBuilder builder = new StringBuilder();
        if (length1 != length2) {
            builder.append("The two strings to not have the same length\n");
        }
        for (int offset = 0; offset < length1;) {
            final int codepoint1 = str1.codePointAt(offset);
            builder.appendCodePoint(codepoint1);
            builder.append('-');
            builder.append(codepoint1);
            builder.append(' ');
            offset += Character.charCount(codepoint1);
        }
        builder.append('\n');
        for (int offset = 0; offset < length2;) {
            final int codepoint2 = str2.codePointAt(offset);
            builder.appendCodePoint(codepoint2);
            builder.append('-');
            builder.append(codepoint2);
            builder.append(' ');
            offset += Character.charCount(codepoint2);
        }
        builder.append('\n');
        return builder.toString();
    }
}
