package ai_intent_engine.config;

import ai_intent_engine.constant.IntentConstants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem(IntentConstants.SYSTEM_PROMPT)
                .build();
    }
}
