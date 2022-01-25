package data.linkchecker.test;

import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.internet.test.TestHelper;
import data.linkchecker.ContentParserException;
import data.linkchecker.LinkDataExtractor;
import data.linkchecker.LinkDataExtractorFactory;
import data.linkchecker.XmlGenerator;
import utils.StringHelper;

public class LinkDataExtractorTest {

    @Test
    void  arsTechnicaIsManaged() throws ContentParserException {
        final String url =
            "https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/";
        final String expectedXml = """
                <ARTICLE><X><T>“Uh, no”—Pfizer scientist denies Holmes’ claim that Pfizer endorsed Theranos tech</T>\
                <ST>Pfizer diagnostic director said Theranos report was “not believable.”</ST>\
                <A>https://arstechnica.com/tech-policy/2021/10/uh-no-pfizer-scientist-denies-holmes-claim-that-pfizer-endorsed-theranos-tech/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Tim</FIRSTNAME><LASTNAME>De Chant</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void gitlabBlogIsManaged() throws ContentParserException {
        final String url =
            "https://about.gitlab.com/blog/2021/11/10/a-special-farewell-from-gitlab-dmitriy-zaporozhets/";
        final String expectedXml = """
                <ARTICLE><X><T>A special farewell from GitLab’s Dmitriy Zaporozhets</T>\
                <A>https://about.gitlab.com/blog/2021/11/10/a-special-farewell-from-gitlab-dmitriy-zaporozhets/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Sid</FIRSTNAME><LASTNAME>Sidbrandij</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Dmitriy</FIRSTNAME><LASTNAME>Zaporozhets</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsIsManaged() throws ContentParserException {
        final String url =
            "https://blogs.oracle.com/javamagazine/unit-test-your-architecture-with-archunit";
        final String expectedXml = """
                <ARTICLE><X><T>Unit Test Your Architecture with ArchUnit</T>\
                <ST>Discover architectural defects at build time.</ST>\
                <A>https://blogs.oracle.com/javamagazine/unit-test-your-architecture-with-archunit</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Jonas</FIRSTNAME><LASTNAME>Havers</LASTNAME></AUTHOR>\
                <DATE><YEAR>2019</YEAR><MONTH>8</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void quantaMagazineIsManaged() throws ContentParserException {
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
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatch3Blue1BrownIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=-RdOwhmqP5s";
        final String expectedXml = """
                <ARTICLE><X><T>Newton's Fractal (which Newton knew nothing about)</T>\
                <A>https://www.youtube.com/watch?v=-RdOwhmqP5s</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAstronoGeekIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=7rTKxHoU_Rc";
        final String expectedXml = """
                <ARTICLE><X><T>🪐❔David Hahn, l'ado qui a fabriqué un réacteur nucléaire chez lui</T>\
                <A>https://www.youtube.com/watch?v=7rTKxHoU_Rc</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>29</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Arnaud</FIRSTNAME><LASTNAME>Thiry</LASTNAME></AUTHOR><DATE>\
                <YEAR>2021</YEAR><MONTH>4</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHistoryOfTheEarth() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=0sbwUeTyDb0";
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
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJavaInsideJavaNewscast() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=eDgBnjOid-g";
        final String expectedXml = """
                <ARTICLE><X><T>What Happens to Finalization in JDK 18? - Inside Java Newscast #15</T>\
                <A>https://www.youtube.com/watch?v=eDgBnjOid-g</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Nicolai</FIRSTNAME><LASTNAME>Parlog</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJavaJepCafe() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=NDaA9MrTLBM";
        final String expectedXml = """
                <ARTICLE><X><T>Text Blocks - JEP Café #5</T>\
                <A>https://www.youtube.com/watch?v=NDaA9MrTLBM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>José</FIRSTNAME><LASTNAME>Paumard</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchJMEnervePasJExplique() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=JjgcD2o7IME";
        final String expectedXml = """
                <ARTICLE><X><T>☀️Du Soleil à ITER : une Histoire de la FUSION #11 Science</T>\
                <A>https://www.youtube.com/watch?v=JjgcD2o7IME</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>25</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Bertrand</FIRSTNAME><LASTNAME>Augustin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLeRéveilleurIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=OAyYSlMhgI4";
        final String expectedXml = """
                <ARTICLE><X><T>Les terres rares.</T>\
                <A>https://www.youtube.com/watch?v=OAyYSlMhgI4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>38</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Rodolphe</FIRSTNAME><LASTNAME>Meyer</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMonsieurBidouilleIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=36WpRwY2DYw";
        final String expectedXml = """
                <ARTICLE><X><T>☀️ ITER ET LA FUSION - Visite du chantier du plus gros tokamak du monde</T>\
                <A>https://www.youtube.com/watch?v=36WpRwY2DYw</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>51</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dimitri</FIRSTNAME><LASTNAME>Ferrière</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMonsieurPhiIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=-VdXi2LMPyE";
        final String expectedXml = """
                <ARTICLE><X><T>2500 ans de philosophie (et on ne s'entend toujours pas sur ce que c'est)</T>\
                <A>https://www.youtube.com/watch?v=-VdXi2LMPyE</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thibaut</FIRSTNAME><LASTNAME>Giraud</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPasseScienceIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=yfFck7EfptU";
        final String expectedXml = """
                <ARTICLE><X><T>Hacking et virus informatiques (dans le monde réel) bonus: Corewar! - Passe-science #43</T>\
                <A>https://www.youtube.com/watch?v=yfFck7EfptU</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thomas</FIRSTNAME><LASTNAME>Cabaret</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>5</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchQuadriviuumTremensIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=fiekVUXhvZE";
        final String expectedXml = """
                <ARTICLE><X><T>★★ Les Dyadiques et leurs liens avec l'écriture Binaire - La Classification des Nombres #4</T>\
                <A>https://www.youtube.com/watch?v=fiekVUXhvZE</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>42</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tristan</FIRSTNAME><LASTNAME>Audam-Dabidin</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Keshika</FIRSTNAME><LASTNAME>Dabidin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2020</YEAR><MONTH>3</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchRobertMilesIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=zkbPdEHEyEI";
        final String expectedXml = """
                <ARTICLE><X><T>We Were Right! Real Inner Misalignment</T>\
                <A>https://www.youtube.com/watch?v=zkbPdEHEyEI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Robert</FIRSTNAME><LASTNAME>Miles</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchSabineHossenfelderIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=3hApcpGJETA";
        final String expectedXml = """
                <ARTICLE><X><T>Where Did the Big Bang Happen?</T>\
                <A>https://www.youtube.com/watch?v=3hApcpGJETA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sabine</FIRSTNAME><LASTNAME>Hossenfelder</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScienceClicIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=EkE229ybABc";
        final String expectedXml = """
                <ARTICLE><X><T>Où va le temps dans un Trou noir ?</T>\
                <A>https://www.youtube.com/watch?v=EkE229ybABc</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>10</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Alessandro</FIRSTNAME><LASTNAME>Roussel</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }


    @Test
    void youtubeWatchScienceEtonnanteIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=FGwmAEMabm4";
        final String expectedXml = """
                <ARTICLE><X><T>Comment Mesurer L'Univers ? 🔭🌕☀️✨🌌</T>\
                <A>https://www.youtube.com/watch?v=FGwmAEMabm4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>48</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Louapre</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>1</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }
    @Test
    void youtubeWatchStandupMathsIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=ueEOHk1UzrA";
        final String expectedXml = """
                <ARTICLE><X><T>Find your own ABC Conjecture Triple</T>\
                <A>https://www.youtube.com/watch?v=ueEOHk1UzrA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchScience4All() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=sAjm3-IaRtI";
        final String expectedXml = """
                <ARTICLE><X><T>Zuckerberg en prison ?</T>\
                <A>https://www.youtube.com/watch?v=sAjm3-IaRtI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>38</MINUTE><SECOND>52</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Lê</FIRSTNAME><LASTNAME>Nguyên Hoang</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>12</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchThomaths() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=G5nbqZnlvHo";
        final String expectedXml = """
                <ARTICLE><X><T>Thomaths 14 : Solides Réguliers</T>\
                <A>https://www.youtube.com/watch?v=G5nbqZnlvHo</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>34</SECOND></DURATION>\
                </X><AUTHOR><FIRSTNAME>Alexander</FIRSTNAME><LASTNAME>Thomas</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchTricTracIsManaged() throws ContentParserException {
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
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchVeritasiumIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=cUzklzVXJwo";
        final String expectedXml = """
                <ARTICLE><X><T>How Imaginary Numbers Were Invented</T>\
                <A>https://www.youtube.com/watch?v=cUzklzVXJwo</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>28</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Derek</FIRSTNAME><LASTNAME>Muller</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>31</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void  youtubeWatchWebDevSimplifiedIsManaged() throws ContentParserException {
        final String url =
            "https://www.youtube.com/watch?v=mnmYwRoSisg";
        final String expectedXml = """
                <ARTICLE><X><T>10 Must Know Git Commands That Almost Nobody Knows</T>\
                <A>https://www.youtube.com/watch?v=mnmYwRoSisg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Cook</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void urlIsCleanedFromUtmParameters() throws ContentParserException {
        final String url =
            "https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/?utm_source=pocket-app&utm_medium=share";
        final String expectedXml = """
                <ARTICLE><X><T>Mathematical Analysis of Fruit Fly Wings Hints at Evolution’s Limits</T>\
                <ST>A painstaking study of wing morphology shows both the striking uniformity of individuals in a species and a subtle pattern of linked variations that evolution can exploit.</ST>\
                <A>https://www.quantamagazine.org/mathematical-analysis-of-fruit-fly-wings-hints-at-evolutions-limits-20210920/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Elena</FIRSTNAME><LASTNAME>Renken</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, XmlGenerator.generateXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    private LinkDataExtractor getExtractor(final String txt) throws ContentParserException {
        final URL url = StringHelper.convertStringToUrl(txt);
        final Path path = TestHelper.getTestDatapath(getClass());
        return LinkDataExtractorFactory.build(path, url);
    }
}
