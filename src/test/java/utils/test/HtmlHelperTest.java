package utils.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import utils.HtmlHelper;

class HtmlHelperTest {

    @ParameterizedTest
    @CsvSource({
        "Smale&#039;s&nbsp;paradox,Smale's paradox",
        "l&rsquo;addition,l’addition"
        })
    void stringIsProperlyDecoded(final String encodedString,
                                 final String expectedDecodedString) {
        Assertions.assertEquals(expectedDecodedString, HtmlHelper.unescape(encodedString));
    }

    @Test
    void spacesAreProperlyUnduplicated() {
        final String input = " a b  d  efg  h i     j k l    mn   ";
        final String expected = " a b d efg h i j k l mn ";
        Assertions.assertEquals(expected, HtmlHelper.unduplicateSpace(input));
    }

    @Test
    void NonBreakingSpacesAreReplaceBySpaces() {
        final String input = "Jean-Luc Mélenchon, président du groupe LFI a l'Assemblée Nationale, a remis en cause mercredi 14 octobre l'utilité d'instaurer d'un couvre-feu nocturne dans plusieurs grandes villes à partir de samedi car \"60 % des contaminations (de Covid-19) ont lieu au travail ou à l'école ou à l'université\". Environ la moitié des clusters en France du 9 mai au 5 octobre ont bien été détectés au travail ou dans les établissements scolaires, selon Santé Publique France, mais les cas issus des clusters ne sont qu'une partie minime du total des contaminations, expliquent des épidémiologistes.";
        final String expected = "Jean-Luc Mélenchon, président du groupe LFI a l'Assemblée Nationale, a remis en cause mercredi 14 octobre l'utilité d'instaurer d'un couvre-feu nocturne dans plusieurs grandes villes à partir de samedi car \"60 % des contaminations (de Covid-19) ont lieu au travail ou à l'école ou à l'université\". Environ la moitié des clusters en France du 9 mai au 5 octobre ont bien été détectés au travail ou dans les établissements scolaires, selon Santé Publique France, mais les cas issus des clusters ne sont qu'une partie minime du total des contaminations, expliquent des épidémiologistes.";
        Assertions.assertEquals(expected, HtmlHelper.unduplicateSpace(input));
    }

    @Test
    void htmlTagsAreProperlyRemoved() {
        final String input = "<h1>this title is <b>bold</b> and <i>italic</i></h1>";
        final String expected = "this title is bold and italic";
        Assertions.assertEquals(expected, HtmlHelper.removeHtmlTags(input));
    }

    @Test
    void htmlTagsAreProperlyRemovedInLongString() {
        final String input = "<p><strong>Alors que l'Europe se déconfine progressivement malgré quelques remous, l'Iran est touché de plein fouet par une deuxième vague de Covid-19 qui suscite de très vives inquiétudes</strong> <strong>: les hôpitaux se remplissent, et selon nos Observateurs, dans plusieurs régions, les lits manquent. Si les autorités minimisent dans les chiffres qu'elles communiquent l'ampleur de cette vague, nos Observateurs livrent des témoignages sans ambiguïté</strong> <strong>: ils voient des hôpitaux débordés et des villes qui se reconfinent et une situation déjà alarmante</strong> <strong>: des 31 provinces du pays, 14 sont passées en \"rouge\", signe d'une circulation maximum du virus.  </strong></p><p></p>";
        final String expected = "Alors que l'Europe se déconfine progressivement malgré quelques remous, l'Iran est touché de plein fouet par une deuxième vague de Covid-19 qui suscite de très vives inquiétudes : les hôpitaux se remplissent, et selon nos Observateurs, dans plusieurs régions, les lits manquent. Si les autorités minimisent dans les chiffres qu'elles communiquent l'ampleur de cette vague, nos Observateurs livrent des témoignages sans ambiguïté : ils voient des hôpitaux débordés et des villes qui se reconfinent et une situation déjà alarmante : des 31 provinces du pays, 14 sont passées en \"rouge\", signe d'une circulation maximum du virus.  ";
        Assertions.assertEquals(expected, HtmlHelper.removeHtmlTags(input));
    }
}
