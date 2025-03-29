package client.ui;

import client.ServerFacade;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static client.ui.DrawChessBoard.drawBoard;

public class GamePlayClient implements Client{

    private final ServerFacade server;
    private final Repl repl;


    public GamePlayClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
        this.repl = repl;

    }

    public String eval(String input) throws IOException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> redraw();
//            case "leave" -> leave();
//            case "move" -> move(params);
//            case "resign" -> resign();
//            case "highlight" -> highlight(params);
            default -> help();
        };
    }

    public String redraw() {
        boolean upsideDown = (Objects.equals(repl.color, "BLACK"));
        drawBoard(repl.gameData.game().gameBoard, upsideDown);
        return "";
    }

    public String help() {
        return """
                - "redraw" (print chess board again)
                - "move <start position> <end position> <promotion piece>" (make a move - only include promotion piece if moving pawn to edge of board)
                - "resign" (forfeit game)
                - "highlight <position>" (highlight legal moves for a position)
                - "leave" (leave game, return to previous menu)
                - "help" (print this menu again)
                """;
    }
}
