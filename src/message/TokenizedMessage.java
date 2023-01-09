package message;

public class TokenizedMessage extends Message {
    private final String token;

    public TokenizedMessage(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
