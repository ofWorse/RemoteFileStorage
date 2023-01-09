package server.service;

import exception.AppException;

public class LoginCredentialsFaultException extends AppException {
    public LoginCredentialsFaultException(String message) {
        super(message);
    }
}
