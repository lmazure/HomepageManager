## Stats

```
warnings = 124
lines prod = 9514
lines test = 3939
test coverage =
              main     test
instructions  66.0%    89.1%
branches      44.8%    77.5%
lines         65.9%    94.7%
methods       53.0%/52.2%    97.9%
```


```
              main     test
instructions  68.8%    92.7%
branches      47.1%    77.5%
lines         67.9%    95.2%
methods       52.1%    98.8%
→ increase method coverage
```


```
2024-11-03
warnings = 111
lines prod = 10942
lines test = 4704
test coverage =
              main     test
instructions  69.1%    93.0%
branches      48.3%    62.5%
lines         67.8%    94.9%
methods       54.0%    98.1%
```


```
2024-11-16
warnings = 111
lines prod = 10953
lines test = 3966
test coverage =
              main     test
instructions  69.3%    93.6%
branches      48.9%    62.5%
lines         68.2%    95.2%
methods       53.9%    97.9%
```




## Urgent - to be fixed before publication



## Must be done in this issue





## To do for next issue



repair

```
HomepageManager (unit tests)
fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest
testNoSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testNoSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/thoughts-on-ai-progress-dec-2025-video
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkNoSubtitle(LinkDataExtractorTestBase.java:53)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testNoSubtitle(DwarkeshPodcastLinkContentParserTest.java:45)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fthoughts-on-ai-progress-dec-2025-video+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

test2Authors(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
test2Authors(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkAuthors(LinkDataExtractorTestBase.java:441)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.check2Authors(LinkDataExtractorTestBase.java:175)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.test2Authors(DwarkeshPodcastLinkContentParserTest.java:93)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 5 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 8 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 13 more

test2Authors(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkAuthors(LinkDataExtractorTestBase.java:441)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.check2Authors(LinkDataExtractorTestBase.java:175)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.test2Authors(DwarkeshPodcastLinkContentParserTest.java:93)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 5 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 8 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 13 more

testSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkSubtitle(LinkDataExtractorTestBase.java:43)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testSubtitle(DwarkeshPodcastLinkContentParserTest.java:35)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkSubtitle(LinkDataExtractorTestBase.java:43)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testSubtitle(DwarkeshPodcastLinkContentParserTest.java:35)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testSubtitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/openai-huggingface
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkSubtitle(LinkDataExtractorTestBase.java:43)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testSubtitle(DwarkeshPodcastLinkContentParserTest.java:35)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fopenai-huggingface+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testDuration(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testDuration(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkDuration(LinkDataExtractorTestBase.java:112)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testDuration(DwarkeshPodcastLinkContentParserTest.java:133)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testDuration(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkDuration(LinkDataExtractorTestBase.java:112)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testDuration(DwarkeshPodcastLinkContentParserTest.java:133)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testOtherLink(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testOtherLink(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkOtherLink(LinkDataExtractorTestBase.java:452)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testOtherLink(DwarkeshPodcastLinkContentParserTest.java:122)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testOtherLink(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkOtherLink(LinkDataExtractorTestBase.java:452)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testOtherLink(DwarkeshPodcastLinkContentParserTest.java:122)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testOtherLink(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/the-next-paradigm
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkOtherLink(LinkDataExtractorTestBase.java:452)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testOtherLink(DwarkeshPodcastLinkContentParserTest.java:122)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fthe-next-paradigm+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testOtherLink(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/the-sample-efficiency-black-hole
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkOtherLink(LinkDataExtractorTestBase.java:452)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testOtherLink(DwarkeshPodcastLinkContentParserTest.java:122)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fthe-sample-efficiency-black-hole+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testTitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testTitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkTitle(LinkDataExtractorTestBase.java:32)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testTitle(DwarkeshPodcastLinkContentParserTest.java:23)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testTitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkTitle(LinkDataExtractorTestBase.java:32)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testTitle(DwarkeshPodcastLinkContentParserTest.java:23)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testTitle(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/thoughts-on-ai-progress-dec-2025-video
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkTitle(LinkDataExtractorTestBase.java:32)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testTitle(DwarkeshPodcastLinkContentParserTest.java:23)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fthoughts-on-ai-progress-dec-2025-video+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

test1Author(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
test1Author(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/thoughts-on-ai-progress-dec-2025-video
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkAuthors(LinkDataExtractorTestBase.java:441)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.check1Author(LinkDataExtractorTestBase.java:143)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.test1Author(DwarkeshPodcastLinkContentParserTest.java:69)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 5 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Fthoughts-on-ai-progress-dec-2025-video+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 8 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 13 more

testPublicationDate(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
testPublicationDate(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/richard-sutton
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkPublicationDate(LinkDataExtractorTestBase.java:100)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testPublicationDate(DwarkeshPodcastLinkContentParserTest.java:56)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Frichard-sutton+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

testPublicationDate(fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://www.dwarkesh.com/p/elon-musk
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkPublicationDate(LinkDataExtractorTestBase.java:100)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.test.DwarkeshPodcastLinkContentParserTest.testPublicationDate(DwarkeshPodcastLinkContentParserTest.java:56)
Caused by: java.lang.RuntimeException: Error while invoking the constructor
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:503)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:487)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 4 more
Caused by: fr.mazure.homepagemanager.data.linkchecker.ContentParserException: Failed to parse answer of https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=1&q=https%3A%2F%2Fwww.dwarkesh.com%2Fp%2Felon-musk+%22Dwarkesh+Patel%22&key=AIzaSyA0E3d1F4j-pkGm41R2m-5CB41ZgQXo52E: 
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:49)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.dwarkeshpodcast.DwarkeshPodcastLinkContentParser.<init>(DwarkeshPodcastLinkContentParser.java:121)
	at java.base/jdk.internal.reflect.DirectConstructorHandleAccessor.newInstance(DirectConstructorHandleAccessor.java:62)
	at java.base/java.lang.reflect.Constructor.newInstanceWithCaller(Constructor.java:502)
	at java.base/java.lang.reflect.Constructor.newInstance(Constructor.java:486)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.construct(LinkDataExtractorTestBase.java:501)
	... 7 more
Caused by: org.json.JSONException: A JSONObject text must begin with '{' at 0 [character 1 line 1]
	at org.json@20251224/org.json.JSONTokener.syntaxError(JSONTokener.java:581)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:221)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:534)
	at org.json@20251224/org.json.JSONObject.<init>(JSONObject.java:516)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.YouTubeHelper.getVideoURL(YouTubeHelper.java:47)
	... 12 more

fr.mazure.homepagemanager.data.linkchecker.huggingface.test.HuggingFaceLinkContentParserTest
test4Authors(fr.mazure.homepagemanager.data.linkchecker.huggingface.test.HuggingFaceLinkContentParserTest)
test4Authors(fr.mazure.homepagemanager.data.linkchecker.huggingface.test.HuggingFaceLinkContentParserTest)
java.lang.IllegalStateException: Exception while retrieving https://huggingface.co/blog/llama2
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(LinkDataExtractorTestBase.java:484)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.checkAuthors(LinkDataExtractorTestBase.java:441)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.check4Authors(LinkDataExtractorTestBase.java:279)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.huggingface.test.HuggingFaceLinkContentParserTest.test4Authors(HuggingFaceLinkContentParserTest.java:58)
Caused by: org.opentest4j.AssertionFailedError: expected: <[▭ first=Philipp ▭ last=Schmid ▭ ▭ order=western, ▭ first=Omar ▭ last=Sanseviero ▭ ▭ order=western, ▭ first=Pedro ▭ last=Cuenca ▭ ▭ order=western, ▭ first=Lewis ▭ last=Tunstall ▭ ▭ order=western]> but was: <[]>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$8(LinkDataExtractorTestBase.java:445)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.lambda$11(LinkDataExtractorTestBase.java:488)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 5 more

fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest
dzoneIsManaged(fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest)
org.opentest4j.AssertionFailedError: expected: <<ARTICLE><X><T>Explore Annotations in Java 8</T><ST>Explore the evolution of annotations in Java 8 and how they are being used today!</ST><A>https://dzone.com/articles/explore-annotations-in-java-8</A><L>en</L><F>HTML</F></X><AUTHOR><FIRSTNAME>Niamul</FIRSTNAME><LASTNAME>Sanjavi</LASTNAME></AUTHOR><DATE><YEAR>2019</YEAR><MONTH>10</MONTH><DAY>22</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>> but was: <<ARTICLE><X><T>Explore Annotations in Java 8</T><ST>Explore the evolution of annotations in Java 8 and how they are being used today!</ST><A>https://dzone.com/articles/explore-annotations-in-java-8</A><L>en</L><F>HTML</F></X><DATE><YEAR>2019</YEAR><MONTH>10</MONTH><DAY>22</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest.dzoneIsManaged(LinkDataExtractorTest.java:117)

youtubeWatchTheMadPodcastWithMattTurckIsManaged(fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest)
org.opentest4j.AssertionFailedError: expected: <<ARTICLE><X><T>Anthropic’s Felix Rieseberg: Claude Cowork, Mythos, and the SaaS Extinction</T><A>https://www.youtube.com/watch?v=9MEJ4syOVrQ</A><L>en</L><F>MP4</F><DURATION><MINUTE>58</MINUTE><SECOND>8</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Turck</LASTNAME></AUTHOR><DATE><YEAR>2026</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>> but was: <<ARTICLE><X><T>Anthropic Engineering Lead: How AI Agents Replace Modern SaaS | Felix Rieseberg</T><A>https://www.youtube.com/watch?v=9MEJ4syOVrQ</A><L>en</L><F>MP4</F><DURATION><MINUTE>58</MINUTE><SECOND>8</SECOND></DURATION></X><AUTHOR><FIRSTNAME>Matt</FIRSTNAME><LASTNAME>Turck</LASTNAME></AUTHOR><DATE><YEAR>2026</YEAR><MONTH>4</MONTH><DAY>10</DAY></DATE><COMMENT>XXXXX</COMMENT></ARTICLE>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest.youtubeWatchTheMadPodcastWithMattTurckIsManaged(LinkDataExtractorTest.java:3775)

fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest
illegalCharactersInAuthorLastName(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The last name of author prefix=Prefix first=FirstName middle=MiddleName last=Last!Name suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The last name of author prefix=Prefix first=FirstName middle=MiddleName last=Last!Name suffix=NameSuffix given=GivenName order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorLastName(AuthorsCheckerTest.java:197)

wellKnownAuthorIsMissing(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The list of authors of article "https://lexfridman.com/grant-sanderson-2" (▭ first=Grant ▭ last=Sanderson ▭ ▭) does not contain the expected list for the site (▭ first=Lex ▭ last=Fridman ▭ ▭)<<IncorrectAuthorList>>
The list of authors of article "https://www.numberphile.com/podcast/ben-sparks" (▭ first=Ben ▭ last=Sparks ▭ ▭) does not contain the expected list for the site (▭ first=Brady ▭ last=Haran ▭ ▭)<<IncorrectAuthorList>>> but was: <The list of authors of article "https://lexfridman.com/grant-sanderson-2" (▭ first=Grant ▭ last=Sanderson ▭ ▭ order=western) does not contain the expected list for the site (▭ first=Lex ▭ last=Fridman ▭ ▭ order=western)<<IncorrectAuthorList>>
The list of authors of article "https://www.numberphile.com/podcast/ben-sparks" (▭ first=Ben ▭ last=Sparks ▭ ▭ order=western) does not contain the expected list for the site (▭ first=Brady ▭ last=Haran ▭ ▭ order=western)<<IncorrectAuthorList>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.wellKnownAuthorIsMissing(AuthorsCheckerTest.java:52)

illegalCharactersInAuthorFirstName(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The first name of author prefix=Prefix first=First;Name middle=MiddleName last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The first name of author prefix=Prefix first=First;Name middle=MiddleName last=LastName suffix=NameSuffix given=GivenName order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorFirstName(AuthorsCheckerTest.java:149)

illegalCharactersInAuthorMiddleName(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The middle name of author prefix=Prefix first=FirstName middle=Middle"Name last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The middle name of author prefix=Prefix first=FirstName middle=Middle"Name last=LastName suffix=NameSuffix given=GivenName order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorMiddleName(AuthorsCheckerTest.java:173)

illegalCharactersInAuthorNameSuffix(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The name suffix of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=Name&Suffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The name suffix of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=Name&Suffix given=GivenName order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorNameSuffix(AuthorsCheckerTest.java:221)

illegalCharactersInAuthorGivenName(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The given name of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=Given=Name contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The given name of author prefix=Prefix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=Given=Name order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorGivenName(AuthorsCheckerTest.java:245)

duplicatedAuthor(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The list of authors of article "https://www.youtube.com/watch?v=aW61yxnQvio" contains duplicated author: ▭ ▭ ▭ ▭ ▭ given=Tarsa<<DuplicatedAuthor>>> but was: <The list of authors of article "https://www.youtube.com/watch?v=aW61yxnQvio" contains duplicated author: ▭ ▭ ▭ ▭ ▭ given=Tarsa order=western<<DuplicatedAuthor>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.duplicatedAuthor(AuthorsCheckerTest.java:101)

wellKnownAuthorIsNotAlone(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The list of authors of article "https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile" (▭ first=Leah ▭ last=Stockley ▭ ▭;▭ first=Grant ▭ last=Sanderson ▭ ▭) is not equal to the expected list for the site (▭ first=Leah ▭ last=Stockley ▭ ▭)<<IncorrectAuthorList>>> but was: <The list of authors of article "https://www.inspiredtester.com/inspired-tester-blog/embracing-change-testing-to-agile" (▭ first=Leah ▭ last=Stockley ▭ ▭ order=western;▭ first=Grant ▭ last=Sanderson ▭ ▭ order=western) is not equal to the expected list for the site (▭ first=Leah ▭ last=Stockley ▭ ▭ order=western)<<IncorrectAuthorList>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.wellKnownAuthorIsNotAlone(AuthorsCheckerTest.java:77)

illegalCharactersInAuthorPrefixName(fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest)
org.opentest4j.AssertionFailedError: expected: <The name prefix of author prefix=Pre,fix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=GivenName contains an illegal character<<AuthorWithIllegalCharacters>>> but was: <The name prefix of author prefix=Pre,fix first=FirstName middle=MiddleName last=LastName suffix=NameSuffix given=GivenName order=western contains an illegal character<<AuthorWithIllegalCharacters>>>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.NodeValueCheckerTestBase.test(NodeValueCheckerTestBase.java:46)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.nodechecker.test.AuthorsCheckerTest.illegalCharactersInAuthorPrefixName(AuthorsCheckerTest.java:125)

fr.mazure.homepagemanager.utils.internet.test.HtmlHelperTest
testCharsetExtraction(fr.mazure.homepagemanager.utils.internet.test.HtmlHelperTest)
testCharsetExtraction(fr.mazure.homepagemanager.utils.internet.test.HtmlHelperTest)
java.lang.IllegalStateException: Exception while retrieving https://www.lemondeinformatique.fr/actualites/lire-log4j-une-autre-vulnerabilite-corrigee-par-apache-85265.html
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:77)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.CachedSiteDataRetriever.retrieve(CachedSiteDataRetriever.java:37)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.test.HtmlHelperTest.testCharsetExtraction(HtmlHelperTest.java:116)
Caused by: org.opentest4j.AssertionFailedError: expected: <ISO-8859-1> but was: <UTF-8>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.test.HtmlHelperTest.lambda$0(HtmlHelperTest.java:126)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:144)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieveInternal(SynchronousSiteDataRetriever.java:122)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.data.dataretriever.SynchronousSiteDataRetriever.retrieve(SynchronousSiteDataRetriever.java:75)
	... 2 more

fr.mazure.homepagemanager.utils.internet.test.Mp3HelperTest
testDuration(fr.mazure.homepagemanager.utils.internet.test.Mp3HelperTest)
testDuration(fr.mazure.homepagemanager.utils.internet.test.Mp3HelperTest)
org.opentest4j.AssertionFailedError: expected: <PT12S> but was: <PT13S>
	at org.junit.jupiter.api.Assertions.assertEquals(Assertions.java:1199)
	at fr.mazure.homepagemanager/fr.mazure.homepagemanager.utils.internet.test.Mp3HelperTest.testDuration(Mp3HelperTest.java:29)
```









