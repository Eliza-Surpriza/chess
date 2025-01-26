package chess;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator {

    public BishopMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up and right
        addMoveLoop(endPosition, 1, 1);
        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, 1);
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 1, -1);
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, -1);
        return possibleMoves;
    }
}