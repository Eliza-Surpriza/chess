package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @BeforeEach
    void setUp() {
        // register username "felicity" password "1774"
        // register user "samantha" password "1904"
        // login user "samantha" password "1904"
    }

    @Test
    void register() {
        // try to sign up with username "felicity" password "1774" get error
        // sign in with username "kit" password "1934"
    }

    @Test
    void login() {
        // sign in with username "felicity" password "wrong_password" get error
        // sign in with username "felicity" password "1774"
    }

    @Test
    void logout() {
        // logout with wrong auth token, get error
        // logout with correct auth token, no error
    }
}