package service;

import dataaccess.AuthDAO;
import exception.UnauthorizedException;
import model.AuthData;

public class AuthService {
    private final AuthDAO authDAO;

    public AuthService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void authorize(String authToken) throws UnauthorizedException {
        if ((authDAO.getAuth(authToken) == null)) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
