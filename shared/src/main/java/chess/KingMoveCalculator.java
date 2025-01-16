package chess;

import java.util.Collection;

public class KingMoveCalculator extends PieceMoveCalculator {

    public KingMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up
        if(myPosition.getRow() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // up and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 8 && myPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 8 && myPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1 && myPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1 && myPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }
}