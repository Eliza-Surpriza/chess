package chess;

import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator{
    public PawnMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        int forwardOne = 1, firstRow = 2;
        ChessPiece.PieceType promote = null;
        if(pieceColor == ChessGame.TeamColor.BLACK){forwardOne = -1; firstRow = 7;}
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() + forwardOne <= 8 && myPosition.getRow() + forwardOne >= 1) {
            if (myPosition.getRow() + forwardOne == 8 || myPosition.getRow() + forwardOne == 1) {promote = ChessPiece.PieceType.QUEEN;}
            // actually, add moves with promotion piece as QUEEN, BISHOP, KNIGHT, and ROOK

            // forward one
            endPosition = new ChessPosition(endPosition.getRow() + forwardOne, endPosition.getColumn());
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, promote));
            }
            // capture left
            endPosition = new ChessPosition(myPosition.getRow() + forwardOne, myPosition.getColumn() - 1);
            if(board.getPiece(endPosition) != null && endPosition.getColumn() >= 1){
                if(board.getPiece(endPosition).getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, promote));
                }
            }
            // capture right
            endPosition = new ChessPosition(myPosition.getRow() + forwardOne, myPosition.getColumn() + 1);
            if(board.getPiece(endPosition) != null && endPosition.getColumn() <= 8){
                if(board.getPiece(endPosition).getTeamColor() != pieceColor) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, promote));
                }
            }
        }
        // forward two
        if(myPosition.getRow() == firstRow){
            endPosition = new ChessPosition(myPosition.getRow() + 2 * forwardOne, myPosition.getColumn());
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, promote));
            }
        }
        return possibleMoves;
    }
}
