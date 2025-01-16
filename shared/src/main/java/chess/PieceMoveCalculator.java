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

class BishopMoveCalculator extends PieceMoveCalculator {

    public BishopMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        ChessMove move = new ChessMove(myPosition, endPosition, null);
        // up and right
        while(endPosition.getRow() < 8 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() < 8 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        return possibleMoves;
    }
}

class RookMoveCalculator extends PieceMoveCalculator {

    public RookMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up
        while(endPosition.getRow() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        return possibleMoves;
    }
}

class QueenMoveCalculator extends PieceMoveCalculator {

    public QueenMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up
        while(endPosition.getRow() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn());
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow(), endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // up and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() < 8 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // up and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() < 8 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() + 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and right
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() < 8) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() + 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        // down and left
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        while(endPosition.getRow() > 1 && endPosition.getColumn() > 1) {
            endPosition = new ChessPosition(endPosition.getRow() - 1, endPosition.getColumn() - 1);
            if(board.getPiece(endPosition) == null){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else if (board.getPiece(endPosition).getTeamColor() != pieceColor) {
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                break;
            } else {break;}
        }
        return possibleMoves;
    }
}

class KnightMoveCalculator extends PieceMoveCalculator {

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

class KingMoveCalculator extends PieceMoveCalculator {

    public KingMoveCalculator(ChessPiece.PieceType type, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        super(type, board, myPosition, pieceColor);

    }

    public Collection<ChessMove> getPossibleMoves() {
        Collection<ChessMove> possibleMoves = super.getPossibleMoves();
        ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
        // up
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn());
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