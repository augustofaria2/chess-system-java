package chess;

import boardgame.Board;

public class ChessMatch {//coração do sistema, regras.
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8); //dimensões do tabuleiro de xadrez
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
}
