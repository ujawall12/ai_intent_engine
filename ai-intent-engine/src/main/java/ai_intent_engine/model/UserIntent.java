package ai_intent_engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserIntent {

    @JsonProperty("intent")
    private String intent;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("timeframe")
    private String timeframe;

    @JsonProperty("confidence")
    private double confidence;

}
