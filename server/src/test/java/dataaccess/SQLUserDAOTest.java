package dataaccess;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SQLUserDAOTest {
    UserDAO userDAO = new SQLUserDAO();

    public SQLUserDAOTest() throws SQLException, DataAccessException {
    }

    @Test
    void createUser() throws DataAccessException {

    }
}
