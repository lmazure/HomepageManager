package utils.internet.test;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.internet.YoutubeApi;
import utils.internet.YoutubeVideoDto;

class YoutubeApiTest {

    @Test
    void noCreationDateNoLanguags() {
        final YoutubeApi api = buildApi();
        final YoutubeVideoDto dto = api.getData("aQo8tYQuWQw");
        Assertions.assertEquals("Les hexaflexagones  - Micmaths", dto.getTitle());
        Assertions.assertEquals("Découvrez les hexaflexagones et leurs propriétés incroyables.\n"
                + "\n"
                + "\n"
                + "Toutes mes vidéos sur les flexagones sont regroupées dans la playlist suivante : https://www.youtube.com/playlist?list=PLNefH6S6myiO_5HBDdtP_r_LlCcVwA04I", dto.getDescription());
        Assertions.assertNull(dto.getRecordingDate());
        Assertions.assertEquals(LocalDate.of(2014, 7, 12), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(4 * 60 + 44), dto.getDuration());
        Assertions.assertNull(dto.getTextLanguage());
        Assertions.assertNull(dto.getAudioLanguage());
        Assertions.assertTrue(dto.isAllowed());
    }

    @Test
    void languagesDatesDisallowedVideo() {
        final YoutubeApi api = buildApi();
        final YoutubeVideoDto dto = api.getData("dM_JivN3HvI");
        Assertions.assertEquals("L' économie de la connaissance par Idriss ABERKANE", dto.getTitle());
        Assertions.assertEquals("[Mars 2015] Conférence sur l'économie de la connaissance,  le biomimétisme et la Blue Economy\n"
                + "\n"
                + "Pour contribuer à la traduction des sous-titres :\n"
                + "http://www.youtube.com/timedtext_video?ref=share&v=dM_JivN3HvI\n"
                + "\n"
                + "► Idriss ABERKANE est professeur en géopolitique et économie de la connaissance à l'École centrale, chercheur affilié au Kozmetsky Global Collaboratory de l’université de Stanford et chercheur affilié au CNRS. Il est également éditorialiste pour Le Point.\n"
                + "► Lien vers sa formation : https://bebooda.fr/formations/liberez-cerveau-neuroergonomie/\n"
                + "Son livre \"Libérez-votre cerveau ! \" (sortie le 6 octobre 2016) : \n"
                + "- Amazon : https://www.amazon.fr/Lib%C3%A9rez-votre-cerveau-Idriss-Aberkane/dp/222118758X\n"
                + "- FNAC : http://livre.fnac.com/a9483923/Idriss-Aberkane-Liberez-votre-cerveau", dto.getDescription());
        Assertions.assertEquals(LocalDate.of(2015, 3, 26), dto.getRecordingDate());
        Assertions.assertEquals(LocalDate.of(2015, 7, 24), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(2 * 3600 + 17 * 60 + 15), dto.getDuration());
        Assertions.assertEquals(Locale.FRENCH, dto.getTextLanguage());
        Assertions.assertEquals(Locale.FRENCH, dto.getAudioLanguage());
        Assertions.assertFalse(dto.isAllowed());
    }

    @Test
    void timezoneIsProperlyHandled() {
        final YoutubeApi api = buildApi();
        final YoutubeVideoDto dto = api.getData("wl6Re86QZxs");
        Assertions.assertEquals("Harry Potter : l'énigme des potions - Micmaths", dto.getTitle());
        Assertions.assertEquals("Dans le premier tome des aventures d'Harry Potter, Harry et Hermione se trouvent confrontés à une énigme de logique.\n"
                + "\n"
                + "Vous pouvez découvrir de nombreux autres vidéastes scientifiques sur Vidéosciences : http://videosciences.cafe-sciences.org/\n"
                + "Et pour plus de vidéos culturelles allez faire un tour du côté de la Vidéothèque d'Alexandrie : https://videothequealexandrie.fr/\n"
                + "\n"
                + "Pour me suivre : \n"
                + "Twitter : https://twitter.com/mickaellaunay\n"
                + "Facebook : https://www.facebook.com/micmaths", dto.getDescription());
        Assertions.assertNull(dto.getRecordingDate());
        Assertions.assertEquals(LocalDate.of(2017, 6, 25), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(11 * 60 + 34), dto.getDuration());
        Assertions.assertEquals(Locale.FRENCH, dto.getTextLanguage());
        Assertions.assertEquals(Locale.FRENCH, dto.getAudioLanguage());
        Assertions.assertTrue(dto.isAllowed());
    }

    @Test
    void durationIsProperlyHandled() {
        final YoutubeApi api = buildApi();
        final YoutubeVideoDto dto = api.getData("CHDYMwSj9ok");
        Assertions.assertEquals("Le grand quiz du Jeudi 14 mai 2020 - Live", dto.getTitle());
        Assertions.assertEquals("Pour ceux qui veulent d'abord jouer, s'entraîner ou voir leur progrès : https://kahoot.it/challenge/05175633?challenge-id=70a9744f-9774-4e81-ba2f-573819774869_1589478737080  \n"
                + "\n"
                + "\n"
                + "\n"
                + "Un quiz mathématique qui brasse culture, connaissance, logique et énigmes ! Bref, du divertissement intelligent... Alors ? Seul, en duo ou en famille, prêts à relever le défi ?", dto.getDescription());
        Assertions.assertNull(dto.getRecordingDate());
        Assertions.assertEquals(LocalDate.of(2020, 5, 14), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(1 * 3600 + 19 * 60 + 23), dto.getDuration());
        Assertions.assertEquals(Locale.FRENCH, dto.getTextLanguage());
        Assertions.assertEquals(Locale.FRENCH, dto.getAudioLanguage());
        Assertions.assertTrue(dto.isAllowed());
    }

    private YoutubeApi buildApi() {
        return new YoutubeApi("HomepageManager", "XXX", "FR");
    }
}
