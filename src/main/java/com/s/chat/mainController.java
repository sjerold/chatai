package com.s.chat;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

@RestController
public class mainController {

    private final ChatClient ollamaChatClient;

    public mainController() {
        OllamaApi ollamaApi = OllamaApi.builder().baseUrl("http://localhost:11434").build();
        OllamaOptions ollamaOptions = OllamaOptions.builder().model("deepseek-r1:latest").build();
        ChatModel model = OllamaChatModel.builder().defaultOptions(ollamaOptions).ollamaApi(ollamaApi).build();
        this.ollamaChatClient = ChatClient.builder(model).build();
    }

    @GetMapping(value = "/ollama", produces = "text/html;charset=utf-8")
    public Flux<String> ollama(@RequestParam String prompt) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return Flux.empty();
        }
        return this.ollamaChatClient.prompt().user(prompt).stream().content();
    }
}
