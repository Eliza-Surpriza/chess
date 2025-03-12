package dataaccess;

import java.sql.SQLException;

import exception.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    UserDAO userDAO = new SQLUserDAO();

    public SQLUserDAOTest() throws SQLException, DataAccessException {
    }

    @Test
    void createUser() throws DataAccessException {
        // I guess I will create a user, and then try to get that user
        UserData userData = new UserData("pug-queen", "green", "sophie");
        userDAO.createUser(userData);
        assertEquals(userData, userDAO.getUser("pug-queen"));
    }
}
