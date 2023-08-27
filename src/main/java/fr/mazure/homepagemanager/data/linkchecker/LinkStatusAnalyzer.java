package fr.mazure.homepagemanager.data.linkchecker;

import java.util.Set;

import fr.mazure.homepagemanager.data.internet.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.internet.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Base class for all the link status analyzers (i.e. the classes deciding if the effective data matches the expected data)
 */
public class LinkStatusAnalyzer {

    private static final WellKnownRedirections _redirectionData = new WellKnownRedirections();

    /**
     * @param expectedData data as expected in the XML file
     * @param effectiveData data as retrieved from Internet
     * @return true if and only if effectiveData matches expectedData
     */
    public static boolean doesEffectiveDataMatchesExpectedData(final LinkData expectedData,
                                                               final FullFetchedLinkData effectiveData) {
        final Set<LinkStatus> expectedStatuses = getPossibleStatuses(effectiveData);
        return expectedStatuses.contains(expectedData.getStatus());
    }

    /**
     * @param effectiveData data as retrieved from Internet
     * @return true if maximum number of redirection has been reached
     */
    public static boolean hasMaximumNumberOfRedirectionsBeenReached(final FullFetchedLinkData effectiveData) {
        return numberOfRedirections(effectiveData) == SynchronousSiteDataRetriever.getMaximumNumberOfRedirections();
    }

    private static Set<LinkStatus> getPossibleStatuses(final FullFetchedLinkData effectiveData) {
        if (hasMaximumNumberOfRedirectionsBeenReached(effectiveData)) {
            return Set.of(LinkStatus.DEAD);
        }
        return _redirectionData.getMatch(effectiveData).statuses();
    }

    private static int numberOfRedirections(final FullFetchedLinkData effectiveData) {
        int n = 0;
        HeaderFetchedLinkData d = effectiveData.previousRedirection();
        while (d != null) {
            n++;
            d = d.previousRedirection();
        }
        return n;
    }
}

