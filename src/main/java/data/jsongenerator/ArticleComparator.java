package data.jsongenerator;

import java.util.Comparator;

public class ArticleComparator implements Comparator<Article>
{
    @Override
    public int compare(final Article arg0,
                       final Article arg1) {
        
        final String title0 = arg0.getLinks()[0].getTitle();
        final String title1 = arg1.getLinks()[0].getTitle();
        final int c1 = StringHelper.compare(title0, title1);
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
        return StringHelper.compare(subtitle0, subtitle1);
    }
}
