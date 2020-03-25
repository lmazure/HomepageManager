package data.nodechecker.checker.nodeChecker;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;

import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagSelection.InclusionTagSelector;
import data.nodechecker.tagSelection.TagSelector;
import utils.XMLHelper;

public class MissingSpaceChecker extends NodeChecker {

	static final Set<String> s_authorizedList = new HashSet<String>(Arrays.asList("a.k.a.",
			                                                                      "Ampersand.js",
			                                                                      "asm.js",
			                                                                      "autosrb.pl",
                                                                                  "ASP.NET",
			                                                                      "Bubbl.us",
			                                                                      "clinicaltrials.gov",
			                                                                      "Clipboard.com",
			                                                                      "distributed.net",
			                                                                      "e.g.",
			                                                                      "Famo.us",
			                                                                      "Heu?reka",
			                                                                      "i.e.",
			                                                                      "Intl.RelativeTimeFormat",
			                                                                      "Intro.js",
			                                                                      "Java.Next",
			                                                                      "MANIFEST.MF",
			                                                                      "MSCTF.DLL",
			                                                                      "MVC.NET",
			                                                                      ".NET",
			                                                                      "Node.js",
			                                                                      "Normalize.css",
			                                                                      "OpenOffice.org",
			                                                                      "P.Anno",
			                                                                      "quantum.country",
			                                                                      "redhat.com",
			                                                                      "Sails.js",
			                                                                      "Three.js",
			                                                                      "tween.js",
			                                                                      "U.S.",
			                                                                      "Venus.js",
			                                                                      "view.json",
			                                                                      "Wallaby.js",
			                                                                      "xml:id",
			                                                                      "X.org",
			                                                                      "xsl:key"));

    static final InclusionTagSelector s_selector = new InclusionTagSelector(new String[] {
            NodeChecker.COMMENT,
            NodeChecker.TITLE,
            });
    
    @Override
    public TagSelector getTagSelector() {
        return s_selector;
    }

    @Override
    public NodeRule[] getRules() {
        final NodeRule a[]= new NodeRule[1];
        a[0] = new NodeRule() { @Override
        public CheckStatus checkElement(final Element e) { return checkMissingSpace(e);}
                            @Override
                            public String getDescription() { return "space is missing"; } };
        return a;
    }

    private CheckStatus checkMissingSpace(final Element e) {

        final List<String> list = XMLHelper.getFirstLevelTextContent(e);
        if (list.size() == 0) return null;

        for (final String l: list) {
        	if (Arrays.stream(l.split(" ")).anyMatch(MissingSpaceChecker::isInvalid)) {
                return new CheckStatus("\"" + e.getTextContent() + "\" is missing a space");        		
        	}
        }
        
       return null;
    }
    
    private static boolean isInvalid(final String str) {

    	for (final String a: s_authorizedList) {
    		final String s = str.replace(a, "");
    		if (containsNoLetter(s)) {
    			return false;
    		}
    	}

    	final char[] chars = str.toCharArray();

    	if (isVersionString(chars)) return false;

    	return containsPunctuation(chars);
    }

    private static boolean containsNoLetter(final String str) {
    	final char[] chars = str.toCharArray();
    	for (int i = 0; i < chars.length; i++) {
    		if (Character.isLetter(chars[i])) return false;
    	}    	
    	return true;    	
    }
    
    private static boolean isVersionString(final char[] chars) {
    	for (int i = 0; i < chars.length; i++) {
    		if (!isPunctuation(chars[i]) && !Character.isDigit(chars[i])) return false; // TODO sould test for dot instead of punctuation
    	}    	
    	return true;
    }
    
    private static boolean containsPunctuation(final char[] chars) {
    	for (int i = 0; i < chars.length - 1; i++) {
    		if (isPunctuation(chars[i])  && Character.isAlphabetic(chars[i + 1])) return true;
    	}    	
    	return false;
    }
    
    private static boolean isPunctuation(final char c) {    	
    	return (c == ',') || (c == '.') || (c == '!') || (c == '?') || (c == ':') || (c == ';');
    }
}
