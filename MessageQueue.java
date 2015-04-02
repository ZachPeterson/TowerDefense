import java.util.Vector;

/**
 * Singleton class implementation for the message queue. Handles storing all
 * game messages. Works simply as a message queue, has no dispatch system. All
 * classes that need to handle messages should get an instance of this and
 * retrieve messages from it.
 *
 * @author Zachary Peterson
 * @version 1.0
 */
public class MessageQueue {
    private Vector<Message> myMessages;
    private static MessageQueue instance = new MessageQueue();

    private MessageQueue() {
        // Private constructor, this should never be called
        // Clients should instead use get instance
        myMessages = new Vector<Message>();
    }

    /**
     * Returns the singleton instance for the MessageQueue.
     *
     * @return Singleton instance for the message queue.
     */
    public static MessageQueue getInstance() {
        return instance;
    }

    /**
     * Pushes a messages onto the queue.
     *
     * @param messageToPush The message to add to the queue.
     */
    public void push(Message messageToPush) {
        synchronized (myMessages) {
            if (messageToPush != null) {
                myMessages.add(messageToPush);
            }
        }
    }

    /**
     * Retrieves the oldest message with a given recipient.
     *
     * @param intendedRecipient The intended recipient to check for.
     * @return The oldest message with the given recipient.
     */
    public Message pop(MessageRecipient intendedRecipient) {
        synchronized (myMessages) {
            for (int i = 0; i < myMessages.size(); i++) {
                if (myMessages.get(i).getMessageRecipient()
                    == intendedRecipient) {
                    return myMessages.remove(i);
                }
            }
            return null;
        }
    }

    /**
     * Peeks at the oldest message in the queue, does not remove it.
     *
     * @param intendedRecipient The intended recipient to check for.
     * @return The oldest message with the given recipient.
     */
    public Message peek(MessageRecipient intendedRecipient) {
        synchronized (myMessages) {
            for (int i = 0; i < myMessages.size(); i++) {
                if (myMessages.get(i) != null
                    && myMessages.get(i).getMessageRecipient()
                    == intendedRecipient) {
                    return myMessages.get(i);
                }
            }
            return null;
        }
    }
}
