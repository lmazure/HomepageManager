package fr.mazure.homepagemanager.data.linkchecker.radiofrance.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.radiofrance.RadioFranceLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of RadioFranceLinkContentParser
 */
class RadioFranceLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|La Nuit tragique d’un Golden Boy, le mirage Pascal Jeandet",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|Coupe de monde 1934 ou le triomphe du fascisme",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|Marita Lorenz, l’agente de la CIA envoyée à Cuba pour empoisonner son ex-amant Fidel Castro",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|Melita Norwood, vieille dame anglaise et ex-espionne de Staline",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|Naissance d’un prodige",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|Les mots qui tuent",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|L'étoile de papier",
    }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(RadioFranceLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|Aujourd’hui dans Affaires sensibles : la Nuit tragique d’un Golden Boy, le mirage Pascal Jeandet",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|Aujourd’hui dans Affaires sensibles, la Coupe du monde de football 1934 en Italie ou le triomphe du fascisme.",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|En 1959, cette jeune Allemande de 19 ans débarque à Cuba en pleine révolution et devient la maîtresse de Fidel Castro. Rentrée aux Etats Unis, elle est manipulée par la CIA",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|Qui aurait pu croire que cette vieille dame anglaise avait trahi son pays ? Melita Norwood est pour ainsi dire l’espionne parfaite, celle dont rêvent tous les services de renseignement. Efficace, courageuse, discrète, celle qui, pendant des décennies, a agi dans l’ombre sans être jamais découverte.",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|À 12 ans, détenu avec sa mère au camp d'internement de Rieucros en Lozère, Alexandre Grothendieck découvre la beauté du cercle et des polyèdres. Pour échapper au tumulte de la guerre, l'adolescent juif apatride se réfugie dans l’abstraction.",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|Comment vit-on quand un mot peut être fatal ? Qu'est-ce que la langue raconte de la vie sous Staline ?",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|Kafka sur la plage, jouant au milieu des enfants. Son carnet d’hébreu, son rêve de Palestine, sa dernière année à Berlin avec Dora Diamant et l’éternelle question : de quoi ai-je hérité et que vais-je à mon tour laisser ?",
    }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(RadioFranceLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|2026-05-13",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|2024-05-30",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|2021-07-10",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|2026-06-28",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|2024-08-05",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|2025-06-18",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|2024-07-05",
    }, delimiter = '|')
    void testPublicationDate(final String url,
                             final String expectedPublicationDate) {
        checkPublicationDate(RadioFranceLinkContentParser.class, url, expectedPublicationDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|PT48M20S",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|PT48M57S",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|PT41M26S",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|PT39M2S",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|PT59M1S",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|PT59M8S",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|PT57M40S",
    }, delimiter = '|')
    void testDuration(final String url,
                      final String expectedDuration) {
        checkDuration(RadioFranceLinkContentParser.class, url, expectedDuration);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|Fabrice||Drouelle|||Franck||Cognard|||Adrien||Carat||",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|Fabrice||Drouelle|||Franck||Cognard|||Adrien||Morat||",
    }, delimiter = '|')
    void test3Authors(final String url,
                      final String expectedFirstName1,
                      final String expectedMiddleName1,
                      final String expectedLastName1,
                      final String expectedNameSuffix1,
                      final String expectedGivenName1,
                      final String expectedFirstName2,
                      final String expectedMiddleName2,
                      final String expectedLastName2,
                      final String expectedNameSuffix2,
                      final String expectedGivenName2,
                      final String expectedFirstName3,
                      final String expectedMiddleName3,
                      final String expectedLastName3,
                      final String expectedNameSuffix3,
                      final String expectedGivenName3) {
        check3Authors(RadioFranceLinkContentParser.class,
                      url,
                      // author 1
                      null,
                      expectedFirstName1,
                      expectedMiddleName1,
                      expectedLastName1,
                      expectedNameSuffix1,
                      expectedGivenName1,
                      // author 2
                      null,
                      expectedFirstName2,
                      expectedMiddleName2,
                      expectedLastName2,
                      expectedNameSuffix2,
                      expectedGivenName2,
                      // author 3
                      null,
                      expectedFirstName3,
                      expectedMiddleName3,
                      expectedLastName3,
                      expectedNameSuffix3,
                      expectedGivenName3);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|Stéphanie||Duncan",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|Stéphanie||Duncan",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|Marie||Durrieu",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|Marie||Chartron",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|Christine||Lecerf",
    }, delimiter = '|')
    void test1Author(final String url,
                     final String expectedFirstName,
                     final String expectedMiddleName,
                     final String expectedLastName) {
        check1Author(RadioFranceLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     null,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-mercredi-13-mai-2026-3938758|fr",
        "https://www.radiofrance.fr/franceinter/podcasts/affaires-sensibles/affaires-sensibles-du-jeudi-30-mai-2024-3028690|fr",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/marita-lorenz-l-agente-de-la-cia-envoyee-a-cuba-pour-empoisonner-son-ex-amant-fidel-castro-6825291|fr",
        "https://www.radiofrance.fr/franceinter/podcasts/espions-une-histoire-vraie/vadim-krasikov-le-tueur-prefere-de-poutine-4287237|fr",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/naissance-d-un-prodige-6979092|fr",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/les-mots-qui-tuent-6008423|fr",
        "https://www.radiofrance.fr/franceculture/podcasts/les-grandes-traversees/l-etoile-de-papier-1181737|fr",
    }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(RadioFranceLinkContentParser.class, url, expectedLanguage);
    }
}
