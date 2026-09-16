package fr.mazure.homepagemanager.data.jsongenerator.test;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.jsongenerator.Author;
import fr.mazure.homepagemanager.data.jsongenerator.AuthorFactory;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of {@link AuthorFactory} focusing on the {@code order} attribute being part of identity.
 */
class AuthorFactoryOrderTest {

    private static AuthorData authorData(final AuthorData.NameOrder order) {
        return new AuthorData(Optional.empty(),
                              Optional.of("An"),
                              Optional.empty(),
                              Optional.of("Nguyen"),
                              Optional.empty(),
                              Optional.empty(),
                              order);
    }

    @SuppressWarnings("static-method")
    @Test
    void sameNameSameOrderReturnsSameAuthor() {
        final AuthorFactory factory = new AuthorFactory();
        final Author a1 = factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        final Author a2 = factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        Assertions.assertSame(a1, a2);
    }

    @SuppressWarnings("static-method")
    @Test
    void sameNameDifferentOrderReturnsDistinctAuthors() {
        final AuthorFactory factory = new AuthorFactory();
        final Author western = factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        final Author eastern = factory.buildAuthor(authorData(AuthorData.NameOrder.EASTERN));
        Assertions.assertNotSame(western, eastern);
        Assertions.assertEquals(AuthorData.NameOrder.WESTERN, western.getOrder());
        Assertions.assertEquals(AuthorData.NameOrder.EASTERN, eastern.getOrder());
    }

    @SuppressWarnings("static-method")
    @Test
    void sameNameDifferentOrderProducesTwoEntries() {
        final AuthorFactory factory = new AuthorFactory();
        factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        factory.buildAuthor(authorData(AuthorData.NameOrder.EASTERN));
        final Author[] authors = factory.getAuthors();
        Assertions.assertEquals(2, authors.length);
    }

    @SuppressWarnings("static-method")
    @Test
    void sameNameSameOrderProducesOneEntry() {
        final AuthorFactory factory = new AuthorFactory();
        factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        factory.buildAuthor(authorData(AuthorData.NameOrder.WESTERN));
        final Author[] authors = factory.getAuthors();
        Assertions.assertEquals(1, authors.length);
    }
}
