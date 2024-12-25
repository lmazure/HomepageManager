package fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Expected redirection data
 */
public class WellKnownRedirections {

    private final List<RedirectionMatcher> _matchers;

    /**
     * constructor
     */
    public WellKnownRedirections() {

        _matchers = new ArrayList<>();

        final Set<Integer> successCodes = new HashSet<>();
        successCodes.add(null);
        successCodes.add(Integer.valueOf(200));
        successCodes.add(Integer.valueOf(202));

        final Set<Integer> errorCodes = new HashSet<>();
        errorCodes.add(null);
        errorCodes.add(Integer.valueOf(400));
        errorCodes.add(Integer.valueOf(401));
        errorCodes.add(Integer.valueOf(403));
        errorCodes.add(Integer.valueOf(404));
        errorCodes.add(Integer.valueOf(409));
        errorCodes.add(Integer.valueOf(410));
        errorCodes.add(Integer.valueOf(429));
        errorCodes.add(Integer.valueOf(500));
        errorCodes.add(Integer.valueOf(502));
        errorCodes.add(Integer.valueOf(503));
        //errorCodes.add(Integer.valueOf(504)); ignored for the time being
        errorCodes.add(Integer.valueOf(520));
        errorCodes.add(Integer.valueOf(521));
        errorCodes.add(Integer.valueOf(522));
        errorCodes.add(Integer.valueOf(525));
        errorCodes.add(Integer.valueOf(999));  // TODO handle fucking LinkedIn

        final Set<Integer> redirectionCodes = new HashSet<>();
        redirectionCodes.add(Integer.valueOf(301));
        redirectionCodes.add(Integer.valueOf(302));
        redirectionCodes.add(Integer.valueOf(303));
        redirectionCodes.add(Integer.valueOf(307));
        redirectionCodes.add(Integer.valueOf(308));

        {
            final RedirectionMatcher fromYoutubeChannelToYoutubeChannel1 = new RedirectionMatcher("from YouTube channel to YouTube channel",
                                                                                                  Set.of(LinkStatus.OK));
            fromYoutubeChannelToYoutubeChannel1.add("\\Qhttps://www.youtube.com/channel/\\E" + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(302)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel1.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2F\\E" + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(302)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel1.add("\\Qhttps://consent.youtube.com/ml?continue=https://www.youtube.com/channel/\\E"  + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(200)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel1.compile();
            _matchers.add(fromYoutubeChannelToYoutubeChannel1);
        }

        {
            final RedirectionMatcher fromYoutubeChannelToYoutubeChannel = new RedirectionMatcher("from YouTube channel to YouTube channel",
                                                                                                 Set.of(LinkStatus.OK, LinkStatus.OBSOLETE));
            fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://www.youtube.com/channel/\\E" + RedirectionMatcher.ANY_STRING,
                                                   Set.of(Integer.valueOf(302)),
                                                   RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fchannel%2F\\E" + RedirectionMatcher.ANY_STRING,
                                                   Set.of(Integer.valueOf(303)),
                                                   RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel.add("\\Qhttps://www.youtube.com/channel/\\E" + RedirectionMatcher.ANY_STRING,
                                                   Set.of(Integer.valueOf(200)),
                                                   RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel.compile();
            _matchers.add(fromYoutubeChannelToYoutubeChannel);
        }

        {
            final RedirectionMatcher fromYoutubeChannelToYoutubeChannel2 = new RedirectionMatcher("from YouTube channel to YouTube channel",
                                                                                                  Set.of(LinkStatus.OK));
            // we cannot use a named group because the parameter of the last step contains an encoded version of the channel name
            // for example, https://www.youtube.com/c/Tumourrasmoinsb%C3%AAteARTE is encoded into https://www.youtube.com/c/Tumourrasmoinsb%25C3%25AAteARTE
            fromYoutubeChannelToYoutubeChannel2.add("\\Qhttps://www.youtube.com/c/\\E.*",
                                                    Set.of(Integer.valueOf(302)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel2.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fc%2F\\E" + RedirectionMatcher.ANY_STRING,
                                                    Set.of(Integer.valueOf(302)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel2.add("\\Qhttps://consent.youtube.com/ml?continue=https://www.youtube.com/c/\\E.*\\Q?cbrd%3D1&gl=FR&hl=en&cm=2&pc=yt&src=1\\E",
                                                    Set.of(Integer.valueOf(200)),
                                                    RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeChannelToYoutubeChannel2.compile();
            _matchers.add(fromYoutubeChannelToYoutubeChannel2);
        }

        {
            final RedirectionMatcher fromYoutubeUserToYoutubeUser1 = new RedirectionMatcher("from YouTube user to YouTube user",
                                                                                            Set.of(LinkStatus.OK));
            fromYoutubeUserToYoutubeUser1.add("\\Qhttps://www.youtube.com/user/\\E" + RedirectionMatcher.ANY_STRING,
                                              Set.of(Integer.valueOf(302)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser1.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fuser%2F\\E" + RedirectionMatcher.ANY_STRING,
                                              Set.of(Integer.valueOf(302)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser1.add("\\Qhttps://consent.youtube.com/ml?continue=https://www.youtube.com/user/\\E"  + RedirectionMatcher.ANY_STRING,
                                              Set.of(Integer.valueOf(200)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser1.compile();
            _matchers.add(fromYoutubeUserToYoutubeUser1);
        }

        {
            final RedirectionMatcher fromYoutubeUserToYoutubeUser2 = new RedirectionMatcher("from YouTube user to YouTube user",
                                                                                            Set.of(LinkStatus.OK));
            fromYoutubeUserToYoutubeUser2.add("\\Qhttps://www.youtube.com/user/\\E(?<user>.*)",
                                              Set.of(Integer.valueOf(302)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser2.add("\\Qhttps://consent.youtube.com/m?continue=https%3A%2F%2Fwww.youtube.com%2Fuser%2F\\E" + RedirectionMatcher.ANY_STRING,
                                              Set.of(Integer.valueOf(303)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser2.add("\\Qhttps://www.youtube.com/user/\\E\\k<user>\\Q?cbrd=1&ucbcb=1\\E",
                                              Set.of(Integer.valueOf(200)),
                                              RedirectionMatcher.Multiplicity.ONE);
            fromYoutubeUserToYoutubeUser2.compile();
            _matchers.add(fromYoutubeUserToYoutubeUser2);
        }

        {
            final RedirectionMatcher mediumAnalytics = new RedirectionMatcher("Medium analytics",
                                                                              Set.of(LinkStatus.OK,
                                                                                     LinkStatus.OBSOLETE));
            mediumAnalytics.add("\\Qhttps://\\E(?<site>[^/]+)/(?<article>.+)",
                                Set.of(Integer.valueOf(307)),
                                RedirectionMatcher.Multiplicity.ONE);
            mediumAnalytics.add("\\Qhttps://\\Emedium.com/m/global-identity-2\\?redirectUrl=https%3A%2F%2F\\k<site>%2F\\k<article>",
                                Set.of(Integer.valueOf(307)),
                                RedirectionMatcher.Multiplicity.ONE);
            mediumAnalytics.add("\\Qhttps://\\E\\k<site>/\\k<article>\\?gi=[0-9a-z]+",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            mediumAnalytics.compile();
            _matchers.add(mediumAnalytics);
        }

        {
            final RedirectionMatcher oReillyRemoved1 = new RedirectionMatcher("removed from O’Reilly",
                                                                              Set.of(LinkStatus.REMOVED));
            oReillyRemoved1.add("(\\Qhttp://www.linuxdevcenter.com/pub/a/linux/\\E|\\Qhttp://www.onlamp.com/\\E(lpt|pub)/a/(apache|onlamp|php|python|security)/)" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved1.add("\\Qhttps://www.oreilly.com/ideas/\\E",
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved1.add("\\Qhttps://www.oreilly.com/radar/\\E",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved1.compile();
            _matchers.add(oReillyRemoved1);
        }

        {
            final RedirectionMatcher oReillyRemoved2 = new RedirectionMatcher("removed from O’Reilly",
                                                                              Set.of(LinkStatus.REMOVED));
            oReillyRemoved2.add("\\Qhttp://www.oreillynet.com/\\E(pub/a/(network|oreilly/security/news)|(mac|onlamp|xml)/blog)/" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved2.add("\\Qhttps://www.oreillynet.com/\\E(pub/a/(network|oreilly/security/news)|(mac|onlamp|xml)/blog)/" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved2.add("\\Qhttp://archive.oreilly.com/pub/\\E(a/(network|oreilly/security/news)|post)/" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved2.add("\\Qhttps://www.oreilly.com/\\E",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved2.compile();
            _matchers.add(oReillyRemoved2);
        }

        {
            final RedirectionMatcher oReillyRemoved3 = new RedirectionMatcher("removed from O’Reilly",
                                                                              Set.of(LinkStatus.REMOVED));
            oReillyRemoved3.add("(\\Qhttp://www.onjava.com/pub/a/onjava/\\E|\\Qhttp://www.onjava.com/catalog/javaadn\\E)" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved3.add("\\Qhttps://www.oreilly.com/radar/\\E",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved3.compile();
            _matchers.add(oReillyRemoved3);
        }

        {
            final RedirectionMatcher oReillyRemoved4 = new RedirectionMatcher("removed from O’Reilly",
                                                                              Set.of(LinkStatus.REMOVED));
            oReillyRemoved4.add("\\Qhttp://linux.oreillynet.com/pub/\\E" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved4.add("\\Qhttps://www.oreilly.com/radar/\\E",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved4.compile();
            _matchers.add(oReillyRemoved4);
        }

        {
            final RedirectionMatcher oReillyRemoved5 = new RedirectionMatcher("removed from O’Reilly",
                                                                              Set.of(LinkStatus.REMOVED));
            oReillyRemoved5.add("\\Qhttp://www.oreillynet.com/pub/a/\\E" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved5.add("\\Qhttps://www.oreillynet.com/pub/a/\\E" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved5.add("\\Qhttp://archive.oreilly.com/pub/a/\\E" + RedirectionMatcher.ANY_STRING,
                                Set.of(Integer.valueOf(301)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved5.add("\\Qhttps://www.oreilly.com/\\E",
                                Set.of(Integer.valueOf(200)),
                                RedirectionMatcher.Multiplicity.ONE);
            oReillyRemoved5.compile();
            _matchers.add(oReillyRemoved5);
        }

        {
            final RedirectionMatcher ibmRemoved1 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved1.add("https://www.ibm.com/developerworks/(java|linux|opensource|web)/library/" + RedirectionMatcher.ANY_STRING,
                            Set.of(Integer.valueOf(301)),
                            RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved1.add("https://developer.ibm.com/(languages/java|technologies|technologies/linux|technologies/systems|technologies/web-development)/",
                            Set.of(Integer.valueOf(200)),
                            RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved1.compile();
            _matchers.add(ibmRemoved1);
        }

        {
            final RedirectionMatcher ibmRemoved2 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved2.add("\\Qhttps://www.ibm.com/developerworks/library/\\E" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved2.add("https://developer.ibm.com/(|devpractices/devops/|technologies(|/linux|/linux/tutorials|/mobile|/web-development)/)",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved2.compile();
            _matchers.add(ibmRemoved2);
        }

        {
            final RedirectionMatcher ibmRemoved3 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved3.add("\\Qhttps://www.ibm.com/developerworks/java/library/co-tmline/\\Q" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved3.add("\\Qhttps://developer.ibm.com/product-doclinks/\\E",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved3.compile();
            _matchers.add(ibmRemoved3);
        }

        {
            final RedirectionMatcher ibmRemoved4 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved4.add("https://www.ibm.com/developerworks/opensource/library/" + RedirectionMatcher.ANY_STRING,
                            Set.of(Integer.valueOf(301)),
                            RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved4.add("\\Qhttps://developer.ibm.com/devpractices/open-source-development/\\E",
                            Set.of(Integer.valueOf(200)),
                            RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved4.compile();
            _matchers.add(ibmRemoved4);
        }

        {
            final RedirectionMatcher ibmRemoved5 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved5.add("https://www.ibm.com/developerworks/aix/library/" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved5.add("https://developer.ibm.com/components/aix/",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved5.compile();
            _matchers.add(ibmRemoved5);
        }

        {
            final RedirectionMatcher ibmRemoved6 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved6.add("\\Qhttps://www.ibm.com/developerworks/cloud/library/\\E" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved6.add("\\Qhttps://developer.ibm.com/depmodels/cloud/\\E",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved6.compile();
            _matchers.add(ibmRemoved6);
        }

        {
            final RedirectionMatcher ibmRemoved7 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved7.add("\\Qhttps://www.ibm.com/developerworks/websphere/techjournal/\\E" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved7.add("\\Qhttps://developer.ibm.com/depmodels/cloud/\\E",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved7.compile();
            _matchers.add(ibmRemoved7);
        }
        {
            final RedirectionMatcher ibmRemoved8 = new RedirectionMatcher("removed from IBM",
                                                                          Set.of(LinkStatus.REMOVED));
            ibmRemoved8.add("https://www.ibm.com/developerworks/(architecture|power|rational|systems|tivoli|webservices|xml)/library/" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved8.add("\\Qhttps://developer.ibm.com/\\E",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            ibmRemoved8.compile();
            _matchers.add(ibmRemoved8);
        }

        {
            final RedirectionMatcher developerIbmRemoved = new RedirectionMatcher("removed from developer.ibm.com",
                                                                                  Set.of(LinkStatus.REMOVED));
            developerIbmRemoved.add("\\Qhttps://developer.ibm.com/\\E(articles|tutorials)/" + RedirectionMatcher.ANY_STRING,
                           Set.of(Integer.valueOf(302)),
                           RedirectionMatcher.Multiplicity.ONE);
            developerIbmRemoved.add("\\Qhttps://developer.ibm.com/\\E(languages/(java|javascript)|technologies/web-development(/tutorials)?)/",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            developerIbmRemoved.compile();
            _matchers.add(developerIbmRemoved);
        }

        {
            final RedirectionMatcher rational = new RedirectionMatcher("removed from Rational",
                                                                       Set.of(LinkStatus.REMOVED));
            rational.add("\\Qhttp://www.rational.com/technotes/devtools_html/Purify_html/technote_\\E.+\\.html",
                           Set.of(Integer.valueOf(301)),
                           RedirectionMatcher.Multiplicity.ONE);
            rational.add("\\Qhttps://www.ibm.com/products/engineering-lifecycle-management\\E",
                           Set.of(Integer.valueOf(200)),
                           RedirectionMatcher.Multiplicity.ONE);
            rational.compile();
            _matchers.add(rational);
        }

        {
            final RedirectionMatcher sun = new RedirectionMatcher("removed from java.sun.com",
                                                                  Set.of(LinkStatus.REMOVED));
            sun.add("\\Qhttp://java.sun.com/developer/\\E" + RedirectionMatcher.ANY_STRING,
                    Set.of(Integer.valueOf(301)),
                    RedirectionMatcher.Multiplicity.ONE);
            sun.add("\\Qhttps://java.sun.com/developer/\\E" + RedirectionMatcher.ANY_STRING,
                    Set.of(Integer.valueOf(301)),
                    RedirectionMatcher.Multiplicity.ONE);
            sun.add("\\Qhttp://www.oracle.com/technetwork/java/index.html\\E",
                    Set.of(Integer.valueOf(301)),
                    RedirectionMatcher.Multiplicity.ONE);
            sun.add("\\Qhttp://www.oracle.com/java/technologies/?er=221886\\E",
                    Set.of(Integer.valueOf(301)),
                    RedirectionMatcher.Multiplicity.ONE);
            sun.add("\\Qhttps://www.oracle.com/java/technologies/?er=221886\\E",
                    Set.of(Integer.valueOf(200)),
                    RedirectionMatcher.Multiplicity.ONE);
            sun.compile();
            _matchers.add(sun);
        }

        {
            final RedirectionMatcher channel9Removed1 = new RedirectionMatcher("removed from Channel 9",
                                                                               Set.of(LinkStatus.REMOVED));
            channel9Removed1.add("\\Qhttps://channel9.msdn.com/\\E(Blogs|Series|Shows)" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.add("\\Qhttps://learn.microsoft.com/shows/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.add("\\Qhttps://aka.ms/Ch9Update\\E",
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.add("\\Qhttps://learn.microsoft.com/shows/\\E",
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E",
                                 Set.of(Integer.valueOf(200)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed1.compile();
            _matchers.add(channel9Removed1);
        }

        {
            final RedirectionMatcher channel9Removed2 = new RedirectionMatcher("removed from Channel 9",
                                                                               Set.of(LinkStatus.REMOVED));
            channel9Removed2.add("\\Qhttps://channel9.msdn.com/\\EEvent" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.add("\\Qhttps://learn.microsoft.com/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.add("\\Qhttps://learn.microsoft.com/en-us/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.add("\\Qhttps://aka.ms/Ch9Update\\E",
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.add("\\Qhttps://learn.microsoft.com/shows/\\E",
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E",
                                 Set.of(Integer.valueOf(200)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed2.compile();
            _matchers.add(channel9Removed2);
        }

        {
            final RedirectionMatcher channel9Removed3 = new RedirectionMatcher("removed from Channel 9",
                                                                               Set.of(LinkStatus.REMOVED));
            channel9Removed3.add("\\Qhttps://channel9.msdn.com/posts/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed3.add("\\Qhttps://learn.microsoft.com/shows\\E",
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed3.add("\\Qhttps://learn.microsoft.com/en-us/shows\\E",
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed3.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E",
                                 Set.of(Integer.valueOf(200)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed3.compile();
            _matchers.add(channel9Removed3);
        }

        {
            final RedirectionMatcher channel9Removed4 = new RedirectionMatcher("removed from Channel 9",
                                                                               Set.of(LinkStatus.REMOVED));
            channel9Removed4.add("\\Qhttps://channel9.msdn.com/Events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed4.add("\\Qhttps://learn.microsoft.com/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed4.add("\\Qhttps://learn.microsoft.com/en-us/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed4.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E",
                                 Set.of(Integer.valueOf(200)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed4.compile();
            _matchers.add(channel9Removed4);
        }

        {
            final RedirectionMatcher channel9Removed5 = new RedirectionMatcher("removed from Channel 9",
                                                                               Set.of(LinkStatus.REMOVED));
            channel9Removed5.add("\\Qhttps://channel9.msdn.com/Events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.add("\\Qhttps://learn.microsoft.com/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.add("\\Qhttps://learn.microsoft.com/en-us/events/\\E" + RedirectionMatcher.ANY_STRING,
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.add("\\Qhttps://aka.ms/Ch9Update\\E",
                                 Set.of(Integer.valueOf(301)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.add("\\Qhttps://learn.microsoft.com/shows/\\E",
                                 Set.of(Integer.valueOf(302)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.add("\\Qhttps://learn.microsoft.com/en-us/shows/\\E",
                                 Set.of(Integer.valueOf(200)),
                                 RedirectionMatcher.Multiplicity.ONE);
            channel9Removed5.compile();
            _matchers.add(channel9Removed5);
        }

        {
            final RedirectionMatcher msdnRemoved1 = new RedirectionMatcher("removed from MSDN",
                                                                           Set.of(LinkStatus.REMOVED));
            msdnRemoved1.add("\\Qhttps://msdn.microsoft.com/en-us/vstudio/\\E" + RedirectionMatcher.ANY_STRING,
                             Set.of(Integer.valueOf(301)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved1.add("\\Qhttp://www.visualstudio.com\\E",
                             Set.of(Integer.valueOf(301)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved1.add("\\Qhttps://www.visualstudio.com/\\E",
                             Set.of(Integer.valueOf(301)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved1.add("\\Qhttps://visualstudio.microsoft.com/\\E",
                             Set.of(Integer.valueOf(200)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved1.compile();
            _matchers.add(msdnRemoved1);
        }

        {
            final RedirectionMatcher msdnRemoved2 = new RedirectionMatcher("removed from MSDN",
                                                                           Set.of(LinkStatus.REMOVED));
            msdnRemoved2.add("\\Qhttps://msdn.microsoft.com/en-us/\\E(vstudio|library)/" + RedirectionMatcher.ANY_STRING,
                             Set.of(Integer.valueOf(301)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved2.add("\\Qhttps://learn.microsoft.com\\E",
                             Set.of(Integer.valueOf(302)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved2.add("\\Qhttps://learn.microsoft.com/en-us/\\E",
                             Set.of(Integer.valueOf(200)),
                             RedirectionMatcher.Multiplicity.ONE);
            msdnRemoved2.compile();
            _matchers.add(msdnRemoved2);
        }

        {
            final RedirectionMatcher techrepublic1 = new RedirectionMatcher("removed from TechRepublic",
                                                                            Set.of(LinkStatus.REMOVED));
            techrepublic1.add("\\Qhttps://www.techrepublic.com/article/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(301)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic1.add("\\Qhttps://www.techrepublic.com/topic/\\E(cxo|developer|hardware|microsoft|security)/",
                              Set.of(Integer.valueOf(200)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic1.compile();
            _matchers.add(techrepublic1);
        }

        {
            final RedirectionMatcher techrepublic2 = new RedirectionMatcher("removed from TechRepublic",
                                                                            Set.of(LinkStatus.REMOVED));
            techrepublic2.add("\\Qhttps://www.techrepublic.com/blog/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(301)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic2.add("\\Qhttps://www.techrepublic.com/article/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(301)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic2.add("\\Qhttps://www.techrepublic.com/topic/\\E(cxo|developer|hardware|microsoft)/",
                              Set.of(Integer.valueOf(200)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic2.compile();
            _matchers.add(techrepublic2);
        }

        {
            final RedirectionMatcher techrepublic3 = new RedirectionMatcher("removed from TechRepublic",
                                                                            Set.of(LinkStatus.REMOVED));
            techrepublic3.add("\\Qhttps://www.techrepublic.com/blog/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(301)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic3.add("\\Qhttps://www.techrepublic.com/article/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(404)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic3.compile();
            _matchers.add(techrepublic3);
        }

        {
            final RedirectionMatcher techrepublic4 = new RedirectionMatcher("removed from TechRepublic",
                                                                           Set.of(LinkStatus.REMOVED));
            techrepublic4.add("\\Qhttps://www.techrepublic.com/article/\\E" + RedirectionMatcher.ANY_STRING,
                              Set.of(Integer.valueOf(404)),
                              RedirectionMatcher.Multiplicity.ONE);
            techrepublic4.compile();
            _matchers.add(techrepublic4);
        }

        {
            final RedirectionMatcher redirectionToItself = new RedirectionMatcher("redirection to itself",
                                                                                  Set.of(LinkStatus.OK));
            redirectionToItself.add("(?<site>https?://.*)",
                                    redirectionCodes,
                                    RedirectionMatcher.Multiplicity.ONE);
            redirectionToItself.add("https?://.*",
                                    redirectionCodes,
                                    RedirectionMatcher.Multiplicity.ONE_OR_MANY);
            redirectionToItself.add("\\k<site>",
                                    successCodes,
                                    RedirectionMatcher.Multiplicity.ONE);
            redirectionToItself.compile();
            _matchers.add(redirectionToItself);
        }

        {
            final RedirectionMatcher redirectionToItself = new RedirectionMatcher("redirection to locale",
                                                                                  Set.of(LinkStatus.OK));
            redirectionToItself.add("(?<site>https?://.*)",
                                    redirectionCodes,
                                    RedirectionMatcher.Multiplicity.ONE);
            redirectionToItself.add("https?://.*",
                                    redirectionCodes,
                                    RedirectionMatcher.Multiplicity.ZERO_OR_MANY);
            redirectionToItself.add("\\k<site>/(fr|fr-fr|fr_fr|en|us|us-en)/?",
                                    successCodes,
                                    RedirectionMatcher.Multiplicity.ONE);
            redirectionToItself.compile();
            _matchers.add(redirectionToItself);
        }


        {
            // for buggy sites such as https://docs.trychroma.com/ which returns 307 but have no "Location" in the answer header
            final RedirectionMatcher redirectionEndingInSuccess = new RedirectionMatcher("redirection not being redirected",
                                                                                         Set.of());
            redirectionEndingInSuccess.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                           redirectionCodes,
                                           RedirectionMatcher.Multiplicity.ONE_OR_MANY);
            redirectionEndingInSuccess.compile();
            _matchers.add(redirectionEndingInSuccess);
        }

        {
            final RedirectionMatcher redirectionEndingInSuccess = new RedirectionMatcher("redirection ending in success (last URL should be used)",
                                                                                         Set.of());
            redirectionEndingInSuccess.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                           redirectionCodes,
                                           RedirectionMatcher.Multiplicity.ONE_OR_MANY);
            redirectionEndingInSuccess.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                           Set.of(Integer.valueOf(200)),
                                           RedirectionMatcher.Multiplicity.ONE);
            redirectionEndingInSuccess.compile();
            _matchers.add(redirectionEndingInSuccess);
        }

        {
            final RedirectionMatcher redirectionEndingInError = new RedirectionMatcher("redirection ending with an error code",
                                                                                       Set.of(LinkStatus.DEAD));
            redirectionEndingInError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                         redirectionCodes,
                                         RedirectionMatcher.Multiplicity.ONE_OR_MANY);
            redirectionEndingInError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                                         errorCodes,
                                         RedirectionMatcher.Multiplicity.ONE);
            redirectionEndingInError.compile();
            _matchers.add(redirectionEndingInError);
        }

        {
            final RedirectionMatcher basicError = new RedirectionMatcher("direct failure",
                                                                         Set.of(LinkStatus.DEAD));
            basicError.add("https?://"  + RedirectionMatcher.ANY_STRING,
                           errorCodes,
                           RedirectionMatcher.Multiplicity.ONE);
            basicError.compile();
            _matchers.add(basicError);
        }

        {
            final RedirectionMatcher basicOk = new RedirectionMatcher("direct success",
                                                                      Set.of(LinkStatus.OK,
                                                                             LinkStatus.ZOMBIE,
                                                                             LinkStatus.OBSOLETE));
            basicOk.add("https?://" + RedirectionMatcher.ANY_STRING,
                        successCodes,
                        RedirectionMatcher.Multiplicity.ONE);
            basicOk.compile();
            _matchers.add(basicOk);
        }
    }

    /**
     * Compute the possible statuses of the redirection chain
     *
     * @param effectiveData redirection chain
     * @return possible statuses
     */
    public Match getMatch(final FullFetchedLinkData effectiveData) {
        for (final RedirectionMatcher matcher: _matchers) {
            if (matcher.doesRedirectionMatch(effectiveData)) {
                return matchOf(matcher);
            }
        }
        throw new UnsupportedOperationException(effectiveDataToString(effectiveData));
    }

    private static String effectiveDataToString(final FullFetchedLinkData effectiveData) {
        final StringBuilder builder = new StringBuilder();
        builder.append(effectiveData.url());
        builder.append("|");
        if (effectiveData.headers().isPresent()) {
            builder.append(HttpHelper.getResponseCodeFromHeaders(effectiveData.headers().get()));
        }
        builder.append("→");
        HeaderFetchedLinkData d = effectiveData.previousRedirection();
        while (d != null) {
            builder.append(d.url());
            builder.append("|");
            if (d.headers().isPresent()) {
                builder.append(HttpHelper.getResponseCodeFromHeaders(d.headers().get()));
            }
            builder.append("→");
            d = d.previousRedirection();
        }
        return builder.toString();
    }

    private static Match matchOf(final RedirectionMatcher matcher) {
        return new Match(matcher.getName(), matcher.getStatuses());
    }

    /**
     * Result of a redirection chain matching
     *
     * @param name Name of the matcher
     * @param statuses Possible statuses of the redirection chain
     *
     */
    public record Match(String name, Set<LinkStatus> statuses) {}
}
