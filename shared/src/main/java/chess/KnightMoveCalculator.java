package chess;

import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator{
    public KnightMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        // up and right narrow
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, 2, 1);
        // down and right narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, -2, 1);
        // down and left narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, -2, -1);
        // up and left narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, 2, -1);
        // up and right wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, 1, 2);
        // down and right wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, -1, 2);
        // down and left wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, -1, -2);
        // up and left wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        addOneMove(endPosition, 1, -2);

        return possibleMoves;
    }
}