manage

```
https://www.quantamagazine.org/what-is-the-positive-grassmannian-and-why-does-it-show-up-everywhere-20260625/
The list of effective authors is not the effective one.
The following authors are effectively present but are unexpected: ▭ ▭ ▭ ▭ ▭ given=Williams order=western
The following authors are expected but are effectively missing: ▭ first=Lauren ▭ last=Williams ▭ ▭ order=western
Expected 2 links (https://www.quantamagazine.org/what-is-the-positive-grassmannian-and-why-does-it-show-up-everywhere-20260625/, https://www.youtube.com/watch?v=iAaGDYz3f9k), but got 1 links (https://www.quantamagazine.org/what-is-the-positive-grassmannian-and-why-does-it-show-up-everywhere-20260625/)
```





find a way to proofread with Claude





clean up the structure of aigoogle.xml







add `https://refactoring.fm/podcast` for Lucas Rossi's podcasts






structure the aiopenai.xml page, at least by having a "models" group



 

use https://earendil.com/posts/subscribe/ as a test to differentiate RSS vs. Atom



many Americanishes of abbreven.xml were not detected





understand the bad color of
```
#> git config set --local user.name Laurent
  #> git config set --local user.email laurent@gilgamesh
```




`<PROMPT/>`is missing for a lot of `<CODESAMPLE language="bash">` cases




