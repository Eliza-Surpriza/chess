package chess;

import java.util.Collection;

public class BishopMoveCalculator extends PieceMoveCalculator {
    public BishopMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        diagonalLines();
        return possibleMoves;
    }
}
