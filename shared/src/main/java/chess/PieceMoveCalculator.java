package chess;


import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {

    public final ChessPiece.PieceType type;
    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessGame.TeamColor pieceColor;
    public Collection<ChessMove> possibleMoves;

    public PieceMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
        this.type = type;
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        this.possibleMoves = new ArrayList<>();
    }

    public void addMoveKnightKing(ChessPosition end) {
        if (board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor) {
            possibleMoves.add(new ChessMove(myPosition, end, null));
        }
    }

    public void addMoveLoop(ChessPosition end, int over, int up) {
        while(end.getRow() < 8 && end.getColumn() > 1 && end.getRow() > 1 && end.getColumn() < 8) {
            end = new ChessPosition(end.getRow() + over, end.getColumn() + up);
            if(board.getPiece(end) == null){
                possibleMoves.add(new ChessMove(myPosition, end, null));
            } else if (board.getPiece(end).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, end, null));
                break;
            } else {break;}
        }
    }

    public Collection<ChessMove> getPossibleMoves() {
        return possibleMoves;
    }
}