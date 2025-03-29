package client.ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static chess.ChessGame.TeamColor.WHITE;
import static client.ui.EscapeSequences.*;

public class DrawChessBoard {
    public static final String edgeColor = SET_BG_COLOR_BLUE;
    private static Collection<ChessMove> toHighlight;


    public static void drawBoard(ChessBoard chessBoard, boolean upsideDown, Collection<ChessMove> toHighlight) {
        DrawChessBoard.toHighlight = toHighlight;
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
            drawSquare(out, lineNum, edgeColor);
            for (int j = 8; j >= 1; j--) {
                boardSquare(i, j, chessBoard, out);
            }
            drawSquare(out, lineNum, edgeColor);
            newLine(out);
        }
    }

    static void rightSideUpLoop(PrintStream out, ChessBoard chessBoard) {
        for (int i = 8; i >= 1; i--) {
            String lineNum = " " + i + " ";
            drawSquare(out, lineNum, edgeColor);
            for (int j = 1; j <= 8; j++) {
                boardSquare(i, j, chessBoard, out);
            }
            drawSquare(out, lineNum, edgeColor);
            newLine(out);
        }
    }

    static void boardSquare(int i, int j, ChessBoard chessBoard, PrintStream out) {
        String color = ((i + j) % 2 == 0) ? SET_BG_COLOR_DARK_GREEN : SET_BG_COLOR_WHITE;
        if (isInHighlight(i, j)) {
            color = SET_BG_COLOR_MAGENTA;
        }
        ChessPosition position = new ChessPosition(i, j);
        drawSquare(out, chooseString(chessBoard.getPiece(position)), color);
    }

    static boolean isInHighlight(int i, int j) {
        for (ChessMove chessMove : toHighlight) {
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
        drawSquare(out, EMPTY, edgeColor);
        String[] letters = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
        if (upsideDown) {
            letters = new String[]{ " h ", " g ", " f ", " e ", " d ", " c ", " b ", " a " };
        }
        for (String letter : letters) {
            drawSquare(out, letter, edgeColor);
        }
        drawSquare(out, EMPTY, edgeColor);
        newLine(out);
    }



}