manage

```
 tag = "TITLE"
 value = "shot-scraper"
 violation = "a TITLE must start with an uppercase letter"
 detail = "TITLE "shot-scraper" must start with an uppercase"
```






manage

```
https://stackoverflow.blog/2024/11/05/how-google-is-helping-developers-get-better-answers-from-ai/
The list of formats is not the expected one.
expected formats: [MP3]
effective formats: [HTML]
```




all link/javanews.xml entries should be https://inside.java/ + youtube (or maybe not since https://inside.java/ contains no information)







add parser for `https://inside.java/2026/08/20/podcast-067/`










gérer

```
Title = "Be Careful Where you Send Your Code"
URL = https://windsurf.com/blog/self-hosting-for-code-security
Expected status = ZOMBIE
Effective HTTP code = 307 Temporary Redirect
Effective HTTP code of last redirection = 200 OK
Redirection chain = https://windsurf.com/blog/self-hosting-for-code-security → https://windsurf.com/not-found
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses =
Look for article = https://www.google.com/search?q=%22Be+Careful+Where+you+Send+Your+Code%22
```










regenerate the stats




## To do 



repair unit tests
- fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test.WellKnownRedirectionsTest.oracleBlog(String)
- fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test.WellKnownRedirectionsTest.redirectionsEndingWith404(String)
- fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test.WellKnownRedirectionsTest.redirectionsToLocale(String)
- fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest.oracleBlogsJavaIsManaged()
- fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTest.oracleBlogsJavaMagazineIsManaged()



