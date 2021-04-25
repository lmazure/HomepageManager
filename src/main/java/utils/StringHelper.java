package utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

public class StringHelper {

    public static final Set<String> englishWords = new HashSet<>(Arrays.asList(
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
            "iron",
            "japan"
        ));

    public static final Set<String> frenchWords = new HashSet<>(Arrays.asList(
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
            "histoire",
            "fer",
            "japon"
        ));

    public static Optional<Locale> guessLanguage(final String text) {

        //System.out.println(text);

        final String[] words = text.split("\\W");

        int french = 0;
        int english = 0;

        for (final String word: words) {
            final String w = word.toLowerCase();
            if (englishWords.contains(w))  {
                english++;
                // System.out.println("en " + english + " " + word);
            } else if (frenchWords.contains(w))  {
                french++;
                // System.out.println("fr " + french + " " + word);
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
     * Convert a string to URL, return null if the string is an invalid URL
     *
     * @param str
     * @return
     */
    public static URL convertStringToUrl(final String str) {
        try {
            return new URL(str);
        } catch (@SuppressWarnings("unused") final MalformedURLException e) {
            return null;
        }
    }
}
