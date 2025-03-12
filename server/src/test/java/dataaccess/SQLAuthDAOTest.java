package dataaccess;

import exception.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {
    AuthDAO authDAO = new SQLAuthDAO();

    SQLAuthDAOTest() throws SQLException {
    }


    @BeforeEach
    void setUp() {
        authDAO.createAuth("aspen");
    }

    @AfterEach
    void tearDown() {
        authDAO.clearAuth();
    }

    @Test
    void createAuth() {
        AuthData result = authDAO.createAuth("ponderosa");
        assertEquals("ponderosa", result.username());
    }

    @Test
    void deleteAuth() {
        AuthData ponderosaData = authDAO.createAuth("ponderosa");
        AuthData willowData = authDAO.createAuth("willow");
        authDAO.deleteAuth(ponderosaData.authToken());
        assertNull(authDAO.getAuth(ponderosaData.authToken()));
        AuthData willowResult = authDAO.getAuth(willowData.authToken());
        assertEquals("willow", willowResult.username());
    }

    @Test
    void clearAuth() {
        AuthData ponderosaData = authDAO.createAuth("ponderosa");
        authDAO.clearAuth();
        assertNull(authDAO.getAuth(ponderosaData.authToken()));
    }

    @Test
    void getAuth() {
        AuthData ponderosaData = authDAO.createAuth("ponderosa");
        AuthData result = authDAO.getAuth(ponderosaData.authToken());
        assertEquals("ponderosa", result.username());
    }

    @Test
    void getAuthBadData() {
        AuthData result = authDAO.getAuth("I don't exist");
        assertNull(result);
    }
}