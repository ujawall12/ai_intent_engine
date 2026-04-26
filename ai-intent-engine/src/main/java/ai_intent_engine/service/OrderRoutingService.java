package ai_intent_engine.service;

import ai_intent_engine.model.ApiResponse;
import ai_intent_engine.model.UserIntent;

public interface OrderRoutingService {

    ApiResponse route(UserIntent intent);
}
