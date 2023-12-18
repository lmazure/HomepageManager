package fr.mazure.homepagemanager.data.nodechecker.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

class ArticleCommentCheckerTest extends NodeValueCheckerTestBase {

    @SuppressWarnings("static-method")
    @Test
    void commentSpeakingAboutNonExistentSubtitle() {

        final String content =
            """
            <?xml version="1.0"?>\
            <?xml-stylesheet type="text/xsl" href="../css/strict.xsl"?>\
            <PAGE xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="../css/schema.xsd" xml:lang="en">\
            <TITLE>TypeScript</TITLE>\
            <PATH>links/typescript.xml</PATH>\
            <DATE><YEAR>2018</YEAR><MONTH>9</MONTH><DAY>22</DAY></DATE>\
            <CONTENT>\
            <ITEM><ARTICLE><X><T>title</T><A>https://example.com/page</A><L>en</L><F>HTML</F><DATE><YEAR>2010</YEAR><MONTH>9</MONTH><DAY>23</DAY></DATE></X><COMMENT>The subtitle says it all.</COMMENT></ARTICLE></ITEM>\
            </CONTENT>\
            </PAGE>""";
        try {
            test(content,
                 "Comment refers a non-existant subtitle<<CommentReferringMissingSubtitle>>");
        } catch (@SuppressWarnings("unused") final SAXException e) {
            Assertions.fail("SAXException");
        }
    }

}
