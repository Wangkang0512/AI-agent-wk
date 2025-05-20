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
}