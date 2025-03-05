package service;

import dataaccess.MemoryAuthDAO;
import model.AuthData;

public class AuthService {
    private final MemoryAuthDAO authDAO;

    public AuthService(MemoryAuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    AuthData createAuth(String username) {
        return authDAO.createAuth(username);
    }
}
