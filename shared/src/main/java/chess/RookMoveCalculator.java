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
        addMoveLoop(endPosition, 0, 1);
        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 1, 0);
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 0, -1);
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, 0);
        return possibleMoves;
    }
}