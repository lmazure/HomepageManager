package data.jsongenerator2;

import java.util.Comparator;

public class ArticleComparator2 implements Comparator<Article2>
{
    @Override
    public int compare(final Article2 arg0,
                       final Article2 arg1) {
        
        final String title0 = arg0.getLinks()[0].getTitle();
        final String title1 = arg1.getLinks()[0].getTitle();
        final int c1 = StringHelper2.compare(title0, title1);
        if (c1 != 0) return c1;
        
        final String subtitle0 = arg0.getLinks()[0].getSubtitle();
        final String subtitle1 = arg1.getLinks()[0].getSubtitle();
        if ( subtitle0 == null ) {
            if ( subtitle1 == null ) {
                return 0;
            }
            return 1;
        }
        if ( subtitle1 == null ) {
            return -1;
        }
        return StringHelper2.compare(subtitle0, subtitle1);
    }
}