fix TypeScript warnings






support of Eastern author names
- generate an error if the order is not always the same for a given author 






- GIVENNAME is very misleading since it can mean firstname






add a rule to not mix `-` and `—`



rewrite the parsing of MP3 files






reindex the first articles of https://martinfowler.com/articles/exploring-gen-ai.html












Since more and more sites require throttling, we should use virtual threads and run more of them in parallel.




See how to look for the disappeared articles in webarchive.







check with Claude and this prompt:

```
Proofread the comments (not the titles) in this list of media. Report the errors. Do not write a corrected version of the text.
```










manage

```

Title = "Software Peer Reviews: An Executive Overview with Karl Wiegers"
URL = https://www2.smartbear.com/Wiegers-Webinar-OnDemand.html
Expected status = ZOMBIE
Effective HTTP code = 302 Found
Effective HTTP code of last redirection = 200 OK
Redirection chain = https://www2.smartbear.com/Wiegers-Webinar-OnDemand.html → https://smartbear.com
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Software+Peer+Reviews%3A+An+Executive+Overview+with+Karl+Wiegers%22
```

```
Title = "Always Fix Broken Windows"
URL = https://smartbear.com/blog/always-fix-broken-windows/
Expected status = OK
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = https://smartbear.com/blog/always-fix-broken-windows/ → https://smartbear.com/blog/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Always+Fix+Broken+Windows%22
```





manage

```
https://blog.kilo.ai/p/23-events
The expected date 2026-07-03 is not equal to the effective date 2026-07-02
```













manage refactoring.fm












detect sentence should start with a capital letter







add global check that a URL does not appear twice










indiquer que des liens sont internes (par exemple cursor et windsurf sur la page codeass)

















add missing comments




 
 


verify that I do not read twice the MP3 files










Why 300 threads are created when putting a breakpoint in fr.mazure.homepagemanager.utils.internet.HttpHelper.throttle(String)???





We should be able to get rid of IbmLinkContentChecker.isUrlManaged and use the parser method instead  
but IBM is currently down…




understand why the fixes add a ^M at the end of the line








Manage Martin Fowler dates on links/design.xml








check the bookmarks of https://twimlai.com/podcast/twimlai/  
some YouTube videos are missing  






understand why report files are now displayed in the Browser rather than in Notepad++






author popup should also indicate articles for which the name appears in the comment








avoid empty line warning in <CODESAMPLE>





syntax hightlighting does not work in a table, see /notes/java.html







 
repare YoutubeChannelUserLinkContentParser






check the dates of https://oxide-and-friends.transistor.fm/episodes/shell-game-with-evan-ratliff






gérer

```
WARNING: A restricted method in java.lang.System has been called
WARNING: java.lang.System::load has been called by com.sun.glass.utils.NativeLibLoader in module javafx.graphics (file:/G:/Documents/home/repository/org/openjfx/javafx-graphics/26-ea+3/javafx-graphics-26-ea+3-win.jar)
WARNING: Use --enable-native-access=javafx.graphics to avoid a warning for callers in this module
WARNING: Restricted methods will be blocked in a future release unless native access is enabled
```







the articles of fr.mazure.homepagemanager.data.linkchecker.ibm.test.IbmLinkContentParserTest.testSubtitle(String, String) have been rewritten, add test for checking (old and new) authors












→ write a skill to facilitate the generation of parsers?









