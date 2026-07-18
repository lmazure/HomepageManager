package fr.mazure.homepagemanager.data.linkchecker.gitbutlerblog.test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import fr.mazure.homepagemanager.data.linkchecker.gitbutlerblog.GitButlerBlogLinkContentParser;
import fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase;

/**
 * Tests of GitButlerBlogLinkContentParser class
 *
 */
class GitButlerBlogLinkContentParserTest extends LinkDataExtractorTestBase {

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.gitbutler.com/ship-faster-butler-flow|Butler Flow: shipping code faster (but less like Alfred, more like CI on steroids) - Part 1",
        "https://blog.gitbutler.com/gerrit-mode|Use GitButler for your Gerrit workflow",
        "https://blog.gitbutler.com/gitbutler-agent-assist|Getting Started With GitButler Agents",
        "https://blog.gitbutler.com/simplifying-git|Simplifying Git by Using GitButler",
        }, delimiter = '|')
    void testTitle(final String url,
                   final String expectedTitle) {
        checkTitle(GitButlerBlogLinkContentParser.class, url, expectedTitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.gitbutler.com/ship-faster-butler-flow|Dig into what Butler Flow is, why it exists, how it works, and how adopting it can help modern development teams solve one of the perpetual pains in software.",
        "https://blog.gitbutler.com/gerrit-mode|GitButler now has native Gerrit Mode support. Enable it with one config setting and get automatic Change-Id injection, smart push behavior, and linked change URLs.",
        "https://blog.gitbutler.com/gitbutler-agent-assist|GitButler built a new way to integrate AI-powered code generation directly into your version control workflow.",
        "https://blog.gitbutler.com/simplifying-git|Git is great - and has been for twenty years. But in that time, how many of us have advanced our git skills? Most of us know about five or six commands: clone, status, push/pull, add, commit, branch, and checkout.",
        }, delimiter = '|')
    void testSubtitle(final String url,
                      final String expectedSubtitle) {
        checkSubtitle(GitButlerBlogLinkContentParser.class, url, expectedSubtitle);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.gitbutler.com/ship-faster-butler-flow|2025-10-23",
        "https://blog.gitbutler.com/gerrit-mode|2025-11-10",
        "https://blog.gitbutler.com/gitbutler-agent-assist|2026-01-09",
        "https://blog.gitbutler.com/simplifying-git|2026-02-22",
        }, delimiter = '|')
    void testDate(final String url,
                  final String expectedDate) {
        checkCreationDate(GitButlerBlogLinkContentParser.class, url, expectedDate);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.gitbutler.com/ship-faster-butler-flow|PJ||Hagerty|",
        "https://blog.gitbutler.com/gerrit-mode|Scott||Chacon|",
        "https://blog.gitbutler.com/gitbutler-agent-assist|Mattias||Granlund|",
        "https://blog.gitbutler.com/simplifying-git|PJ||Hagerty|",
        }, delimiter = '|')
    void testAuthor(final String url,
                    final String expectedFirstName,
                    final String expectedMiddleName,
                    final String expectedLastName,
                    final String expectedNameSuffix) {
        check1Author(GitButlerBlogLinkContentParser.class,
                     url,
                     null,
                     expectedFirstName,
                     expectedMiddleName,
                     expectedLastName,
                     expectedNameSuffix,
                     null);
    }

    @SuppressWarnings("static-method")
    @ParameterizedTest
    @CsvSource(value = {
        "https://blog.gitbutler.com/ship-faster-butler-flow|en",
        "https://blog.gitbutler.com/gerrit-mode|en",
        "https://blog.gitbutler.com/gitbutler-agent-assist|en",
        "https://blog.gitbutler.com/simplifying-git|en",
        }, delimiter = '|')
    void testLanguage(final String url,
                      final String expectedLanguage) {
        checkLanguage(GitButlerBlogLinkContentParser.class, url, expectedLanguage);
    }
}
