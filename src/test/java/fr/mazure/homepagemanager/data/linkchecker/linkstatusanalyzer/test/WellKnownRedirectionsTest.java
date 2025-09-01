package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
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
        "http://httpbin.org/status/200",
        "https://example.com",
        })
    void direct200(final String url) {
        test(url,
             false,
             Integer.valueOf(200),
             "direct success",
             Set.of(LinkStatus.OK, LinkStatus.ZOMBIE, LinkStatus.OBSOLETE));
    }

    @Disabled("semanticscholar does not reply anymore with 202")
    @ParameterizedTest
    @CsvSource({
        "https://www.semanticscholar.org/paper/Lisp-%253A-Good-News-Bad-News-How-to-Win-Big-Gabriel/1021849fe18475707bd5fe99c6fac4f77279098a",
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
        "http://httpbin.org/status/401",
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
        "http://httpbin.org/status/403",
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
        "http://httpbin.org/status/404",
        "https://scienceetonnante.com/blog/2017/11/12/glyphosate-le-nouvel-amiante/",
        })
    void direct404(final String url) {
        test(url,
             false,
             Integer.valueOf(404),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 406
    @Disabled("lemonde.fr does not reply anymore with 106")
    @ParameterizedTest
    @CsvSource({
        "hhttps://www.lemonde.fr/blog/realitesbiomedicales/2022/11/28/covid-19-comment-omicron-a-t-il-evolue-depuis-son-emergence-il-y-a-un-an/",
        })
    void direct406(final String url) {
        test(url,
             false,
             Integer.valueOf(406),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 409
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/409",
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
        "http://httpbin.org/status/410",
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

    // URLs giving directly a 426
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/426",
        "https://www.samba.org/jitterbug/", // TODO why does this site returns 426?
        })
    void direct426(final String url) {
        test(url,
             false,
             Integer.valueOf(426),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 502
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/502",
        })
    void direct502(final String url) {
        test(url,
             false,
             Integer.valueOf(502),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 503
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/503",
        "http://www.nplus1executive.com/",
        })
    void direct503(final String url) {
        test(url,
             false,
             Integer.valueOf(503),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 521
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/521",
        })
    void direct521(final String url) {
        test(url,
             false,
             Integer.valueOf(521),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 522
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/522",
        //"http://ganttproject.biz", this site sometimes return 302
        })
    void direct522(final String url) {
        test(url,
             false,
             Integer.valueOf(522),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    // URLs giving directly a 525
    @ParameterizedTest
    @CsvSource({
        "http://httpbin.org/status/525",
        })
    void direct525(final String url) {
        test(url,
             false,
             Integer.valueOf(525),
             "direct failure",
             Set.of(LinkStatus.DEAD));
    }

    @Disabled("need find another example of such a bad redirection")
    @ParameterizedTest
    @CsvSource({
        "https://docs.trychroma.com/", // returns 307 but have no "Location" in the answer header
        })
    void redirectionsNotBeingRedirected(final String url) {
        test(url,
             false,
             Integer.valueOf(307),
             "redirection not being redirected",
             Set.of());
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.4d.com",
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
        "https://link.springer.com/",
        })
    void redirectionsToItself(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "redirection to itself",
             Set.of(LinkStatus.OK));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.dell.com",      // https://www.dell.com/fr-fr
        "https://www.ibm.com",       // https://www.ibm.com/fr-fr
        "https://www.loria.fr",      // https://www.loria.fr/en/
        "https://www.microsoft.com", // https://www.microsoft.com/fr-fr/
        "https://www.msn.com",       // https://www.msn.com/fr-fr
        "https://www.nintendo.com",  // https://www.nintendo.com/us/
        "https://www.real.com",      // https://www.real.com/fr
        })
    void redirectionsToLocale(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "redirection to locale",
             Set.of(LinkStatus.OK));
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
        "http://weblogs.java.net/blog/arnold/archive/2005/06/generics_consid_1.html",
        "http://www.hays.lu/prd_consump/groups/hays_common/@fr/@content/documents/digitalasset/hays_714488.pdf",
        "http://www.ineris.fr/centredoc/Communication_site_web_INERIS.pdf",
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
        "https://www.youtube.com/c/QuantaScienceChannel",
        "https://www.youtube.com/c/Tumourrasmoinsb%C3%AAteARTE",
        })
    void youtubeChannel(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from YouTube channel to YouTube channel",
             Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/@java",
        })
    void youtubeAtChannel(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from YouTube @ channel to YouTube @ channel",
             Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }


    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/playlist?list=PLi6K9w_UbfFS393cQii0mC3nEy2NS7kv8",
        })
    void youtubePlaylist(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from YouTube Playlist to YouTube Playlist",
             Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.youtube.com/user/dirtybiology",
        "https://www.youtube.com/user/TheWandida",
        })
    void youtubeUser(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "from YouTube user to YouTube user",
             Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
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
        "http://linux.oreillynet.com/pub/a/linux/2002/05/09/uid.html",
        "http://www.linuxdevcenter.com/pub/a/linux/2004/03/04/warp_pipe.html",
        "http://www.onjava.com/catalog/javaadn/excerpt/javaadn_ch05.pdf",
        "http://www.onjava.com/pub/a/onjava/2003/06/25/commons.html",
        "http://www.onlamp.com/lpt/a/apache/2001/08/16/code_red.htm",
        "http://www.onlamp.com/pub/a/onlamp/2005/02/24/pg_buildfarm.html",
        "http://www.onlamp.com/pub/a/php/2005/12/20/php_ant.html",
        "http://www.onlamp.com/pub/a/python/2001/07/19/pythonnews.html",
        "http://www.onlamp.com/pub/a/security/2004/09/16/open_source_security_myths.html",
        "http://www.oreillynet.com/mac/blog/2005/12/jmf_a_mistake_asking_to_be_rem.html",
        "http://www.oreillynet.com/onlamp/blog/2005/12/two_things_that_bother_me_abou.html",
        "http://www.oreillynet.com/pub/a/linux/rt/07282000/transcript.html",
        "http://www.oreillynet.com/pub/a/network/2003/05/20/secureprogckbk.html",
        "http://www.oreillynet.com/pub/a/oreilly/security/news/2004/03/08/netsec.html",
        "http://www.oreillynet.com/pub/a/wireless/2003/12/04/tftp.html",
        "http://www.oreillynet.com/xml/blog/2006/06/understanding_xforms_component.html",
        "http://radar.oreilly.com/2013/07/putting-developers-to-the-test.html",
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
        "https://www.ibm.com/developerworks/aix/library/au-boost_parser/",
        "https://www.ibm.com/developerworks/architecture/library/ar-cloudaws1/",
        "https://www.ibm.com/developerworks/cloud/library/cl-json-verification/",
        "https://www.ibm.com/developerworks/java/library/co-tmline/index.html",
        "https://www.ibm.com/developerworks/java/library/j-mer1022.html",
        "https://www.ibm.com/developerworks/java/library/os-lombok/index.html",
        "https://www.ibm.com/developerworks/java/library/se-banner/index.html",
        "https://www.ibm.com/developerworks/java/library/x-simplexobjs/",
        "https://www.ibm.com/developerworks/library/a-devops1/",
        "https://www.ibm.com/developerworks/library/j-jtp07265/",
        "https://www.ibm.com/developerworks/library/l-psyco/index.html",
        "https://www.ibm.com/developerworks/library/l-sp2/index.html",
        "https://www.ibm.com/developerworks/library/mo-jquery-responsive-design/",
        "https://www.ibm.com/developerworks/library/os-imagemagick/",
        "https://www.ibm.com/developerworks/library/x-javaxmlvalidapi/index.html",
        "https://www.ibm.com/developerworks/linux/library/l-perl-2-python",
        "https://www.ibm.com/developerworks/opensource/library/os-ecgui1",
        "https://www.ibm.com/developerworks/power/library/pa-spec14/index.html",
        "https://www.ibm.com/developerworks/rational/library/edge/08/mar08/curran/index.html",
        "https://www.ibm.com/developerworks/systems/library/es-debug/index.html",
        "https://www.ibm.com/developerworks/tivoli/library/s-csscript/",
        "https://www.ibm.com/developerworks/web/library/wa-mashupsecure/",
        "https://www.ibm.com/developerworks/webservices/library/ws-array/",
        "https://www.ibm.com/developerworks/websphere/library/techarticles/0908_funk/0908_funk.html",
        "https://www.ibm.com/developerworks/websphere/techjournal/1203_noiret/1203_noiret.html",
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
        "https://developer.ibm.com/articles/j-5things18/",
        "https://developer.ibm.com/articles/j-javaee8-json-binding-1/",
        "https://developer.ibm.com/articles/wa-sailsjs3/",
        "https://developer.ibm.com/tutorials/wa-build-deploy-web-app-sailsjs-2-bluemix",
        "https://developer.ibm.com/tutorials/wa-implement-a-single-page-application-with-angular2/",
        "https://developer.ibm.com/tutorials/wa-manage-state-with-redux-p1-david-geary/",
        })
    void developerIbm(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from developer.ibm.com",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "http://www.rational.com/technotes/devtools_html/Purify_html/technote_14961.html",
        })
    void rational(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from Rational",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://channel9.msdn.com/Blogs/David+Gristwood/An-F-Tutorial-with-Don-Syme-2-of-4 ",
        "https://channel9.msdn.com/Events/Lang-NEXT/Lang-NEXT-2014/CSharp",
        "https://channel9.msdn.com/Events/Patterns-Practices-Symposium-Online/pattern-practices-symposium-2013/Introducing-Git-Version-Control-into-Your-Team",
        "https://channel9.msdn.com/posts/Mads-Torgersen-Visual-Studio-Async-CTP-SP1-Refresh-Overview",
        "https://channel9.msdn.com/Series/mvcConf/mvcConf-2-Brandom-Satrom-BDD-in-ASPNET-MVC-using-SpecFlow-WatiN-and-WatiN-Test-Helpers",
        "https://channel9.msdn.com/Shows/Going+Deep/Erik-Meijer-and-Bart-De-Smet-LINQ-to-Anything",
        })
    void channel9(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from Channel 9",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://msdn.microsoft.com/en-us/library/aa964145.aspx",
        "https://msdn.microsoft.com/en-us/vstudio/bb892758",
        "https://msdn.microsoft.com/en-us/vstudio/dd442479",
        })
    void msdn(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from MSDN",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "http://java.sun.com/developer/JDCTechTips/2003/tt0422.html#2",
        "http://java.sun.com/developer/technicalArticles/releases/data/",
        })
    void sun(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from java.sun.com",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.techrepublic.com/article/10-stupid-user-stories-the-madness-persists/",
        "https://www.techrepublic.com/article/10-things-you-should-know-about-nosql-databases/",
        "https://www.techrepublic.com/article/10-traits-to-look-for-when-youre-hiring-a-programmer/",
        "https://www.techrepublic.com/article/anatomy-of-word-using-excel-information-in-word-documents/",
        "https://www.techrepublic.com/article/a-quick-word-trick-for-typing-text-into-a-scanned-document/",
        "https://www.techrepublic.com/blog/cio-insights/windows-8-cheat-sheet/236/",
        "https://www.techrepublic.com/blog/tr-dojo/five-windows-command-prompt-tips-every-it-pro-should-know/",
        "https://www.techrepublic.com/article/10-common-network-security-design-flaws/",
        })
    void techRepublitcRedirect(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from TechRepublic",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.techrepublic.com/article/34-timesaving-mouse-tricks-for-word-users/",
        })
    void techRepublicDirect404(final String url) {
        test(url,
             false,
             Integer.valueOf(404),
             "removed from TechRepublic",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://www.techrepublic.com/blog/windows-and-office/determine-if-your-hardware-can-support-windows-xp-mode-in-windows-7/",
        "https://www.techrepublic.com/blog/windows-and-office/quick-tip-ensure-services-restart-upon-failure-in-windows-7/",
        })
    void techRepublicRedirect404(final String url) {
        test(url,
             true,
             Integer.valueOf(404),
             "removed from TechRepublic",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://yuiblog.com/blog/2013/05/30/pure/",
        })
    void yuiblog(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "removed from YUI blog",
             Set.of(LinkStatus.REMOVED));
    }

    @ParameterizedTest
    @CsvSource({
        "https://searchenginewatch.com/article/2064541/Numbers-Numbers-But-What-Do-They-Mean",
        })
    void redirectionTowardFake404Page(final String url) {
        test(url,
             true,
             Integer.valueOf(200),
             "redirection ending in success (last URL should be used)",
             Set.of());
    }

    private void test(final String url,
                      final boolean redirectionIsExpected,  // use to ensure that test data is up-to-date
                      final Integer expectedCode,           // use to ensure that test data is up-to-date
                      final String expectedMatcherName,
                      final Set<LinkStatus> expectedStatuses) {
        final SynchronousSiteDataRetriever retriever = buildDataSiteRetriever();
        final AtomicBoolean consumerHasBeenCalled = new AtomicBoolean(false);
        retriever.retrieve(url,
                (final FullFetchedLinkData d) -> {
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
