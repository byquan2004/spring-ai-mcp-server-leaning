# mcp-demo

## 项目介绍
mcp-demo 是一个基于 Spring AI 的演示项目，基于stdio（standard input/output）方式运行旨在展示 MCP（Model Context Protocol）服务的基本功能。

## 参考文档
[Spring AI](https://spring.io/projects/spring-ai)

## 免费接口仓库
本项目参考了 [free-api](https://github.com/fangzesheng/free-api) 提供的免费接口资源。
更多接口资源请查看 [public-apis](https://github.com/public-apis/public-apis) 提供的免费接口资源。

## 开发环境要求
- JDK 17
- Maven 3.x+

## 运行方式
```bash
# 打包
mvn clean package
# 运行
java -jar target/mcp-demo-0.0.1.jar
```

## The MCP Server

**包含如下工具**：
- 根据歌曲名称和音乐平台搜索歌曲ID
- 根据音乐 ID 和平台类型获取歌曲播放详情，包括播放地址、歌词、封面图等
- 搜索根据国内大学名称返回大学信息
- 获取一张随机女神**图片**
- 获取一张随机女神**视频**
- 获取今日头条新闻
- 获取 bing 每日壁纸
- 获取实时段子内容

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