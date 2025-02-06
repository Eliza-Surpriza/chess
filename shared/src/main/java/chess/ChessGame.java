package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {
    public TeamColor currentTeam;
    public ChessBoard gameBoard;

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
        gameBoard.resetBoard();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            ChessGame clone = (ChessGame) super.clone();
            ChessBoard frog = clone.getBoard().clone();
            clone.setBoard(frog);
            clone.setTeamTurn(clone.getTeamTurn());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public ChessGame deepCopy() {
        try {
            return (ChessGame) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = gameBoard.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, startPosition);
        Collection<ChessMove> valid = new ArrayList<>();
        for (ChessMove move : possibleMoves) {
            // deep copy the chess game
            ChessGame gameCopy = this.deepCopy();
            gameCopy.setTeamTurn(piece.getTeamColor());
            // make move
            gameCopy.doMove(move);
            // check if in check/checkmate using methods
            if (!gameCopy.isInCheck(gameCopy.getTeamTurn())) {
                valid.add(move);
            }
        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = gameBoard.getPiece(move.startPosition);
        if (piece == null || piece.getTeamColor() != currentTeam) {
            throw new InvalidMoveException();
        }
        // hmm actually I don't think I need to check edges because I check if it's in valid moves.
        //move.getStartPosition().getRow() > 8 || move.getStartPosition().getColumn() < 1 || move.getEndPosition().getRow() > 8 || move.getEndPosition().getColumn() < 1 ||
        Collection<ChessMove> valid = validMoves(move.startPosition);
        if (valid.contains(move)) {
            doMove(move);
            if (currentTeam == TeamColor.WHITE) {
                this.setTeamTurn(TeamColor.BLACK);
            } else {this.setTeamTurn(TeamColor.WHITE);}
        } else {
            throw new InvalidMoveException();
        }
    }

    public void doMove(ChessMove move) {
        // do move
        ChessPiece piece = gameBoard.getPiece(move.startPosition);
        if (move.promotionPiece != null) {
            piece = new ChessPiece(currentTeam, move.promotionPiece);
        }
        gameBoard.addPiece(move.endPosition, piece);
        gameBoard.removePiece(move.startPosition);

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // for all the pieces on the other team:
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() != teamColor) {
                    // for all their valid moves:
                    Collection<ChessMove> moves = gameBoard.getPiece(position).pieceMoves(gameBoard, position);
                    for (ChessMove move : moves) {
                        // if it includes the square the king is on, return true
                        if (gameBoard.getPiece(move.endPosition) != null && gameBoard.getPiece(move.endPosition).getPieceType() == ChessPiece.PieceType.KING) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // if in check
        if (isInCheck(teamColor)) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {

                    // deep copy the chess board
                    // for all their valid moves:
                    // if not in check anymore, return false
                }
            }
            return true;
        } else {
            return false;
        }

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // for piece in team
        // if get valid moves method returns not empty collection, return false
        // return true
        // how to check if a collection is empty: .isEmpty()
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor) {
                    // for all their valid moves:
                    Collection<ChessMove> valid = validMoves(position);
                    if (!valid.isEmpty()) {return false;}
                }
            }
        }
        return true;

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return gameBoard;
    }
}
