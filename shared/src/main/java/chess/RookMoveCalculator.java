package chess;

import java.util.Collection;

public class RookMoveCalculator extends PieceMoveCalculator {
    public RookMoveCalculator(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor color) {
        super(board, myPosition, color);
    }

    public Collection<ChessMove> getPossibleMoves() {
        straightLines();
        return possibleMoves;
    }
}
