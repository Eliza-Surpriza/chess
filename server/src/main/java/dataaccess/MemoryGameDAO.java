package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextId = 1;

    public GameData createGame(String gameName) {
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(nextId++, null, null, gameName, game);

        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public Collection<GameData> listGames() {
        return games.values();
    }

    public void clearGames() {
        games.clear();
    }

    public void updateGame(GameData gameData) {
        games.put(gameData.gameID(), gameData);
    }

}
