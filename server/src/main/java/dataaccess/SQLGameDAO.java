package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO extends SQLDAO implements GameDAO{
    public GameData createGame(String gameName) {
        return null;
        // copy from others
//        ChessGame game = new ChessGame();
//        GameData gameData = new GameData(nextId++, null, null, gameName, game);
        // figure out auto increment key
        // json ify
        // put in database (but first add another string to the database manager. do varchar(2000) for gamedata
    }

    public GameData getGame(int gameID) {
        return null;
        // copy get stuff from others
    }

    public Collection<GameData> listGames() {
        return List.of();
        // copy from petshop
    }

    public void updateGame(GameData gameData) {
        // hmm think about it for a bit
    }

    public void clearGames() {
        clearTable("games");
    }
}
