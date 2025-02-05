package chess;

import java.util.Collection;

public class QueenMoveCalculator extends PieceMoveCalculator{
    public QueenMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        // up
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 1, 0);
        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 0, 1);
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, -1, 0);
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addMoveLoop(endPosition, 0, -1);
        // up and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
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
