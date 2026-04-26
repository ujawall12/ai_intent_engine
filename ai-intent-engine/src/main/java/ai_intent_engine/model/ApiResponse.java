package ai_intent_engine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {

    private boolean success;
    private String intent;
    private double confidence;
    private String message;
    private Object data;

    public static ApiResponse success(String intent,
                                      double confidence,
                                      String message,
                                      Object data) {
        return ApiResponse.builder()
                .success(true)
                .intent(intent)
                .confidence(confidence)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse clarificationNeeded(String message) {
        return ApiResponse.builder()
                .success(false)
                .intent("UNCLEAR")
                .confidence(0.0)
                .message(message)
                .build();
    }
}
