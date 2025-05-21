package com.wk.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WK
 * version 1.0
 */
@SpringBootTest
class WebSearchToolTest {
    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Test
    void TestSearchWeb() {
        WebSearchTool tool = new WebSearchTool(searchApiKey);
        String query = "小猫的分类";
        String result = tool.searchWeb(query);
        System.out.println(result);
    }
}