manage  
https://windsurf.com/blog/windsurf-wave-13  
Subtitle "Wave 13: Shipmas - Parallel Agents, Git Worktrees, and Near-Frontier Coding Performance For Free With SWE-1.5" does not appear in the page  
The problem is that the whole page content is generated by JavaScript









 


public abstract List<ExtractedLinkData> getLinks() throws ContentParserException; is a kludge  
no other method of LinkDataExtractor throws an exception  
at the same time, fix: "public abstract List<ExtractedLinkData> getLinks() throws ContentParserException;"









manage  
https://gwern.net/ai-daydreaming  
author "Branwen" does not appear in the page







réparer TechRepublic







analyze if I could exploit webarchives.org










find a heuristic for Veritasium authors  
youtubeWatchVeritasiumCasperMebiusIsManaged is currently disabled







fix author extraction for https://www.youtube.com/watch?v=DhZORrqL3xI







mettre à jour les dates de baeldung dans links/java.xml  
then clean up
- links/java.xml
- links/javabasics.xml
- links/javaui.xml
- links/javamt.xml









understand why cookies must be switched on for Oracle blog















ajouter une assertion sur le code HTTP dans fr.mazure.homepagemanager.data.linkchecker.test.LinkDataExtractorTestBase.perform(Class<? extends LinkDataExtractor>, String, Consumer<LinkDataExtractor>)











merge les deux pages sur OpenAI








manque des tooltips sur les flèches de renvoi et sur la boussole









gérer

```
 tag = "COMMENT"
 value = ""Flow Matching for Generative Modelinghttps://arxiv.org/abs/2210.02747enHTML": a mathematical description of Flow Matching, a mechanism to train Continuous Normalizing Flows."
 violation = "a COMMENT must not contain US syntax"
 detail = "COMMENT ""Flow Matching for Generative Modelinghttps://arxiv.org/abs/2210.02747enHTML": a mathematical description of Flow Matching, a mechanism to train Continuous Normalizing Flows." contains american word " Normalizing"  matching regexp "\W\p{L}{2,}izing", it should be "sing""
```







corriger les liens d'Aunt Minnie











the duration is incorrect for https://lexfridman.com/deepseek-dylan-patel-nathan-lambert






gérer Redirection chain = https://www.rando-hauteloire.fr/ → https://www.rando-hauteloire.fr/?lang=en



 

nettoyer space.xml : space exploration vs space telescope






gérer les spans chaotiques qui empêchent d'extraire les authors de Chrome blog








mauvaise date pour https://developer.ibm.com/articles/j-java-streams-5-brian-goetz/, 5 articles qui ont été réécrits  
idem pour https://developer.ibm.com/articles/j-java-streams-1-brian-goetz/







manage  
```
https://www.youtube.com/watch?v=EvknN89JoWo  
expected duration PT22M34S is not in the real duration interval [PT596H31M23S,PT-596H-31M-22S]
```





the reviewer of https://www.baeldung.com/java-linked-hashmap is not extracted






publication date smaller or equal to creation date must be detected








podcast  
gérer LWAIM








implémenter quick fix de missing link











détecter les liens arxiv mal formattés






comprendre pourquoi la langue de https://www.youtube.com/watch?v=vGP4pQdCocw n'est pas reconnue








détecter les indentations incorrectes






détecter les vidéos Youtube qui sont marquées "zombie" mais qui ne sont, en fait, pas private







comprendre pourquoi les fichiers sont regénés au démarrage









gCérer en ajountant une nouvelle weel-known redirection

```
Title = "Companies determined to retain 'secret sauce'"
URL = http://news.com.com/2009-1022_3-5198605.html
Expected status = ZOMBIE
Effective HTTP code = 302 Found
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://news.com.com/2009-1022_3-5198605.html → https://gen.xyz/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Companies+determined+to+retain+%27secret+sauce%27%22
```

```
Title = "How India is handling international backlash"
URL = http://news.com.com/2009-1022_3-5198950.html
Expected status = ZOMBIE
Effective HTTP code = 302 Found
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://news.com.com/2009-1022_3-5198950.html → https://gen.xyz/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22How+India+is+handling+international+backlash%22
```

```
Title = "The next battlefields for advanced technology"
URL = http://news.com.com/2009-1022_3-5198957.html
Expected status = ZOMBIE
Effective HTTP code = 302 Found
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://news.com.com/2009-1022_3-5198957.html → https://gen.xyz/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
```









repair failing unit tests of fr.mazure.homepagemanager.data.linkchecker.linkstatusanalyzer.test.WellKnownRedirectionsTest.channel9(String)







gérer

```
    public static Path getTestDatapath(final Class<?> clazz) {
        return Paths.get("G:\\Documents\\tmp\\hptmp\\test\\" + clazz.getSimpleName());  // TODO this should not be hard-coded
    }
```







nettoyer links/design.xml







gérer https://www.superdatascience.com/podcast/





gérer

```
https://stackoverflow.blog/2024/08/15/practical-tips-for-retrieval-augmented-generation-rag/
The list of effective authors is not the effective one.
The following authors are effectively present but are unexpected: ▭ first=Cameron middle=R. last=Wolfe suffix=PhD ▭
The following authors are expected but are effectively missing: ▭ first=Cameron middle=R. last=Wolfe ▭ ▭
```







gérer

```
Title = "The SMAQ stack for big data"
Subtitle = "Storage, MapReduce and Query are ushering in data-driven products and services."
URL = http://radar.oreilly.com/2010/09/the-smaq-stack-for-big-data.html
Expected status = OK
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://radar.oreilly.com/2010/09/the-smaq-stack-for-big-data.html → https://www.oreilly.com/radar/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22The+SMAQ+stack+for+big+data%22+%22Storage%2C+MapReduce+and+Query+are+ushering+in+data-driven+products+and+services.%22
```







