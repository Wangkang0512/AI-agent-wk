package com.wk.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WK
 * version 1.0
 */
@SpringBootTest
class WebScrapingToolTest {

    @Test
    public void testScrapeWebPage() {
        WebScrapingTool tool = new WebScrapingTool();
        String url = "https://www.baidu.cn";
        String result = tool.scrapeWebPage(url);
        assertNotNull(result);
    }

}