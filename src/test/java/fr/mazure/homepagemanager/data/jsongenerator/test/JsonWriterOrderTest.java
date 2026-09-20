package fr.mazure.homepagemanager.data.jsongenerator.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import fr.mazure.homepagemanager.data.jsongenerator.ArticleFactory;
import fr.mazure.homepagemanager.data.jsongenerator.AuthorFactory;
import fr.mazure.homepagemanager.data.jsongenerator.JsonWriter;
import fr.mazure.homepagemanager.data.jsongenerator.KeywordFactory;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;

/**
 * Tests of {@link JsonWriter#generateAuthorJson} focusing on the {@code order} field.
 */
class JsonWriterOrderTest {

    private static AuthorData authorData(final String firstName,
                                         final String lastName,
                                         final AuthorData.NameOrder order) {
        return new AuthorData(Optional.empty(),
                              Optional.of(firstName),
                              Optional.empty(),
                              Optional.of(lastName),
                              Optional.empty(),
                              Optional.empty(),
                              order);
    }

    @SuppressWarnings("static-method")
    @Test
    void orderFieldIsAlwaysEmitted(@TempDir final File tempDir) throws IOException {
        final AuthorFactory authorFactory = new AuthorFactory();
        authorFactory.buildAuthor(authorData("Martin", "Fowler", AuthorData.NameOrder.WESTERN));
        authorFactory.buildAuthor(authorData("An", "Nguyen", AuthorData.NameOrder.EASTERN));

        final JsonWriter writer = new JsonWriter(new ArticleFactory(), authorFactory, new KeywordFactory());
        final String fileName = "author.json";
        writer.generateAuthorJson(tempDir, fileName);

        final File output = new File(tempDir, fileName);
        final String content = Files.readString(output.toPath(), StandardCharsets.UTF_8);

        // every author object must contain an "order" field
        Assertions.assertTrue(content.contains("\"order\" : \"western\""), "western order should be emitted");
        Assertions.assertTrue(content.contains("\"order\" : \"eastern\""), "eastern order should be emitted");

        // count occurrences of "order" — should be exactly 2 (one per author)
        final int orderCount = countOccurrences(content, "\"order\"");
        Assertions.assertEquals(2, orderCount, "each author should have exactly one order field");
    }

    @SuppressWarnings("static-method")
    @Test
    void orderFieldEmittedWhenNoNamePartsPresent(@TempDir final File tempDir) throws IOException {
        final AuthorFactory authorFactory = new AuthorFactory();
        // author with only a given name, no other name parts
        authorFactory.buildAuthor(new AuthorData(Optional.empty(),
                                                 Optional.empty(),
                                                 Optional.empty(),
                                                 Optional.empty(),
                                                 Optional.empty(),
                                                 Optional.of("nickname"),
                                                 AuthorData.NameOrder.WESTERN));

        final JsonWriter writer = new JsonWriter(new ArticleFactory(), authorFactory, new KeywordFactory());
        writer.generateAuthorJson(tempDir, "author.json");

        final String content = Files.readString(new File(tempDir, "author.json").toPath(), StandardCharsets.UTF_8);
        Assertions.assertTrue(content.contains("\"order\" : \"western\""), "order should be emitted even with no name parts");
    }

    private static int countOccurrences(final String str, final String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }
}
