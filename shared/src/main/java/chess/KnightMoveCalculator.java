package chess;

import java.util.Collection;

public class KnightMoveCalculator extends PieceMoveCalculator {

    public KnightMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up and right narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 7 && myPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 2, endPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        // up and left narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 7 && myPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() + 2, endPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and right narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 2 && myPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() - 2, endPosition.getColumn() + 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and left narrow
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 2 && myPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 2, endPosition.getColumn() - 1);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // up and right wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 8 && myPosition.getColumn() < 7) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() + 2);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }

        // up and left wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() < 8 && myPosition.getColumn() > 2) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() - 2);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and right wide
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1 && myPosition.getColumn() < 7) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() + 2);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        if(myPosition.getRow() > 1 && myPosition.getColumn() > 2) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() - 2);
            if (board.getPiece(endPosition) == null) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }
}