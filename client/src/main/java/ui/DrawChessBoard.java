package ui;

public class DrawChessBoard {
    /*
     step 1. study tic tac toe

     receive a chess game object
     also receive instructions on if I should do upside down or not
     if we're not upside down:
     first: the top: empty abcdefgh empty
     then iterate through the rows 8 to 1
     for (int i = 8; i >= 1; i--) {
     draw row(i, chessboard) }
     then draw bottom: empty abcdefgh empty

     but if we are upside down:
     top: empty hgfedcba empty
     for (int i = 1; i <= 8; i++) {
     draw row(i, chessboard) }
     bottom: empty hgfedcba empty

     except that for top, draw row is
     for (int j = 1; j <= 8; j++) {
         drawSquare(chessboard.getPiece(i,j));
    }
     and for bottom, draw row is:
     for (int j = 8; j >= 1; j--) {
     drawSquare(chessboard.getPiece(i,j));
    }

     hmmmmmm wait can I do the same iteration but switch the i and j???

     ok main function:
     do top separated by upsidedownness
     for (int j = 8; j >= 1; j--) {
         lineNum = i if normal, j if upside down (except I need to turn it into a string, not int)
         drawSquare(lineNum, edgeColor);
         for (int j = 1; j <= 8; j++) {
             color is dark if i + j is even:) can you do mod in java? probably:) I feel very smart for figuring that out!!
             else color is light
             if upside down: drawSquare(chooseString(chessboard.getPiece(j,i)), color);
             else: drawSquare(chooseString(chessboard.getPiece(i,j)), color);
         }
         drawSquare(lineNum, edgeColor);
     }
     do bottom separated by upsidedownness:)

     ok how does drawSquare work?
     I should actually just have it take a string and have a separate function find the string

     String chooseString(chessPiece) {
         if chessPiece is null, return EMPTY
         otherwise, do a switch:)
         color = piece.color (idk)
         character = switch chessPiece.pieceType:
         case queen: WHITE QUEEN if color == WHITE else BLACK QUEEN
         etc:)
         return character:)
     }

     void drawSquare(character, color) {
        set background space to color
        draw an empty space, then the character, then empty space
     }

     void drawHeaderNormal(upside down?) {
        drawSquare(empty, edgeColor);
        if upside down: loop through h -> a
        else: loop through a -> h
        drawSquare(empty, edgeColor);
     }

    */



}
