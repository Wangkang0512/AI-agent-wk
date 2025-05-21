package com.wk.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "test.pdf";
        String content = "test https://www.baidu.cn";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
