package utils;

public class StringHelper {

	public static String guessLanguage(final String text) {
		
		final String[] words = text.split("\\W");
		
		int french = 0;
		int english = 0;		
		
		for (String word: words) {
			if (word.equalsIgnoreCase("a") ||
				word.equalsIgnoreCase("an") ||
				word.equalsIgnoreCase("the") ||
				word.equalsIgnoreCase("some") ||
				word.equalsIgnoreCase("is") ||
				word.equalsIgnoreCase("are") ||
				word.equalsIgnoreCase("at"))  {
				//System.out.println("en " + english + " " + word);
			    english++;
			} else if (word.equalsIgnoreCase("un") ||
				word.equalsIgnoreCase("une") ||
				word.equalsIgnoreCase("le") ||
				word.equalsIgnoreCase("la") ||
				word.equalsIgnoreCase("les") ||
				word.equalsIgnoreCase("des") ||
				word.equalsIgnoreCase("est") ||
				word.equalsIgnoreCase("sont") ||
				word.equalsIgnoreCase("à"))  {
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
