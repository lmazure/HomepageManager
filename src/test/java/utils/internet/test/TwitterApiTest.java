package utils.internet.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import data.SecretRepository;
import utils.internet.twitter.TwitterApi;
import utils.internet.twitter.TwitterUserDto;

/**
*
*/
public class TwitterApiTest {

    @SuppressWarnings("static-method")
    @Test
    void descriptionIsRetrieved() {
        final TwitterApi api = buildApi();
        final TwitterUserDto dto = api.getUser("ElJj");
        Assertions.assertEquals("Mais si, c'est le mec qui fait des vidéos de maths ! Une fois, j'ai regardé, j'ai pas tout compris mais c'était sympa.", dto.getDescription());
    }

    private static TwitterApi buildApi() {
        return new TwitterApi(SecretRepository.getTwitterApiKey(), SecretRepository.getTwitterApiSecretKey());
    }
}
