package fr.mazure.homepagemanager.data.linkchecker.test;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
import fr.mazure.homepagemanager.data.linkchecker.ExtractedLinkData;
import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkFormat;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkProtection;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 *  Tests of XmlGenerator class
 */
class XmlGeneratorTest {

    @SuppressWarnings("static-method")
    @Test
    void generateXml() {
        final ExtractedLinkData linkData = new ExtractedLinkData("title",
                                                                 new String[] { "subtitle"},
                                                                 "https://www.ars-technica.com/toto.html",
                                                                 Optional.of(LinkStatus.DEAD),
                                                                 Optional.of(LinkProtection.FREE_REGISTRATION),
                                                                 new LinkFormat[] { LinkFormat.HTML },
                                                                 new Locale[] { Locale.FRENCH },
                                                                 Optional.empty(),
                                                                 Optional.of(LocalDate.parse("2020-01-01")));
        final Optional<TemporalAccessor> publicationDate = Optional.of(LocalDate.parse("2024-02-02"));
        final String xml = XmlGenerator.generateXml(Collections.singletonList(linkData),
                                                    publicationDate,
                                                    Collections.singletonList(WellKnownAuthors.SIMON_WILLISON),
                                                    1,
                                                    "comment begin @0@ @1@ @C[my code]@ end");
        Assertions.assertEquals("""
                <ARTICLE><X status="dead" protection="free_registration" quality="1"><T>title</T>\
                <ST>subtitle</ST>\
                <A>https://www.ars-technica.com/toto.html</A>\
                <L>fr</L><F>HTML</F><DATE><YEAR>2020</YEAR><MONTH>1</MONTH><DAY>1</DAY></DATE></X>\
                <AUTHOR><FIRSTNAME>Simon</FIRSTNAME><LASTNAME>Willison</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>2</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>comment begin <AUTHOR><FIRSTNAME>Simon</FIRSTNAME><LASTNAME>Willison</LASTNAME></AUTHOR> @1@ <CODEROUTINE>my code</CODEROUTINE> end</COMMENT></ARTICLE>\
                """,
                xml);
    }
}
