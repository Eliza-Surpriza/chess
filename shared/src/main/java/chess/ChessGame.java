package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame implements Cloneable {
    public TeamColor currentTeam;
    public ChessBoard gameBoard;
    public boolean gameOver;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }
        return currentTeam == chessGame.currentTeam && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTeam, gameBoard);
    }

    public ChessGame() {
        this.currentTeam = TeamColor.WHITE;
        this.gameBoard = new ChessBoard();
        this.gameOver = false;
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

    public void endGame() {
        gameOver = true;
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
            ChessGame gameCopy = this.deepCopy();
            gameCopy.doMove(move);
            if (!gameCopy.isInCheck(piece.getTeamColor())) {
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
        Collection<ChessMove> valid = validMoves(move.startPosition);
        if (valid.contains(move)) {
            doMove(move);
            if (currentTeam == TeamColor.WHITE) {
                this.setTeamTurn(TeamColor.BLACK);
            } else {
                this.setTeamTurn(TeamColor.WHITE);
            }
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
            if (checkInnerLoop(row, teamColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkInnerLoop(int row, TeamColor teamColor) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition position = new ChessPosition(row, col);
            if (gameBoard.getPiece(position) != null
                    && gameBoard.getPiece(position).getTeamColor() != teamColor
                    && checkAllMoves(position)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkAllMoves(ChessPosition position) {
        Collection<ChessMove> moves = gameBoard.getPiece(position).pieceMoves(gameBoard, position);
        for (ChessMove move : moves) {
            if (gameBoard.getPiece(move.endPosition) != null
                    && gameBoard.getPiece(move.endPosition).getPieceType() == ChessPiece.PieceType.KING) {
                return true;
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
        return isInCheck(teamColor) && checkmateLoop(teamColor);

    }

    private boolean checkmateLoop(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            if (!checkmateInnerLoop(row, teamColor)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkmateInnerLoop(int row, TeamColor teamColor) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition position = new ChessPosition(row, col);
            if (gameBoard.getPiece(position) != null
                    && gameBoard.getPiece(position).getTeamColor() == teamColor) {
                return checkAllMoves(position, teamColor);
            }
        }
        return true;

    }

    private boolean checkAllMoves(ChessPosition position, TeamColor teamColor) {
        Collection<ChessMove> moves = gameBoard.getPiece(position).pieceMoves(gameBoard, position);
        for (ChessMove move : moves) {
            ChessGame gameCopy = this.deepCopy();
            gameCopy.doMove(move);
            if (!gameCopy.isInCheck(teamColor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        for (int row = 1; row <= 8; row++) {
            if(!stalemateInnerLoop(row, teamColor)) {
                return false;
            }
        }
        return true;
    }

    private boolean stalemateInnerLoop(int row, TeamColor teamColor) {
        for (int col = 1; col <= 8; col++) {
            if (!stalemateByPosition(row, col, teamColor)) {
                return false;
            }
        }
        return true;
    }

    private boolean stalemateByPosition(int row, int col, TeamColor teamColor) {
        ChessPosition position = new ChessPosition(row, col);
        if (gameBoard.getPiece(position) != null && gameBoard.getPiece(position).getTeamColor() == teamColor) {
            Collection<ChessMove> valid = validMoves(position);
            return valid.isEmpty();
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
