package chess;


import java.util.Collection;

public class PieceMoveCalculator {

    private final ChessPiece.PieceType type;
    private final ChessBoard board;
    private final ChessPosition myPosition;
    public Collection<ChessMove> possibleMoves;

    public PieceMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition){
        this.type = type;
        this.board = board;
        this.myPosition = myPosition;
    }

    public Collection<ChessMove> getPossibleMoves() {
        return possibleMoves;
    }
}

class BishopMoveCalculator extends PieceMoveCalculator {

    public BishopMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition) {
        super(type, board, myPosition);
    }
}