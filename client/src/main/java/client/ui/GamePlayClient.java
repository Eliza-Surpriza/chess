package client.ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.ServerFacade;
import client.ServerMessageObserver;
import model.CreateRequest;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.*;

import static client.ui.DrawChessBoard.drawBoard;

public class GamePlayClient implements Client {

    private final ServerFacade server;
    private final Repl repl;


    public GamePlayClient(String serverUrl, Repl repl) throws IOException {
        server = new ServerFacade(serverUrl, repl);
        this.repl = repl;
    }

    public String eval(String input) throws IOException {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "redraw" -> redraw(null, null);
            case "leave" -> leave();
            case "move" -> move(params);
            case "resign" -> resign();
            case "highlight" -> highlight(params);
            default -> help();
        };
    }

    public String move(String ... params) throws IOException {
        if (repl.color != repl.game.currentTeam) {
            throw new IOException("wait for your turn");
        }
        if (params.length >= 2) {
            ChessPosition startPosition = readPosition(params[0]);
            ChessPosition endPosition = readPosition(params[1]);
            ChessPiece.PieceType promoPiece = ((params.length == 3) ? getPieceFromString(params[2]) : null);
            ChessMove move = new ChessMove(startPosition, endPosition, promoPiece);
            server.makeMove(repl.authToken, repl.gameData.gameID(), move);
            // ok all of the following actually goes in the server lol
//            try {
//                repl.gameData.game().makeMove(move);
//            } catch(chess.InvalidMoveException e) {
//                throw new IOException("Invalid move. Try highlighting valid moves.");
//            }
//            boolean inCheck = repl.gameData.game().isInCheck(repl.color);
//            boolean inCheckmate = repl.gameData.game().isInCheckmate(repl.color);
//            boolean inStalemate = repl.gameData.game().isInStalemate(repl.color);
            // use websocket to adjust game for everyone
            // redraw board (maybe highlighted?) but also do this for everyone
            // golly I think I did a lot here that I was supposed to actually do in the websocket server.
            // I think I'll give up on highlighting the squares for a move

//            Collection<ChessMove> moveTo = Collections.singletonList(move);
//            redraw(startPosition, moveTo);
            // I think this also turns into a websocket message lol
            return "moving from " + params[0] + " to " + params[1];
        }
        throw new IOException("Expected: highlight b4 (or other position)");
    }

    public ChessPiece.PieceType getPieceFromString(String piece) throws IOException {
        return switch(piece) {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "rook" -> ChessPiece.PieceType.ROOK;
            default -> throw new IOException("Enter promotion piece in lowercase, like \"queen\"");
        };
    }

    public String resign() throws IOException {
        if (repl.color == null) {
            throw new IOException("Only players can resign. If you are done observing, type \"leave\"");
        }
        System.out.print("Type \"yes\" to confirm your desire to resign and forfeit the game\n" + ">>> ");
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        if (Objects.equals(line, "yes")) {
            server.resign(repl.authToken, repl.gameData.gameID());
            return "";
        }
        return "Ok, you can now continue gameplay.";
    }

    public String leave() throws IOException {
        server.leave(repl.authToken, repl.gameData.gameID());
        repl.isInGame = false;
        repl.game = null;
        // add call to websocket to notify others
        // also in websocket set player to null (make sure to check if player is observer)
        return "left game";
    }

    public String redraw(ChessPosition moveFrom, Collection<ChessMove> moveTo) {
        boolean upsideDown = (Objects.equals(repl.color, ChessGame.TeamColor.BLACK));
        drawBoard(repl.game.gameBoard, upsideDown, moveFrom, moveTo);
        return "";
    }

    public String highlight(String ... params) throws IOException {
        if (params.length == 1) {
            ChessPosition chessPosition = readPosition(params[0]);
            Collection<ChessMove> moveTo = repl.game.validMoves(chessPosition);
            redraw(chessPosition, moveTo);
            return "valid moves for " + params[0] + "\n";
        }
        throw new IOException("Expected: highlight b4 (or other position)");
    }

    private ChessPosition readPosition(String position) throws IOException {
        try {
            int row = Character.getNumericValue(position.charAt(1));
            if (row < 1 || row > 8) {
                throw new IOException("write chess position as \"b4\" or \"e8\"");
            }
            int col = switch(position.charAt(0)) {
                case 'a' -> 1;
                case 'b' -> 2;
                case 'c' -> 3;
                case 'd' -> 4;
                case 'e' -> 5;
                case 'f' -> 6;
                case 'g' -> 7;
                case 'h' -> 8;
                default -> throw new IOException("write chess position as \"b4\" or \"e8\"");
            };
            return new ChessPosition(row, col);
        } catch (Exception e) {
            throw new IOException("write chess position as \"b4\" or \"e8\"");
        }
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
