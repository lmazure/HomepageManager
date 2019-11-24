package data.jsongenerator;

import java.util.Comparator;

public  class ArticleDataComparator implements Comparator<Article> {

    @Override
    public int compare(final Article a0, final Article a1) {
        
        final Integer y0 = a0.getDateYear();
        final Integer y1 = a1.getDateYear();
        if (y0 == null) return -1;
        if (y1 == null) return 1;
        if (y0.intValue() != y1.intValue()) return y0.intValue() - y1.intValue();
        
        final Integer m0 = a0.getDateMonth();
        final Integer m1 = a1.getDateMonth();
        if (m0 == null) return -1;
        if (m1 == null) return 1;
        if (m0.intValue() != m1.intValue()) return m0.intValue() - m1.intValue();
        
        final Integer d0 = a0.getDateDay();
        final Integer d1 = a1.getDateDay();
        if (d0 == null) return -1;
        if (d1 == null) return 1;
        return d0.intValue() - d1.intValue();
    }       
}
