package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.dataretriever.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections.Match;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

class WellKnownRedirectionsTest {

    // URLs giving directly a 200 (success)
    @ParameterizedTest
    @CsvSource({
        "https://example.com",
        })
    void direct200(final String url) {
        test(url,
             false,
             Integer.valueOf(200),
             "direct success",
             Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE));
    }

    // URLs giving directly a 202 (success)
    @ParameterizedTest
    @CsvSource({
        "https://www.semanticscholar.org/paper/The-Native-POSIX-Thread-Library-for-Linux-Drepper-Molnar/ffced47e5604b66736d365030bfe532d11285433?p2df",
        })
    void direct202(final String url) {
        test(url,
             false,
             Integer.valueOf(202),
             "direct success",
             Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE));
    }

    // URLs giving directly a 401
    @ParameterizedTest
    @CsvSource({
        "https://www.uop.edu.jo/download/research/members/csharp_ebook.pdf",
        })
    void direct401(final String url) {
        test(url,
             false,
             Integer.valueOf(401),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 403
    @ParameterizedTest
    @CsvSource({
        "https://www.pnas.org/doi/pdf/10.1073/pnas.1810141115",
        })
    void direct403(final String url) {
        test(url,
             false,
             Integer.valueOf(403),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 404
    @ParameterizedTest
    @CsvSource({
        "https://scienceetonnante.com/2017/11/12/glyphosate-le-nouvel-amiante/",
        })
    void direct404(final String url) {
        test(url,
             false,
             Integer.valueOf(404),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 409
    @ParameterizedTest
    @CsvSource({
        "http://forums.construx.com/forums/t/432.aspx",
        })
    void direct409(final String url) {
        test(url,
             false,
             Integer.valueOf(409),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 410
    @ParameterizedTest
    @CsvSource({
        "https://dzone.com/articles/unit-test-insanity",
        "https://dzone.com/articles/the-developers-guide-to-collections-lists",
        })
    void direct410(final String url) {
        test(url,
             false,
             Integer.valueOf(410),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 503
    @ParameterizedTest
    @CsvSource({
        "http://www.nplus1executive.com/",
        })
    void direct503(final String url) {
        test(url,
             false,
             Integer.valueOf(503),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.4d.com",
        "https://www.ibm.com",
        })
    void redirectionsEndingInSuccess(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "redirection ending in success (last URL should be used)",
             Set.of());
    }

    @ParameterizedTest
    @CsvSource({
        "http://blogs.msdn.com/b/jw_on_tech/archive/2012/03/14/why-i-joined-microsoft.aspx",
        })
    void redirectionsEndingWith403(final String url) {
        test(url,
             true,
             Integer.valueOf(403),
             "redirection ending with an error code",
             Set.of(LinkStatus.DEAD));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.gamasutra.com/view/feature/3413/orphans_preferred.php",
        })
    void redirectionsEndingWith404(final String url) {
        test(url,
             true,
             Integer.valueOf(404),
             "redirection ending with an error code",
             Set.of(LinkStatus.DEAD));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/channel/UCUHW94eEFW7hkUMVaZz4eDg",
        "https://www.youtube.com/channel/UC1Ue7TuX3iH4y8-Qrjj-hyg",
        })
    void youtubeChannel(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from Youtube channel to cookies configuration",
             Set.of(LinkStatus.OK));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/user/dirtybiology",
        })
    void youtubeUser(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from Youtube user to cookies configuration",
             Set.of(LinkStatus.OK));
    }

    @ParameterizedTest
    @CsvSource({
        "https://blog.sparksuite.com/7-ways-to-speed-up-gitlab-ci-cd-times-29f60aab69f9",
        })
    void medium(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "Medium analytics",
             Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    @ParameterizedTest
    @CsvSource({
        "https://twitter.com/3blue1brown"
        })
    void twitter(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "Twitter",
             Set.of(LinkStatus.OK));
    }

    @ParameterizedTest
    @CsvSource({
        "http://www.onjava.com/catalog/javaadn/excerpt/javaadn_ch05.pdf",
        "http://www.onjava.com/pub/a/onjava/2003/06/25/commons.html",
        "http://www.onlamp.com/pub/a/onlamp/2005/02/24/pg_buildfarm.html",
        "http://www.onlamp.com/pub/a/php/2005/12/20/php_ant.html",
        "http://www.onlamp.com/pub/a/python/2001/07/19/pythonnews.html",
        "http://www.onlamp.com/pub/a/security/2004/09/16/open_source_security_myths.html",
        "http://www.oreillynet.com/onlamp/blog/2005/12/two_things_that_bother_me_abou.html",
        "http://www.oreillynet.com/pub/a/network/2003/05/20/secureprogckbk.html",
        "http://www.oreillynet.com/pub/a/oreilly/security/news/2004/03/08/netsec.html",
        "http://www.oreillynet.com/xml/blog/2006/06/understanding_xforms_component.html",
        })
    void oReilly(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from O’Reilly",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.ibm.com/developerworks/java/library/j-mer1022.html",
        "https://www.ibm.com/developerworks/java/library/os-lombok/index.html",
        "https://www.ibm.com/developerworks/java/library/x-simplexobjs/",
        "https://www.ibm.com/developerworks/library/a-devops1/",
        "https://www.ibm.com/developerworks/library/j-jtp07265/",
        "https://www.ibm.com/developerworks/library/l-psyco/index.html",
        "https://www.ibm.com/developerworks/library/l-sp2/index.html",
        "https://www.ibm.com/developerworks/library/os-imagemagick/",
        "https://www.ibm.com/developerworks/library/x-javaxmlvalidapi/index.html",
        "https://www.ibm.com/developerworks/linux/library/l-perl-2-python",
        "https://www.ibm.com/developerworks/opensource/library/os-ecgui1",
        "https://www.ibm.com/developerworks/power/library/pa-spec14/index.html",
        "https://www.ibm.com/developerworks/rational/library/edge/08/mar08/curran/index.html",
        "https://www.ibm.com/developerworks/tivoli/library/s-csscript/",
        "https://www.ibm.com/developerworks/web/library/wa-mashupsecure/",
        "https://www.ibm.com/developerworks/webservices/library/ws-array/",
        "https://www.ibm.com/developerworks/xml/library/x-matters32/index.html",
        })
    void ibm(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from IBM",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://channel9.msdn.com/Shows/Going+Deep/Erik-Meijer-and-Bart-De-Smet-LINQ-to-Anything",
        "https://channel9.msdn.com/Blogs/David+Gristwood/An-F-Tutorial-with-Don-Syme-2-of-4 ",
        "https://channel9.msdn.com/posts/Mads-Torgersen-Visual-Studio-Async-CTP-SP1-Refresh-Overview",
        })
    void channel9(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from Channel 9",
             Set.of(LinkStatus.REMOVED));
    }

    private void test(final String url,
                      final boolean redirectionIsExpected,  // use to ensure that test data is up-to-date
                      final Integer expectedCode,           // use to ensure that test data is up-to-date
                      final String expectedMatcherName,
                      final Set<LinkStatus> expectedStatuses) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                (final Boolean b, final FullFetchedLinkData d) -> {
                    consumerHasBeenCalled.set(true);
                    if (redirectionIsExpected) {
                        Assertions.assertNotNull(d.previousRedirection(), "the test data is out-of-date: a redirection was expected");
                    } else {
                        Assertions.assertNull(d.previousRedirection(), "the test data is out-of-date: no redirection was expected");
                    }
                    Assertions.assertEquals(expectedCode, getCodeOfLastRedirection(d), "the test data is out-of-date: the expected code is not the expected one");
                    assertMatch(expectedMatcherName, expectedStatuses, d);
                },
                false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
    }

    private static void assertMatch(final String expectedMatcherName,
                                    final Set<LinkStatus> expectedStatuses,
                                    final FullFetchedLinkData effectiveData) {
        final WellKnownRedirections data = new WellKnownRedirections();
        final Match effectiveStatuses = data.getMatch(effectiveData);
        Assertions.assertEquals(expectedMatcherName, effectiveStatuses.name());
        Assertions.assertEquals(expectedStatuses, effectiveStatuses.statuses());
    }

    private static Integer getCodeOfLastRedirection(final FullFetchedLinkData data) {
        final HeaderFetchedLinkData last = lastRedirection(data);
        if (last == null) {
            if (data.headers().isPresent()) {
                return Integer.valueOf(HttpHelper.getResponseCodeFromHeaders(data.headers().get()));
            }
            return null;
        }
        if (last.headers().isEmpty()) {
            return null;
        }
        return Integer.valueOf(HttpHelper.getResponseCodeFromHeaders(last.headers().get()));
    }

    private static HeaderFetchedLinkData lastRedirection(final FullFetchedLinkData data) {
        HeaderFetchedLinkData d = data.previousRedirection();
        if (d == null) {
            return null;
        }
        while (d.previousRedirection() != null) {
            d = d.previousRedirection();
        }
        return d;
    }

    private SynchronousSiteDataRetriever buildDataSiteRetriever() {
        return new SynchronousSiteDataRetriever(TestHelper.buildSiteDataPersister(getClass()));
    }
}
