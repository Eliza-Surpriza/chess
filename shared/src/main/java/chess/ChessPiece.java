package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public String toString() {
        String letter = switch (type) {
            case PieceType.KING -> "K";
            case PieceType.BISHOP -> "B";
            case PieceType.QUEEN -> "Q";
            case PieceType.KNIGHT -> "N";
            case PieceType.PAWN -> "P";
            case PieceType.ROOK -> "R";
        };
        if(pieceColor == ChessGame.TeamColor.BLACK){letter = letter.toLowerCase();}
        return letter;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        PieceMoveCalculator calculator = switch (type) {
            case PieceType.BISHOP -> new BishopMoveCalculator(type, board, myPosition, pieceColor);
            case PieceType.ROOK -> new RookMoveCalculator(type, board, myPosition, pieceColor);
            case PieceType.QUEEN -> new QueenMoveCalculator(type, board, myPosition, pieceColor);
            case PieceType.KNIGHT -> new KnightMoveCalculator(type, board, myPosition, pieceColor);
            case PieceType.KING -> new KingMoveCalculator(type, board, myPosition, pieceColor);
            case PieceType.PAWN -> new PawnMoveCalculator(type, board, myPosition, pieceColor);
        };

        return calculator.getPossibleMoves();
    }
}
