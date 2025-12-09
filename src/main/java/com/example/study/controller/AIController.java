package com.example.study.controller;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AIController {
    private final ChatModel chatModel;

    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestParam String message) {
        SseEmitter emitter = new SseEmitter(30_000L);
        String systemPrompt = "你是一个专业的AI助手，回答要简洁、准确。"
                + "如果遇到不确定的问题，请如实说明。"
                + "用简体中文回答问题,非必要不能用别的语言防止用户看不懂"
                + "当前时间：" + LocalDateTime.now();
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            executor.submit(() -> {
                try {
                    ChatClient.create(chatModel)
                            .prompt()
                            .system(systemPrompt)
                            .user(message)
                            .stream()
                            .chatResponse()
                            .subscribe(
                                    chatResponse -> {
                                        try {
                                            emitter.send(SseEmitter.event()
                                                    .data(Objects.requireNonNull(chatResponse.getResult().getOutput().getText()))
                                                    .id(IdUtil.fastSimpleUUID())
                                            );
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    },
                                    emitter::completeWithError,
                                    emitter::complete
                            );
                } catch (Exception e) {
                    emitter.completeWithError(e);
                }
            });
        }
        return emitter;
    }
}
