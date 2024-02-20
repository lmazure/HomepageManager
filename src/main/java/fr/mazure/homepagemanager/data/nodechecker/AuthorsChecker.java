package fr.mazure.homepagemanager.data.nodechecker;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthorsOfLink;
import fr.mazure.homepagemanager.data.nodechecker.tagselection.InclusionTagSelector;
import fr.mazure.homepagemanager.utils.xmlparsing.ArticleData;
import fr.mazure.homepagemanager.utils.xmlparsing.AuthorData;
import fr.mazure.homepagemanager.utils.xmlparsing.ElementType;
import fr.mazure.homepagemanager.utils.xmlparsing.LinkData;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParser;
import fr.mazure.homepagemanager.utils.xmlparsing.XmlParsingException;

/**
 *
 */
public class AuthorsChecker extends NodeChecker {

    private static final InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

    private static final Pattern s_fixControl = Pattern.compile("[ \\.\\p{IsAlphabetic}]+");
    private static final Pattern s_nameControl = Pattern.compile("[- \\.\\p{IsAlphabetic}’]+");
    private static final Pattern s_givenControl = Pattern.compile("[-_@ ’\\.\\*\\p{IsAlphabetic}0-9]+");

    /**
    * constructor
    */
    public AuthorsChecker() {
        super(s_selector,
              AuthorsChecker::checkWellKnownAuthors, "author list contains expected list of well known authors",
              AuthorsChecker::detectDuplicatedAuthor, "author list contains duplicated authors",
              AuthorsChecker::detectWrongCharacters, "author name contains illegal characters");
    }

    private static CheckStatus checkWellKnownAuthors(final Element e) {

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }

        for (final LinkData link: articleData.links()) {
            final Optional<WellKnownAuthorsOfLink.KnownAuthors> expectedWellKnownAuthors = WellKnownAuthorsOfLink.getWellKnownAuthors(link.getUrl());
            if (expectedWellKnownAuthors.isPresent()) {
                if (expectedWellKnownAuthors.get().canHaveOtherAuthors()) {
                    if (!articleData.authors().containsAll(expectedWellKnownAuthors.get().compulsoryAuthors())) {
                        return new CheckStatus("IncorrectAuthorList",
                                               "The list of authors of article \"" +
                                               link.getUrl() +
                                               "\" (" +
                                               formatAuthorList(articleData.authors()) +
                                               ") does not contain the expected list for the site (" +
                                               formatAuthorList(expectedWellKnownAuthors.get().compulsoryAuthors()) +
                                               ")",
                                               Optional.empty());
                    }
                } else if (!articleData.authors().containsAll(expectedWellKnownAuthors.get().compulsoryAuthors()) ||
                           !expectedWellKnownAuthors.get().compulsoryAuthors().containsAll(articleData.authors())) {
                    return new CheckStatus("IncorrectAuthorList",
                                           "The list of authors of article \"" +
                                           link.getUrl() +
                                           "\" (" +
                                           formatAuthorList(articleData.authors()) +
                                           ") is not equal to the expected list for the site (" +
                                           formatAuthorList(expectedWellKnownAuthors.get().compulsoryAuthors()) +
                                           ")",
                                           Optional.empty());
                }
            }
        }

        return null;
    }

    private static CheckStatus detectDuplicatedAuthor(final Element e) {

        final Set<AuthorData> authors = new HashSet<>();

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }

        for (final AuthorData author: articleData.authors()) {
            if (authors.contains(author)) {
                return new CheckStatus("DuplicatedAuthor",
                                       "The list of authors of article \"" +
                                       articleData.links().get(0).getUrl() +
                                       "\" contains duplicated author: " +
                                       author,
                                       Optional.empty());
            }
            authors.add(author);
        }
        return null;
    }

    private static CheckStatus detectWrongCharacters(final Element e) {

        ArticleData articleData;
        try {
            articleData = XmlParser.parseArticleElement(e);
        } catch (final XmlParsingException ex) {
            return new CheckStatus("ArticleParsingError",
                                   "Failed to parse article (" + ex.getMessage() + ")",
                                   Optional.empty());
        }

        for (final AuthorData author: articleData.authors()) {
            if (author.getNamePrefix().isPresent() && !s_fixControl.matcher(author.getNamePrefix().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The name prefix of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
            if (author.getFirstName().isPresent() && !s_nameControl.matcher(author.getFirstName().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The first name of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
            if (author.getMiddleName().isPresent() && !s_nameControl.matcher(author.getMiddleName().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The middle name of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
            if (author.getLastName().isPresent() && !s_nameControl.matcher(author.getLastName().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The last name of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
            if (author.getNameSuffix().isPresent() && !s_fixControl.matcher(author.getNameSuffix().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The name suffix of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
            if (author.getGivenName().isPresent() && !s_givenControl.matcher(author.getGivenName().get()).matches()) {
                return new CheckStatus("AuthorWithIllegalCharacters",
                                       "The given name of author " +
                                       author +
                                      " contains an illegal character",
                                      Optional.empty());
            }
        }
        return null;
    }

    private static String formatAuthorList(final List<AuthorData> list) {
        return String.join(";", list.stream().map(AuthorData::toString).collect(Collectors.toList()));
    }
}