détecter les articles avec plusieurs liens qui n'ont pas les mêmes qualités, obsolescence, voir autre chose





corriger le fenêtre de création de XML qui ne s'aggrandit pas



gérer

```
Title = "Single-User Subversion"
URL = http://www.onlamp.com/pub/a/onlamp/2002/10/31/subversion.html
Expected status = REMOVED
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://www.onlamp.com/pub/a/onlamp/2002/10/31/subversion.html → https://www.oreilly.com/ideas/ → https://www.oreilly.com/radar/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Single-User+Subversion%22
```

```
Title = "The New Breed of Version Control Systems"
URL = http://www.onlamp.com/pub/a/onlamp/2004/01/29/scm_overview.html
Expected status = REMOVED
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://www.onlamp.com/pub/a/onlamp/2004/01/29/scm_overview.html → https://www.oreilly.com/ideas/ → https://www.oreilly.com/radar/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22The+New+Breed+of+Version+Control+Systems%22
```







The cleaning of https://blogs.oracle.com/javamagazine/post/java-quiz-anonymous-classes?source=:so:tw:or:awr:jav:::&SC=:so:tw:or:awr:jav:::&pcode= does not work.




gérer

```
Title = "Synchronizing Networks with NTP"
URL = http://www.linuxdevcenter.com/pub/a/linux/2003/01/02/ntp.html
Expected status = REMOVED
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://www.linuxdevcenter.com/pub/a/linux/2003/01/02/ntp.html b�� https://www.oreilly.com/ideas/ → https://www.oreilly.com/radar/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Synchronizing+Networks+with+NTP%22
```









nettoyer links/chrome.xml




la navigation sur la page des authors ne marche plus





nettoyer links/windows.xml


 


le cancel des checks en cours en cas de nouveau save n'a plus l'air de marcher






gérer

```
Title = "Reusing Exceptions"
URL = http://java.sun.com/developer/JDCTechTips/2003/tt0422.html#2
Expected status = REMOVED
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://java.sun.com/developer/JDCTechTips/2003/tt0422.html#2 → https://java.sun.com/developer/JDCTechTips/2003/tt0422.html → http://www.oracle.com/technetwork/java/index.html → http://www.oracle.com/java/technologies/?er=221886 → https://www.oracle.com/java/technologies/?er=221886
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22Reusing+Exceptions%22
```

```
Title = "When are two strings equal?"
URL = http://java.sun.com/developer/JDCTechTips/2004/tt0504.html#2
Expected status = REMOVED
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = http://java.sun.com/developer/JDCTechTips/2004/tt0504.html#2 → https://java.sun.com/developer/JDCTechTips/2004/tt0504.html → http://www.oracle.com/technetwork/java/index.html → http://www.oracle.com/java/technologies/?er=221886 � https://www.oracle.com/java/technologies/?er=221886
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22When+are+two+strings+equal%3F%22
```






bug : le titre de la fenêtre des authors ne devrait pas être un lien






extraction des deux auteurs de https://generatingconversation.substack.com/p/you-cant-build-a-moat-with-ai?utm_source=pocket_mylist




nettoyer links/boardgame.xml



voir comment exploiter WellKnownAuthors dans la génération XML




gérer le problème de langue sur youtube, notamment links/games.xml




gérer

```
 tag = "TITLE"
 value = "blackpenredpen (SteveChow)"
 violation = "a TITLE must start with an uppercase letter"
 detail = "TITLE "blackpenredpen (SteveChow)" must start with an uppercase"
```





gérer

```
https://blogs.oracle.com/javamagazine/post/java-switch-primitive-types
The list of effective authors is not the effective one.
The following authors are effectively present but are unexpected: ▭ first=Mikalai middle=Zaikin,Simon last=Roberts ▭ ▭
The following authors are expected but are effectively missing: ▭ first=Mikalai ▭ last=Zaikin ▭ ▭,▭ first=Simon ▭ last=Roberts ▭ ▭
```

```
https://blogs.oracle.com/javamagazine/post/java-text-block-string-indent-whitespace
subtitle "Text blocks ease programming long string literals, but you need to know the details." should not be present
The list of effective authors is not the effective one.
The following authors are effectively present but are unexpected: ▭ first=Mikalai middle=Zaikin,Simon last=Roberts ▭ ��
The following authors are expected but are effectively missing: ▭ first=Mikalai ▭ last=Zaikin ▭ ▭,▭ first=Simon �- last=Roberts ▭ ▭
```



nettoyer links:Java.xml




nettoyer livingbeing.xml





https://www.nextinpact.com/article/70628/a-decouverte-jupyter-logiciel-scientifique-libre-aux-millions-dutilisateurs  
title "À la découverte de Jupyter, un logiciel scientifique libre aux millions d'utilisateurs" does not appear in the page

 

 
 gérer

```
         if (url.startsWith("https://netflixtechblog.com/")) {
            return true;
        }
```




corriger affichage du popup author sur téléphone


mettre une scrollbar dans le dialogue de génération du XML
en fait le problème est présent quand on utilise le bouton "Paste URL"



gérer

```
Title = "How Do I: Use Visual Studio 2008 Code Metrics to Evaluate Code Complexity?"
URL = https://msdn.microsoft.com/en-us/vstudio/bb892758
Expected status = ZOMBIE
Effective HTTP code = 301 Moved Permanently
Effective HTTP code of last redirection = 200 OK
Redirection chain = https://msdn.microsoft.com/en-us/vstudio/bb892758 → https://learn.microsoft.com → https://learn.microsoft.com/en-us/
Redirection matcher = redirection ending in success (last URL should be used)
Redirection matcher expected statuses = 
Look for article = https://www.google.com/search?q=%22How+Do+I%3A+Use+Visual+Studio+2008+Code+Metrics+to+Evaluate+Code+Complexity%3F%22
```



