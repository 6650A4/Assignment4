package Model.Message;

/**
 * The type Abstract message.
 */
public abstract class AbstractMessage {
    private String message;

    /**
     * Instantiates a new Abstract message.
     *
     * @param message the message
     */
    public AbstractMessage(String message) {
        this.message = message;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
