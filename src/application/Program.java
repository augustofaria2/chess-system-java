package application;

import chess.ChessMatch;

public class Program {

	public static void main(String[] args) {
		
		ChessMatch chessMatch = new ChessMatch(); //gera o tabuleiro
		UI.printBoard(chessMatch.getPieces()); //coloca as peças no tabuleiro
	}
}
