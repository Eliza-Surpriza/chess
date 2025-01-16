package chess;


import java.util.ArrayList;
import java.util.Collection;

public class PieceMoveCalculator {

    public final ChessPiece.PieceType type;
    public final ChessBoard board;
    public final ChessPosition myPosition;
    public final ChessGame.TeamColor pieceColor;
    public Collection<ChessMove> possibleMoves;

    public PieceMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
        this.type = type;
        this.board = board;
        this.myPosition = myPosition;
        this.pieceColor = pieceColor;
        this.possibleMoves = new ArrayList<ChessMove>();
    }

    public Collection<ChessMove> getPossibleMoves() {
        return possibleMoves;
    }
}