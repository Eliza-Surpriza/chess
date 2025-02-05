package chess;


import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {
    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessGame.TeamColor pieceColor;
    public Collection<ChessMove> possibleMoves;

    public PieceMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        this.possibleMoves = new ArrayList<>();
    }

    public void addOneMove(ChessPosition end, int up, int over) {
        if(end.getRow() + over <= 8 && end.getRow() + over >= 1 && end.getColumn() + up <= 8 && end.getColumn() + up >= 1) {
            end = new ChessPosition(end.getRow() + over, end.getColumn() + up);
            if(board.getPiece(end) == null || board.getPiece(end).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, end, null));
            }
        }
    }

    public void addMoveLoop(ChessPosition end, int up, int over) {
        while(end.getRow() + over <= 8 && end.getRow() + over >= 1 && end.getColumn() + up <= 8 && end.getColumn() + up >= 1) {
            end = new ChessPosition(end.getRow() + over, end.getColumn() + up);
            if(board.getPiece(end) == null) {
                possibleMoves.add(new ChessMove(myPosition, end, null));
            } else if (board.getPiece(end).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, end, null));
                break;
            } else {
                break;
            }
        }
    }

    public Collection<ChessMove> getPossibleMoves() {
        return possibleMoves;
    }
}