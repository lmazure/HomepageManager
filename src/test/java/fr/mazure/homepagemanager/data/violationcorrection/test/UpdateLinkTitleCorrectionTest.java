package fr.mazure.homepagemanager.data.violationcorrection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkTitleCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;

/**
 * Test of UpdateLinkTitleCorrection class
 */
public class UpdateLinkTitleCorrectionTest {

    @SuppressWarnings("static-method")
    @Test
    void titleIsUpdated() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Visual Perception with Deep Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Visual Perception with Machine Learning</T><A>https://www.youtube.com/watch?v=3boKlkPBckA</A><L>en</L><F>MP4</F><DURATION><MINUTE>57</MINUTE><SECOND>25</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Yann</FIRSTNAME><LASTNAME>Le Cun</LASTNAME></AUTHOR><DATE><YEAR>2008</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>Machine Learning: a description of the structure and learning methodology of a deep multi-layered architecture. Yann gives some examples for real-time video analysis.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final ViolationCorrection correction = new UpdateLinkTitleCorrection("Visual Perception with Deep Learning",
                                                                             "Visual Perception with Machine Learning",
                                                                             "https://www.youtube.com/watch?v=3boKlkPBckA");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void titleIsUpdatedWhenSubtitleIsPresent() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Nvidia AI plays Minecraft, wins machine-learning conference award</T><ST>NeurIPS 2022 honors MineDojo for playing Minecraft when instructed by written prompts.</ST><A>https://arstechnica.com/information-technology/2022/11/nvidia-wins-award-for-ai-that-can-play-minecraft-on-command/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Benj</FIRSTNAME><LASTNAME>Edwards</LASTNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>28</DAY></DATE><COMMENT>The title says it all.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Nvidia AI plays Warcraft, wins machine-learning conference award</T><ST>NeurIPS 2022 honors MineDojo for playing Minecraft when instructed by written prompts.</ST><A>https://arstechnica.com/information-technology/2022/11/nvidia-wins-award-for-ai-that-can-play-minecraft-on-command/</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Benj</FIRSTNAME><LASTNAME>Edwards</LASTNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>11</MONTH><DAY>28</DAY></DATE><COMMENT>The title says it all.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final ViolationCorrection correction = new UpdateLinkTitleCorrection("Nvidia AI plays Minecraft, wins machine-learning conference award",
                                                                             "Nvidia AI plays Warcraft, wins machine-learning conference award",
                                                                             "https://arstechnica.com/information-technology/2022/11/nvidia-wins-award-for-ai-that-can-play-minecraft-on-command/");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void titleWithAmpersandIsUpdated() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>Real mathematical magic: The king’s algorithm &amp; Sallow’s geomagic</T><A>https://www.youtube.com/watch?v=FANbncTMCGc</A><L>en</L><F>MP4</F><DURATION><MINUTE>33</MINUTE><SECOND>24</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Burkard</FIRSTNAME><LASTNAME>Polster</LASTNAME></AUTHOR><DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>4</DAY></DATE><COMMENT>A "proof" that <AUTHOR><FIRSTNAME>Claude-Gaspar</FIRSTNAME><LASTNAME>Bachet de Méziriac</LASTNAME></AUTHOR>’s method generates a magic square and applying magic squares to geometrical shapes.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>New magic in magic squares</T><A>https://www.youtube.com/watch?v=FANbncTMCGc</A><L>en</L><F>MP4</F><DURATION><MINUTE>33</MINUTE><SECOND>24</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Burkard</FIRSTNAME><LASTNAME>Polster</LASTNAME></AUTHOR><DATE><YEAR>2023</YEAR><MONTH>2</MONTH><DAY>4</DAY></DATE><COMMENT>A "proof" that <AUTHOR><FIRSTNAME>Claude-Gaspar</FIRSTNAME><LASTNAME>Bachet de Méziriac</LASTNAME></AUTHOR>’s method generates a magic square and applying magic squares to geometrical shapes.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final ViolationCorrection correction = new UpdateLinkTitleCorrection("Real mathematical magic: The king’s algorithm & Sallow’s geomagic",
                                                                             "New magic in magic squares",
                                                                             "https://www.youtube.com/watch?v=FANbncTMCGc");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void NewTitleWithDollarIsUpdated() {

        final String content =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>How to Use an LLM in a SaaS Platform</T><A>https://www.youtube.com/watch?v=fH8fJYWfJcg</A><L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>53</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Arjan</FIRSTNAME><LASTNAME>Egges</LASTNAME></AUTHOR><DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>6</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Arjan</FIRSTNAME><LASTNAME>Egges</LASTNAME></AUTHOR> describes his first steps to build learntail.com, using OpenAI and Langchain to generates quizzes.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final String expected =
            """
            <?xml version="1.0"?>\r
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\r
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd">\r
            <TITLE>test</TITLE>\r
            <PATH>dummy-dir/test.xml</PATH>\r
            <DATE><YEAR>2016</YEAR><MONTH>1</MONTH><DAY>30</DAY></DATE>\r
            <CONTENT>\r
            <BLIST><TITLE>Articles and videos</TITLE>\r
            <ITEM><ARTICLE><X><T>🤬 How the #@%$! do you use an LLM in a $aa$$ platform?</T><A>https://www.youtube.com/watch?v=fH8fJYWfJcg</A><L>en</L><F>MP4</F><DURATION><MINUTE>11</MINUTE><SECOND>53</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Arjan</FIRSTNAME><LASTNAME>Egges</LASTNAME></AUTHOR><DATE><YEAR>2023</YEAR><MONTH>10</MONTH><DAY>6</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Arjan</FIRSTNAME><LASTNAME>Egges</LASTNAME></AUTHOR> describes his first steps to build learntail.com, using OpenAI and Langchain to generates quizzes.</COMMENT></ARTICLE></ITEM>\r
            </BLIST>\r
            </CONTENT>\r
            </PAGE>""";

        final ViolationCorrection correction = new UpdateLinkTitleCorrection("How to Use an LLM in a SaaS Platform",
                                                                             "🤬 How the #@%$! do you use an LLM in a $aa$$ platform?",
                                                                             "https://www.youtube.com/watch?v=fH8fJYWfJcg");
        Assertions.assertEquals(expected, correction.apply(content));
    }

    @SuppressWarnings("static-method")
    @Test
    void description() {
        final ViolationCorrection correction = new UpdateLinkTitleCorrection("Real mathematical magic: The king’s algorithm & Sallow’s geomagic",
                                                                             "New magic in magic squares",
                                                                             "https://www.youtube.com/watch?v=FANbncTMCGc");
        Assertions.assertEquals("Update the link title", correction.getDescription());
    }
}
