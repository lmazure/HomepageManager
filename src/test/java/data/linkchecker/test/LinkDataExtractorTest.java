package data.linkchecker.test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.LinkDataExtractorFactory;
import data.linkchecker.XmlGenerator;

class LinkDataExtractorTest {

    @Test
    void quantaMagazineIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/";
        final String expectedXml = "<ARTICLE><X><T>Mathematician Answers Chess Problem About Attacking Queens</T><ST>The n-queens problem is about finding how many different ways queens can be placed on a chessboard so that none attack each other. A mathematician has now all but solved it.</ST><A>https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leila</FIRSTNAME><LASTNAME>Sloman</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>21</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void standupMathsYoutubeWatchIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=ueEOHk1UzrA";
        final String expectedXml = "<ARTICLE><X><T>Find your own ABC Conjecture Triple</T><A>https://www.youtube.com/watch?v=ueEOHk1UzrA</A><L>en</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>15</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void urlIsCleanedFromUtmParameters() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?utm_source=pocket-app&utm_medium=share";
        final String expectedXml = "<ARTICLE><X><T>Mathematical Analysis of Fruit Fly Wings Hints at Evolution’s Limits</T><ST>A painstaking study of wing morphology shows both the striking uniformity of individuals in a species and a subtle pattern of linked variations that evolution can exploit.</ST><A>https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Elena</FIRSTNAME><LASTNAME>Renken</LASTNAME></AUTHOR><DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>20</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    private String generateXml(final String txt) throws MalformedURLException, ContentParserException {
        final URL url = new URL(txt);
        final Path path = TestHelper.getTestDatapath(this.getClass());
        final LinkDataExtractor extractor = LinkDataExtractorFactory.build(path, url);
        return XmlGenerator.generateXml(extractor);
    }
}
