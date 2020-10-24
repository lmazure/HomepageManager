package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class StringHelper {

    public static final Set<String> englishWords = new HashSet<String>(Arrays.asList(
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
            "since",
            "by",
            "in",
            "small",
            "big",
            "new",
            "map",
            "sky",
            "japan"
        ));

    public static final Set<String> frenchWords = new HashSet<String>(Arrays.asList(
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
            "depuis",
            "par",
            "dans",
            "petit", "petite", "petits", "petites",
            "grand", "grande", "grands", "grandes",
            "nouveau", "nouveaux", "nouvelle", "nouvelles",
            "carte",
            "ciel",
            "japon"
        ));

    public static Locale guessLanguage(final String text) {

        //System.out.println(text);

        final String[] words = text.split("\\W");

        int french = 0;
        int english = 0;

        for (final String word: words) {
            final String w = word.toLowerCase();
            if (englishWords.contains(w))  {
                //System.out.println("en " + english + " " + word);
                english++;
            } else if (frenchWords.contains(w))  {
                //System.out.println("fr " + french + " " + word);
                french++;
            }
        }

        if (english > french) {
            return Locale.ENGLISH;
        }

        return Locale.FRENCH;
    }
}
