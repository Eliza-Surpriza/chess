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


    public GamePlayClient(String serverUrl, Repl repl) {
        server = new ServerFacade(serverUrl);
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
        if (repl.color != repl.gameData.game().currentTeam) {
            throw new IOException("wait for your turn");
        }
        if (params.length >= 2) {
            ChessPosition startPosition = readPosition(params[0]);
            ChessPosition endPosition = readPosition(params[1]);
            ChessPiece.PieceType promoPiece = ((params.length == 3) ? getPieceFromString(params[2]) : null);
            ChessMove move = new ChessMove(startPosition, endPosition, promoPiece);
            try {
                repl.gameData.game().makeMove(move);
            } catch(chess.InvalidMoveException e) {
                throw new IOException("Invalid move. Try highlighting valid moves.");
            }
            boolean inCheck = repl.gameData.game().isInCheck(repl.color);
            boolean inCheckmate = repl.gameData.game().isInCheckmate(repl.color);
            boolean inStalemate = repl.gameData.game().isInStalemate(repl.color);
            // use websocket to adjust game for everyone
            // redraw board (maybe highlighted?) but also do this for everyone
            Collection<ChessMove> moveTo = Collections.singletonList(move);
            redraw(startPosition, moveTo);

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

    public String resign() {
        // set other player to be the winner
        // set the game to be over
        // send message to others
        return "resigning";
    }

    public String leave() {
        repl.isInGame = false;
        repl.gameData = null;
        // add call to websocket to notify others
        return "left game";
    }

    public String redraw(ChessPosition moveFrom, Collection<ChessMove> moveTo) {
        boolean upsideDown = (Objects.equals(repl.color, ChessGame.TeamColor.BLACK));
        drawBoard(repl.gameData.game().gameBoard, upsideDown, moveFrom, moveTo);
        return "";
    }

    public String highlight(String ... params) throws IOException {
        if (params.length == 1) {
            ChessPosition chessPosition = readPosition(params[0]);
            Collection<ChessMove> moveTo = repl.gameData.game().validMoves(chessPosition);
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
