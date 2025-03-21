package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {//coração do sistema, regras.
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8); //dimensões do tabuleiro de xadrez
		inicialSetup(); //coloca as peças
	}
	
	public ChessPiece[][] getPieces() { //retorna uma matriz de peças de xadrez para essa partida
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for(int i = 0; i < board.getRows(); i++) {
			for(int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i,j); //interpreta como ChessPiece e n Piece. (downcasting)
			}
		}
		return mat;
	}
	
	private void inicialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2,1)); /*Camada de board e não de chess,
		ou seja, funciona como matriz normal*/
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}
}
