package chess;

import java.util.Collection;

public class PawnMoveCalculator extends PieceMoveCalculator{
    public PawnMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        int forwardOne = 1, firstRow = 2;
        if(pieceColor == ChessGame.TeamColor.BLACK){forwardOne = -1; firstRow = 7;}
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() + forwardOne <= 8 && myPosition.getRow() + forwardOne >= 1) {
            // forward one
            endPosition = new ChessPosition(endPosition.getRow() + forwardOne, endPosition.getColumn());
            if (board.getPiece(endPosition) == null) {
                if (myPosition.getRow() + forwardOne == 8 || myPosition.getRow() + forwardOne == 1) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                } else possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
            // capture left (update logic so you don't go out of bounds. check bounds first, then set new endPosition, then check for enemies)

            if(myPosition.getColumn() > 1){
                endPosition = new ChessPosition(myPosition.getRow() + forwardOne, myPosition.getColumn() - 1);
                if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != pieceColor) {
                    if (myPosition.getRow() + forwardOne == 8 || myPosition.getRow() + forwardOne == 1) {
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    } else possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
            // capture right
            if(myPosition.getColumn() < 8){
                endPosition = new ChessPosition(myPosition.getRow() + forwardOne, myPosition.getColumn() + 1);
                if(board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != pieceColor) {
                    if (myPosition.getRow() + forwardOne == 8 || myPosition.getRow() + forwardOne == 1) {
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                        possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                    } else possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
        // forward two
        if(myPosition.getRow() == firstRow){
            endPosition = new ChessPosition(myPosition.getRow() + 2 * forwardOne, myPosition.getColumn());
            ChessPosition penguin = new ChessPosition(myPosition.getRow() + forwardOne, myPosition.getColumn());
            if (board.getPiece(endPosition) == null && board.getPiece(penguin) == null) {
                endPosition = new ChessPosition(myPosition.getRow() + 2 * forwardOne, myPosition.getColumn());
                if (myPosition.getRow() + forwardOne == 8 || myPosition.getRow() + forwardOne == 1) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.QUEEN));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.BISHOP));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.KNIGHT));
                    possibleMoves.add(new ChessMove(myPosition, endPosition, ChessPiece.PieceType.ROOK));
                } else possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }
}
