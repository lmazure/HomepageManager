package data.knowledge;

import java.util.List;

import utils.xmlparsing.AuthorData;

public class WellKnownAuthors {

    private final List<AuthorData> _compulsoryAuthors;
    private final boolean _canHaveOtherAuthors;

    WellKnownAuthors(final List<AuthorData> compulsoryAuthors,
                     final boolean canHaveOtherAuthors) {
        _compulsoryAuthors = compulsoryAuthors;
        _canHaveOtherAuthors = canHaveOtherAuthors;
    }
    
    /**
     * @return list of authors that must be present as authors of this link
     */
    public List<AuthorData> getCompulsoryAuthors() {
        return _compulsoryAuthors;
    }
    
    public boolean canHaveOtherAuthors() {
        return _canHaveOtherAuthors;
    }
}
