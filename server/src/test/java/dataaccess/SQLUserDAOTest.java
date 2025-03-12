package dataaccess;

import java.sql.SQLException;

import exception.DataAccessException;
import exception.UnauthorizedException;
import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    UserDAO userDAO = new SQLUserDAO();

    public SQLUserDAOTest() throws SQLException, DataAccessException {
    }
    @BeforeEach
    void setUp() {
        UserData userData = new UserData("cello", "dad", "practice");
        userDAO.createUser(userData);
    }

    @AfterEach
    void cleanUp() {
        // clear!
        userDAO.clearUsers();
    }

    @Test
    void createUser() throws DataAccessException {
        // I guess I will create a user, and then try to get that user
        UserData userData = new UserData("pug-queen", "green", "sophie");
        userDAO.createUser(userData);
        assertEquals(userData, userDAO.getUser("pug-queen"));
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
        assertEquals(expected, result);
    }
}
