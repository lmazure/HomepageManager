package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StringHelper {

	public static final Set<String> englishWords = new HashSet<String>(Arrays.asList(
			"a", "an",
			"the",
			"this", "that", "these",
			"some",
			"not",
			"is",
			"are",
			"at",
			"and",
			"or",
			"in",
			"small",
			"big",
			"map",
			"japan"
		));
	
	public static final Set<String> frenchWords = new HashSet<String>(Arrays.asList(
			"le", "la", "les",
			"des",
			"ces",
			"pas",
			"est",
			"sont",
			"à",
			"et",
			"ou",
			"dans",
			"petit", "petite", "petits",	"petites",
			"grand", "grande", "grands",	"grandes",
			"carte",
			"japon"
		));
	
	public static String guessLanguage(final String text) {

		//System.out.println(text);
		
		final String[] words = text.split("\\W");
		
		int french = 0;
		int english = 0;		
		
		for (String word: words) {
			word = word.toLowerCase();
			if (englishWords.contains(word))  {
				//System.out.println("en " + english + " " + word);
			    english++;
			} else if (frenchWords.contains(word))  {
				//System.out.println("fr " + french + " " + word);
				french++;
			}
		}
		
		if (english > french) {
			return "en";
		}
		
		return "fr";
	}
}
