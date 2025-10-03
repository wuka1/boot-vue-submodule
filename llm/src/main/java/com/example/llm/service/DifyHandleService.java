package com.example.llm.service;

import com.example.llm.api.DifyApiClient;
import com.example.llm.api.DifyApiPaths;
import com.example.llm.dto.ChatRequestDTO;
import com.example.llm.vo.DifyStreamEventVO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/12
 * *@Version 1.0
 **/

@Service
@RequiredArgsConstructor
@Slf4j
public class DifyHandleService {

    private final DifyApiClient difyApiClient;
    private final StringBuilder fullAnswer = new StringBuilder();
    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Flux<String> processStream(Flux<String> difyStream) {
        return difyStream
                .filter(event -> !isBlank(event)) // 过滤空行
                .flatMap(this::parseEvent)         // 解析JSON
                .flatMap(this::handleEvent)        // 处理不同事件
                .doFinally(signalType -> {
                    if (signalType == SignalType.ON_COMPLETE) {
                        System.out.println("完整回答: " + fullAnswer);
                    }
                });
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    // 解析JSON事件
    private Mono<DifyStreamEventVO> parseEvent(String jsonStr) {
        try {
            return Mono.just(objectMapper.readValue(jsonStr, DifyStreamEventVO.class));
        } catch (Exception e) {
            return Mono.error(new RuntimeException("JSON解析失败: " + jsonStr, e));
        }
    }

    // 处理不同类型的事件
    private Mono<String> handleEvent(DifyStreamEventVO event) {
        return Mono.fromCallable(() -> {
            switch (event.getEvent()) {
                case "message":
                    return processMessageEvent(event);

                case "message_end":
                    return processMessageEndEvent(event);

                default:
                    return "未知事件类型: " + event.getEvent();
            }
        });
    }

    private String processMessageEvent(DifyStreamEventVO event) {
        // 处理Unicode转义字符（如\u3002）
        String decodedAnswer = StringEscapeUtils.unescapeJava(event.getAnswer());

        // 累积完整回答
        fullAnswer.append(decodedAnswer);

        // 打印当前片段
        System.out.println("收到部分回答: " + decodedAnswer);

        // 返回给前端的内容
        return decodedAnswer;
    }

    private String processMessageEndEvent(DifyStreamEventVO event) {
        DifyStreamEventVO.Metadata.Usage usage = event.getMetadata().getUsage();

        // 打印使用统计
        System.out.printf(
                "对话完成！\n" +
                        "消息ID: %s\n" +
                        "Token使用: 提示=%d, 补全=%d, 总计=%d\n" +
                        "延迟: %.2f秒\n",
                event.getMessage_id(),
                usage.getPrompt_tokens(),
                usage.getCompletion_tokens(),
                usage.getTotal_tokens(),
                usage.getLatency()
        );

        return "[END]"; // 结束标记
    }

    public Flux<String> streamChat(ChatRequestDTO userInput) {
        return difyApiClient.postStream(DifyApiPaths.CHAT_MESSAGES, userInput)
                .transform(this::processStream)
                .doOnSubscribe(s -> System.out.println("请求已发送"))
                .doOnError(Throwable::printStackTrace);
    }
}
