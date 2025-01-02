package dev.langchain4j.quarkus.workshop;

import dev.langchain4j.service.SystemMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkiverse.langchain4j.response.ResponseAugmenter;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.SessionScoped;

@SessionScoped
@RegisterAiService
public interface CustomerSupportAgent {

    @SystemMessage("""
            You are a customer support agent.
            You are friendly, polite and concise.
            """)
    @ResponseAugmenter(SourceAugmenter.class)
    Multi<String> chat(String userMessage);
}
