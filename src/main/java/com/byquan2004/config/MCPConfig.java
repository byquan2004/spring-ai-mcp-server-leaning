package com.byquan2004.config;

import com.byquan2004.tools.SearchMusicService;
import com.byquan2004.tools.HotToolService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MCPConfig {
    @Bean
    public ToolCallbackProvider tools(
            HotToolService tool,
            SearchMusicService tool2
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(tool,tool2)
                .build();
    }

}
