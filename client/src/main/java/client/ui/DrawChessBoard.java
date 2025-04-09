package client.ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;
import static client.ui.EscapeSequences.*;

public class DrawChessBoard {
    public static final String EDGE_COLOR = SET_BG_COLOR_BLUE;
    private static ChessPosition moveFrom;
    private static Collection<ChessMove> moveTo;


    public static void drawBoard(ChessBoard chessBoard, boolean upsideDown, ChessPosition moveFrom, Collection<ChessMove> moveTo) {
        DrawChessBoard.moveFrom = moveFrom;
        DrawChessBoard.moveTo = moveTo;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        drawHeader(out, upsideDown);
        if (upsideDown) {
            upsideDownLoop(out, chessBoard);
        } else {
            rightSideUpLoop(out, chessBoard);
        }
        drawHeader(out, upsideDown);
    }

    static void upsideDownLoop(PrintStream out, ChessBoard chessBoard) {
        for (int i = 1; i <= 8; i++) {
            String lineNum = " " + i + " ";
            drawSquare(out, lineNum, EDGE_COLOR);
            for (int j = 8; j >= 1; j--) {
                boardSquare(i, j, chessBoard, out);
            }
            drawSquare(out, lineNum, EDGE_COLOR);
            newLine(out);
        }
    }

    static void rightSideUpLoop(PrintStream out, ChessBoard chessBoard) {
        for (int i = 8; i >= 1; i--) {
            String lineNum = " " + i + " ";
            drawSquare(out, lineNum, EDGE_COLOR);
            for (int j = 1; j <= 8; j++) {
                boardSquare(i, j, chessBoard, out);
            }
            drawSquare(out, lineNum, EDGE_COLOR);
            newLine(out);
        }
    }

    static void boardSquare(int i, int j, ChessBoard chessBoard, PrintStream out) {
        String color = ((i + j) % 2 == 0) ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_WHITE;
        if (moveFrom != null && i == moveFrom.getRow() && j == moveFrom.getColumn()) {
            color = SET_BG_COLOR_MAGENTA;
        }
        if (inCollection(i, j, moveTo)) {
            color = SET_BG_COLOR_GREEN;
        }
        ChessPosition position = new ChessPosition(i, j);
        drawSquare(out, chooseString(chessBoard.getPiece(position)), color);
    }

    static boolean inCollection(int i, int j, Collection<ChessMove> collection) {
        if (collection == null) {
            return false;
        }
        for (ChessMove chessMove : collection) {
            if (chessMove.getEndPosition().getRow() == i && chessMove.getEndPosition().getColumn() == j) {
                return true;
            }
        }
        return false;
    }

    static void newLine(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.println();
    }

    static String chooseString(ChessPiece chessPiece) {
        if (chessPiece == null) {return EMPTY;}
        ChessGame.TeamColor color = chessPiece.getTeamColor();
        return switch (chessPiece.getPieceType()) {
            case QUEEN -> (color == WHITE) ? WHITE_QUEEN : BLACK_QUEEN;
            case KING -> (color == WHITE) ? WHITE_KING : BLACK_KING;
            case BISHOP -> (color == WHITE) ? WHITE_BISHOP : BLACK_BISHOP;
            case KNIGHT -> (color == WHITE) ? WHITE_KNIGHT : BLACK_KNIGHT;
            case ROOK -> (color == WHITE) ? WHITE_ROOK : BLACK_ROOK;
            case PAWN -> (color == WHITE) ? WHITE_PAWN : BLACK_PAWN;
        };
    }

    static void drawSquare(PrintStream out, String contents, String color) {
        out.print(color);
        out.print(contents);
    }

    static void drawHeader(PrintStream out, boolean upsideDown) {
        drawSquare(out, EMPTY, EDGE_COLOR);
        String[] letters = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        if (upsideDown) {
            letters = new String[]{ " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a " };
        }
        for (String letter : letters) {
            drawSquare(out, letter, EDGE_COLOR);
        }
        drawSquare(out, EMPTY, EDGE_COLOR);
        newLine(out);
    }



}
