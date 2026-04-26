package ai_intent_engine.service;

import ai_intent_engine.model.UserIntent;

public interface IntentExtractionService {

    UserIntent extractIntent(String userMessage);
}
