package data.jsongenerator;

import java.util.Comparator;

public class ArticleComparator implements Comparator<Article>
{
    @Override
    public int compare(final Article article0,
                       final Article article1) {

        // TODO rewrite this to use the title of all the links and all the subtitles
        final String title0 = article0.getLinks()[0].getTitle();
        final String title1 = article1.getLinks()[0].getTitle();
        final int c1 = StringHelper.compare(title0, title1);
        if (c1 != 0) return c1;
        
        final String subtitle0[] = article0.getLinks()[0].getSubtitles();
        final String subtitle1[] = article1.getLinks()[0].getSubtitles();
        if (subtitle0.length == 0) {
            if (subtitle1.length == 0) {
                return 0;
            }
            return 1;
        }
        if (subtitle1.length == 0) {
            return -1;
        }
        return StringHelper.compare(subtitle0[0], subtitle1[0]);
    }
}
