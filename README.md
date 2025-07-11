# MCP-SERVER

## Introduction
The project is built based on Spring AI and runs through the stdio (standard input/output) protocol, aiming to demonstrate the basic functions of the MCP (Model Context Protocol) service. The project integrates a variety of practical tools, including obtaining random goddess pictures, videos, IP address details, news, hacker news, weather information, and web search functions that support Google and Baidu.

## Resource
- [Spring AI](https://spring.io/projects/spring-ai)
- [public-apis](https://github.com/public-apis/public-apis)
- [free-api](https://github.com/fangzesheng/free-api)

## Requirement
- JDK 17+
- Maven 3.x+

## The MCP Server and include tools
### ADD mcpServer
```bash
{
  "mcpServers": {
    "spring-ai-mcp-demo": {
      "command": "java",
      "args": [
        "-jar",
        "/target/mcp-server-0.0.1.jar"
      ]
    }
  }
}
```
### Include tools
- Get random goddess **picture**
- Get random goddess **video**
- Get IP address details
- Get news
- Get Hacker news
- Get weather
- Google web search
- Baidu web search

## Disclaimer
This project is for educational purposes only. 