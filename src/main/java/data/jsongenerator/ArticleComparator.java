package data.jsongenerator;

import java.util.Comparator;
import java.util.Optional;

public class ArticleComparator implements Comparator<Article>
{
    @Override
    public int compare(final Article arg0,
                       final Article arg1) {
        
        final String title0 = arg0.getLinks()[0].getTitle();
        final String title1 = arg1.getLinks()[0].getTitle();
        final int c1 = StringHelper.compare(title0, title1);
        if (c1 != 0) return c1;
        
        final Optional<String> subtitle0 = arg0.getLinks()[0].getSubtitle();
        final Optional<String> subtitle1 = arg1.getLinks()[0].getSubtitle();
        if (subtitle0.isEmpty()) {
            if (subtitle1.isEmpty()) {
                return 0;
            }
            return 1;
        }
        if (subtitle1.isEmpty()) {
            return -1;
        }
        return StringHelper.compare(subtitle0.get(), subtitle1.get());
    }
}
