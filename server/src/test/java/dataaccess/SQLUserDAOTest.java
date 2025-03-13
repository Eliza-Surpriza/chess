package dataaccess;

import java.sql.SQLException;

import exception.DataAccessException;
import exception.UnauthorizedException;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    UserDAO userDAO = new SQLUserDAO();

    @BeforeEach
    void setUp() {
        UserData userData = new UserData("cello", "dad", "practice");
        userDAO.createUser(userData);
    }

    @AfterEach
    void cleanUp() {
        userDAO.clearUsers();
    }

    @Test
    void createUser() throws DataAccessException {
        // I guess I will create a user, and then try to get that user
        UserData userData = new UserData("pug-queen", "green", "sophie");
        userDAO.createUser(userData);
        UserData result = userDAO.getUser("pug-queen");
        assertEquals("pug-queen", result.username());
    }

    @Test
    void createUserBadData() {
        UserData badData = new UserData(null, null, null);
        assertThrows(DataAccessException.class, () -> userDAO.createUser(badData));
    }

    @Test
    void getUserNotThere() {
        UserData result = userDAO.getUser("I don't exist");
        assertNull(result);
    }

    @Test
    void getUser() {
        UserData result = userDAO.getUser("cello");
        UserData expected = new UserData("cello", "dad", "practice");
        assertEquals(expected.username(), result.username());
    }

    @Test
    void verifyPassword() {
        UserData celloData = userDAO.getUser("cello");
        LoginRequest request = new LoginRequest("cello", "dad");
        assertTrue(userDAO.verifyPassword(celloData, request));
    }

    @Test
    void verifyPasswordWrong() {
        UserData celloData = userDAO.getUser("cello");
        LoginRequest request = new LoginRequest("cello", "wrong password");
        assertFalse(userDAO.verifyPassword(celloData, request));
    }

    @Test
    void clear() {
        userDAO.clearUsers();
        assertNull(userDAO.getUser("pug-queen"));
    }
}
