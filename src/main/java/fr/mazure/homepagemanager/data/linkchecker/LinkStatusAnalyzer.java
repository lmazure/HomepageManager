package fr.mazure.homepagemanager.data.linkchecker;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import fr.mazure.homepagemanager.data.dataretriever.FullFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.HeaderFetchedLinkData;
import fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever;
import fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.WellKnownRedirections;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkStatusCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.UpdateLinkUrlCorrection;
import fr.mazure.homepagemanager.data.violationcorrection.ViolationCorrection;
import fr.mazure.homepagemanager.utils.internet.HttpHelper;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkStatus;

/**
 * Base class for all the link status analyzers (i.e. the classes deciding if the effective data matches the expected data)
 */
public class LinkStatusAnalyzer {

    private static final WellKnownRedirections _redirectionData = new WellKnownRedirections();

    /**
     * @param expectedStatus status as expected in the XML file
     * @param effectiveData data as retrieved from Internet
     * @return true if and only if effectiveData matches expectedData
     */
    public static boolean doesEffectiveDataMatchesExpectedData(final LinkStatus expectedStatus,
                                                               final FullFetchedLinkData effectiveData) {
        final Set<LinkStatus> expectedStatuses = getPossibleStatuses(effectiveData);
        return expectedStatuses.contains(expectedStatus);
    }

    /**
     * Propose a correction for a link
     *
     * @param expectedData expected link data
     * @param effectiveData effective link data
     * @return proposed correction
     */
    public static Optional<ViolationCorrection> getProposedCorrection(final LinkData expectedData,
                                                                      final FullFetchedLinkData effectiveData) {
        final Set<LinkStatus> expectedStatuses = getPossibleStatuses(effectiveData);
        if (expectedStatuses.size() == 1) {
            final LinkStatus status = expectedStatuses.iterator().next();
            return Optional.of(new UpdateLinkStatusCorrection(expectedData.getStatus(), status, expectedData.getUrl()));
        }
        if (extractHttpCode(effectiveData.headers()).isPresent() &&
            ((extractHttpCode(effectiveData.headers()).get().intValue() == HttpURLConnection.HTTP_MOVED_PERM) ||
             (extractHttpCode(effectiveData.headers()).get().intValue() == HttpURLConnection.HTTP_MOVED_TEMP) ||
             (extractHttpCode(effectiveData.headers()).get().intValue() == 308))) {
            if (effectiveData.previousRedirection() != null) {
                HeaderFetchedLinkData d = effectiveData.previousRedirection();
                while (d.previousRedirection() != null) {
                    d = d.previousRedirection();
                }
                return Optional.of(new UpdateLinkUrlCorrection(expectedData.getUrl(), d.url()));
            }
        }

        return Optional.empty();
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

    private static Optional<Integer> extractHttpCode(final Optional<Map<String, List<String>>> headers) {
        if (headers.isEmpty()) {
            return Optional.empty();
        }
        final int code = HttpHelper.getResponseCodeFromHeaders(headers.get());
        return Optional.of(Integer.valueOf(code));
    }}

