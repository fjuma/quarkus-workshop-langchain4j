package dev.langchain4j.quarkus.workshop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;

import dev.langchain4j.data.segment.TextSegment;
import io.quarkiverse.langchain4j.response.AiResponseAugmenter;
import io.quarkiverse.langchain4j.response.ResponseAugmenterParams;
import io.smallrye.mutiny.Multi;

@ApplicationScoped
public class SourceAugmenter implements AiResponseAugmenter<String> {

    @Override
    public Multi<String> augment(Multi<String> stream, ResponseAugmenterParams params) {
        // Specify the sources from which the context that was passed to the LLM originated from
        List<String> sources = new ArrayList<>();
        sources.add("Sources consulted:\n");
        params.augmentationResult()
                .contents()
                .stream()
                .map(content -> content.textSegment().metadata().get("source") + "\n")
                .forEach(sources::add);
        return stream.onCompletion().continueWith(sources);
    }
}