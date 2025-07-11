package com.byquan2004.tools;

import com.alibaba.fastjson2.JSONObject;
import com.byquan2004.config.OpenAPIConfig;
import com.byquan2004.model.SearchResult;
import com.byquan2004.model.Story;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author byquan2004
 */
@Component
public class HotToolService {

    private final RestTemplate restTemplate;
    private final OpenAPIConfig config;

    static {
        new HttpHeaders().set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
    }

    private HotToolService(RestTemplate restTemplate, OpenAPIConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }
    @Tool(name = "get_goddess_image",description = """
        Get a random goddess picture, without parameters, and return the picture link URL.
    """
    )
    public JSONObject getGoddessImage() {
        ResponseEntity<String> response = restTemplate.exchange(config.getUrl2(), HttpMethod.GET, null, String.class);
        return JSONObject.parse(response.getBody());
    }
    @Tool(name = "get_goddess_video",description = """
        Get the url access link of high-quality young lady related content video.
    """
    )
    public JSONObject getGoddessVideo() {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl3(), HttpMethod.GET, null, String.class);
        return JSONObject.parse(response.getBody());
    }
    @Tool(name = "get_news",description = """
       Get news content from various sources collected by Inshorts app.
    """
    )
    public JSONObject getNews(
            @ToolParam(description = "category options [all/national/business/sports/world/politics/technology/startup/entertainment/miscellaneous/hatke/science/automobile]")
                              String category) {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl4()+"?category="+category,
                HttpMethod.GET,
                null,
                String.class
        );
        return JSONObject.parse(response.getBody());
    }

    @Tool(name = "get_city_weather",description = """
        Get city information content.
    """
    )
    public JSONObject getWeatherByCityName(@ToolParam(description = "city name") String city) {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl5()+"/"+city,
                HttpMethod.GET,
                null,
                String.class
        );
        return JSONObject.parse(response.getBody());
    }
    @Tool(name = "get_ip_info",description = """
        Get ip information content.
    """
    )
    public JSONObject getIpInfo(@ToolParam(description = "ip v4 address") String ip) {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl6()+"/"+ip,
                HttpMethod.GET,
                null,
                String.class
        );
        return JSONObject.parse(response.getBody());
    }
    @Tool(name = "baidu_web_search",description = "Search the web using Baidu (no API key required)")
    public List<SearchResult> baiduSearch(
            @ToolParam(description = "Search query") String query,
            @ToolParam(description = "Maximum number of results to return (default: 5)") Integer limit) {

        if (limit == null || limit < 1) {
            limit = 5;
        }
        if (limit > 10) {
            limit = 10;
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://www.baidu.com/s")
                    .queryParam("wd", query)
                    .build()
                    .toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String html = response.getBody();

            return parseSearchResults(html, limit);
        } catch (Exception e) {
            throw new RuntimeException("Baidu search error: " + e.getMessage(), e);
        }
    }
    @Tool(name = "get_hacker_news", description = "Get stories from Hacker News")
    public List<Story> getStories(
            @ToolParam(description = "Type of stories to fetch (top, new, ask, show, jobs)") String type,
            @ToolParam(description = "Number of stories to return (max 30)") Integer limit) {

        List<String> validStoryTypes = Arrays.asList("top", "new", "ask", "show", "jobs");
        // Validate and set defaults
        if (type == null || !validStoryTypes.contains(type)) {
            type = "top";
        }

        if (limit == null || limit < 1) {
            limit = 10;
        }
        if (limit > 30) {
            limit = 30;
        }

        try {
            List<Story> stories = fetchStories(type);
            return stories.subList(0, Math.min(limit, stories.size()));
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch Hacker News stories: " + e.getMessage(), e);
        }
    }
    @Tool(name = "google_web_search", description = "Search the web using Google (no API key required)")
    public List<SearchResult> search(
            @ToolParam(description = "Search query") String query,
            @ToolParam(description = "Maximum number of results to return (default: 5)") Integer limit) {

        if (limit == null || limit < 1) {
            limit = 5;
        }
        if (limit > 10) {
            limit = 10;
        }

        try {
            String url = UriComponentsBuilder.fromHttpUrl("https://www.google.com/search")
                    .queryParam("q", query)
                    .build()
                    .toUriString();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            String html = response.getBody();

            return parseSearchResults(html, limit);
        } catch (Exception e) {
            throw new RuntimeException("Search error: " + e.getMessage(), e);
        }
    }

    private List<SearchResult> parseSearchResults(String html, int limit) {
        List<SearchResult> results = new ArrayList<>();

        // 提取搜索结果，使用正则表达式
        // 百度搜索结果的HTML结构可能需要根据实际情况调整
        Pattern titlePattern = Pattern.compile("<h3 class=\"[^\"]*\">\\s*<a[^>]*>(.*?)</a>\\s*</h3>");
        Pattern urlPattern = Pattern.compile("<a[^>]*href=\"(http[s]?://[^\"]+)\"[^>]*class=\"[^\"]*c-showurl[^\"]*\"");
        Pattern descPattern = Pattern.compile("<div class=\"c-abstract\"[^>]*>(.*?)</div>");

        Matcher titleMatcher = titlePattern.matcher(html);
        Matcher urlMatcher = urlPattern.matcher(html);
        Matcher descMatcher = descPattern.matcher(html);

        int count = 0;
        while (titleMatcher.find() && count < limit) {
            String title = cleanHtml(titleMatcher.group(1));

            // 尝试找到对应的URL和描述
            String url = "";
            if (urlMatcher.find()) {
                url = urlMatcher.group(1);
            }

            String description = "";
            if (descMatcher.find()) {
                description = cleanHtml(descMatcher.group(1));
            }

            results.add(new SearchResult(title, url, description));
            count++;
        }

        return results;
    }
    private String cleanHtml(String html) {
        return html.replaceAll("<[^>]+>", "").trim();
    }
    private List<Story> fetchStories(String type) {
        String baseUrl = config.getUrl7();
        String url = type.equals("top") ? baseUrl : baseUrl + "/" + type;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String html = response.getBody();

        List<Story> stories = new ArrayList<>();

        // 解析HTML以提取故事信息
        // 匹配故事行
        Pattern storyPattern = Pattern.compile("<tr class='athing' id='(\\d+)'>(.*?)</tr>\\s*<tr>(.*?)</tr>", Pattern.DOTALL);
        Matcher storyMatcher = storyPattern.matcher(html);

        while (storyMatcher.find()) {
            String storyId = storyMatcher.group(1);
            String titleRow = storyMatcher.group(2);
            String metadataRow = storyMatcher.group(3);

            // 提取标题和URL
            Pattern titlePattern = Pattern.compile("<a[^>]*class=\"titlelink\"[^>]*>(.*?)</a>");
            Pattern urlPattern = Pattern.compile("<a[^>]*class=\"titlelink\"[^>]*href=\"([^\"]+)\"");
            Pattern rankPattern = Pattern.compile("<span class=\"rank\">(\\d+)\\.</span>");

            Matcher titleMatcher = titlePattern.matcher(titleRow);
            Matcher urlMatcher = urlPattern.matcher(titleRow);
            Matcher rankMatcher = rankPattern.matcher(titleRow);

            String title = "";
            url = "";
            int rank = 0;

            if (titleMatcher.find()) {
                title = cleanHtml(titleMatcher.group(1));
            }

            if (urlMatcher.find()) {
                url = urlMatcher.group(1);
                // 处理相对URL
                if (url.startsWith("item?id=")) {
                    url = baseUrl + "/" + url;
                }
            }

            if (rankMatcher.find()) {
                rank = Integer.parseInt(rankMatcher.group(1));
            }

            // 提取元数据
            Pattern pointsPattern = Pattern.compile("(\\d+) points");
            Pattern authorPattern = Pattern.compile("user\\?id=([^\"&]+)");
            Pattern timePattern = Pattern.compile("<span class=\"age\"[^>]*title=\"([^\"]+)\"");
            Pattern commentPattern = Pattern.compile("(\\d+)&nbsp;comments?");

            Matcher pointsMatcher = pointsPattern.matcher(metadataRow);
            Matcher authorMatcher = authorPattern.matcher(metadataRow);
            Matcher timeMatcher = timePattern.matcher(metadataRow);
            Matcher commentMatcher = commentPattern.matcher(metadataRow);

            int points = 0;
            String author = "";
            String time = "";
            int commentCount = 0;

            if (pointsMatcher.find()) {
                points = Integer.parseInt(pointsMatcher.group(1));
            }

            if (authorMatcher.find()) {
                author = authorMatcher.group(1);
            }

            if (timeMatcher.find()) {
                time = timeMatcher.group(1);
            }

            if (commentMatcher.find()) {
                commentCount = Integer.parseInt(commentMatcher.group(1));
            }

            Story story = new Story(title, url, points, author, time, commentCount, rank);
            stories.add(story);
        }

        return stories;
    }

}
