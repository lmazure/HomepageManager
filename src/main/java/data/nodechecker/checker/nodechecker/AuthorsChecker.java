package data.nodechecker.checker.nodechecker;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.w3c.dom.Element;

import data.knowledge.WellKnownAuthors;
import data.knowledge.WellKnownAuthorsOfLink;
import data.nodechecker.checker.CheckStatus;
import data.nodechecker.tagselection.InclusionTagSelector;
import utils.xmlparsing.ArticleData;
import utils.xmlparsing.AuthorData;
import utils.xmlparsing.ElementType;
import utils.xmlparsing.LinkData;
import utils.xmlparsing.XmlParser;
import utils.xmlparsing.XmlParsingException;

public class AuthorsChecker extends NodeChecker {

    private final static InclusionTagSelector s_selector = new InclusionTagSelector(new ElementType[] {
            ElementType.ARTICLE
            });

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
            return new CheckStatus("Failed to parse article (" + ex.getMessage() + ")");
        }

        for (LinkData link: articleData.getLinks()) {
            final Optional<WellKnownAuthors> expectedWellKnownAuthors = WellKnownAuthorsOfLink.getWellKnownAuthors(link.getUrl());
            if (expectedWellKnownAuthors.isPresent()) {
                if (expectedWellKnownAuthors.get().canHaveOtherAuthors()) {
                    if (!articleData.getAuthors().containsAll(expectedWellKnownAuthors.get().getCompulsoryAuthors())) {
                        return new CheckStatus("The list of authors of article \"" +
                                link.getUrl() +
                                "\" (" +
                                formatAuthorList(articleData.getAuthors()) +
                                ") does not contain the expected list for the site (" +
                                formatAuthorList(expectedWellKnownAuthors.get().getCompulsoryAuthors()) +
                                ")");
                    }
                } else if (!articleData.getAuthors().containsAll(expectedWellKnownAuthors.get().getCompulsoryAuthors()) ||
                           !expectedWellKnownAuthors.get().getCompulsoryAuthors().containsAll(articleData.getAuthors())) {
                    return new CheckStatus("The list of authors of article \"" +
                            link.getUrl() +
                            "\" (" +
                            formatAuthorList(articleData.getAuthors()) +
                            ") is not equal to the expected list for the site (" +
                            formatAuthorList(expectedWellKnownAuthors.get().getCompulsoryAuthors()) +
                            ")");
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
            return new CheckStatus("Failed to parse article (" + ex.getMessage() + ")");
        }

        for (AuthorData author: articleData.getAuthors()) {
            if (authors.contains(author)) {
                return new CheckStatus("The list of authors of article \"" +
                                       articleData.getLinks().get(0).getUrl() +
                                       "\" contains duplicated author: " +
                                       author);
            }
            authors.add(author);
        }
        return null;
    }

    private static String formatAuthorList(final List<AuthorData> list) {
        return String.join(";", list.stream().map(data -> data.toString()).collect(Collectors.toList()));
    }
}
