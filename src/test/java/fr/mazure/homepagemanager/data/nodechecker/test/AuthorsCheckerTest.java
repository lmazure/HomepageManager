package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class AuthorsCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void correctWellKnownAuthorsAreIgnored() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>The Happy Twin - with Ben Sparks</T><A>https://www.numberphile.com/podcast/ben-sparks</A><L>en</L><F>MP3</F><DURATION><HOUR>1</HOUR><MINUTE>2</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Brady</FIRSTNAME><LASTNAME>Haran</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>5</MONTH><DAY>27</DAY></DATE><COMMENT><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR> describes his life.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>#118 – Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown</T><A>https://lexfridman.com/grant-sanderson-2</A><L>en</L><F>MP3</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>52</SECOND></DURATION></X><X><T>Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown | Lex Fridman Podcast #118</T><A>https://www.youtube.com/watch?v=U_6AYX42gkU</A><L>en</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>25</SECOND></DURATION><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>24</DAY></DATE></X><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Lex</FIRSTNAME><LASTNAME>Fridman</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>23</DAY></DATE><COMMENT>A long interview.</COMMENT></ARTICLE></ITEM>
            </CONTENT>\
            </PAGE>""";
        try {
            test(content);
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void wellKnownAuthorIsMissing() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>The Happy Twin - with Ben Sparks</T><A>https://www.numberphile.com/podcast/ben-sparks</A><L>en</L><F>MP3</F><DURATION><HOUR>1</HOUR><MINUTE>2</MINUTE><SECOND>21</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Ben</FIRSTNAME><LASTNAME>Sparks</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>5</MONTH><DAY>27</DAY></DATE><COMMENT>Ben Sparks describes his life.</COMMENT></ARTICLE></ITEM>\
            <ITEM><ARTICLE><X><T>#118 – Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown</T><A>https://lexfridman.com/grant-sanderson-2</A><L>en</L><F>MP3</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>52</SECOND></DURATION></X><X><T>Grant Sanderson: Math, Manim, Neural Networks &amp; Teaching with 3Blue1Brown | Lex Fridman Podcast #118</T><A>https://www.youtube.com/watch?v=U_6AYX42gkU</A><L>en</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>8</MINUTE><SECOND>25</SECOND></DURATION><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>24</DAY></DATE></X><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2020</YEAR><MONTH>8</MONTH><DAY>23</DAY></DATE><COMMENT>A long interview.</COMMENT></ARTICLE></ITEM>
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The list of authors of article \"https://lexfridman.com/grant-sanderson-2\" (▭ first=Grant ▭ last=Sanderson ▭ ▭) does not contain the expected list for the site (▭ first=Lex ▭ last=Fridman ▭ ▭)<<IncorrectAuthorList>>",
                 "The list of authors of article \"https://www.numberphile.com/podcast/ben-sparks\" (▭ first=Ben ▭ last=Sparks ▭ ▭) does not contain the expected list for the site (▭ first=Brady ▭ last=Haran ▭ ▭)<<IncorrectAuthorList>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void wellKnownAuthorIsNotAlone() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2020</YEAR><MONTH>12</MONTH><DAY>31</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>embracing change - testing to agile</T><A>https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Leah</FIRSTNAME><LASTNAME>Stockley</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Grant</FIRSTNAME><LASTNAME>Sanderson</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>3</MONTH><DAY>21</DAY></DATE><COMMENT>Context Driven Testing and Agile are a good match, but this blog is too polished.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The list of authors of article \"https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile\" (▭ first=Leah ▭ last=Stockley ▭ ▭;▭ first=Grant ▭ last=Sanderson ▭ ▭) is not equal to the expected list for the site (▭ first=Leah ▭ last=Stockley ▭ ▭)<<IncorrectAuthorList>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void duplicatedAuthor() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #35 - Maud CHALMEL &amp; Thibaut de la Touane</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Guillaume</FIRSTNAME><LASTNAME>Chifoumi</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>François</FIRSTNAME><LASTNAME>Décamp</LASTNAME></AUTHOR><AUTHOR><GIVENNAME>Tarsa</GIVENNAME></AUTHOR><AUTHOR><GIVENNAME>Muss Ino</GIVENNAME></AUTHOR><AUTHOR><FIRSTNAME>Pénélope</FIRSTNAME></AUTHOR><AUTHOR><GIVENNAME>Tarsa</GIVENNAME></AUTHOR><AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR><AUTHOR><FIRSTNAME>Thibaut</FIRSTNAME><LASTNAME>de la Touane</LASTNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Heat, Clockworker, Super Mega Lucky Box, Turing Machine, the games of Triton Noir, a visit of Volumique workshop, and a interview of <AUTHOR><FIRSTNAME>Maud</FIRSTNAME><LASTNAME>Chalmel</LASTNAME></AUTHOR>.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The list of authors of article \"https://www.youtube.com/watch?v=aW61yxnQvio\" contains duplicated author: ▭ ▭ ▭ ▭ ▭ given=Tarsa<<DuplicatedAuthor>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorPrefixName() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Pre,fix</NAMEPREFIX><FIRSTNAME>FirstName</FIRSTNAME><MIDDLENAME>MiddleName</MIDDLENAME><LASTNAME>LastName</LASTNAME><NAMESUFFIX>NameSuffix</NAMESUFFIX><GIVENNAME>GivenName</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The name prefix of author prefix=Pre,fix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorFirstName() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Prefix</NAMEPREFIX><FIRSTNAME>First;Name</FIRSTNAME><MIDDLENAME>MiddleName</MIDDLENAME><LASTNAME>LastName</LASTNAME><NAMESUFFIX>NameSuffix</NAMESUFFIX><GIVENNAME>GivenName</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The first name of author prefix=Prefix first=First;Name middle=MiddleName last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorMiddleName() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Prefix</NAMEPREFIX><FIRSTNAME>FirstName</FIRSTNAME><MIDDLENAME>Middle\"Name</MIDDLENAME><LASTNAME>LastName</LASTNAME><NAMESUFFIX>NameSuffix</NAMESUFFIX><GIVENNAME>GivenName</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The middle name of author prefix=Prefix first=FirstName middle=Middle\"Name last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorLastName() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Prefix</NAMEPREFIX><FIRSTNAME>FirstName</FIRSTNAME><MIDDLENAME>MiddleName</MIDDLENAME><LASTNAME>Last!Name</LASTNAME><NAMESUFFIX>NameSuffix</NAMESUFFIX><GIVENNAME>GivenName</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The last name of author prefix=Prefix first=FirstName middle=MiddleName last=Last!Name suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorNameSuffix() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Prefix</NAMEPREFIX><FIRSTNAME>FirstName</FIRSTNAME><MIDDLENAME>MiddleName</MIDDLENAME><LASTNAME>LastName</LASTNAME><NAMESUFFIX>Name&amp;Suffix</NAMESUFFIX><GIVENNAME>GivenName</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The name suffix of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=Name&Suffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

    @SuppressWarnings("static-method")
    @Test
    void illegalCharactersInAuthorGivenName() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>25</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>Tric Trac Show #12345</T><A>https://www.youtube.com/watch?v=aW61yxnQvio</A><L>fr</L><F>MP4</F><DURATION><HOUR>2</HOUR><MINUTE>0</MINUTE><SECOND>6</SECOND></DURATION></X><AUTHOR><NAMEPREFIX>Prefix</NAMEPREFIX><FIRSTNAME>FirstName</FIRSTNAME><MIDDLENAME>MiddleName</MIDDLENAME><LASTNAME>LastName</LASTNAME><NAMESUFFIX>NameSuffix</NAMESUFFIX><GIVENNAME>Given=Name</GIVENNAME></AUTHOR><DATE><YEAR>2022</YEAR><MONTH>10</MONTH><DAY>13</DAY></DATE><COMMENT>Some games…</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "The given name of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=Given=Name contains an illegal character<<AuthorWithIllegalCharacters>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }
}
