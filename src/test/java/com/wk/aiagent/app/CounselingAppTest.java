package com.wk.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author WK
 * version 1.0
 */
@SpringBootTest
@Slf4j
class CounselingAppTest {

    @Resource
    private CounselingApp counselingApp;

    @Test
    void testChat(){
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好,我是王康";
        String answer = counselingApp.doChat(message, chatId);
        System.out.println(answer);
        Assertions.assertNotNull(answer);

        message = "我现在学业上受到了困扰，感觉自己很努力了，但还是考不好";
        answer = counselingApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        System.out.println(answer);


    }

    @Test
    void doChat() {
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好,我是王康";
        CounselingApp.PsychologyReport psychologyReport = counselingApp.doChatWithReport(message, chatId);
        System.out.println(psychologyReport);
        Assertions.assertNotNull(psychologyReport);

        message = "我现在学业上受到了困扰，感觉自己很努力了，但还是考不好，觉得自己好没用";
        psychologyReport = counselingApp.doChatWithReport(message, chatId);
        System.out.println(psychologyReport);
        Assertions.assertNotNull(psychologyReport);


    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好,我是王康";
        CounselingApp.PsychologyReport psychologyReport = counselingApp.doChatWithReport(message, chatId);
        System.out.println(psychologyReport);
        Assertions.assertNotNull(psychologyReport);

        message = "我现在学业上受到了困扰，感觉自己很努力了，但还是考不好，觉得自己好没用";
        psychologyReport = counselingApp.doChatWithReport(message, chatId);
        System.out.println(psychologyReport);
        Assertions.assertNotNull(psychologyReport);
    }



    @Test
    void doChatWithTools() {

        // 测试联网搜索问题的答案
        testMessage("我在北京，我心情不好，想出去走走，推荐几个适合地方");

        // 测试网页抓取：心理咨询案例分析
        testMessage("最近学业压力太大，看看知乎上（https://www.zhihu.com/）的同学是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的包含‘保持心态’作为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的咨询报告为文件");

        // 测试 PDF 生成
        testMessage("生成一份‘走出失恋’PDF");
    }
    private void testMessage(String message) {
        String chatId = UUID.randomUUID().toString();
        String answer = counselingApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }
}