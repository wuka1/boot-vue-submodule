package com.example.llm.vo;

import lombok.Data;

import java.util.List;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/14
 * *@Version 1.0
 **/

@Data
public class DifyStreamEventVO {
    private String event; // "message" 或 "message_end"
    private String conversation_id;
    private String message_id;
    private long created_at;
    private String task_id;
    private String id;
    private String answer; // 仅 message 事件有值
    private String from_variable_selector;
    private Metadata metadata; // 仅 message_end 事件有值

    // 嵌套元数据类
    @Data
    public static class Metadata {
        private Object annotation_reply; // 可能为 null
        private List<Object> retriever_resources;
        private Usage usage;

        // 使用量统计
        @Data
        public static class Usage {
            private int prompt_tokens;
            private int completion_tokens;
            private int total_tokens;
            private double latency;
            // 其他字段可根据需要添加
        }
    }
}
