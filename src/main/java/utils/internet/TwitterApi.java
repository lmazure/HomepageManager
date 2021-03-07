package utils.internet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Base64;

import utils.ExitHelper;

public class TwitterApi {

    private final String _token;

    public TwitterApi(final String apiKey,
                      final String apiSecretKey) {
        _token = getBearerToken(apiKey, apiSecretKey);
    }

    public TwitterUserDto getUser(final String userId) {
        final HttpRequest request = HttpRequest.newBuilder()
                                               .uri(URI.create(getUserUrlFromName(userId)))
                                               .setHeader("Authorization", "Bearer " + _token)
                                               .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                                               .build();
        final HttpClient client = HttpClient.newHttpClient();
        try {
            final HttpResponse<String>  response = client.send(request, BodyHandlers.ofString());
            String tok = response.body();
            tok = tok.replaceAll(".*description\":\"", "");
            tok = tok.replaceAll("\".*", "");
            return new TwitterUserDto(tok);
        } catch (final IOException | InterruptedException e) {
            ExitHelper.exit(e);
        }
        // NOT REACHED
        return null;
    }

    private static final String ALL_USER_FIELDS = "user.fields=id,created_at,username,name,location,url,verified,profile_image_url,public_metrics,pinned_tweet_id,description,protected";

    private static String getUserUrlFromName(String username) {
        return "https://api.twitter.com/2/users/by/username/" +
               username +
               "?" +
               ALL_USER_FIELDS;
      }

    private static String getBearerToken(final String apiKey,
                                         final String apiSecretKey) {
        final String url = "https://api.twitter.com/oauth2/token";
        final String valueToCrypt = apiKey + ":" + apiSecretKey;
        final String cryptedValue = Base64.getEncoder().encodeToString(valueToCrypt.getBytes());
        final HttpRequest request = HttpRequest.newBuilder()
                                               .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                                               .uri(URI.create(url))
                                               .setHeader("Authorization", "Basic " + cryptedValue)
                                               .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                                               .build();
        final HttpClient client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response;
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String tok = response.body();
            tok = tok.replace("{\"token_type\":\"bearer\",\"access_token\":\"", "");
            tok = tok.replace("\"}", "");
            return tok;
        } catch (final IOException | InterruptedException e) {
            ExitHelper.exit(e);
        }
        // NOT REACHED
        return null;
      }
}
