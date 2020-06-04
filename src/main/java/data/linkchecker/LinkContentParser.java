package data.linkchecker;

public class LinkContentParser {

	private final String _data;
	private String _language;
	
	public LinkContentParser(final String data) {
		_data = data;
	}

	public String getLanguage() {
		
		if (_language == null) {
			_language= extractLanguage();
		}
		
		return _language;
	}

	private String extractLanguage() {

		final String data = _data.replaceAll("<SCRIPT *>[^>]*</SCRIPT *>", "")
				                 .replaceAll("<[^>]*>", "");
		final String[] words = data.split("\\W");
		
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
