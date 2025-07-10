package com.byquan2004.tools;

import com.alibaba.fastjson2.JSONObject;
import com.byquan2004.config.OpenAPIConfig;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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
    public JSONObject searchUniversity(@NotNull(message = "the university name be not null") String universityName){
        try {
            String url  = config.getUrl1() + "?daxue=" + universityName;
            ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if(!res.getStatusCode().is2xxSuccessful()) {
                JSONObject obj = new JSONObject();
                obj.put(
                        "message",
                        "Sorry, there may be a problem with the service, or the content you searched does not exist."
                );
                obj.put("error", res.getStatusCode().toString());
                return obj;
            }

            return JSONObject.parse(res.getBody());
        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }

    @Tool(
            name = "random_goddess_image",
            description = """
    获取一张随机女神图片，无需参数，返回图片链接 URL。
    """
    )
    public JSONObject getRandomGoddessImage() {
        try {
            ResponseEntity<String> response = restTemplate.exchange(config.getUrl3(), HttpMethod.GET, null, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                JSONObject error = new JSONObject();
                error.put("message", "请求失败，请稍后重试");
                error.put("error", response.getStatusCode().toString());
                return error;
            }

            return JSONObject.parse(response.getBody());

        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }

    @Tool(
            name = "get_goddess_video",
            description = """
        获取高质量小姐姐相关内容视频url访问链接。
        """
    )
    public JSONObject getGoddessVideo() {
        try {
            String url = config.getUrl3() + "?type=" + URLEncoder.encode("json", StandardCharsets.UTF_8);
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, null, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return JSONObject.parseObject(response.getBody());
            } else {
                return buildErrorResponse("请求失败，状态码：" + response.getStatusCode());
            }
        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }

    @Tool(
            name = "get_news_feed",
            description = """
        获取新闻资讯内容，接口返回 json 格式数据，包含数据详情、总数、是否有更多数据等信息，来源今日头条。
        """
    )
    public JSONObject getNewsFeed() {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    config.getUrl4(),
                    HttpMethod.GET,
                    null,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return JSONObject.parseObject(response.getBody());
            } else {
                JSONObject error = new JSONObject();
                error.put("message", "请求失败，请稍后再试");
                error.put("error", response.getStatusCode().toString());
                return error;
            }
        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }

    @Tool(
            name = "get_bing_wallpaper",
            description = """
        获取最近的 Bing 壁纸信息，接口返回 json 格式数据，包含壁纸的 URL、起始日期、版权信息等内容
        """
    )
    public JSONObject getBingWallpaper() {
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    config.getUrl5(),
                    HttpMethod.GET,
                    null,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return JSONObject.parseObject(response.getBody());
            } else {
                JSONObject error = new JSONObject();
                error.put("message", "请求失败，请稍后再试");
                error.put("error", response.getStatusCode().toString());
                return error;
            }
        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }

    @Tool(
            name = "get_real_time_joke",
            description = """
        获取实时段子内容，支持指定查询类型（可选项：all/video/image/gif/text），可选填页码和每页返回数量。
        type 参数为必填，page 和 count 为选填
        """
    )
    public JSONObject getRealTimeJoke(
            @NotNull(message = "type 不能为空，可选项：all/video/image/gif/text") String type,
            String page,
            String count
    ) {
        try {
            StringBuilder urlBuilder = new StringBuilder(config.getUrl1());
            urlBuilder.append("?type=").append(URLEncoder.encode(type, StandardCharsets.UTF_8));
            if (page != null) {
                urlBuilder.append("&page=").append(page);
            }
            if (count != null) {
                urlBuilder.append("&count=").append(count);
            }

            ResponseEntity<String> response = restTemplate.exchange(
                    urlBuilder.toString(),
                    HttpMethod.GET,
                    null,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return JSONObject.parseObject(response.getBody());
            } else {
                JSONObject error = new JSONObject();
                error.put("message", "请求失败，请稍后再试");
                error.put("error", response.getStatusCode().toString());
                return error;
            }
        } catch (Exception e) {
            return buildErrorResponse("系统异常：" + e.getMessage());
        }
    }


    private JSONObject buildErrorResponse(String message) {
        JSONObject error = new JSONObject();
        error.put("success", false);
        error.put("message", message);
        return error;
    }

}
