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
public class SearchMusicService {

    private final RestTemplate restTemplate;
    private final OpenAPIConfig config;

    private SearchMusicService(RestTemplate restTemplate, OpenAPIConfig config) {
        this.restTemplate = restTemplate;
        this.config = config;
    }

    @Tool(
        name = "search_music_id",
        description = """
        根据歌曲名称和音乐平台搜索歌曲ID信息，支持平台有：
        网易云(wy)、QQ音乐(qq)、酷狗(kw)、咪咕(mg)、千千音乐(qi)
        """
    )
    public JSONObject searchMusicId(
        @NotNull(message = "歌曲名不能为空") String name,
        @NotNull(message = "音乐平台类型不能为空") String type,
        String page,
        String limit
    ) {
        try {
            String url = config.getUrl2() + "?name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                    + "&type=" + type
                    + "&page=" + (page != null ? page : "1")
                    + "&limit=" + (limit != null ? limit : "10");

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                JSONObject err = new JSONObject();
                err.put("message", "请求失败，请稍后再试");
                err.put("error", response.getStatusCode().toString());
                return err;
            }

            return JSONObject.parse(response.getBody());
        } catch (Exception e) {
            JSONObject err = new JSONObject();
            err.put("message", "系统异常，请联系管理员");
            err.put("error", e.getMessage());
            return err;
        }
    }

    @Tool(
            name = "search_music_detail",
            description = """
        根据音乐 ID 和平台类型获取歌曲播放详情，包括播放地址、歌词、封面图等。
        支持平台：网易云(wy)、QQ音乐(qq)、酷狗(kw)、咪咕(mg)、千千音乐(qi)
        """
    )
    public JSONObject getMusicDetail(
            @NotNull(message = "歌曲 ID 不能为空") String id,
            @NotNull(message = "音乐平台类型不能为空") String type
    ) {
        try {
            String url = config.getUrl2()
                    + "?id=" + URLEncoder.encode(id, StandardCharsets.UTF_8)
                    + "&type=" + URLEncoder.encode(type, StandardCharsets.UTF_8);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                JSONObject error = new JSONObject();
                error.put("message", "请求失败，请稍后再试");
                error.put("error", response.getStatusCode().toString());
                return error;
            }

            return JSONObject.parse(response.getBody());

        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("message", "系统异常，请联系管理员");
            error.put("error", e.getMessage());
            return error;
        }
    }
}
