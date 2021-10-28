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

public class LinkDataExtractorTest {

    @Test
    void  arsTechnicaIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/";
        final String expectedXml = """
                <ARTICLE><X><T>“Uh, no”—Pfizer scientist denies Holmes’ claim that Pfizer endorsed Theranos tech</T>\
                <ST>Pfizer diagnostic director said Theranos report was “not believable.”</ST>\
                <A>https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Tim</FIRSTNAME><MIDDLENAME>De</MIDDLENAME><LASTNAME>Chant</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void quantaMagazineIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/";
        final String expectedXml = """
                <ARTICLE><X><T>Mathematician Answers Chess Problem About Attacking Queens</T>\
                <ST>The n-queens problem is about finding how many different ways queens can be placed on a chessboard so that none attack each other. A mathematician has now all but solved it.</ST>\
                <A>https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Leila</FIRSTNAME><LASTNAME>Sloman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void  youtubeWatch3Blue1BrownIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=-RdOwhmqP5s";
        final String expectedXml = """
                <ARTICLE><X><T>Newton's Fractal (which Newton knew nothing about)</T>\
                <A>https://www.youtube.com/watch?v=-RdOwhmqP5s</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchLeRéveilleurIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=OAyYSlMhgI4";
        final String expectedXml = """
                <ARTICLE><X><T>Les terres rares.</T>\
                <A>https://www.youtube.com/watch?v=OAyYSlMhgI4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>38</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Rodolphe</FIRSTNAME><LASTNAME>Meyer</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchMonsieurBidouilleIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=36WpRwY2DYw";
        final String expectedXml = """
                <ARTICLE><X><T>☀️ ITER ET LA FUSION - Visite du chantier du plus gros tokamak du monde</T>\
                <A>https://www.youtube.com/watch?v=36WpRwY2DYw</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>51</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dimitri</FIRSTNAME><LASTNAME>Ferrière</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchRobertMilesIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=zkbPdEHEyEI";
        final String expectedXml = """
                <ARTICLE><X><T>We Were Right! Real Inner Misalignment</T>\
                <A>https://www.youtube.com/watch?v=zkbPdEHEyEI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Robert</FIRSTNAME><LASTNAME>Miles</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchSabineHossenfelderIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=3hApcpGJETA";
        final String expectedXml = """
                <ARTICLE><X><T>Where Did the Big Bang Happen?</T>\
                <A>https://www.youtube.com/watch?v=3hApcpGJETA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sabine</FIRSTNAME><LASTNAME>Hossenfelder</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchStandupMathsIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=ueEOHk1UzrA";
        final String expectedXml = """
                <ARTICLE><X><T>Find your own ABC Conjecture Triple</T>\
                <A>https://www.youtube.com/watch?v=ueEOHk1UzrA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void youtubeWatchTricTracIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=eJapfznmA4U";
        final String expectedXml = """
                <ARTICLE><X><T>FLIP, L'émission quotidienne - Les lieux éphémères</T>\
                <A>https://www.youtube.com/watch?v=eJapfznmA4U</A>\
                <L>fr</L><F>MP4</F><DURATION><HOUR>1</HOUR><MINUTE>0</MINUTE><SECOND>54</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Guillaume</FIRSTNAME><LASTNAME>Chifoumi</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>François</FIRSTNAME><LASTNAME>Décamp</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>13</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void  youtubeWatchWebDevSimplifiedIsManaged() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=mnmYwRoSisg";
        final String expectedXml = """
                <ARTICLE><X><T>10 Must Know Git Commands That Almost Nobody Knows</T>\
                <A>https://www.youtube.com/watch?v=mnmYwRoSisg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Cook</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    @Test
    void urlIsCleanedFromUtmParameters() throws MalformedURLException, ContentParserException {
        final String url =
            "https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?utm_source=pocket-app&utm_medium=share";
        final String expectedXml = """
                <ARTICLE><X><T>Mathematical Analysis of Fruit Fly Wings Hints at Evolution’s Limits</T>\
                <ST>A painstaking study of wing morphology shows both the striking uniformity of individuals in a species and a subtle pattern of linked variations that evolution can exploit.</ST>\
                <A>https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Elena</FIRSTNAME><LASTNAME>Renken</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        Assertions.assertEquals(expectedXml, generateXml(url));
    }

    private String generateXml(final String txt) throws MalformedURLException, ContentParserException {
        final URL url = new URL(txt);
        final Path path = TestHelper.getTestDatapath(getClass());
        final LinkDataExtractor extractor = LinkDataExtractorFactory.build(path, url);
        return XmlGenerator.generateXml(extractor);
    }
}
