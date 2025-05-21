package com.wk.aiagent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WK
 * version 1.0
 */
@SpringBootTest
class WkManusTest {


    @Resource
    private WkManus wkManus;

    @Test
    void run() {
        String userPrompt ="""
                我的另一半居住在重庆杨家坪，请帮我找到 3 公里内合适的约会地点，
                并结合一些网络图片，制定一份详细的约会计划，
                并以 PDF 格式输出""";
        String result = wkManus.run(userPrompt);
        System.out.println(result);
    }
}