gérer les vidéo youtube mises à privé  
pex https://www.youtube.com/watch?v=OW1J61jg-3U  
les mettre en removed


afficher les métadonnées des articles dans les tables



netoyer build.xml


nettoyer javahistory.xml


corriger problème de reconnaissance des langues sur YouTube

réimplémenter toute la logique twitter: il répond désormais 200 même si le compte n'existe pas

nettoyer test.xml

nettoyer coding.xml

indiquer tous les HTTP codes de redirection et en déduire quand la correction de l'URL devrait aussi �*tre proposée

corriger les liens de fda.org

le début du nom des fichiers ne devrait par C�tre présent

nettoyer tous les liens auntminnie

problème de date avec https://developer.ibm.com/articles/os-developers-know-rust/

il manque le sous-titre pour https://stackoverflow.blog/2022/09/08/this-is-not-your-grandfathers-perl/

faire que java.xml soit propre

nettoyer links/job.xml

certaines des chaines listées dans IncorrectSpaceChecker sons suspectes

corriger le fait qu'on reporte une erreur de type WrongLiveness et qu'on propose une correction de l'URL en cas de redirection

les artciles de https://www.nytimes.com/ devraient être en payed_registration

bring back main method coverage to 53.0%

tester les LinkContentChecker

remettre en marche le test testSimultaneousRetrieval

gestion des podcasts Quanta Magazine

voir gestion des redirections infinies de https://www.washingtonpost.com/health/amid-threats-and-political-pushback-public-health-officials-leaving-posts/2020/06/22/6075f7a2-b0cf-11ea-856d-5054296735e5_story.html

gérer

```
https://www.youtube.com/watch?v=b3cTYn6_2sY
language is "en" but this one is unexpected, the expected languages are: fr
https://www.youtube.com/watch?v=laty3vXKRek
language is "ko" but this one is unexpected, the expected languages are: en
```

crash sur URL tps://lekitblob.fr/en/blob-main-page (il manque les deux premier caractères)

tout régénerer → il ne doit y avoir aucun crash


gérer sur perso/main.xml

```
line 0: the local link "../content/map.html?page=links/life.html" has an invalid value
line 0: the local link "../content/map.html?page=notes/eclipse.html" has an invalid value
line 0: the local link "../content/map.html?page=bouffe/095.htm" has an invalid value
line 0: the local link "../content/content.html?sort=author" has an invalid value
```


nettoyer links/chrome.xml

réparer testSimultaneousRetrieval

chaque redirection matcher recompile la chaine de redirections, ce n'est pas performant
en plus cela est recalculé plusieurs fois dans LinkCheckRunner.isDataExpected

si le programme passe minuit, la vérification de la date de la page croit toujours que nous sommes la veille

```
Title = "ChatGPT"
URL = https://chat.openai.com
Expected status = 
Effective error = "Failed to get input stream: java.io.IOException: Server returned HTTP response code: 403 for URL: https://chat.openai.com"
Effective HTTP code = 403 Forbidden
Look for article = https://www.google.com/search?q=%22ChatGPT%22

Title = "How ChatGPT Hijacks Democracy"
URL = https://www.nytimes.com/2023/01/15/opinion/ai-chatgpt-lobbying-democracy.html
Expected status = 
Effective error = "Failed to get input stream: java.io.IOException: Server returned HTTP response code: 403 for URL: https://www.nytimes.com/2023/01/15/opinion/ai-chatgpt-lobbying-democracy.html"
Effective HTTP code = 403 Forbidden
Look for article = https://www.google.com/search?q=%22How+ChatGPT+Hijacks+Democracy%22
```

fix height of HTML table cells
- → I believe it does not work when we change the text of the cell
- → analyse the differents height, the current cut 'n paste of StachOverflow, doing a max of five heights, is stupid

scrolling with the mouse does not work above the HTML column

gérer les faux sous-titres tels que https://stackoverflow.blog/2017/06/15/developers-use-spaces-make-money-use-tabs/

possible next quick fixes
- space/casing problem

le title récupéré n'est pas bon pour https://stackoverflow.blog/2021/07/14/getting-started-with-python/  
https://stackoverflow.blog/2021/03/15/getting-started-with-rust/  
title "Getting started with … Rust" is not equal to the real title "Getting started with ... Rust"

les CODEROUTINE sont écrits trop bas dans le HTML généré

dans la table, on ne devrait afficher que le nom des fichiers à partir de la racine de la homepage

corriger

```
janv. 28, 2023 7:22:00 AM com.sun.javafx.application.PlatformImpl startup
WARNING: Unsupported JavaFX configuration: classes were loaded from 'unnamed module @795df74'
```
voir https://stackoverflow.com/questions/67854139/javafx-warning-unsupported-javafx-configuration-classes-were-loaded-from-unna

le process continue de tourner

upgrader google-api-services-youtube

Homepage Taper esc devrait fermer la recherche

ne pas tout reconstruire à partir du moment où un seul des fichiers résultats n'est plus à jour

mettre en place https://developer.mozilla.org/en-US/docs/Web/HTML/Element/details

spin off de la page
- dev tools
- + mettre IDE en dessous de dev tools

nettoyer notes/csharp.xml

