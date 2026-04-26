package ai_intent_engine.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQueryRequest {

    @NotBlank(message = "Message cannot be blank")
    @Size(min = 3, max = 500, message = "Message must be between 3 and 500 characters")
    private String message;
}