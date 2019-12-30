package data.jsongenerator;

import java.util.Comparator;
import java.util.Optional;

import utils.xmlparsing.DateData;

public  class ArticleDataComparator implements Comparator<Article> {

    @Override
    public int compare(final Article a0, final Article a1) {
        
        final Optional<DateData> date0 = a0.getDateData();
        final Optional<DateData> date1 = a1.getDateData();
        if (date0.isEmpty()) {
            if (date1.isPresent()) {
                return -1;                
            }
        } else {
            if (date1.isEmpty()) {
                return 1;
            }
            if (date0.get().getDateYear() != date1.get().getDateYear()) {
                return date0.get().getDateYear() - date1.get().getDateYear();
            }
        }
        
        final Optional<Integer> m0 = date0.get().getDateMonth();
        final Optional<Integer> m1 = date1.get().getDateMonth();
        if (m0.isEmpty()) {
            if (m1.isPresent()) {
                return -1;                
            }
        } else {
            if (m1.isEmpty()) {
                return 1;
            }
            if (m0.get() != m1.get()) {
                return m0.get() - m1.get();            
            }
        }
        
        final Optional<Integer> d0 = date0.get().getDateDay();
        final Optional<Integer> d1 = date1.get().getDateDay();
        if (d0.isEmpty()) {
            if (d1.isPresent()) {
                return -1;                
            }
            return 0;
        }
        if (d1.isEmpty()) {
            return 1;
        }
        return d0.get() - d1.get();
    }       
}
