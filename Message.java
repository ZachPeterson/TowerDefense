/**
 * Class to represent a message to transmit between parts of the game. Takes in
 * a recipient, a message type, and the data the message takes.
 *
 * @param <T> The type of data for the message to store.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class Message<T> {
    private MessageRecipient messageRecipient;
    private MessageType messageType;
    private T messageData;

    /**
     * Constructs a new message with given recipient, type and data.
     *
     * @param messageRecipient Who to send the message to.
     * @param messageType The type of message to send.
     * @param messageData The data to send with the message.
     */
    public Message(MessageRecipient messageRecipient, MessageType messageType,
        T messageData) {
        this.messageRecipient = messageRecipient;
        this.messageType = messageType;
        this.messageData = messageData;
    }

    /**
     * Returns the recipient for this message.
     *
     * @return The recipient for this message.
     */
    public MessageRecipient getMessageRecipient() {
        return messageRecipient;
    }

    /**
     * Returns the type for this message.
     *
     * @return The type for this message.
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Returns the data for this message.
     *
     * @return The data for this message.
     */
    public T getMessageData() {
        return messageData;
    }
}
