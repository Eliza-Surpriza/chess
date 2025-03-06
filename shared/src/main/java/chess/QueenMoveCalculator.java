package chess;

import java.util.Collection;

public class QueenMoveCalculator extends PieceMoveCalculator {
    public QueenMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        straightLines();
        diagonalLines();
        return possibleMoves;
    }
}
