package fr.mazure.homepagemanager.data;

/**
 * Manage the secrets (e.g. the API keys)
 */
public class SecretRepository {

    /**
     * @return YouTube application name
     */
    public static String getYoutubeApplicationName() {
        return getEnvironmentVariableValue("YOUTUBE_APPLICATION_NAME", "YouTube application name");
    }

    /**
     * @return YouTube API key
     */
    public static String getYoutubeApiKey() {
        return getEnvironmentVariableValue("YOUTUBE_API_KEY", "YouTube API key");
    }

    /**
     * @return Twitter API key
     */
    public static String getTwitterApiKey() {
        return getEnvironmentVariableValue("TWITTER_API_KEY", "Twitter API key");
    }

    /**
     * @return Twitter API secret key
     */
    public static String getTwitterApiSecretKey() {
        return getEnvironmentVariableValue("TWITTER_API_SECRET_KEY", "Twitter API secret key");
    }

    private static String getEnvironmentVariableValue(final String environmentVariableName,
                                                      final String environmentVariableDescription) {
        final String value = System.getenv(environmentVariableName);
        if (value == null) {
            throw new IllegalStateException("environment variable " +
                                            environmentVariableName +
                                            " is undefined (it should contain the " +
                                            environmentVariableDescription +
                                            ")");
        }

        return value;
    }
}
