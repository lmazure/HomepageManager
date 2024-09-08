package fr.mazure.homepagemanager.data.knowledge.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthorsOfLink;
import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthorsOfLink.KnownAuthors;

class WellKnownAuthorsOfLinkTest {

    @SuppressWarnings("static-method")
    @Test
    void hostDefinesAuthor() {
        final KnownAuthors authors = WellKnownAuthorsOfLink.getWellKnownAuthors("https://automathssite.wordpress.com/2017/08/27/plimpton-322-et-la-trigonometrie/");
        Assertions.assertEquals(1, authors.compulsoryAuthors().size());
        Assertions.assertTrue(authors.compulsoryAuthors().contains(WellKnownAuthors.JASON_LAPERONNIE));
        Assertions.assertFalse(authors.canHaveOtherAuthors());
    }

    @SuppressWarnings("static-method")
    @Test
    void hostCanContainSeveralAuthors() {
        final KnownAuthors authors = WellKnownAuthorsOfLink.getWellKnownAuthors("https://podcastaddict.com/nota-bene/episode/170614574");
        Assertions.assertEquals(1, authors.compulsoryAuthors().size());
        Assertions.assertTrue(authors.compulsoryAuthors().contains(WellKnownAuthors.BENJAMIN_BRILLAUD));
        Assertions.assertTrue(authors.canHaveOtherAuthors());
    }

    @SuppressWarnings("static-method")
    @Test
    void unknownSite() {
        final KnownAuthors authors = WellKnownAuthorsOfLink.getWellKnownAuthors("https://example.com");
        Assertions.assertEquals(0, authors.compulsoryAuthors().size());
        Assertions.assertTrue(authors.canHaveOtherAuthors());
    }
}