voir comment assurer que testdata/css/schema.xsd est à jour (tester que c'est le même que dans mazure.fr ?)

gérer

```
 tag = "COMMENT" value = "Cocktail Games at Berlin for the Spiel des Jahres, 13 Mots, Washington Z.C., Cache Ton Cash, Dinosaur Island - Rawr 'n Write, Ticket to Ride - San Fransisco, the games at Place de la République, and Le Grenier Ludique." violation = "space is missing after punctuation" detail = ""Cocktail Games at Berlin for the Spiel des Jahres, 13 Mots, Washington Z.C., Cache Ton Cash, Dinosaur Island - Rawr 'n Write, Ticket to Ride - San Fransisco, the games at Place de la République, and Le Grenier Ludique." is missing a space after punctuation"
```

```
- https://blogs.oracle.com/javamagazine/java-lists-view-unmodifiable-immutable
The expected publication date 2022-06-27 is not equal to the effective date 2022-09-13
```

```
- https://blogs.oracle.com/javamagazine/java-module-definition-export-packages
subtitle "How do you provide access to a module�s classes to code outside that module?" should not be present
```


- check that Baeldung article should have no publication date
- the same for GitLab blog and probably other ones
- the best is probably to define a white list of the few having a publication date
- (and document this creation/publication date somewhere)
- on the other side, IBM must have a creation date

cacher les requêtes à Oracle, elles sont trop lentes

ajouter support de https://blogs.oracle.com/java/post/faster-and-easier-use-and-redistribution-of-java-se

les podcasts de quanta magazine devraient être considérés comme des MP3, pas comme des HTML

modifier la gestion  de DeepSkyVideo

éviter les erreurs sur les wellknown authors

nettoyer
- abbreven.xml
- abbrevfr.xml
- chrome.xml
- links/firefox.xml et autres pages de browser

les tests ne devraient pas hardcoder H:

détecter la présence du givenname sur une page

détecter les <BR/> dans un comment  
Flaky tests must be fixed, some causes why tests are randomly failing  
The author suggests a five steps process to manage the technical debt:  
Some design patterns:<BR/>

vérifier l'ordre des attributs XML

- appliquer https://www.quora.com/Where-does-a-nickname-go-when-writing-a-persons-full-name
- vérifier le bon type d'apostrophe pour person's object
- mettre GIVENNAME entre double quote côté JS

von neumman

comment gérer les changements de nom Barb Palser -> Barb Smith

- lever une alerte si un lien est zombie mais que titre ou le sous-titre est encore présents

regarder les problèmes de titres tels que
- https://martinfowler.com/articles/agileOffshore.html
- https://martinfowler.com/bliki/UseCasesAndStories.html

vérifier titre et sous-titres dans PDF
à tester sur links/javascrp.xml

gérer les newlines dans les titres pex http://hesketh.com/publications/inclusive_web_design_for_the_future/

mettre des tooltips sur les symboles de KEY

perso/weblog.htm n'est plus référenc�)

vérifier que tous mes fichiers (sauf *.html) sont référencés

implémenter un système générique de cache

ranger les fichiers
- design.xml
- coding.xml

mettre un test unitaire pour vérifier que tous les ElementTypes apparaissent quelque part

trier par défaut la liste des problèmes sur la date de dernière modification

mauvaise détection de langue :
_ http://wiki.c2.com/?AbcMetric
- fr au lieu de en
  - https://groups.google.com/forum/#!forum/clean-code-discussion
  - https://root.cern.ch/TaligentDocs/TaligentOnline/DocumentRoot/1.0/Home/index.html

faire un prefetch de la map quand on affiche la page d'accueil

le contrôle
```
https://www.youtube.com/watch?v=TSnsHW6DS6s
YouTube link with no creation date
```
est incorrect ?  
il est normal de pouvoir avoir uniquement une date de publication ?

forcer la mise à jour des caches avant de modifier les fichiers JS de nouveau

nettoyage de bbst.xml

problème des langues
- pas de langue pour http://download.eclipse.org/efxclipse/updates-released/3.0.0/site/

vérification des liens internes

la sauvegarde du rapport de liens toutes les 30 secondes ne marche plus

Chrome indique qu'il n'arrive pas à charger les sourcemaps

supprimer la directory attic  
de plus cela génère des erreurs
```
                   Thread-3 | 20200416T073001.802 | ERROR | URL ../attic/optimtut1.ps.gz is not checked because the URL is malformed
                   Thread-3 | 20200416T073001.993 | ERROR | URL ../attic/tgr_softwareremodeling.pdf is not checked because the URL is malformed
```

private boolean isDataExpected() { //TBD this method is very stupid, we should used a flag instead of computing the status every time

corriger problème de certificat

bug dans HTMLGenerator.java, si le fichier XSLT est modifié, la nouvelle version n'est pas prise en compte

LinkedIn renvoie un HTTP code 999

problème avec le lien http://marie.desplats.free.fr/diagora_home_page.html

Tester robot.txt avec https://www.google.com/webmasters/tools/robots-testing-tool

Ajouter la détection des malwares

Ajouter la règle sur les URLs qui apparaissent plusieurs fois

Accelérer la création des fichier JSON (en extrayant les données de chaque fichier à la modification de celui-ci, en mettant cela dans un fichier de résumé et en ayant la création des JSON qui ne fasse que consolider ces fichiers)

mettre un tooltip sur les expand/contract

remplacer les

```
<script language="JavaScript" src="../scripts/common.js"></script>
```

par

```
<script src="../scripts/common.js"  type="module"></script>
```

corriger le support des MIDDLENAMEs multiples (Boyter dans links/humor.xml)

persons.xml :
- corriger les liens cassés
- restructurer la page pour séparer
  * twitter
  * LinkedIn
  * homepage
  * Wikipedia
  * Wikipédia

tronquer les URLs trop longs dans les tables de contenu

rendre le tri des articles complétement défini et mettre une assertion pour crasher s'il ne l'est pas
* titre
* sous-titre
* date
* premier auteur
