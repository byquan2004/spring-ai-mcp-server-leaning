# mcp-demo

## Introduction
mcp-demo 是一个基于 Spring AI 的演示项目，基于stdio（standard input/output）方式运行旨在展示 MCP（Model Context Protocol）服务的基本功能。

## Resource
[Spring AI](https://spring.io/projects/spring-ai)
[public-apis](https://github.com/public-apis/public-apis)
[free-api](https://github.com/fangzesheng/free-api)

## requirement
- JDK 17
- Maven 3.x+

## ADD the MCP Server and include tools

```bash
{
  "mcpServers": {
    "spring-ai-mcp-demo": {
      "command": "java",
      "args": [
        "-jar",
        "/target/mcp-demo-0.0.1.jar"
      ]
    }
  }
}
```

**包含如下工具**：
- 搜索大学信息
- 获取一张随机女神**图片**
- 获取一张随机女神**视频**
- 获取新闻
- 查询天气
- 查询ip的详细信息
