// --8<-- [start:ragretriever-1]
package dev.langchain4j.quarkus.workshop;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;

public class RagRetriever {

    @Produces
    @ApplicationScoped
    public RetrievalAugmentor create(EmbeddingStore store, EmbeddingModel model) {
        var contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(model)
                .embeddingStore(store)
                .maxResults(2)
                .build();

        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
// --8<-- [end:ragretriever-1]
// --8<-- [start:ragretriever-3]
                .contentInjector(new ContentInjector() {
                    @Override
                    public UserMessage inject(List<Content> list, UserMessage userMessage) {
                        StringBuffer prompt = new StringBuffer("\nAnswer the question based only on the following context:\n");
                        list.forEach(content -> prompt.append(content.textSegment().text()).append("\nSource: ").append(content.textSegment().metadata().get("source")).append("\n"));
                        prompt.append("Question:\n" + userMessage.singleText());
                        return new UserMessage(prompt.toString());
                    }
                })
// --8<-- [end:ragretriever-3]
// --8<-- [start:ragretriever-2]
                .build();
    }
}
// --8<-- [end:ragretriever-2]
