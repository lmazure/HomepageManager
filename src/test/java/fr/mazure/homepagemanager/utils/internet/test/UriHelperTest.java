package fr.mazure.homepagemanager.utils.internet.test;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.utils.internet.UriHelper;

class UriHelperTest {

    @SuppressWarnings("static-method")
    @Test
    void convertInvalidGitterUrlToUri() {
        // this is an incorrect redirect when trying to access https://gitter.im/theintern/intern
        final String str = "https://app.gitter.im/#/room/#theintern_intern:gitter.im";
        final URI uri = UriHelper.convertStringToUri(str);
        Assertions.assertEquals("https://app.gitter.im/#/room/%23theintern_intern:gitter.im", uri.toString());
    }
}
