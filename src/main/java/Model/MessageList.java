package Model;

public class MessageList {


    private String senderMessageName;
    private String senderMessage;
    private String recipientMessage;
    private String chatToken;
    private String message;

    public MessageList(String senderMessageName, String senderMessage, String recipientMessage, String chatToken, String message) {
        this.senderMessageName = senderMessageName;
        this.senderMessage = senderMessage;
        this.recipientMessage = recipientMessage;
        this.chatToken = chatToken;
        this.message = message;
    }

    public String getSenderMessage() {
        return senderMessage;
    }
    public String getSenderMessageName() {
        return senderMessageName;
    }

    public void setSenderMessageName(String senderMessageName) {
        this.senderMessageName = senderMessageName;
    }
    public void setSenderMessage(String senderMessage) {
        this.senderMessage = senderMessage;
    }

    public String getRecipientMessage() {
        return recipientMessage;
    }

    public void setRecipientMessage(String recipientMessage) {
        this.recipientMessage = recipientMessage;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}