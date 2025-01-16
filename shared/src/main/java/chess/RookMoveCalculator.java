package chess;

import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {

    public RookMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up
        while(endPosition.getRow() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() - 1);
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