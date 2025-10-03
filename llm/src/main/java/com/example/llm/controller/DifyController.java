package com.example.llm.controller;

import com.example.llm.dto.ChatRequestDTO;
import com.example.llm.service.DifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * *@Description TODO
 * *@Author wuka
 * *@Date 2025/8/12
 * *@Version 1.0
 **/

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class DifyController {

    private final DifyService difyService;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@RequestBody ChatRequestDTO request) {
        return difyService.streamChat(request);
    }
}
