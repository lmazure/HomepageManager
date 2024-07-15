package fr.mazure.homepagemanager.data.linkchecker.test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.ContentParserException;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractor;
import fr.mazure.homepagemanager.data.linkchecker.LinkDataExtractorFactory;
import fr.mazure.homepagemanager.data.linkchecker.XmlGenerator;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * High level tests of LinkDataExtractors
 */
public class LinkDataExtractorTest {

    @Test
    void arsTechnicaIsManaged() throws ContentParserException {
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
    void baeldungIsManaged() throws ContentParserException {
        final String url = "https://www.baeldung.com/java-unit-testing-best-practices";
        final String expectedXml = """
                <ARTICLE><X><T>Best Practices for Unit Testing in Java</T>\
                <A>https://www.baeldung.com/java-unit-testing-best-practices</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Anshul</FIRSTNAME><LASTNAME>Bansal</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>5</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void dzoneIsManaged() throws ContentParserException {
        final String url = "https://dzone.com/articles/explore-annotations-in-java-8";
        final String expectedXml = """
                <ARTICLE><X><T>Explore Annotations in Java 8</T>\
                <ST>Explore the evolution of annotations in Java 8 and how they are being used today!</ST>\
                <A>https://dzone.com/articles/explore-annotations-in-java-8</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Niamul</FIRSTNAME><LASTNAME>Sanjavi</LASTNAME></AUTHOR>\
                <DATE><YEAR>2019</YEAR><MONTH>10</MONTH><DAY>22</DAY></DATE>\
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
                <AUTHOR><FIRSTNAME>Dmitriy</FIRSTNAME><LASTNAME>Zaporozhets</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Sid</FIRSTNAME><LASTNAME>Sijbrandij</LASTNAME></AUTHOR>\
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
    void mediumWithSubtitleIsManaged() throws ContentParserException {
        final String url = "https://medium.com/rahasak/build-rag-application-using-a-llm-running-on-local-computer-with-gpt4all-and-langchain-13b4b8851db8";
        final String expectedXml = """
                <ARTICLE><X><T>Build RAG Application Using a LLM Running on Local Computer with GPT4All and Langchain</T>\
                <ST>Privacy-preserving LLM without GPU</ST>\
                <A>https://medium.com/rahasak/build-rag-application-using-a-llm-running-on-local-computer-with-gpt4all-and-langchain-13b4b8851db8</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><GIVENNAME>(λx.x)eranga</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>3</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsJavaMagazineIsManaged() throws ContentParserException {
        final String url = "https://blogs.oracle.com/javamagazine/post/java-nio-nio2-buffers-channels-async-future-callback";
        final String expectedXml = """
                <ARTICLE><X><T>Modern file input/output with Java: Going fast with NIO and NIO.2</T>\
                <ST>Reach for these low-level Java APIs when you need to move a lot of file data or socket data quickly.</ST>\
                <A>https://blogs.oracle.com/javamagazine/post/java-nio-nio2-buffers-channels-async-future-callback</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Evans</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>1</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsJavaIsManaged() throws ContentParserException {
        final String url = "https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se";
        final String expectedXml = """
                <ARTICLE><X><T>Faster and Easier Use and Redistribution of Java SE</T>\
                <A>https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Donald</FIRSTNAME><LASTNAME>Smith</LASTNAME></AUTHOR>\
                <DATE><YEAR>2017</YEAR><MONTH>9</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void oracleBlogsWithoutPostIsManaged() throws ContentParserException {
        final String url = "https://blogs.oracle.com/javamagazine/unit-test-your-architecture-with-archunit";
        final String expectedXml = """
                <ARTICLE><X><T>Unit Test Your Architecture with ArchUnit</T>\
                <ST>Discover architectural defects at build time.</ST>\
                <A>https://blogs.oracle.com/javamagazine/post/unit-test-your-architecture-with-archunit</A>\
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
    void oracleBlogsUrlIsProperlyCleanedUp() throws ContentParserException {
        final String url = "https://blogs.oracle.com/javamagazine/post/curly-braces-java-recursion-tail-call-optimization";
        final String expectedXml = """
                <ARTICLE><X><T>Curly Braces #6: Recursion and tail-call optimization</T>\
                <A>https://blogs.oracle.com/javamagazine/post/curly-braces-java-recursion-tail-call-optimization</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Eric</FIRSTNAME><MIDDLENAME>J.</MIDDLENAME><LASTNAME>Bruno</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void SimonWillisonIsManaged() throws ContentParserException {
        final String url = "https://simonwillison.net/2024/Mar/8/gpt-4-barrier/";
        final String expectedXml = """
                <ARTICLE><X><T>The GPT-4 barrier has finally been broken</T>\
                <A>https://simonwillison.net/2024/Mar/8/gpt-4-barrier/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Simon</FIRSTNAME><LASTNAME>Willison</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>3</MONTH><DAY>8</DAY></DATE>\
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
    void stackOverflowBlogIsManaged() throws ContentParserException {
        final String url = "https://stackoverflow.blog/2023/01/09/beyond-git-the-other-version-control-systems-developers-use/";
        final String expectedXml = """
                <ARTICLE><X><T>Beyond Git: The other version control systems developers use</T>\
                <ST>Our developer survey found 93% of developers use Git. But what are the other 7% using?</ST>\
                <A>https://stackoverflow.blog/2023/01/09/beyond-git-the-other-version-control-systems-developers-use/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Ryan</FIRSTNAME><LASTNAME>Donovan</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }    @Test

    void youtubeWatch1LittleCoderIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Pvn4szm2UnQ";
        final String expectedXml = """
                <ARTICLE><X><T>The GPT Store!!!</T>\
                <A>https://www.youtube.com/watch?v=Pvn4szm2UnQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Abdul Majed</FIRSTNAME><LASTNAME>Raja</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>1</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    void youtubeWatch3Blue1BrownIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-RdOwhmqP5s";
        final String expectedXml = """
                <ARTICLE><X><T>From Newton’s method to Newton’s fractal (which Newton knew nothing about)</T>\
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
    void youtubeWatchAiCoffeeBreakWithLetitiaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ec9IQMiJBhs";
        final String expectedXml = """
                <ARTICLE><X><T>Transformers explained | The architecture behind LLMs</T>\
                <A>https://www.youtube.com/watch?v=ec9IQMiJBhs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>47</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Letitia</FIRSTNAME><LASTNAME>Parcalabescu</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>1</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAiFluxIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=wemHDMh2I8c";
        final String expectedXml = """
                <ARTICLE><X><T>Mistral AI Trolls Google With 87GB T*rrent (Beats GPT4)</T>\
                <A>https://www.youtube.com/watch?v=wemHDMh2I8c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>13</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Noah</FIRSTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>12</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAiJasonIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=_j7JEDWuqLE";
        final String expectedXml = """
                <ARTICLE><X><T>Hugging Face + Langchain in 5 mins | Access 200k+ FREE AI models for your AI apps</T>\
                <A>https://www.youtube.com/watch?v=_j7JEDWuqLE</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>9</MINUTE><SECOND>47</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jason</FIRSTNAME><LASTNAME>Zhou</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchAndrejKarpathyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=VMj-3S1tku0";
        final String expectedXml = """
                <ARTICLE><X><T>The spelled-out intro to neural networks and backpropagation: building micrograd</T>\
                <A>https://www.youtube.com/watch?v=VMj-3S1tku0</A>\
                <L>en</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>25</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Andrej</FIRSTNAME><LASTNAME>Karpathy</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchArjanCodesIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=pxuXaaT1u3k";
        final String expectedXml = """
                <ARTICLE><X><T>Python Logging: How to Write Logs Like a Pro!</T>\
                <A>https://www.youtube.com/watch?v=pxuXaaT1u3k</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Arjan</FIRSTNAME><LASTNAME>Egges</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>3</DAY></DATE>\
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
    void youtubeWatchBaborLelefanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=l6g6c_3zmHs";
        final String expectedXml = """
                <ARTICLE><X><T>Cette méthode hallucinante permet d'écrire 417% plus vite 🤯 (feat. @ggmilgram )</T>\
                <A>https://www.youtube.com/watch?v=l6g6c_3zmHs</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Clément</FIRSTNAME><LASTNAME>Berut</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>19</DAY></DATE>\
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
    void youtubeWatchBeyondFireshipIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=cw34KMPSt4k";
        final String expectedXml = """
                <ARTICLE><X><T>How I deploy serverless containers for free</T>\
                <A>https://www.youtube.com/watch?v=cw34KMPSt4k</A><L>en</L><F>MP4</F>\
                <DURATION><MINUTE>6</MINUTE><SECOND>32</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jeff</FIRSTNAME><LASTNAME>Delaney</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>3</MONTH><DAY>15</DAY></DATE>\
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
                <ARTICLE><X><T>How to solve a triple exponential equation with different bases</T>\
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
    void youtubeWatchBlastPalomaMoritzsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=zbChGas32BI";
        final String expectedSureXml = """
                <ARTICLE><X><T>RÉVÉLATIONS SUR LES PRATIQUES "CRIMINELLES" DE TOTAL</T>\
                <A>https://www.youtube.com/watch?v=zbChGas32BI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>40</MINUTE><SECOND>40</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>5</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>RÉVÉLATIONS SUR LES PRATIQUES "CRIMINELLES" DE TOTAL</T>\
                <A>https://www.youtube.com/watch?v=zbChGas32BI</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>40</MINUTE><SECOND>40</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Paloma</FIRSTNAME><LASTNAME>Moritz</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>5</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }
    @Test
    void youtubeWatchChatSceptiqueIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=VubMw46DK0U";
        final String expectedXml = """
                <ARTICLE><X><T>L'erreur Excel qui a coûté la vie à 1500 personnes 💀</T>\
                <A>https://www.youtube.com/watch?v=VubMw46DK0U</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>1</SECOND></DURATION></X>\
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
                <ARTICLE><X><T>Un PROCÈS pour FAKE-UP ? - Clément Freze (avec Vous avez le droit)</T>\
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
    void youtubeWatcCarberraIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=jA5L0GDrVqs";
        final String expectedXml = """
                <ARTICLE><X><T>ALL Python 3.12's major typing improvements</T>\
                <A>https://www.youtube.com/watch?v=jA5L0GDrVqs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Ethan</FIRSTNAME><LASTNAME>Henderson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchContinuousDeliveryIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=_S5iUf0ANyQ";
        final String expectedXml = """
                <ARTICLE><X><T>Are You Chicago Or London When It Comes To TDD?</T>\
                <A>https://www.youtube.com/watch?v=_S5iUf0ANyQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dave</FIRSTNAME><LASTNAME>Farley</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>9</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchComputerfileSteveBagleyMikePoundIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=95ovjnMhUq0";
        final String expectedSureXml = """
                <ARTICLE><X><T>Acropalypse Now - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=95ovjnMhUq0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>52</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>28</DAY>\
                </DATE><COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Acropalypse Now - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=95ovjnMhUq0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>52</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Steve</FIRSTNAME><LASTNAME>Bagley</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Mike</FIRSTNAME><LASTNAME>Pound</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileDavidBrailsfordIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=CE8UhcyJS0I";
        final String expectedSureXml = """
                <ARTICLE><X><T>Busy Beaver Turing Machines - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=CE8UhcyJS0I</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>17</MINUTE><SECOND>56</SECOND></DURATION></X>\
                <DATE><YEAR>2014</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Busy Beaver Turing Machines - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=CE8UhcyJS0I</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>17</MINUTE><SECOND>56</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><MIDDLENAME>F.</MIDDLENAME><LASTNAME>Brailsford</LASTNAME></AUTHOR>\
                <DATE><YEAR>2014</YEAR><MONTH>9</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileRichardGCleggIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=J_EehoXLbIU";
        final String expectedSureXml = """
                <ARTICLE><X><T>Kernelless Kernel Programming (eBPF) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=J_EehoXLbIU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>11</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Kernelless Kernel Programming (eBPF) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=J_EehoXLbIU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>11</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Richard</FIRSTNAME><MIDDLENAME>G.</MIDDLENAME><LASTNAME>Clegg</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileRobertMilesIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=viJt_DXTfwA";
        final String expectedSureXml = """
                <ARTICLE><X><T>ChatGPT with Rob Miles - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=viJt_DXTfwA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>36</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>ChatGPT with Rob Miles - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=viJt_DXTfwA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>36</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Robert</FIRSTNAME><LASTNAME>Miles</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileTimMullerIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Fy3Odm-dny0";
        final String expectedSureXml = """
                <ARTICLE><X><T>TETRA Vulnerability (TETRA:BURST) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=Fy3Odm-dny0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>42</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>TETRA Vulnerability (TETRA:BURST) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=Fy3Odm-dny0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>42</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tim</FIRSTNAME><LASTNAME>Muller</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileMikePoundIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-ShwJqAalOk";
        final String expectedSureXml = """
                <ARTICLE><X><T>Breaking RSA - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=-ShwJqAalOk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>14</MINUTE><SECOND>49</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>5</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Breaking RSA - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=-ShwJqAalOk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>14</MINUTE><SECOND>49</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Mike</FIRSTNAME><LASTNAME>Pound</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>5</MONTH><DAY>10</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileThorstenAltenkirchIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=32bC33nJR3A";
        final String expectedSureXml = """
                <ARTICLE><X><T>Automata &amp; Python - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=32bC33nJR3A</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>9</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Automata &amp; Python - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=32bC33nJR3A</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>9</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thorsten</FIRSTNAME><LASTNAME>Altenkirch</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileLaurenceTrattIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=c32zXYAK7CI";
        final String expectedSureXml = """
                <ARTICLE><X><T>Garbage Collection (Mark &amp; Sweep) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=c32zXYAK7CI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Garbage Collection (Mark &amp; Sweep) - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=c32zXYAK7CI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Laurence</FIRSTNAME><LASTNAME>Tratt</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchComputerfileKaiXutIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=qQ9Wu1IxsYw";
        final String expectedSureXml = """
                <ARTICLE><X><T>Foundations of Data Visualisation - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=qQ9Wu1IxsYw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>7</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>4</MONTH><DAY>13</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Foundations of Data Visualisation - Computerphile</T>\
                <A>https://www.youtube.com/watch?v=qQ9Wu1IxsYw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>7</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kai</FIRSTNAME><LASTNAME>Xu</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>4</MONTH><DAY>13</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchDeepSkyVideosPaulCrowtherIsManaged() throws ContentParserException {
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
    void youtubeWatchDeepSkyVideosMeganGrayIsManaged() throws ContentParserException {
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
    void youtubeWatchDeepSkyVideosMichaelMerryfielIsManaged() throws ContentParserException {
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
    void youtubeWatchDomainOfScienceIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=bD2M7P6dTVA";
        final String expectedXml = """
                <ARTICLE><X><T>The Map of Superconductivity</T>\
                <A>https://www.youtube.com/watch?v=bD2M7P6dTVA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>23</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dominic</FIRSTNAME><LASTNAME>Walliman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>7</MONTH><DAY>3</DAY></DATE>\
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
    void youtubeWatchDrPeyamIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ThaHppIWByk";
        final String expectedXml = """
                <ARTICLE><X><T>can you solve this “impossible” trig problem?</T>\
                <A>https://www.youtube.com/watch?v=ThaHppIWByk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Peyam</FIRSTNAME><MIDDLENAME>Ryan</MIDDLENAME><LASTNAME>Tabrizian</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>14</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchElJjIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=qZWbgBSfTUI";
        final String expectedXml = """
                <ARTICLE><X><T>Les mathématiques de Marvel - Ccc #08</T>\
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
    void youtubeWatchFireshipIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=i8NETqtGHms";
        final String expectedXml = """
                <ARTICLE><X><T>TensorFlow in 100 Seconds</T>\
                <A>https://www.youtube.com/watch?v=i8NETqtGHms</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>2</MINUTE><SECOND>38</SECOND></DURATION>\
                </X><AUTHOR><FIRSTNAME>Jeff</FIRSTNAME><LASTNAME>Delaney</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>8</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchGotoConferenceDaveFarleyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ihOUzzwPFIk";
        final String expectedSureXml = """
                <ARTICLE><X><T>The 3 Types of Unit Test in TDD • Dave Farley • GOTO 2022</T>\
                <A>https://www.youtube.com/watch?v=ihOUzzwPFIk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>17</MINUTE><SECOND>29</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The 3 Types of Unit Test in TDD • Dave Farley • GOTO 2022</T>\
                <A>https://www.youtube.com/watch?v=ihOUzzwPFIk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>17</MINUTE><SECOND>29</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Dave</FIRSTNAME><LASTNAME>Farley</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchGregKamradtDataIndyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=NihPT7KIee8";
        final String expectedXml = """
                <ARTICLE><X><T>The AI Task Force You Need At Work</T>\
                <A>https://www.youtube.com/watch?v=NihPT7KIee8</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>55</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Greg</FIRSTNAME><LASTNAME>Kamradt</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>18</DAY></DATE>\
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
    void youtubeWatchHistoryOfTheEarthIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=0sbwUeTyDb0";
        final String expectedXml = """
                <ARTICLE><X><T>What Was The "Boring Billion" Really Like?</T>\
                <A>https://www.youtube.com/watch?v=0sbwUeTyDb0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>36</MINUTE><SECOND>9</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Kelly</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Pete</FIRSTNAME><LASTNAME>Kelly</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Leila</FIRSTNAME><LASTNAME>Battison</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchHistoryOfTheUniverseIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=uBk-Wst_7aA";
        final String expectedXml = """
                <ARTICLE><X><T>Is There One All Powerful Superforce Controlling The Universe?</T>\
                <A>https://www.youtube.com/watch?v=uBk-Wst_7aA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>36</MINUTE><SECOND>17</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Kelly</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Leila</FIRSTNAME><LASTNAME>Battison</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>5</MONTH><DAY>28</DAY>\
                </DATE><COMMENT>XXXXX</COMMENT></ARTICLE>""";
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
    void youtubeWatchHomoFabulusIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ZdEVVDk48L4";
        final String expectedXml = """
                <ARTICLE><X><T>Climat : comment faire pour que les gens se bougent (une fois qu’ils ont été bien informés)</T>\
                <A>https://www.youtube.com/watch?v=ZdEVVDk48L4</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>25</MINUTE><SECOND>44</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Stéphane</FIRSTNAME><LASTNAME>Debove</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>23</DAY></DATE>\
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
    void youtubeWatchJavaInsideJavaNewscastIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eDgBnjOid-g";
        final String expectedSureXml = """
                <ARTICLE><X><T>What Happens to Finalization in JDK 18? - Inside Java Newscast #15</T>\
                <A>https://www.youtube.com/watch?v=eDgBnjOid-g</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>What Happens to Finalization in JDK 18? - Inside Java Newscast #15</T>\
                <A>https://www.youtube.com/watch?v=eDgBnjOid-g</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>58</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Nicolai</FIRSTNAME><LASTNAME>Parlog</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchJavaJepCafeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=NDaA9MrTLBM";
        final String expectedSureXml = """
                <ARTICLE><X><T>Text Blocks - JEP Café #5</T>\
                <A>https://www.youtube.com/watch?v=NDaA9MrTLBM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Text Blocks - JEP Café #5</T>\
                <A>https://www.youtube.com/watch?v=NDaA9MrTLBM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>José</FIRSTNAME><LASTNAME>Paumard</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
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
                <ARTICLE><X><T>☀️ Soleil vs ITER : une Histoire de la FUSION #11 Science</T>\
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
    void youtubeWatchKevinFangIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=dsHyUgGMht0";
        final String expectedXml = """
                <ARTICLE><X><T>How GitHub's Database Self-Destructed in 43 Seconds</T>\
                <A>https://www.youtube.com/watch?v=dsHyUgGMht0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>3</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kevin</FIRSTNAME><LASTNAME>Fang</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>12</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchKyleHillIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=e3GYg7Y_W7s";
        final String expectedXml = """
                <ARTICLE><X><T>The Lia Radiological Accident - Nuclear Bonfire</T>\
                <A>https://www.youtube.com/watch?v=e3GYg7Y_W7s</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>32</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Hill</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLaChaineEVIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=GdkelMnxiaE";
        final String expectedXml = """
                <ARTICLE><X><T>ABRP vs CHARGEMAP ! 450km pour comparer les 2 planificateurs sur mobile !</T>\
                <A>https://www.youtube.com/watch?v=GdkelMnxiaE</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>46</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jean-Christophe</FIRSTNAME><LASTNAME>Gigniac</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Alexandre</FIRSTNAME><LASTNAME>Homberger</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLaTroncheEnBiaisIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=LsMNe_a5Xn0";
        final String expectedXml = """
                <ARTICLE><X><T>Les jeunes sont débiles</T>\
                <A>https://www.youtube.com/watch?v=LsMNe_a5Xn0</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>31</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thomas</FIRSTNAME><LASTNAME>Durand</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>21</DAY></DATE>\
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
    void youtubeWatchLeSenseOfWonderIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=vMipq5Y6PuU";
        final String expectedSureXml = """
                <ARTICLE><X><T>Ondes gravitationnelles : le cosmos vibre sous nos pieds #LeSOW 9</T>\
                <A>https://www.youtube.com/watch?v=vMipq5Y6PuU</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>33</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Ondes gravitationnelles : le cosmos vibre sous nos pieds #LeSOW 9</T>\
                <A>https://www.youtube.com/watch?v=vMipq5Y6PuU</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>33</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sébastien</FIRSTNAME><LASTNAME>Carassou</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Étienne</FIRSTNAME><LASTNAME>Ledolley</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchLiuZuoLinIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=AMoBy45dYpY";
        final String expectedXml = """
                <ARTICLE><X><T>7 Things I Never Knew About Python Until Recently (Part 1)</T>\
                <A>https://www.youtube.com/watch?v=AMoBy45dYpY</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>7</MINUTE><SECOND>11</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Liu</FIRSTNAME><LASTNAME>Zuo Lin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>22</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchLinguisticaeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=aLerv1-8erQ";
        final String expectedXml = """
                <ARTICLE><X><T>Les LANGUES dans STAR WARS (ft.@ChroniqueNEXUSVI)</T>\
                <A>https://www.youtube.com/watch?v=aLerv1-8erQ</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>34</MINUTE><SECOND>39</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Romain</FIRSTNAME><LASTNAME>Filstroff</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>31</DAY></DATE>\
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
                <ARTICLE><X><T>L'INCROYABLE HISTOIRE DE LA CONJECTURE DE FERMAT CMH#14</T>\
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
    void youtubeWatchMathologer2IsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=WY5X_3q80WY";
        final String expectedXml = """
                <ARTICLE><X><T>Animating Nicomachus's 2000 year old mathematical gem (Mathologer Christmas video)</T>\
                <A>https://www.youtube.com/watch?v=WY5X_3q80WY</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>3</MINUTE><SECOND>29</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Burkard</FIRSTNAME><LASTNAME>Polster</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>24</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMattParker2IsManaged() throws ContentParserException {
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
    void youtubeWatchMatthewBermanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=3BDboYfjWE8";
        final String expectedXml = """
                <ARTICLE><X><T>Did OpenAI Just Secretly Release GPT-5?! ("GPT2-Chatbot")</T>\
                <A>https://www.youtube.com/watch?v=3BDboYfjWE8</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>17</MINUTE><SECOND>11</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matthew</FIRSTNAME><LASTNAME>Berman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>4</MONTH><DAY>30</DAY></DATE>\
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
    void youtubeWatchMichaelRyanClarksonIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=GdcOy6zVFC4";
        final String expectedXml = """
                <ARTICLE><X><T>The Curry-Howard Correspondence</T>\
                <A>https://www.youtube.com/watch?v=GdcOy6zVFC4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>45</MINUTE><SECOND>32</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Michael</FIRSTNAME><MIDDLENAME>Ryan</MIDDLENAME><LASTNAME>Clarkson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>8</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchMinutePhysicsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=vLIPUdru82c";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Problem With The Butterfly Effect</T>\
                <A>https://www.youtube.com/watch?v=vLIPUdru82c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Problem With The Butterfly Effect</T>\
                <A>https://www.youtube.com/watch?v=vLIPUdru82c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Henry</FIRSTNAME><LASTNAME>Reich</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>21</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
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
    void youtubeWatchNotaBonusIsManaged() throws ContentParserException {
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
    void youtubeWatchNumberphileTomCrawfordIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Sgupo9DLMGs";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Distance Between Numbers - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=Sgupo9DLMGs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>33</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Distance Between Numbers - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=Sgupo9DLMGs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>33</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tom</FIRSTNAME><LASTNAME>Crawford</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphilMattParkerIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=EJRXWNWJOrQ";
        final String expectedSureXml = """
                <ARTICLE><X><T>37 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=EJRXWNWJOrQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>2</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <DATE><YEAR>2012</YEAR><MONTH>7</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>37 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=EJRXWNWJOrQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>2</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2012</YEAR><MONTH>7</MONTH><DAY>20</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileMarcusDuSautoyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=nlm07asSU0c";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Beautiful Math of Snakes and Ladders - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=nlm07asSU0c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>45</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>11</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Beautiful Math of Snakes and Ladders - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=nlm07asSU0c</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>21</MINUTE><SECOND>45</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Marcus</FIRSTNAME><LASTNAME>du Sautoy</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>11</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileDavidEisenbudIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=NWahomDHaDs";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Journey to 3264 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=NWahomDHaDs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>37</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>4</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Journey to 3264 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=NWahomDHaDs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>37</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>David</FIRSTNAME><LASTNAME>Eisenbud</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>4</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileTonyFengIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=FmLIGN8ZGdw";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Return of -1/12 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=FmLIGN8ZGdw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>56</SECOND></DURATION></X>\
                <DATE><YEAR>2024</YEAR><MONTH>2</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Return of -1/12 - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=FmLIGN8ZGdw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>56</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tony</FIRSTNAME><LASTNAME>Feng</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Tony</FIRSTNAME><LASTNAME>Padilla</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>2</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileEdwardFrenkelIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=4dyytPboqvE";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Langlands Program - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=4dyytPboqvE</A>\
                <L>en</L><F>MP4</F><DURATION><HOUR>1</HOUR><MINUTE>3</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Langlands Program - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=4dyytPboqvE</A>\
                <L>en</L><F>MP4</F><DURATION><HOUR>1</HOUR><MINUTE>3</MINUTE><SECOND>26</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Edward</FIRSTNAME><LASTNAME>Frenkel</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileJamesGrimeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ZdQFN2XKeKI";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Goat Problem - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ZdQFN2XKeKI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>24</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Goat Problem - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ZdQFN2XKeKI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>James</FIRSTNAME><LASTNAME>Grime</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>24</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileHollyKriegerIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ASJpflLbPkU";
        final String expectedSureXml = """
                <ARTICLE><X><T>Infinite Circles - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ASJpflLbPkU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>53</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>12</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Infinite Circles - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ASJpflLbPkU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>53</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Holly</FIRSTNAME><LASTNAME>Krieger</LASTNAME>\
                </AUTHOR><DATE><YEAR>2023</YEAR><MONTH>12</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileJaredDukerLichtmanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=dwe4-OiRw7M";
        final String expectedSureXml = """
                <ARTICLE><X><T>23% Beyond the Riemann Hypothesis - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=dwe4-OiRw7M</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>23% Beyond the Riemann Hypothesis - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=dwe4-OiRw7M</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jared</FIRSTNAME><MIDDLENAME>Duker</MIDDLENAME><LASTNAME>Lichtman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileAylieanMacDonaldIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ArADlJx7SlU";
        final String expectedSureXml = """
                <ARTICLE><X><T>A New Tile in Newtyle - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ArADlJx7SlU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>50</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>A New Tile in Newtyle - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=ArADlJx7SlU</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>26</MINUTE><SECOND>50</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Ayliean</FIRSTNAME><LASTNAME>MacDonald</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileSophieMacleanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=fczN0BCx0xs";
        final String expectedSureXml = """
                <ARTICLE><X><T>Catalan Numbers - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=fczN0BCx0xs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <DATE><YEAR>2024</YEAR><MONTH>1</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Catalan Numbers - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=fczN0BCx0xs</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>15</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sophie</FIRSTNAME><LASTNAME>Maclean</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>1</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileTonyPadillaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=hzcdcrsyA9Y";
        final String expectedSureXml = """
                <ARTICLE><X><T>Travelling to the Stars by Ladder - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=hzcdcrsyA9Y</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>22</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Travelling to the Stars by Ladder - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=hzcdcrsyA9Y</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>22</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tony</FIRSTNAME><LASTNAME>Padilla</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileGrantSandersonIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=YAsHGOwB408";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Prime Number Race (with 3Blue1Brown) - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=YAsHGOwB408</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>28</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Prime Number Race (with 3Blue1Brown) - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=YAsHGOwB408</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>28</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileHenrySegermanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=3akBMSJ37Uk";
        final String expectedSureXml = """
                <ARTICLE><X><T>What are these strange dice? - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=3akBMSJ37Uk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>11</MONTH><DAY>27</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>What are these strange dice? - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=3akBMSJ37Uk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Henry</FIRSTNAME><LASTNAME>Segerman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>11</MONTH><DAY>27</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileCliffStollIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=KNq0eUBxPXc";
        final String expectedSureXml = """
                <ARTICLE><X><T>Your Klein Bottle is in the Post - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=KNq0eUBxPXc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Your Klein Bottle is in the Post - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=KNq0eUBxPXc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>5</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Cliff</FIRSTNAME><LASTNAME>Stoll</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileNeilSloaneIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=DUaqiM1bGX4";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Yellowstone Permutation - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=DUaqiM1bGX4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>59</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Yellowstone Permutation - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=DUaqiM1bGX4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>20</MINUTE><SECOND>59</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Neil</FIRSTNAME><LASTNAME>Sloane</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>29</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileBenSparksIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=-UBDRX6bk-A";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Light Switch Problem - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=-UBDRX6bk-A</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>30</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Light Switch Problem - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=-UBDRX6bk-A</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>18</MINUTE><SECOND>30</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphileThomasWoolleyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=WR3GqqWAmfw";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Math of Species Conflict - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=WR3GqqWAmfw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>34</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>30</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Math of Species Conflict - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=WR3GqqWAmfw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>34</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Thomas</FIRSTNAME><LASTNAME>Woolley</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>30</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchNumberphile2AylieanMacDonaldIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=k85GOcuTmm0";
        final String expectedSureXml = """
                <ARTICLE><X><T>A New Tile (extra) - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=k85GOcuTmm0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>A New Tile (extra) - Numberphile</T>\
                <A>https://www.youtube.com/watch?v=k85GOcuTmm0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Ayliean</FIRSTNAME><LASTNAME>MacDonald</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeOfficielDefakatorIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=j3fvoM5Er2k";
        final String expectedXml = """
                <ARTICLE><X><T>🤖 Comprendre ChatGPT (avec DefendIntelligence)</T>\
                <A>https://www.youtube.com/watch?v=j3fvoM5Er2k</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>49</MINUTE><SECOND>53</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>Defakator</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>2</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeOsonsCauserIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=b4HfK_coDcc";
        final String expectedXml = """
                <ARTICLE><X><T>Nucléaire : un atout pour l'indépendance de la France ?</T>\
                <A>https://www.youtube.com/watch?v=b4HfK_coDcc</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>29</MINUTE><SECOND>38</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Ludo</FIRSTNAME><LASTNAME>Torbey</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>20</DAY></DATE>\
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
                <ARTICLE><X><T>Our Ancient Relative That Said 'No Thanks' To Land</T>\
                <A>https://www.youtube.com/watch?v=Ke0BjyO41Qg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>9</MINUTE><SECOND>55</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>4</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertEquals(3, extractor.getPossibleAuthors().size());
        Assertions.assertEquals("▭ first=Michelle ▭ last=Barboza-Ramirez ▭ ▭", extractor.getPossibleAuthors().get(0).toString());
        Assertions.assertEquals("▭ first=Kallie ▭ last=Moore ▭ ▭", extractor.getPossibleAuthors().get(1).toString());
        Assertions.assertEquals("▭ first=Blake ▭ last=de Pastino ▭ ▭", extractor.getPossibleAuthors().get(2).toString());
    }

    @Test
    void youtubeWatchPeriodicTableIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ykpTFw6r7Hw";
        final String expectedSureXml = """
                <ARTICLE><X><T>Poaching an Egg in Piranha Solution - Periodic Table of Videos</T>\
                <A>https://www.youtube.com/watch?v=ykpTFw6r7Hw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Poaching an Egg in Piranha Solution - Periodic Table of Videos</T>\
                <A>https://www.youtube.com/watch?v=ykpTFw6r7Hw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>4</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Martyn</FIRSTNAME><LASTNAME>Poliakoff</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>Neil</FIRSTNAME><LASTNAME>Barnes</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>6</MONTH><DAY>11</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchPermacultureAgroecologieEtcIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=pfUK_ADTvgY";
        final String expectedSureXml = """
                <ARTICLE><X><T>Comment greffer facilement les arbres fruitiers ? (mois par mois)</T>\
                <A>https://www.youtube.com/watch?v=pfUK_ADTvgY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>25</MINUTE><SECOND>25</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Comment greffer facilement les arbres fruitiers ? (mois par mois)</T>\
                <A>https://www.youtube.com/watch?v=pfUK_ADTvgY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>25</MINUTE><SECOND>25</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Damien</FIRSTNAME><LASTNAME>Dekarz</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>12</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchPhiloximeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=YVtW5d94KN0";
        final String expectedXml = """
                <ARTICLE><X><T>Justice climatique : la COP où tout a déraillé</T>\
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
    void youtubeWatchPlainlyDifficultIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=XuZvc5mmAXk";
        final String expectedXml = """
                <ARTICLE><X><T>What Caused the 2013 Hueypoxtla, Mexico Radiation Event?  | Plainly Difficult Disaster Documentary</T>\
                <A>https://www.youtube.com/watch?v=XuZvc5mmAXk</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>47</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>John</FIRSTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>28</DAY></DATE>\
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
    void youtubeWatchRabbitHoleSyndromeIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Yhtjd7yGGGA";
        final String expectedXml = """
                <ARTICLE><X><T>ClippyGPT - How I Built Supabase’s OpenAI Doc Search (Embeddings)</T>\
                <A>https://www.youtube.com/watch?v=Yhtjd7yGGGA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>41</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Greg</FIRSTNAME><LASTNAME>Richardson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchRobertMilesAiSafety() throws ContentParserException {
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
    void youtubeWatchRobertMiles2() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eV8F0dz_5y4";
        final String expectedXml = """
                <ARTICLE><X><T>Easily Quit Mindless Scrolling</T>\
                <A>https://www.youtube.com/watch?v=eV8F0dz_5y4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>32</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Robert</FIRSTNAME><LASTNAME>Miles</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>6</MONTH><DAY>20</DAY></DATE>\
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
    void youtubeWatchSamWitteveenIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=06kslWw_QOc";
        final String expectedXml = """
                <ARTICLE><X><T>5 Problems Getting LLM Agents into Production</T>\
                <A>https://www.youtube.com/watch?v=06kslWw_QOc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>11</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sam</FIRSTNAME><LASTNAME>Witteveen</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>6</MONTH><DAY>4</DAY></DATE>\
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
    void youtubeWatchScienceClicPlusIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=zPRbbM4KJBY";
        final String expectedXml = """
                <ARTICLE><X><T>Qu'est-ce qu'un tenseur ?</T>\
                <A>https://www.youtube.com/watch?v=zPRbbM4KJBY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>24</MINUTE><SECOND>54</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Alessandro</FIRSTNAME><LASTNAME>Roussel</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Disabled("it seems that this YouTube channel has been removed")
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
    void youtubeWatchSciHankGreenShowIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=z2bmOGCh1Q8";
        final String expectedSureXml = """
                <ARTICLE><X><T>This Molecule Has Saved Billions of Lives, How Do We Make It Without Killing Ourselves?</T>\
                <A>https://www.youtube.com/watch?v=z2bmOGCh1Q8</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>41</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>This Molecule Has Saved Billions of Lives, How Do We Make It Without Killing Ourselves?</T>\
                <A>https://www.youtube.com/watch?v=z2bmOGCh1Q8</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>41</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Hank</FIRSTNAME><LASTNAME>Green</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>16</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSciStefanChinShowIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=i3_3ga2E8vw";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Future of Particle Accelerators Looks Wild</T>\
                <A>https://www.youtube.com/watch?v=i3_3ga2E8vw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Future of Particle Accelerators Looks Wild</T>\
                <A>https://www.youtube.com/watch?v=i3_3ga2E8vw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>36</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Stefan</FIRSTNAME><LASTNAME>Chin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>17</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
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
    void youtubeWatchSixtySymbolsEmmaChapmanIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=OXMBozstWG0";
        final String expectedSureXml = """
                <ARTICLE><X><T>Searching for Extraterrestrial Life (and the Drake Equation) - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=OXMBozstWG0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>25</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <DATE><YEAR>2024</YEAR><MONTH>2</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Searching for Extraterrestrial Life (and the Drake Equation) - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=OXMBozstWG0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>25</MINUTE><SECOND>27</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Emma</FIRSTNAME><LASTNAME>Chapman</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>2</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }
    @Test

    void youtubeWatchSixtySymbolsEdmundCopelandIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=MAGdU-G5OZg";
        final String expectedSureXml = """
                <ARTICLE><X><T>Black Holes and Dimensional Analysis - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=MAGdU-G5OZg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>31</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Black Holes and Dimensional Analysis - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=MAGdU-G5OZg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>57</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Edmund</FIRSTNAME><LASTNAME>Copeland</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>31</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsMeganGraryIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=66anxw_EuyQ";
        final String expectedSureXml = """
                <ARTICLE><X><T>Untangling the Cosmic Web - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=66anxw_EuyQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <DATE><YEAR>2021</YEAR><MONTH>5</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Untangling the Cosmic Web - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=66anxw_EuyQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>46</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Meghan</FIRSTNAME><LASTNAME>Gray</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>5</MONTH><DAY>7</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsMikeMerrifieldIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=EfwXWNla-Ts";
        final String expectedSureXml = """
                <ARTICLE><X><T>A Curious Problem with Red Galaxies - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=EfwXWNla-Ts</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>42</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>A Curious Problem with Red Galaxies - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=EfwXWNla-Ts</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>12</MINUTE><SECOND>42</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Michael</FIRSTNAME><LASTNAME>Merrifield</LASTNAME>\
                </AUTHOR><DATE><YEAR>2023</YEAR><MONTH>6</MONTH><DAY>1</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsPhilipMoriartyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=GBtfwa-Fexc";
        final String expectedSureXml = """
                <ARTICLE><X><T>ChatGPT does Physics - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=GBtfwa-Fexc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>41</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>ChatGPT does Physics - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=GBtfwa-Fexc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>41</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Philip</FIRSTNAME><LASTNAME>Moriarty</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>23</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsTonyPadillaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=py8k1tn8yw4";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Interesting Physics of Robert Oppenheimer (not the bomb) - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=py8k1tn8yw4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>8</MONTH><DAY>22</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Interesting Physics of Robert Oppenheimer (not the bomb) - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=py8k1tn8yw4</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>19</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Tony</FIRSTNAME><LASTNAME>Padilla</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>8</MONTH><DAY>22</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsBeckySmethurstIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=pGO_GJL17gM";
        final String expectedSureXml = """
                <ARTICLE><X><T>The Biggest Possible Black Hole - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=pGO_GJL17gM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>40</SECOND></DURATION>\
                </X><DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>28</DAY>\
                </DATE><COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>The Biggest Possible Black Hole - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=pGO_GJL17gM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>40</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Becky</FIRSTNAME><LASTNAME>Smethurst</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchSixtySymbolsJamesUnwinIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=BlqHLHxVliI";
        final String expectedSureXml = """
                <ARTICLE><X><T>Is there a Black Hole in our Solar System? - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=BlqHLHxVliI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>5</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Is there a Black Hole in our Solar System? - Sixty Symbols</T>\
                <A>https://www.youtube.com/watch?v=BlqHLHxVliI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Becky</FIRSTNAME><LASTNAME>Smethurst</LASTNAME></AUTHOR>\
                <AUTHOR><FIRSTNAME>James</FIRSTNAME><LASTNAME>Unwin</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>5</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
    }

    @Test
    void youtubeWatchStandupMathsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=ueEOHk1UzrA";
        final String expectedXml = """
                <ARTICLE><X><T>Find your own ABC Conjecture Triple</T>\
                <A>https://www.youtube.com/watch?v=ueEOHk1UzrA</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>28</MINUTE><SECOND>16</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Parker</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchStatedClearlyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=FNynz6Q12Bw";
        final String expectedXml = """
                <ARTICLE><X><T>DNA vs RNA - Differences in Form and Function | Stated Clearly</T>\
                <A>https://www.youtube.com/watch?v=FNynz6Q12Bw</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>49</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jon</FIRSTNAME><LASTNAME>Perry</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>1</MONTH><DAY>25</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchSteveMouldIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Qyn64b4LNJ0";
        final String expectedXml = """
                <ARTICLE><X><T>The Planets Are Weirdly In Sync</T>\
                <A>https://www.youtube.com/watch?v=Qyn64b4LNJ0</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>23</MINUTE><SECOND>21</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Steve</FIRSTNAME><LASTNAME>Mould</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>4</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchSuperDataScienceIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=8i5s7DFAjek";
        final String expectedXml = """
                <ARTICLE><X><T>778: Mixtral 8x22B: SOTA Open-Source LLM Capabilities at a Fraction of the Compute — with Jon Krohn</T>\
                <A>https://www.youtube.com/watch?v=8i5s7DFAjek</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>6</MINUTE><SECOND>30</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Jon</FIRSTNAME><LASTNAME>Krohn</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>4</MONTH><DAY>26</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchTechWorldWithNanaIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=R8_veQiYBjI";
        final String expectedXml = """
                <ARTICLE><X><T>GitHub Actions Tutorial - Basic Concepts and CI/CD Pipeline with Docker</T>\
                <A>https://www.youtube.com/watch?v=R8_veQiYBjI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>32</MINUTE><SECOND>30</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Nana</FIRSTNAME><LASTNAME>Janashia</LASTNAME></AUTHOR>\
                <DATE><YEAR>2020</YEAR><MONTH>10</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchThePrimeTimesIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=GkmUwDXvWiQ";
        final String expectedXml = """
                <ARTICLE><X><T>Why I Quit Copilot | Prime Reacts</T>\
                <A>https://www.youtube.com/watch?v=GkmUwDXvWiQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>35</MINUTE><SECOND>55</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Michael</FIRSTNAME><MIDDLENAME>B.</MIDDLENAME><LASTNAME>Paulson</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>4</MONTH><DAY>6</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchThomathsIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=G5nbqZnlvHo";
        final String expectedSureXml = """
                <ARTICLE><X><T>Thomaths 14 : Les plus beaux solides de l'espace</T>\
                <A>https://www.youtube.com/watch?v=G5nbqZnlvHo</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>34</SECOND></DURATION></X>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Thomaths 14 : Les plus beaux solides de l'espace</T>\
                <A>https://www.youtube.com/watch?v=G5nbqZnlvHo</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>34</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Alexander</FIRSTNAME><LASTNAME>Thomas</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>11</MONTH><DAY>18</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
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
    void youtubeWatchVeryMathTripIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=tShSCFv_rGU";
        final String expectedXml = """
                <ARTICLE><X><T>Les bretelles d'autoroutes ne sont pas des arcs de cercle !</T>\
                <A>https://www.youtube.com/watch?v=tShSCFv_rGU</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>5</MINUTE><SECOND>51</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Manu</FIRSTNAME><LASTNAME>Houdart</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>9</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchWebDevSimplifiedIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=mnmYwRoSisg";
        final String expectedXml = """
                <ARTICLE><X><T>10 Must Know Git Commands That Almost Nobody Knows</T>\
                <A>https://www.youtube.com/watch?v=mnmYwRoSisg</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>15</MINUTE><SECOND>22</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kyle</FIRSTNAME><LASTNAME>Cook</LASTNAME></AUTHOR>\
                <DATE><YEAR>2021</YEAR><MONTH>10</MONTH><DAY>19</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchVousAvezLeDroitIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=w6k7eAA-qDQ";
        final String expectedXml = """
                <ARTICLE><X><T>Fallait-il interdire l’abaya ?</T>\
                <A>https://www.youtube.com/watch?v=w6k7eAA-qDQ</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>8</MINUTE><SECOND>54</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Sébastien</FIRSTNAME><LASTNAME>Canévet</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchWonderWhyIsManaged() throws ContentParserException {
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
    void youtubeWelchLabsWhyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=Phscjl0u6TI";
        final String expectedXml = """
                <ARTICLE><X><T>How Kepler Actually Discovered his Laws</T>\
                <A>https://www.youtube.com/watch?v=Phscjl0u6TI</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>16</MINUTE><SECOND>39</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Stephen</FIRSTNAME><LASTNAME>Welch</LASTNAME></AUTHOR>\
                <DATE><YEAR>2024</YEAR><MONTH>5</MONTH><DAY>8</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchYannicKilcherWhyIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=2zW33LfffPc";
        final String expectedXml = """
                <ARTICLE><X><T>GPT-4 is here! What we know so far (Full Analysis)</T>\
                <A>https://www.youtube.com/watch?v=2zW33LfffPc</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>34</MINUTE><SECOND>9</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Yannic</FIRSTNAME><LASTNAME>Kilcher</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>3</MONTH><DAY>15</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchYoshaEchecsIsManaged() throws ContentParserException {
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
    void youtubeWatchUnknownEnglishChannelIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=eAkb2mpybnM";
        final String expectedSureXml = """
                <ARTICLE><X><T>How I Built the Entire Universe in Minecraft</T>\
                <A>https://www.youtube.com/watch?v=eAkb2mpybnM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>35</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>How I Built the Entire Universe in Minecraft</T>\
                <A>https://www.youtube.com/watch?v=eAkb2mpybnM</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>13</MINUTE><SECOND>35</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>ChrisDaCow</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>3</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchUnknownFrenchChannelIsManaged() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?v=1Q4RoWIfpoY";
        final String expectedSureXml = """
                <ARTICLE><X><T>La Chine est embarrassée face à la guerre en Ukraine</T>\
                <A>https://www.youtube.com/watch?v=1Q4RoWIfpoY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>La Chine est embarrassée face à la guerre en Ukraine</T>\
                <A>https://www.youtube.com/watch?v=1Q4RoWIfpoY</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>10</MINUTE><SECOND>1</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>France Culture</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeWatchUrlInListIsManaged() throws ContentParserException {
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
    void arstechnicaQueryParametersAreRemoved() throws ContentParserException {
        final String url = "https://arstechnica.com/information-technology/2022/11/nvidia-wins-award-for-ai-that-can-play-minecraft-on-command/?comments=1&comments-page=1";
        final String expectedXml = """
                <ARTICLE><X><T>Nvidia AI plays Minecraft, wins machine-learning conference award</T>\
                <ST>NeurIPS 2022 honors MineDojo for playing Minecraft when instructed by written prompts.</ST>\
                <A>https://arstechnica.com/information-technology/2022/11/nvidia-wins-award-for-ai-that-can-play-minecraft-on-command/</A>\
                <L>en</L><F>HTML</F></X>\
                <AUTHOR><FIRSTNAME>Benj</FIRSTNAME><LASTNAME>Edwards</LASTNAME></AUTHOR>\
                <DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>28</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedXml, generateSureXml(extractor));
        Assertions.assertTrue(extractor.getProbableAuthors().isEmpty());
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeQueryParametersAreRemoved() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?app=desktop&v=iCEe6E9Yr9c&embeds_referring_euri=https%3A%2F%2Fwww.l214.com%2F&embeds_referring_origin=https%3A%2F%2Fwww.l214.com&source_ve_path=Mjg2NjY&feature=emb_logo";
        final String expectedSureXml = """
                <ARTICLE><X><T>Enquête inédite : l’enfer des cages pour les pintades du groupe LDC</T>\
                <A>https://www.youtube.com/watch?v=iCEe6E9Yr9c</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>2</MINUTE><SECOND>19</SECOND></DURATION></X>\
                <DATE><YEAR>2023</YEAR><MONTH>8</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final String expectedProbableXml = """
                <ARTICLE><X><T>Enquête inédite : l’enfer des cages pour les pintades du groupe LDC</T>\
                <A>https://www.youtube.com/watch?v=iCEe6E9Yr9c</A>\
                <L>fr</L><F>MP4</F><DURATION><MINUTE>2</MINUTE><SECOND>19</SECOND></DURATION></X>\
                <AUTHOR><GIVENNAME>L214 Éthique &amp; Animaux</GIVENNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>8</MONTH><DAY>9</DAY></DATE>\
                <COMMENT>XXXXX</COMMENT></ARTICLE>""";
        final LinkDataExtractor extractor = getExtractor(url);
        Assertions.assertEquals(expectedSureXml, generateSureXml(extractor));
        Assertions.assertEquals(expectedProbableXml, generateProbableXml(extractor));
        Assertions.assertTrue(extractor.getPossibleAuthors().isEmpty());
    }

    @Test
    void youtubeQueryParametersAreRemoved2() throws ContentParserException {
        final String url = "https://www.youtube.com/watch?si=QejhgLoV5Iy3rKjJ&v=r7HV4s-4ksQ&feature=youtu.be";
        final String expectedXml = """
                <ARTICLE><X><T>Capital One's $200M Cloud Data Breach</T>\
                <A>https://www.youtube.com/watch?v=r7HV4s-4ksQ</A>\
                <L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>18</SECOND></DURATION></X>\
                <AUTHOR><FIRSTNAME>Kevin</FIRSTNAME><LASTNAME>Fang</LASTNAME></AUTHOR>\
                <DATE><YEAR>2023</YEAR><MONTH>4</MONTH><DAY>13</DAY></DATE>\
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
        return XmlGenerator.generateXml(extractor.getLinks(), extractor.getDate(), extractor.getSureAuthors(), 0, "XXXXX");
    }

    private static String generateProbableXml(final LinkDataExtractor extractor) throws ContentParserException {
        final List<AuthorData> allAuthors = new ArrayList<>(extractor.getSureAuthors());
        allAuthors.addAll(extractor.getProbableAuthors());
        return XmlGenerator.generateXml(extractor.getLinks(), extractor.getDate(), allAuthors, 0, "XXXXX");
    }
}
