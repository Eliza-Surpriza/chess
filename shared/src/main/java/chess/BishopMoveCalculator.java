package chess;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator{
    public BishopMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        // up and right
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 1, 1);
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, 1);
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, -1);
        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 1, -1);

        return possibleMoves;
    }
}
