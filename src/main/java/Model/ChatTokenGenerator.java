package Model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class ChatTokenGenerator {

    public static String generateChatToken(String senderMessage, String recipientMessage) throws NoSuchAlgorithmException {

        String[] messages = {senderMessage, recipientMessage};
        Arrays.sort(messages);


        String combinedMessage = messages[0] + messages[1];


        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(combinedMessage.getBytes(StandardCharsets.UTF_8));


        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

//    public static void main(String[] args) {
//        try {
//            String senderMessage = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMUAxMSIsImlzcyI6IllvdXJBcHBOYW1lIiwiZXhwIjoxNzE2NTU1MDc2fQ.L-rjKJpTHw-UMJfWtPQ_hBQR8WOFXpdBaOh8WtVwWTY";
//            String recipientMessage = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyQDIiLCJpc3MiOiJZb3VyQXBwTmFtZSIsImV4cCI6MTcxNjU1NTEwOH0.8GiOt7Z7vJ7n-GL20V85ok0GJ6Vj_Y96O5mgjBBu0Qg";
//            String chatToken = generateChatToken(recipientMessage, senderMessage);
//            System.out.println("Chat Token: " + chatToken);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//    }
}
