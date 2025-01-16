package chess;


import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {

    public final ChessPiece.PieceType type;
    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessGame.TeamColor pieceColor;
    private Collection<ChessMove> possibleMoves;

    public PieceMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
        this.type = type;
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        this.possibleMoves = new ArrayList<ChessMove>();
    }

    public Collection<ChessMove> getPossibleMoves() {
        return possibleMoves;
    }
}

class BishopMoveCalculator extends PieceMoveCalculator {

    public BishopMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessMove move = new ChessMove(myPosition, endPosition, null);
        // up and right
        while(endPosition.getRow() < 8 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() < 8 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        return possibleMoves;
    }
}