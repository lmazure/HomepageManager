package utils.internet;

public class HttpHelper {

    public static String getStringOfCode(final int code) throws InvalidHttpCodeException {

        switch (code) {
            case 100: return "Continue";
            case 101: return "Switching Protocols";
            case 102: return "Processing (WebDAV RFC 2518)";
            case 103: return "Early Hints (experimental RFC 8297)";
            case 200: return "OK";
            case 201: return "Created";
            case 202: return "Accepted";
            case 203: return "Non-Authoritative Information";
            case 204: return "No Content";
            case 205: return "Reset Content";
            case 206: return "Partial Content";
            case 207: return "Multi-Status (WebDAV RFC 2518)";
            case 208: return "Already Reported (WebDAV RFC 2518)";
            case 210: return "Content Different (WebDAV RFC 2518)";
            case 226: return "IM Used (RFC 3229)";
            case 300: return "Multiple Choices";
            case 301: return "Moved Permanently";
            case 302: return "Found";
            case 303: return "See Other";
            case 304: return "Not Modified";
            case 305: return "Use Proxy";
            case 306: return "(Unused)";
            case 307: return "Temporary Redirect";
            case 308: return "Permanent Redirect";
            case 310: return "Too many Redirects";
            case 400: return "Bad Request";
            case 401: return "Unauthorized";
            case 402: return "Payment Required";
            case 403: return "Forbidden";
            case 404: return "Not Found";
            case 405: return "Method Not Allowed";
            case 406: return "Not Acceptable";
            case 407: return "Proxy Authentication Required";
            case 408: return "Request Timeout";
            case 409: return "Conflict";
            case 410: return "Gone";
            case 411: return "Length Required";
            case 412: return "Precondition Failed";
            case 413: return "Request Entity Too Large";
            case 414: return "Request-URI Too Long";
            case 415: return "Unsupported Media Type";
            case 416: return "Requested Range Not Satisfiable";
            case 417: return "Expectation Failed";
            case 418: return "I’m a teapot (RFC 2324)";
            case 421: return "Bad mapping / Misdirected Request";
            case 422: return "Unprocessable entity (WebDAV)";
            case 423: return "Locked (WebDAV)";
            case 424: return "Method failure (WebDAV)";
            case 425: return "Unordered Collection (WebDAV RFC 3648)";
            case 426: return "Upgrade Required (RFC 2817)";
            case 428: return "Precondition Required (RFC 6585)";
            case 429: return "Too Many Requests (RFC 6585)";
            case 431: return "Request Header Fields Too Large (RFC 6585)";
            case 449: return "Retry With (Microsoft)";
            case 450: return "Blocked by Windows Parental Controls (Microsoft)";
            case 451: return "Unavailable For Legal Reasons";
            case 456: return "Unrecoverable Error (WebDAV)";
            case 444: return "No Response (Nginx)";
            case 495: return "SSL Certificate Error (Nginx)";
            case 496: return "SSL Certificate Required (Nginx)";
            case 497: return "HTTP Request Sent to HTTPS Port (Nginx)";
            case 498: return "Token expired/invalid (Nginx)";
            case 499: return "Client Closed Request (Nginx)";
            case 500: return "Internal Server Error";
            case 501: return "Not Implemented";
            case 502: return "Bad Gateway / Proxy Error";
            case 503: return "Service Unavailable";
            case 504: return "Gateway Timeout";
            case 505: return "HTTP Version Not Supported";
            case 506: return "Variant Also Negotiates (RFC 2295)";
            case 507: return "Insufficient storage (WebDAV)";
            case 508: return "Loop detected (WebDAV RFC 5842)";
            case 509: return "Bandwidth Limit Exceeded";
            case 510: return "Not extended (RFC 2774)";
            case 511: return "Network authentication required (RFC 6585)";
            case 520: return "Unknown Error (Cloudflare)";
            case 521: return "Web Server Is Down (Cloudflare)";
            case 522: return "Connection Timed Out (Cloudflare)";
            case 523: return "Origin Is Unreachable (Cloudflare)";
            case 524: return "A Timeout Occurred (Cloudflare)";
            case 525: return "SSL Handshake Failed (Cloudflare)";
            case 526: return "Invalid SSL Certificate (Cloudflare)";
            case 527: return "Railgun Error (Cloudflare)";
            default: throw new InvalidHttpCodeException("Invalid HTTP code (" + code + ")");
        }
    }
}
