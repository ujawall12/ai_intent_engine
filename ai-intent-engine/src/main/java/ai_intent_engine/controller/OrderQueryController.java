package ai_intent_engine.controller;

import ai_intent_engine.model.ApiResponse;
import ai_intent_engine.model.UserIntent;
import ai_intent_engine.model.UserQueryRequest;
import ai_intent_engine.service.IntentExtractionService;
import ai_intent_engine.service.OrderRoutingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class OrderQueryController {

    private final IntentExtractionService intentExtractionService;
    private final OrderRoutingService orderRoutingService;

    public OrderQueryController(IntentExtractionService intentExtractionService,
                                OrderRoutingService orderRoutingService) {
        this.intentExtractionService = intentExtractionService;
        this.orderRoutingService = orderRoutingService;
    }

    @PostMapping("/ai-query")
    public ResponseEntity<ApiResponse> handleQuery(
            @Valid @RequestBody UserQueryRequest request) {

        log.info("Received AI query: {}", request.getMessage());

        UserIntent intent = intentExtractionService.extractIntent(request.getMessage());
        ApiResponse response = orderRoutingService.route(intent);

        log.info("Returning response for intent: {}", intent.getIntent());

        return ResponseEntity.ok(response);
    }
}
