package ai_intent_engine.service;

import ai_intent_engine.exception.IntentExtractionException;
import ai_intent_engine.model.UserIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntentExtractionServiceImpl implements IntentExtractionService {

    private final ChatClient chatClient;

    @Override
    public UserIntent extractIntent(String userMessage) {
        log.info("Extracting intent from message: {}", userMessage);
        try {
            UserIntent intent = chatClient.prompt()
                    .user(userMessage)
                    .call()
                    .entity(UserIntent.class);

            assert intent != null;
            log.info("Extracted intent: {} with confidence: {}",
                    intent.getIntent(), intent.getConfidence());

            return intent;

        } catch (Exception e) {
            log.error("Failed to extract intent from message: {}", userMessage, e);
            throw new IntentExtractionException(
                    "Failed to process your request. Please try again.", e);
        }
    }
}
