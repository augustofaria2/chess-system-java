package application;

import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch(); //gera o tabuleiro
		
		while(true) {
			UI.printBoard(chessMatch.getPieces()); //coloca as peças no tabuleiro
			System.out.println();
			System.out.println("Origem: ");
			ChessPosition source = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de origem
			
			System.out.println();
			System.out.println("Destino: ");
			ChessPosition target = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de destino
			
			ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
		}
	}
}
