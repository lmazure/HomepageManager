package fr.mazure.homepagemanager.utils;

/**
 * Helper to get environment configuration
 */
public class EnvironmentHelper {

    private static final String s_youtubeApiKeyEnvVariableName = "YOUTUBE_DATA_API_KEY";
    private static final String s_youtubeApiKey = getEnvironmentValue(s_youtubeApiKeyEnvVariableName);

    /**
     * Get Youtube API key
     *
     * @return Youtube API key
     */
    public static String getYoutubeApiKey() {
        return s_youtubeApiKey;
    }

    private static String getEnvironmentValue(final String varEnvName) {
        final String apiKey = System.getenv(varEnvName);
        if (apiKey == null) {
            ExitHelper.exit(varEnvName + " environment variable is undefined");
        }
        return apiKey;
    }
}
