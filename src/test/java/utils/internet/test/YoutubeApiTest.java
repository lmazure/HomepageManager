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
        Assertions.assertFalse(dto.getRecordingDate().isPresent());
        Assertions.assertEquals(LocalDate.of(2014, 7, 12), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(4 * 60 + 44), dto.getDuration());
        Assertions.assertFalse(dto.getTextLanguage().isPresent());
        Assertions.assertFalse(dto.getAudioLanguage().isPresent());
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
        Assertions.assertTrue(dto.getRecordingDate().isPresent());
        Assertions.assertEquals(LocalDate.of(2015, 3, 27), dto.getRecordingDate().get());
        Assertions.assertEquals(LocalDate.of(2015, 7, 25), dto.getPublicationDate());
        Assertions.assertEquals(Duration.ofSeconds(2 * 3600 + 17 * 60 + 15), dto.getDuration());
        Assertions.assertTrue(dto.getTextLanguage().isPresent());
        Assertions.assertEquals(Locale.FRENCH, dto.getTextLanguage().get());
        Assertions.assertTrue(dto.getAudioLanguage().isPresent());
        Assertions.assertEquals(Locale.FRENCH, dto.getAudioLanguage().get());
        Assertions.assertFalse(dto.isAllowed());
    }

    private YoutubeApi buildApi() {
        return new YoutubeApi("HomepageManager", "XXX", "FR");
    }
}
