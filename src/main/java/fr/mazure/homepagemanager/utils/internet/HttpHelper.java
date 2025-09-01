package fr.mazure.homepagemanager.utils.internet;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.mazure.homepagemanager.utils.ExitHelper;

/**
 * Helper to manage HTTP requests
 */
public class HttpHelper {

    /**
     * @param code HTTP code
     * @return string describing the code
     * @throws InvalidHttpCodeException the code is not valid
     */
    public static String getStringOfCode(final int code) throws InvalidHttpCodeException {

        return switch (code) {
        case 100 -> "Continue";
        case 101 -> "Switching Protocols";
        case 102 -> "Processing (WebDAV RFC 2518)";
        case 103 -> "Early Hints (experimental RFC 8297)";
        case 200 -> "OK";
        case 201 -> "Created";
        case 202 -> "Accepted";
        case 203 -> "Non-Authoritative Information";
        case 204 -> "No Content";
        case 205 -> "Reset Content";
        case 206 -> "Partial Content";
        case 207 -> "Multi-Status (WebDAV RFC 2518)";
        case 208 -> "Already Reported (WebDAV RFC 2518)";
        case 210 -> "Content Different (WebDAV RFC 2518)";
        case 226 -> "IM Used (RFC 3229)";
        case 300 -> "Multiple Choices";
        case 301 -> "Moved Permanently";
        case 302 -> "Found";
        case 303 -> "See Other";
        case 304 -> "Not Modified";
        case 305 -> "Use Proxy";
        case 306 -> "(Unused)";
        case 307 -> "Temporary Redirect";
        case 308 -> "Permanent Redirect";
        case 310 -> "Too many Redirects";
        case 400 -> "Bad Request";
        case 401 -> "Unauthorized";
        case 402 -> "Payment Required";
        case 403 -> "Forbidden";
        case 404 -> "Not Found";
        case 405 -> "Method Not Allowed";
        case 406 -> "Not Acceptable";
        case 407 -> "Proxy Authentication Required";
        case 408 -> "Request Timeout";
        case 409 -> "Conflict";
        case 410 -> "Gone";
        case 411 -> "Length Required";
        case 412 -> "Precondition Failed";
        case 413 -> "Request Entity Too Large";
        case 414 -> "Request-URI Too Long";
        case 415 -> "Unsupported Media Type";
        case 416 -> "Requested Range Not Satisfiable";
        case 417 -> "Expectation Failed";
        case 418 -> "I’m a teapot (RFC 2324)";
        case 421 -> "Bad mapping / Misdirected Request";
        case 422 -> "Unprocessable entity (WebDAV)";
        case 423 -> "Locked (WebDAV)";
        case 424 -> "Method failure (WebDAV)";
        case 425 -> "Unordered Collection (WebDAV RFC 3648)";
        case 426 -> "Upgrade Required (RFC 2817)";
        case 428 -> "Precondition Required (RFC 6585)";
        case 429 -> "Too Many Requests (RFC 6585)";
        case 431 -> "Request Header Fields Too Large (RFC 6585)";
        case 449 -> "Retry With (Microsoft)";
        case 450 -> "Blocked by Windows Parental Controls (Microsoft)";
        case 451 -> "Unavailable For Legal Reasons";
        case 456 -> "Unrecoverable Error (WebDAV)";
        case 444 -> "No Response (Nginx)";
        case 495 -> "SSL Certificate Error (Nginx)";
        case 496 -> "SSL Certificate Required (Nginx)";
        case 497 -> "HTTP Request Sent to HTTPS Port (Nginx)";
        case 498 -> "Token expired/invalid (Nginx)";
        case 499 -> "Client Closed Request (Nginx)";
        case 500 -> "Internal Server Error";
        case 501 -> "Not Implemented";
        case 502 -> "Bad Gateway / Proxy Error";
        case 503 -> "Service Unavailable";
        case 504 -> "Gateway Timeout";
        case 505 -> "HTTP Version Not Supported";
        case 506 -> "Variant Also Negotiates (RFC 2295)";
        case 507 -> "Insufficient storage (WebDAV)";
        case 508 -> "Loop detected (WebDAV RFC 5842)";
        case 509 -> "Bandwidth Limit Exceeded";
        case 510 -> "Not extended (RFC 2774)";
        case 511 -> "Network authentication required (RFC 6585)";
        case 520 -> "Unknown Error (Cloudflare)";
        case 521 -> "Web Server Is Down (Cloudflare)";
        case 522 -> "Connection Timed Out (Cloudflare)";
        case 523 -> "Origin Is Unreachable (Cloudflare)";
        case 524 -> "A Timeout Occurred (Cloudflare)";
        case 525 -> "SSL Handshake Failed (Cloudflare)";
        case 526 -> "Invalid SSL Certificate (Cloudflare)";
        case 527 -> "Railgun Error (Cloudflare)";
        default -> throw new InvalidHttpCodeException("Invalid HTTP code (" + code + ")");
        };
    }

    /**
     * @param headers headers
     * @return HTTP code
     */
    public static int getResponseCodeFromHeaders(final Map<String, List<String>> headers) {
        final List<String> l = headers.get(null);
        final String ll = l.get(0);
        final String c = ll.split(" ")[1];
        return Integer.parseInt(c);
        //return Integer.parseInt(headers.get(null).get(0).split(" ")[1]);
    }

    /**
     * @param headers headers
     * @return redirect location
     */
    public static String getLocationFromHeaders(final Map<String, List<String>> headers) {
        if (headers.get("Location") != null) {
            return headers.get("Location").get(0);
        }
        if (headers.get("location") != null) {
            return headers.get("location").get(0);
        }
        return null;
    }

    /**
     * Determinate from the header is the payload is gzipped or not
     *
     * @param headers headers
     * @return true if the payload is gzipped
     */
    public static boolean isEncodedWithGzip(final Map<String, List<String>> headers) {

        if (headers.containsKey("Content-Encoding")) {
            return headers.get("Content-Encoding").get(0).equals("gzip");
        }

        if (headers.containsKey("content-encoding")) {
            return headers.get("content-encoding").get(0).equals("gzip");
        }

        return false;
    }
    
    private static final Map<String, Long> s_lastSiteTimestamp = Collections.synchronizedMap(new HashMap<String, Long>());
    private static final Map<String, Integer> s_minDelayPerSite = new HashMap<>();
    static {
        s_minDelayPerSite.put("oxide-and-friends.transistor.fm", Integer.valueOf(3000));
    }
    /**
     * Ensure that the site is not called too often, sleep if necessary
     *
     * @param url URL to be visited
     */
    public static void throttle(final String url) {

        final String host = UriHelper.getHost(url);
        final Integer minDelay = s_minDelayPerSite.get(host);
        if (minDelay == null) {
            return;
        }

        final long now = Instant.now().toEpochMilli();
        final Long timestamp = s_lastSiteTimestamp.get(host);
        if (timestamp != null) {
            final long delay = now - timestamp.longValue();
            final long pauseDuration = minDelay.intValue() - delay;
            if (pauseDuration > 0) {
                System.out.println("Sleeping for " + pauseDuration + " ms for " + host);  
                try {
                    Thread.sleep(pauseDuration);
                } catch (final InterruptedException e) {
                    ExitHelper.exit(e);
                }
            }
		}
		s_lastSiteTimestamp.put(host, Long.valueOf(now));
	}
}
