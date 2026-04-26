package ai_intent_engine.exception;

public class IntentExtractionException extends RuntimeException {

    public IntentExtractionException(String message) {
        super(message);
    }

    public IntentExtractionException(String message, Throwable cause) {
        super(message, cause);
    }
}
