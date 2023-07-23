package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.internet.test.TestHelper;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.RedirectionData.Match;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

class RedirectionDataTest {

    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/channel/UCUHW94eEFW7hkUMVaZz4eDg"
        })
    void youtubeChannel(final String url) {
        assertMatch(url,
                    true,
                    Integer.valueOf(200),
                    "from Google channel to cookies configuration",
                    Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    @ParameterizedTest
    @CsvSource({
        "https://blog.sparksuite.com/7-ways-to-speed-up-gitlab-ci-cd-times-29f60aab69f9"
        })
    void medium(final String url) {
        assertMatch(url,
                    true,
                    Integer.valueOf(200),
                    "Medium analytics",
                    Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    @ParameterizedTest
    @CsvSource({
        "http://blogs.msdn.com/b/jw_on_tech/archive/2012/03/14/why-i-joined-microsoft.aspx"
        })
    void redirectionsEndingWith403(final String url) {
        assertMatch(url,
                    true,
                    Integer.valueOf(403),
                    "redirection ending with an error code",
                    Set.of(LinkStatus.DEAD));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.4d.com",
        " https://www.ibm.com"
        })
    void redirectionsEndingInSuccess(final String url) {
        assertMatch(url,
                    true,
                    Integer.valueOf(200),
                    "redirection ending in success (last URL should be used)",
                    Set.of());
    }

    // URLs giving directly a 404
    @ParameterizedTest
    @CsvSource({
        "https://scienceetonnante.com/2017/11/12/glyphosate-le-nouvel-amiante/"
        })
    void direct404(final String url) {
        assertMatch(url,
                    false,
                    Integer.valueOf(404),
                    "direct failure",
                    Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 403
    @ParameterizedTest
    @CsvSource({
        "https://www.pnas.org/doi/pdf/10.1073/pnas.1810141115"
        })
    void direct403(final String url) {
        assertMatch(url,
                    false,
                    Integer.valueOf(403),
                    "direct failure",
                    Set.of(LinkStatus.DEAD));
    }

    private void assertMatch(final String url,
                             final boolean redirectionIsExpected,  // use to ensure that test data is up-to-date
                             final Integer expectedCode,           // use to ensure that test data is up-to-date
                             final String expectedMatcherName,
                             final Set<LinkStatus> expectedStatuses) {
        final RedirectionData data = new RedirectionData();
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                (final Boolean b, final FullFetchedLinkData d) -> {
                    consumerHasBeenCalled.set(true);
                    if (redirectionIsExpected) {
                        Assertions.assertNotNull(d.previousRedirection(), "the test data is out-of-date: no redirection was expected");
                    } else {
                        Assertions.assertNull(d.previousRedirection(), "the test data is out-of-date: a redirection was expected");
                    }
                    Assertions.assertEquals(expectedCode, getCodeOfLastRedirection(d), "the test data is out-of-date: the expected code is not the expected one");
                    final Match effectiveStatuses = data.getMatch(d);
                    Assertions.assertEquals(expectedMatcherName, effectiveStatuses.name());
                    Assertions.assertEquals(expectedStatuses, effectiveStatuses.statuses());
                },
                false);
        Assertions.assertTrue(consumerHasBeenCalled.get());
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
