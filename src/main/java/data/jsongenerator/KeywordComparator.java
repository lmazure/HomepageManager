package data.jsongenerator;

import java.util.Comparator;

public class KeywordComparator implements Comparator<Keyword>{

    @Override
    public int compare(final Keyword keyword0,
                       final Keyword keyword1) {

    	return keyword0.getId().compareTo(keyword1.getId());
    }
}
