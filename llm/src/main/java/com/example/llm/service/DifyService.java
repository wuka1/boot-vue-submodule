package com.example.llm.service;

import com.example.llm.api.DifyApiClient;
import com.example.llm.api.DifyApiPaths;
import com.example.llm.dto.ChatRequestDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
public class DifyService {

    private final DifyApiClient difyApiClient;

    public Flux<String> streamChat(ChatRequestDTO userInput) {

        return difyApiClient.postStream(DifyApiPaths.CHAT_MESSAGES, userInput)
                .doOnNext(System.out::println)       //作用
                .doOnSubscribe(s -> System.out.println("请求已发送"))
                .doOnError(Throwable::printStackTrace);
    }
}
