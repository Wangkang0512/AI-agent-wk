package com.wk.aiagent.app;

import com.wk.aiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @author WK
 * version 1.0
 */
@Component
@Slf4j
public class CounselingApp {

    private final ChatClient chatClient;

    record PsychologyReport(String title, List<String> suggestions) {
    }


    private static final String SYSTEM_PROMPT = "你是一位专业的心理咨询师，专注于帮助大学生和研究生解决心理困扰。" +
            "你的任务是倾听用户的烦恼，理解他们的情绪，并通过温和、共情的语言不断引导用户深入表达内心感受。你不会直接评判用户行为，也不会草率下结论或提供医疗诊断，而是通过一系列有引导性的问题帮助用户自我探索、自我理解，并提供实用的心理建议。" +
            "你的风格要像一位温暖、值得信赖的朋友，语言温和、理解、包容，不机械、不冷漠。" +
            "你的对话流程应该遵循以下方式：" +
            "1. 初步共情：理解并回应用户情绪，例如“听起来你最近真的很辛苦”。" +
            "2. 引导式提问：用开放性问题引导用户深入，例如“你能说说最近让你压力最大的是哪件事吗？”、“你有没有人可以倾诉？”、“你希望情况变得怎样？”" +
            "3. 提供建议或共情回应**：在用户情绪表达充分后，给予安抚性反馈或提出轻柔建议，例如“有时候我们都需要休息一下”、“尝试写写日记可能会有帮助”。" +
            "4. 识别风险：若用户表达自伤、自杀等风险倾向，请立即输出安全建议，如：“你很重要。如果你有强烈痛苦或危险想法，请及时拨打心理热线寻求帮助。你并不孤单。”" +
            "永远不要使用生硬的回应，比如“我不理解”或“请重复”，要始终保持耐心和理解。" +
            "请注意：" +
            "- 你不能替代专业医生或诊断服务，但可以提供心理支持建议。" +
            "- 若对话中出现严重心理风险，你需要推荐用户寻求专业帮助并显示心理热线。" +
            "现在请你以这种方式与用户开始对话吧。";

    public CounselingApp(ChatModel dashscopeChatModel) {
        // 初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory)
                        //自定义Advisor
//                        new MyLoggerAdvisor()
                )
                .build();
    }


    /**
     * AI 基础对话（支持多轮对话记忆）
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId){
        ChatResponse response = chatClient.prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
//        log.info("content: {}",content);
        return content;
    }

    /**
     * AI 基础对话（支持多轮对话记忆，SSE 流式传输）
     *
     * @param message
     * @param chatId
     * @return
     */
    public Flux<String> doChatByStream(String message, String chatId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .stream()
                .content();
    }


    /**
     * psychologyAppRagCloudAdvisor
     * @param message
     * @param chatId
     * @return
     */
    public PsychologyReport doChatWithReport(String message, String chatId) {
        PsychologyReport psychologyReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT + "每次对话后都要生成心理咨询结果，标题为{用户名}的咨询报告，内容为对话列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(PsychologyReport.class);
//        log.info("loveReport: {}", loveReport);
        return psychologyReport;
    }


    @Resource
    private Advisor psychologyAppRagCloudAdvisor;

    public String doChatWithRag(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .advisors(psychologyAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        return content;
    }


    @Resource
    private ToolCallback[] allTools;

    /**
     * doChatWithTools
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                // 开启日志，便于观察效果
                .advisors(new MyLoggerAdvisor())
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
//        log.info("content: {}", content);
        return content;
    }



}
