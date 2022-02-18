package utils.test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import data.internet.SiteData;
import data.internet.SynchronousSiteDataRetriever;
import data.internet.test.TestHelper;
import utils.HtmlHelper;

public class HtmlHelperTest {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource({
        "Smale&#039;s&nbsp;paradox,Smale's\u00A0paradox",
        "l&rsquo;addition,l’addition",
        "126 &#8211; James Gosling,126 – James Gosling",
        "some name escape sequence can have no ';' for example &reg &AMP,some name escape sequence can have no ';' for example ® &",
        "end of string &#8211;,end of string –",
        "end of string &#xE0;,end of string à",
        "end of string and multiple zeros &#x000E0;,end of string and multiple zeros à",
        })
    void stringIsProperlyDecoded(final String encodedString,
                                 final String expectedDecodedString) {
        Assertions.assertEquals(expectedDecodedString, HtmlHelper.unescape(encodedString));
    }

    @SuppressWarnings("static-method")
    @Test
    void spacesAreProperlyUnduplicated() {
        final String input = " a b  d  efg  h i     j k l    mn   ";
        final String expected = " a b d efg h i j k l mn ";
        Assertions.assertEquals(expected, HtmlHelper.cleanAndUnduplicateSpace(input));
    }

    @SuppressWarnings("static-method")
    @Test
    void emojiAreLeftUnchanged() {
        final String input = "PARCOURSUP 👩🏽‍🎓🏫 et les algorithmes de mariage stable ❤️";
        final String expected = "PARCOURSUP 👩🏽‍🎓🏫 et les algorithmes de mariage stable ❤️";
        Assertions.assertEquals(expected, HtmlHelper.cleanAndUnduplicateSpace(input));
    }

    @SuppressWarnings("static-method")
    @Test
    void htmlTagsAreProperlyRemoved() {
        final String input = "<h1>this title is <b>bold</b> and <i>italic</i></h1>";
        final String expected = "this title is bold and italic";
        Assertions.assertEquals(expected, HtmlHelper.removeHtmlTags(input));
    }

    @SuppressWarnings("static-method")
    @Test
    void htmlTagsAreProperlyRemovedInLongString() {
        final String input = "<p><strong>Alors que l'Europe se déconfine progressivement malgré quelques remous, l'Iran est touché de plein fouet par une deuxième vague de Covid-19 qui suscite de très vives inquiétudes</strong> <strong>: les hôpitaux se remplissent, et selon nos Observateurs, dans plusieurs régions, les lits manquent. Si les autorités minimisent dans les chiffres qu'elles communiquent l'ampleur de cette vague, nos Observateurs livrent des témoignages sans ambiguïté</strong> <strong>: ils voient des hôpitaux débordés et des villes qui se reconfinent et une situation déjà alarmante</strong> <strong>: des 31 provinces du pays, 14 sont passées en \"rouge\", signe d'une circulation maximum du virus.  </strong></p><p></p>";
        final String expected = "Alors que l'Europe se déconfine progressivement malgré quelques remous, l'Iran est touché de plein fouet par une deuxième vague de Covid-19 qui suscite de très vives inquiétudes : les hôpitaux se remplissent, et selon nos Observateurs, dans plusieurs régions, les lits manquent. Si les autorités minimisent dans les chiffres qu'elles communiquent l'ampleur de cette vague, nos Observateurs livrent des témoignages sans ambiguïté : ils voient des hôpitaux débordés et des villes qui se reconfinent et une situation déjà alarmante : des 31 provinces du pays, 14 sont passées en \"rouge\", signe d'une circulation maximum du virus.  ";
        Assertions.assertEquals(expected, HtmlHelper.removeHtmlTags(input));
    }

    
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.lemondeinformatique.fr/actualites/lire-log4j-une-autre-vulnerabilite-corrigee-par-apache-85265.html|iso-8859-1",
        "https://www-archive.mozilla.org/docs/web-developer/xbdhtml/xbdhtml.html|iso-8859-1",
        "http://www.business-esolutions.com/islm.htm|iso-8859-1",
        "https://blog.chromium.org/2009/01/tabbed-browsing-in-google-chrome.html|UTF-8",
        "https://martinfowler.com/bliki/UseCasesAndStories.html|UTF-8",
        "http://hesketh.com/publications/inclusive_web_design_for_the_future/|",
        "https://blog.cleancoder.com/uncle-bob/2014/06/17/IsTddDeadFinalThoughts.html|UTF-8",
        "https://www.rigacci.org/docs/biblio/online/CA-2000-02/CA-2000-02.html|"
    }, delimiter = '|')
    void testCharsetExtraction(final String url,
                               final String expectedCharsetName) {
        final SynchronousSiteDataRetriever retriever = TestHelper.buildDataSiteRetriever(getClass());
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(TestHelper.buildURL(url),
                           (final Boolean b, final SiteData d) -> {
                               Assertions.assertTrue(d.getDataFile().isPresent());
                               final File file = d.getDataFile().get();
                               final Optional<Charset> effectiveCharset = HtmlHelper.getCharset(file);
                               if (expectedCharsetName == null) {
                                   Assertions.assertTrue(effectiveCharset.isEmpty());
                               } else {
                                   final Charset expectedCharset = Charset.forName(expectedCharsetName);
                                   Assertions.assertTrue(effectiveCharset.isPresent());
                                   Assertions.assertEquals(expectedCharset, effectiveCharset.get());
                               }
                               consumerHasBeenCalled.set(true);
                           });
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }
}
