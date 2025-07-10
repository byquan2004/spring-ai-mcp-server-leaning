package com.byquan2004.tools;

import com.alibaba.fastjson2.JSONObject;
import com.byquan2004.config.OpenAPIConfig;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author byquan2004
 */
@Component
public class HotToolService {

    private final RestTemplate restTemplate;
    private final OpenAPIConfig config;

    private HotToolService(RestTemplate restTemplate, OpenAPIConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Tool(
            name = "search university",
            description =
            """
            about the university name return university base info of json.
            """
    )
    public JSONObject searchUniversity(@ToolParam(description = "universityName") String universityName){
        String url  = config.getUrl1() + "?daxue=" + universityName;
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        return JSONObject.parse(res.getBody());
    }

    @Tool(
            name = "get_goddess_image",
            description = """
    Get a random goddess picture, without parameters, and return the picture link URL.
    """
    )
    public JSONObject getGoddessImage() {
        ResponseEntity<String> response = restTemplate.exchange(config.getUrl2(), HttpMethod.GET, null, String.class);
        return JSONObject.parse(response.getBody());
    }

    @Tool(
            name = "get_goddess_video",
            description = """
        Get the url access link of high-quality young lady related content video.
        """
    )
    public JSONObject getGoddessVideo() {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl3(), HttpMethod.GET, null, String.class);
        return JSONObject.parse(response.getBody());
    }

    @Tool(
            name = "get_news",
            description = """
           Get news content from various sources collected by Inshorts app.
        """
    )
    public JSONObject getNews(@ToolParam(description = "category options [all/national/business/sports/world/politics/technology/startup/entertainment/miscellaneous/hatke/science/automobile]")
                              String category) {
        ResponseEntity<String> response = restTemplate.exchange(
                config.getUrl4()+"?category="+category,
                HttpMethod.GET,
                null,
                String.class
        );
        return JSONObject.parse(response.getBody());
    }

    @Tool(
            name = "get_city_weather",
            description = """
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

    @Tool(
            name = "get_ip_info",
            description = """
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

    private JSONObject buildErrorResponse(String message) {
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", message);
        return error;
    }

}
