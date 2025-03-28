package client;

import chess.*;
import client.ui.DrawChessBoard;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);

        // try drawing chessboard
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();
        DrawChessBoard.drawBoard(chessBoard, true);
        DrawChessBoard.drawBoard(chessBoard, false);
    }
}