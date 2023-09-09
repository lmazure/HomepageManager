package fr.mazure.homepagemanager.data.nodechecker;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import fr.mazure.homepagemanager.data.knowledge.WellKnownAuthors;
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

    /**
    * constructor
    */
    public AuthorsChecker() {
        super(s_selector,
              AuthorsChecker::checkWellKnownAuthors, "author list contains expected list of well known authors",
              AuthorsChecker::detectDuplicatedAuthor, "author list contains duplicated authors");
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

        for (LinkData link: articleData.links()) {
            final Optional<WellKnownAuthors> expectedWellKnownAuthors = WellKnownAuthorsOfLink.getWellKnownAuthors(link.getUrl());
            if (expectedWellKnownAuthors.isPresent()) {
                if (expectedWellKnownAuthors.get().canHaveOtherAuthors()) {
                    if (!articleData.authors().containsAll(expectedWellKnownAuthors.get().getCompulsoryAuthors())) {
                        return new CheckStatus("IncorrectAuthorList",
                                               "The list of authors of article \"" +
                                               link.getUrl() +
                                               "\" (" +
                                               formatAuthorList(articleData.authors()) +
                                               ") does not contain the expected list for the site (" +
                                               formatAuthorList(expectedWellKnownAuthors.get().getCompulsoryAuthors()) +
                                               ")",
                                               Optional.empty());
                    }
                } else if (!articleData.authors().containsAll(expectedWellKnownAuthors.get().getCompulsoryAuthors()) ||
                           !expectedWellKnownAuthors.get().getCompulsoryAuthors().containsAll(articleData.authors())) {
                    return new CheckStatus("IncorrectAuthorList",
                                           "The list of authors of article \"" +
                                           link.getUrl() +
                                           "\" (" +
                                           formatAuthorList(articleData.authors()) +
                                           ") is not equal to the expected list for the site (" +
                                           formatAuthorList(expectedWellKnownAuthors.get().getCompulsoryAuthors()) +
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

        for (AuthorData author: articleData.authors()) {
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

    private static String formatAuthorList(final List<AuthorData> list) {
        return String.join(";", list.stream().map(data -> data.toString()).collect(Collectors.toList()));
    }
}
