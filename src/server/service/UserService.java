package server.service;

import exception.AppException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final Map<String, String> userPasswords = Map.of(
            "user", "pass"
    );
    private final Map<String, String> userTokens = new ConcurrentHashMap<>();

    public void checkUserCredentials(String user, String password) {
        if (user == null || password == null ||
                !password.equals(userPasswords.get(user))) {
            throw new LoginCredentialsFaultException("ошибка входа");
        }
    }

    public String getTokenForUser(String user) {
        var token = UUID.randomUUID().toString();
        userTokens.put(token, user);
        return token;
    }

    public String getUserForToken(String token) {
        if (token == null || userTokens.get(token) == null) {
            throw new AppException("незадействованный токен");
        } else {
            return userTokens.get(token);
        }
    }
}
