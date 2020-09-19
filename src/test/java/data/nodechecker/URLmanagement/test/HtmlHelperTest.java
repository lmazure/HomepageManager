package data.nodechecker.URLmanagement.test;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

import data.nodechecker.URLmanagement.HtmlHelper;

public class HtmlHelperTest extends HtmlHelper {

    @Test
    public void decode_singleQuote_correctlyConverted() throws UnsupportedEncodingException
    {
        Assertions.assertEquals("Let's", decode("Let&#39;s".getBytes("ISO-8859-1")));
    }

    @Test
    public void decode_allCharacters_correctlyConverted() throws UnsupportedEncodingException
    {

        String s="", e="";

        s += "&#039;";   e+= "'";
        s += "&#146;";   e+= "’";
        s += "&#39;";    e+= "'";
        s += "&#8216;";  e+= "‘";
        s += "&#8217;";  e+= "’";
        s += "&#8220;";  e+= "“";
        s += "&#8221;";  e+= "”";
        s += "&#8230;";  e+= "…";
        s += "&#x27;";   e+= "'";
        s += "&amp;";    e+= "&";
        s += "&hellip;"; e+= "…";
        s += "&mdash;";  e+= "—";
        s += "&nbsp;";   e+= " ";
        s += "&ndash;";  e+= "–";
        s += "&ouml;";   e+= "ö";
        s += "&quot;";   e+= "\"";

        Assertions.assertEquals(e, decode(s.getBytes("ISO-8859-1")));
    }


    @Test
    public void digest_verticalTab_replacedBySpace() {
        Assertions.assertEquals("foo bar", digest("foo\u000bbar"));
    }

    @Test
    public void digest_breakTag_replacedBySpace() {
        Assertions.assertEquals("foo bar", digest("foo<br/>bar"));
    }

    @Test
    public void digest_newline_replacedBySpace() {
        Assertions.assertEquals("foo bar", digest("foo\nbar"));
    }

    @Test
    public void digest_duplicatedSpace_replacedBySingleSpace() {
        Assertions.assertEquals("foo bar", digest("foo  bar"));
    }
}
