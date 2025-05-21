package com.wk.aiagent.agent;

import cn.hutool.core.util.StrUtil;

import com.wk.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象基础代理类，用来管理代理状态和执行流程
 * 提供状态转换、内存管理和基于步骤执行循环的基础功能
 * 子类必须实现step方法
 */
@Data
@Slf4j
public abstract class BaseAgent {
    // 核心属性
    private String name;

    // 代理描述
//    private String description;

    // 系统提示词
    private String systemPrompt;

    // 下一步提示词
    private String nextStepPrompt;

    // 状态
    private AgentState state = AgentState.IDLE;

    // 执行控制
    private int maxSteps = 10;
    private int currentStep = 0;

    // LLM
    private ChatClient chatClient;

    // memory：代理的记忆存储
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     */
    public String run(String userPrompt) {
        if (this.state != AgentState.IDLE){
            throw new RuntimeException("Cannot run agent from state: " + this.state);
        }

        if (StrUtil.isBlank(userPrompt)){
            throw new RuntimeException("User prompt cannot be blank");
        }

        // 执行
        this.state = AgentState.RUNNING;

        // 记录上下文消息
        messageList.add(new UserMessage(userPrompt));

        // 记录结果
        List<String> results = new ArrayList<>();

        try {
            // 执行循环
            for (int i = 0; i < maxSteps && state != AgentState.FINISHED; i++){
                int stepNumber = i + 1;
                currentStep = stepNumber;
//                log.info("Step {}: {}", stepNumber, next_step_prompt);

                // 单步执行
                String stepResult = step();
                String result = "Step " + stepNumber  + ": " + stepResult;
                results.add(result);
            }

            // 检查是否超出步骤限制
            if (currentStep >= maxSteps){
                this.state = AgentState.ERROR;
                results.add("Terminated: Reached max steps (" + maxSteps + ")");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            state = AgentState.ERROR;
//            log.error("error executing agent", e);
            return "执行错误" + e.getMessage();
        }finally {
            this.cleanup();
        }
    }

    /**
     * 定义单步
     * @return
     */
    public abstract String step();


    /**
     * 清理资源
     */
    public void cleanup(){
        // 子类可以重写
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystemPrompt() {
        return systemPrompt;
    }

    public void setSystemPrompt(String systemPrompt) {
        this.systemPrompt = systemPrompt;
    }

    public String getNextStepPrompt() {
        return nextStepPrompt;
    }

    public void setNextStepPrompt(String nextStepPrompt) {
        this.nextStepPrompt = nextStepPrompt;
    }

    public ChatClient getChatClient() {
        return chatClient;
    }

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public AgentState getState() {
        return state;
    }

    public void setState(AgentState state) {
        this.state = state;
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStep) {
        this.currentStep = currentStep;
    }
}
