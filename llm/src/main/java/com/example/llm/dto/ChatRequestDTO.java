package com.example.llm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * *@Description 相关配置查看dify的api
 * *@Author wuka
 * *@Date 2025/8/12
 * *@Version 1.0
 **/

@Data
public class ChatRequestDTO {
    private Map<String, Object> inputs = Collections.emptyMap(); // 其他输入参数

    private String query;             // 用户输入请求
    private String user;             // 用户标识

    @JsonProperty("response_mode")
    private String responseMode = "streaming"; // 回复模式，推荐streaming

    @JsonProperty("conversation_id")
    private String conversationId = ""; //选填

    private List<File> files = Collections.emptyList();

    // 内部类
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class File {
        private String type;               // 支持类型: "image"
        @JsonProperty("transfer_method")
        private String transferMethod;    // "remote_url" 或 "local_file"
        private String url;               // 远程图片地址
        @JsonProperty("upload_file_id")
        private String uploadFileId;      // 本地上传文件 ID
    }
}