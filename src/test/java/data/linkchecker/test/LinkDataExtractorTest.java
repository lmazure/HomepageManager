package data.linkchecker.test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.LinkDataExtractorFactory;
import data.linkchecker.XmlGenerator;
import utils.xmlparsing.AuthorData;

public class LinkDataExtractorTest {

    @Test
    void  arsTechnicaIsManaged() throws ContentParserException {
        final String url = "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/";
        final String expectedXml = """
                <ARTICLE><X><T>“Uh, no”—Pfizer scientist denies Holmes’ claim that Pfizer endorsed Theranos tech</T>\
                <ST>Pfizer diagnostic director said Theranos report was “not believable.”</ST>\
                <A>https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Tim</FIRSTNAME><LASTNAME>De Chant</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  baeldungIsManaged() throws ContentParserException {
        final String url = "https://www.baeldung.com/java-unit-testing-best-practices";
        final String expectedXml = """
                <ARTICLE><X><T>Best Practices For Unit Testing In Java</T>\
                <A>https://www.baeldung.com/java-unit-testing-best-practices</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Anshul</FIRSTNAME><LASTNAME>Bansal</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void gitHubBlogIsManaged() throws ContentParserException {
        final String url = "https://github.blog/2022-10-03-highlights-from-git-2-38/";
        final String expectedXml = """
                <ARTICLE><X><T>Highlights from Git 2.38</T>\
                <ST>Another new release of Git is here! Take a look at some of our highlights on what's new in Git 2.38.</ST>\
                <A>https://github.blog/2022-10-03-highlights-from-git-2-38/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Taylor</FIRSTNAME><LASTNAME>Blau</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void gitLabBlogIsManaged() throws ContentParserException {
        final String url = "https://about.gitlab.com/blog/2021/11/10/a-special-farewell-from-gitlab-dmitriy-zaporozhets/";
        final String expectedXml = """
                <ARTICLE><X><T>A special farewell from GitLab’s Dmitriy Zaporozhets</T>\
                <A>https://about.gitlab.com/blog/2021/11/10/a-special-farewell-from-gitlab-dmitriy-zaporozhets/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Sid</FIRSTNAME><LASTNAME>Sidbrandij</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Dmitriy</FIRSTNAME><LASTNAME>Zaporozhets</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void mediumIsManaged() throws ContentParserException {
        final String url = "https://medium.com/@kentbeck_7670/limbo-scaling-software-collaboration-afd4f00db4b";
        final String expectedXml = """
                <ARTICLE><X><T>Limbo: Scaling Software Collaboration</T>\
                <A>https://medium.com/@kentbeck_7670/limbo-scaling-software-collaboration-afd4f00db4b</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Kent</FIRSTNAME><LASTNAME>Beck</LASTNAME></AUTHOR>\
                <DATE><YEAR>2018</YEAR><MONTH>7</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsIsManaged() throws ContentParserException {
        final String url = "https://blogs.oracle.com/javamagazine/unit-test-your-architecture-with-archunit";
        final String expectedXml = """
                <ARTICLE><X><T>Unit Test Your Architecture with ArchUnit</T>\
                <ST>Discover architectural defects at build time.</ST>\
                <A>https://blogs.oracle.com/javamagazine/unit-test-your-architecture-with-archunit</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Jonas</FIRSTNAME><LASTNAME>Havers</LASTNAME></AUTHOR>\
                <DATE><YEAR>2019</YEAR><MONTH>8</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsWithPostinUrlIsManaged() throws ContentParserException {
        final String url = "https://blogs.oracle.com/javamagazine/post/java-nio-nio2-buffers-channels-async-future-callback";
        final String expectedXml = """
                <ARTICLE><X><T>Modern file input/output with Java: Going fast with NIO and NIO.2</T>\
                <ST>Reach for these low-level Java APIs when you need to move a lot of file data or socket data quickly.</ST>\
                <A>https://blogs.oracle.com/javamagazine/java-nio-nio2-buffers-channels-async-future-callback</A><L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Evans</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>1</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void quantaMagazineIsManaged() throws ContentParserException {
        final String url = "https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/";
        final String expectedXml = """
                <ARTICLE><X><T>Mathematician Answers Chess Problem About Attacking Queens</T>\
                <ST>The n-queens problem is about finding how many different ways queens can be placed on a chessboard so that none attack each other. A mathematician has now all but solved it.</ST>\
                <A>https://www.quantamagazine.org/mathematician-answers-chess-problem-about-attacking-queens-20210921/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Leila</FIRSTNAME><LASTNAME>Sloman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatch3Blue1BrownIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-RdOwhmqP5s";
        final String expectedXml = """
                <ARTICLE><X><T>Newton’s method, and the fractal it creates that Newton knew nothing about</T>\
                <A>https://www.youtube.com/watch?v=-RdOwhmqP5s</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAstronoGeekIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=7rTKxHoU_Rc";
        final String expectedXml = """
                <ARTICLE><X><T>🪐❔David Hahn, l'ado qui a fabriqué un réacteur nucléaire chez lui</T>\
                <A>https://www.youtube.com/watch?v=7rTKxHoU_Rc</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>29</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Arnaud</FIRSTNAME><LASTNAME>Thiry</LASTNAME></AUTHOR><DATE>\
                <YEAR>2021</YEAR><MONTH>4</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAurelienSamaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=LXh6262dZp0";
        final String expectedXml = """
                <ARTICLE><X><T>Samaventure 17 - La Richesse d'un Homme !</T>\
                <A>https://www.youtube.com/watch?v=LXh6262dZp0</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>27</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Aurélien</FIRSTNAME><LASTNAME>Sama</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>27</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAypierreIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=FH-A2Hq6pG4";
        final String expectedXml = """
                <ARTICLE><X><T>Lundi Pivipi -  RUSH à l'ancienne</T>\
                <A>https://www.youtube.com/watch?v=FH-A2Hq6pG4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>45</MINUTE><SECOND>24</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Aymeric</FIRSTNAME><LASTNAME>Pierre</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchBaladeMentaleIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=7KJbzgoBUX0";
        final String expectedXml = """
                <ARTICLE><X><T>La disparition de la mer Méditerranée</T>\
                <A>https://www.youtube.com/watch?v=7KJbzgoBUX0</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>25</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Théo</FIRSTNAME><LASTNAME>Drieu</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchBlackPenRedPenIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eex9Gm_rOrQ";
        final String expectedXml = """
                <ARTICLE><X><T>homemade triple exponential equation with different bases</T>\
                <A>https://www.youtube.com/watch?v=eex9Gm_rOrQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>7</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Steve</FIRSTNAME><LASTNAME>Chow</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchChatSceptiqueIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=VubMw46DK0U";
        final String expectedXml = """
                <ARTICLE><X><T>L'erreur Excel qui a coûté la vie à 1500 personnes 💀</T>\
                <A>https://www.youtube.com/watch?v=VubMw46DK0U</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>0</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Nathan</FIRSTNAME><LASTNAME>Uyttendaele</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchClémentFrezeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=fUuseKwzZB4";
        final String expectedXml = """
                <ARTICLE><X><T>Un PROCÈS pour Fake-Up ? - Clément Freze (avec Vous avez le droit)</T>\
                <A>https://www.youtube.com/watch?v=fUuseKwzZB4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>39</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Clément</FIRSTNAME><LASTNAME>Freze</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>4</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchComputerfileIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-ShwJqAalOk";
        final String expectedXml = """
                <ARTICLE><X><T>Breaking RSA - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=-ShwJqAalOk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>14</MINUTE><SECOND>49</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Mike</FIRSTNAME><LASTNAME>Pound</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>5</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchDrBeckyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=6v72L_1L4lQ";
        final String expectedXml = """
                <ARTICLE><X><T>LIVE | The first image from the James Webb Space Telescope! (and why it looks a bit naff)</T>\
                <A>https://www.youtube.com/watch?v=6v72L_1L4lQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>53</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Becky</FIRSTNAME><LASTNAME>Smethurst</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchDeepSkyVideosIsManaged1() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=MkzyMS_hNwU";
        final String expectedSureXml = """
                <ARTICLE><X><T>M28 - Millisecond Pulsar - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=MkzyMS_hNwU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>48</SECOND></DURATION></X>\
                <DATE><YEAR>2016</YEAR><MONTH>2</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>M28 - Millisecond Pulsar - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=MkzyMS_hNwU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>48</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Paul</FIRSTNAME><LASTNAME>Crowther</LASTNAME></AUTHOR>\
                <DATE><YEAR>2016</YEAR><MONTH>2</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchDeepSkyVideosIsManaged2() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=17LITLns-pk";
        final String expectedSureXml = """
                <ARTICLE><X><T>M50 - Spinning Stars - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=17LITLns-pk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <DATE><YEAR>2016</YEAR><MONTH>7</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>M50 - Spinning Stars - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=17LITLns-pk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Meghan</FIRSTNAME><LASTNAME>Gray</LASTNAME></AUTHOR>\
                <DATE><YEAR>2016</YEAR><MONTH>7</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchDeepSkyVideosIsManaged3() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=2d2YCUdt2gc";
        final String expectedSureXml = """
                <ARTICLE><X><T>M61 - Barred Spiral Galaxy - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=2d2YCUdt2gc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>M61 - Barred Spiral Galaxy - Deep Sky Videos</T>\
                <A>https://www.youtube.com/watch?v=2d2YCUdt2gc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Michael</FIRSTNAME><LASTNAME>Merrifield</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchElJjIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=qZWbgBSfTUI";
        final String expectedXml = """
                <ARTICLE><X><T>J'ai regardé 43 films et séries Marvel Studios et j'y ai trouvé... des maths ! - Ccc #08</T>\
                <A>https://www.youtube.com/watch?v=qZWbgBSfTUI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>42</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jérôme</FIRSTNAME><LASTNAME>Cottanceau</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>30</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchDirtyBiologyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=_LxktNzm8ME";
        final String expectedXml = """
                <ARTICLE><X><T>Pourquoi certaines civilisations du Pacifique ont disparu et pas d'autres ?</T>\
                <A>https://www.youtube.com/watch?v=_LxktNzm8ME</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>7</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Léo</FIRSTNAME><LASTNAME>Grasset</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>5</MONTH><DAY>13</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHygieneMentaleIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=sDPk-r18sb0";
        final String expectedXml = """
                <ARTICLE><X><T>Ep35 La  Régression vers la Moyenne</T>\
                <A>https://www.youtube.com/watch?v=sDPk-r18sb0</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>28</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Christophe</FIRSTNAME><LASTNAME>Michel</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>22</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHistoryOfTheEarthIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=0sbwUeTyDb0";
        final String expectedXml = """
                <ARTICLE><X><T>What Was The "Boring Billion" Really Like?</T>\
                <A>https://www.youtube.com/watch?v=0sbwUeTyDb0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>36</MINUTE><SECOND>9</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Kelly</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Pete</FIRSTNAME><LASTNAME>Kelly</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Kelly</FIRSTNAME><LASTNAME>Battison</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJavaInsideJavaNewscastIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eDgBnjOid-g";
        final String expectedXml = """
                <ARTICLE><X><T>What Happens to Finalization in JDK 18? - Inside Java Newscast #15</T>\
                <A>https://www.youtube.com/watch?v=eDgBnjOid-g</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Nicolai</FIRSTNAME><LASTNAME>Parlog</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJavaJepCafeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=NDaA9MrTLBM";
        final String expectedXml = """
                <ARTICLE><X><T>Text Blocks - JEP Café #5</T>\
                <A>https://www.youtube.com/watch?v=NDaA9MrTLBM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>José</FIRSTNAME><LASTNAME>Paumard</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHeurekaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=UuIt21q9gCY";
        final String expectedXml = """
                <ARTICLE><X><T>Les marchés financiers vous cachent beaucoup de choses</T>\
                <A>https://www.youtube.com/watch?v=UuIt21q9gCY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>50</MINUTE><SECOND>14</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Gilles</FIRSTNAME><LASTNAME>Mitteau</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHolgerVoormannIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=GnNnQY5ujFg";
        final String expectedXml = """
                <ARTICLE><X><T>Eclipse 2022-03 Java IDE Improvements</T>\
                <A>https://www.youtube.com/watch?v=GnNnQY5ujFg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>14</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Holger</FIRSTNAME><LASTNAME>Voormann</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>3</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJamyEpicurieuxIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=5cdqxrZJt5o";
        final String expectedXml = """
                <ARTICLE><X><T>Les arnaques scientifiques les plus folles</T>\
                <A>https://www.youtube.com/watch?v=5cdqxrZJt5o</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>43</SECOND></DURATION>\
                </X><AUTHOR><FIRSTNAME>Jamy</FIRSTNAME><LASTNAME>Gourmaud</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>7</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJMEnervePasJExpliqueIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=JjgcD2o7IME";
        final String expectedXml = """
                <ARTICLE><X><T>☀️ Du Soleil à ITER : une Histoire de la FUSION #11 Science</T>\
                <A>https://www.youtube.com/watch?v=JjgcD2o7IME</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>25</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Bertrand</FIRSTNAME><LASTNAME>Augustin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJonPerryIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=rLGam7Lx1Z4";
        final String expectedXml = """
                <ARTICLE><X><T>Do "Essential Genes" debunk evolution? Evolutionary Question #26</T>\
                <A>https://www.youtube.com/watch?v=rLGam7Lx1Z4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jon</FIRSTNAME><LASTNAME>Perry</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>4</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLeDessousDesCartesIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=dz0dEzAHKUo";
        final String expectedXml = """
                <ARTICLE><X><T>Ukraine : la menace nucléaire, un tournant ? - Le Dessous des cartes | ARTE</T>\
                <A>https://www.youtube.com/watch?v=dz0dEzAHKUo</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>48</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Émilie</FIRSTNAME><LASTNAME>Aubry</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>4</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLeRéveilleurIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=OAyYSlMhgI4";
        final String expectedXml = """
                <ARTICLE><X><T>Les terres rares.</T>\
                <A>https://www.youtube.com/watch?v=OAyYSlMhgI4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>38</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Rodolphe</FIRSTNAME><LASTNAME>Meyer</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMathadorIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=EcPPjZVB2vA";
        final String expectedXml = """
                <ARTICLE><X><T>L'INCROYABLE HISTOIRE DE LA CONJECTURE DE FERMAT</T>\
                <A>https://www.youtube.com/watch?v=EcPPjZVB2vA</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>59</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Franck</FIRSTNAME><LASTNAME>Dunas</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>9</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMathologerIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=6ZrO90AI0c8";
        final String expectedXml = """
                <ARTICLE><X><T>Tesla’s 3-6-9 and Vortex Math: Is this really the key to the universe?</T>\
                <A>https://www.youtube.com/watch?v=6ZrO90AI0c8</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>29</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Burkard</FIRSTNAME><LASTNAME>Polster</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMathParker2IsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=gusXBTyg1O4";
        final String expectedXml = """
                <ARTICLE><X><T>My response to being reverse-Dereked</T>\
                <A>https://www.youtube.com/watch?v=gusXBTyg1O4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>35</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>7</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMichaelLaunayIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=8D_ThIqoJL8";
        final String expectedXml = """
                <ARTICLE><X><T>L'étonnant puzzle fractal de von Koch - Micmaths</T>\
                <A>https://www.youtube.com/watch?v=8D_ThIqoJL8</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Mickaël</FIRSTNAME><LASTNAME>Launay</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>4</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMinutePhysicsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=vLIPUdru82c";
        final String expectedXml = """
                <ARTICLE><X><T>The Problem With The Butterfly Effect</T>\
                <A>https://www.youtube.com/watch?v=vLIPUdru82c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Henry</FIRSTNAME><LASTNAME>Reich</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMonsieurBidouilleIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=36WpRwY2DYw";
        final String expectedXml = """
                <ARTICLE><X><T>☀️À l'intérieur d'ITER - Visite du chantier du plus gros tokamak du monde</T>\
                <A>https://www.youtube.com/watch?v=36WpRwY2DYw</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>51</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dimitri</FIRSTNAME><LASTNAME>Ferrière</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMonsieurPhiIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-VdXi2LMPyE";
        final String expectedXml = """
                <ARTICLE><X><T>2500 ans de philosophie (et on ne s'entend toujours pas sur ce que c'est)</T>\
                <A>https://www.youtube.com/watch?v=-VdXi2LMPyE</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thibaut</FIRSTNAME><LASTNAME>Giraud</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMNotaBonusIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=QwLZ_i1qK8A";
        final String expectedXml = """
                <ARTICLE><X><T>La bataille des nombres au Moyen Âge - Le jeu de la Rithmomachie</T>\
                <A>https://www.youtube.com/watch?v=QwLZ_i1qK8A</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Benjamin</FIRSTNAME><LASTNAME>Brillaud</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>4</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPasseScienceIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=yfFck7EfptU";
        final String expectedXml = """
                <ARTICLE><X><T>Hacking et virus informatiques (dans le monde réel) bonus: Corewar! - Passe-science #43</T>\
                <A>https://www.youtube.com/watch?v=yfFck7EfptU</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thomas</FIRSTNAME><LASTNAME>Cabaret</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>5</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPBSEonsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Ke0BjyO41Qg";
        final String expectedXml = """
                <ARTICLE><X><T>Our Ancient Relative That Said 'No Thanks' To Life On Land</T>\
                <A>https://www.youtube.com/watch?v=Ke0BjyO41Qg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>9</MINUTE><SECOND>55</SECOND></DURATION></X\
                ><AUTHOR><FIRSTNAME>Michelle</FIRSTNAME><LASTNAME>Barboza-Ramirez</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>4</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPeriodicTableIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ykpTFw6r7Hw";
        final String expectedXml = """
                <ARTICLE><X><T>Poaching an Egg in Piranha Solution - Periodic Table of Videos</T>\
                <A>https://www.youtube.com/watch?v=ykpTFw6r7Hw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Martyn</FIRSTNAME><LASTNAME>Poliakoff</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Neil</FIRSTNAME><LASTNAME>Barnes</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPhiloximeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=YVtW5d94KN0";
        final String expectedXml = """
                <ARTICLE><X><T>Climat : ce moment où ça a déraillé</T>\
                <A>https://www.youtube.com/watch?v=YVtW5d94KN0</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>14</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Maxime</FIRSTNAME><LASTNAME>Lambrecht</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>5</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPrimerIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=XTcP4oo4JI4";
        final String expectedXml = """
                <ARTICLE><X><T>How To Catch A Cheater With Math</T>\
                <A>https://www.youtube.com/watch?v=XTcP4oo4JI4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>22</MINUTE><SECOND>37</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Justin</FIRSTNAME><LASTNAME>Helps</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchQuadriviuumTremensIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=fiekVUXhvZE";
        final String expectedXml = """
                <ARTICLE><X><T>★★ Les Dyadiques et leurs liens avec l'écriture Binaire - La Classification des Nombres #4</T>\
                <A>https://www.youtube.com/watch?v=fiekVUXhvZE</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>42</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tristan</FIRSTNAME><LASTNAME>Audam-Dabidin</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Keshika</FIRSTNAME><LASTNAME>Dabidin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2020</YEAR><MONTH>3</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchRobertMilesIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=zkbPdEHEyEI";
        final String expectedXml = """
                <ARTICLE><X><T>We Were Right! Real Inner Misalignment</T>\
                <A>https://www.youtube.com/watch?v=zkbPdEHEyEI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Robert</FIRSTNAME><LASTNAME>Miles</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchSabineHossenfelderIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=3hApcpGJETA";
        final String expectedXml = """
                <ARTICLE><X><T>Where Did the Big Bang Happen?</T>\
                <A>https://www.youtube.com/watch?v=3hApcpGJETA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sabine</FIRSTNAME><LASTNAME>Hossenfelder</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScienceClicIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=EkE229ybABc";
        final String expectedXml = """
                <ARTICLE><X><T>Où va le temps dans un Trou noir ?</T>\
                <A>https://www.youtube.com/watch?v=EkE229ybABc</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>10</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Alessandro</FIRSTNAME><LASTNAME>Roussel</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScienceDeComptoirIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Y4yeTTOTfO8";
        final String expectedXml = """
                <ARTICLE><X><T>🌱La SOLUTION pour SAUVER LE CLIMAT🔥 (personne n'y avait pensé)</T>\
                <A>https://www.youtube.com/watch?v=Y4yeTTOTfO8</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>35</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Valentine</FIRSTNAME><LASTNAME>Delattre</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>4</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScienceEtonnanteIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=FGwmAEMabm4";
        final String expectedXml = """
                <ARTICLE><X><T>Comment Mesurer L'Univers ? 🔭🌕☀️✨🌌</T>\
                <A>https://www.youtube.com/watch?v=FGwmAEMabm4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>48</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Louapre</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>1</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScilabusIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=url1TFdHlSI";
        final String expectedXml = """
                <ARTICLE><X><T>L’écriture inclusive a-t-elle un intérêt ? Quelles preuves ?</T>\
                <A>https://www.youtube.com/watch?v=url1TFdHlSI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>14</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Viviane</FIRSTNAME><LASTNAME>Lalande</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>3</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchSingingBananaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=FR_71HyBytE";
        final String expectedXml = """
                <ARTICLE><X><T>Mastermind with Steve Mould</T>\
                <A>https://www.youtube.com/watch?v=FR_71HyBytE</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>James</FIRSTNAME><LASTNAME>Grime</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>3</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchStandupMathsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ueEOHk1UzrA";
        final String expectedXml = """
                <ARTICLE><X><T>Find your own ABC Conjecture Triple</T>\
                <A>https://www.youtube.com/watch?v=ueEOHk1UzrA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScience4AllIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=sAjm3-IaRtI";
        final String expectedXml = """
                <ARTICLE><X><T>Trafiquant d'humains et marchand de haine #FacebookFiles</T>\
                <A>https://www.youtube.com/watch?v=sAjm3-IaRtI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>38</MINUTE><SECOND>52</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchThomathsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=G5nbqZnlvHo";
        final String expectedXml = """
                <ARTICLE><X><T>Thomaths 14 : Solides Réguliers</T>\
                <A>https://www.youtube.com/watch?v=G5nbqZnlvHo</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>34</SECOND></DURATION>\
                </X><AUTHOR><FIRSTNAME>Alexander</FIRSTNAME><LASTNAME>Thomas</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchTomScottIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=kBaLb1C4WAg";
        final String expectedXml = """
                <ARTICLE><X><T>This town forgot to be a city</T>\
                <A>https://www.youtube.com/watch?v=kBaLb1C4WAg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>5</MINUTE><SECOND>33</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Scott</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>2</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchTricTracIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eJapfznmA4U";
        final String expectedXml = """
                <ARTICLE><X><T>FLIP, L'émission quotidienne - Les lieux éphémères</T>\
                <A>https://www.youtube.com/watch?v=eJapfznmA4U</A>\
                <L>fr</L><F>MP4</F><DURATION><HOUR>1</HOUR><MINUTE>0</MINUTE><SECOND>54</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Guillaume</FIRSTNAME><LASTNAME>Chifoumi</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>François</FIRSTNAME><LASTNAME>Décamp</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>13</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchVeritasiumIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=cUzklzVXJwo";
        final String expectedXml = """
                <ARTICLE><X><T>How Imaginary Numbers Were Invented</T>\
                <A>https://www.youtube.com/watch?v=cUzklzVXJwo</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>28</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Derek</FIRSTNAME><LASTNAME>Muller</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>31</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchWebDevSimplifiedIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=mnmYwRoSisg";
        final String expectedXml = """
                <ARTICLE><X><T>10 Must Know Git Commands That Almost Nobody Knows</T>\
                <A>https://www.youtube.com/watch?v=mnmYwRoSisg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Cook</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchWonderWhyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=DfdewkU3_Hg";
        final String expectedXml = """
                <ARTICLE><X><T>The Country That Didn't Want Independence</T>\
                <A>https://www.youtube.com/watch?v=DfdewkU3_Hg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>WonderWhy</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>30</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchYoshaEchecsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=_PqORG75Cr4";
        final String expectedXml = """
                <ARTICLE><X><T>Les indices les plus compromettants contre Hans Niemann</T>\
                <A>https://www.youtube.com/watch?v=_PqORG75Cr4</A><L>fr</L><F>MP4</F>\
                <DURATION><MINUTE>26</MINUTE><SECOND>20</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Yosha</FIRSTNAME><LASTNAME>Iglesias</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchUnknownEnglishChannelIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eAkb2mpybnM";
        final String expectedXml = """
                <ARTICLE><X><T>How I Built the Entire Universe in Minecraft</T>\
                <A>https://www.youtube.com/watch?v=eAkb2mpybnM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>35</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>ChrisDaCow</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchUnknownFrenchChannelIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=1Q4RoWIfpoY";
        final String expectedXml = """
                <ARTICLE><X><T>La Chine est embarrassée face à la guerre en Ukraine</T>\
                <A>https://www.youtube.com/watch?v=1Q4RoWIfpoY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>France Culture</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchUrlInListIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=C926N9zMJkU&list=WL&index=11";
        final String expectedXml = """
                <ARTICLE><X><T>5 Must Know VSCode Shortcuts</T>\
                <A>https://www.youtube.com/watch?v=C926N9zMJkU</A>\
                <L>en</L><F>MP4</F><DURATION><SECOND>49</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Cook</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>3</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void urlIsCleanedFromUtmParameters() throws ContentParserException {
        final String url = "https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?utm_source=pocket-app&utm_medium=share";
        final String expectedXml = """
                <ARTICLE><X><T>Mathematical Analysis of Fruit Fly Wings Hints at Evolution’s Limits</T>\
                <ST>A painstaking study of wing morphology shows both the striking uniformity of individuals in a species and a subtle pattern of linked variations that evolution can exploit.</ST>\
                <A>https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Elena</FIRSTNAME><LASTNAME>Renken</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void wiredIsManaged() throws ContentParserException {
        final String url = "https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/";
        final String expectedXml = """
                <ARTICLE><X><T>Answer to a 150-Year-Old Math Conundrum Brings More Mystery</T>\
                <ST>A 150-year-old conundrum about how to group people has been solved, but many puzzles remain.</ST>\
                <A>https://www.wired.com/2015/06/answer-150-year-old-math-conundrum-brings-mystery/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Erica</FIRSTNAME><LASTNAME>Klarreich</LASTNAME></AUTHOR>\
                <DATE><YEAR>2015</YEAR><MONTH>6</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    private LinkDataExtractor getExtractor(final String url) throws ContentParserException {
        final Path path = TestHelper.getTestDatapath(getClass());
        return LinkDataExtractorFactory.build(path, url);
    }

    private static String generateSureXml(final LinkDataExtractor extractor) throws ContentParserException {
        return XmlGenerator.generateXml(extractor.getLinks(), extractor.getDate(), extractor.getSureAuthors());
    }

    private static String generateProbableXml(final LinkDataExtractor extractor) throws ContentParserException {
        final List<AuthorData> allAuthors = new ArrayList<>(extractor.getSureAuthors());
        allAuthors.addAll(extractor.getProbableAuthors());
        return XmlGenerator.generateXml(extractor.getLinks(), extractor.getDate(), allAuthors);
    }
}
