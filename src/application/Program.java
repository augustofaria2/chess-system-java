package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch(); //gera o tabuleiro
		
		while(true) { //trata as exceções e permite digitar novamente após o nextLine
			try {
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces()); //coloca as peças no tabuleiro
				System.out.println();
				System.out.println("Origem: ");
				ChessPosition source = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de origem
				
				System.out.println();
				System.out.println("Destino: ");
				ChessPosition target = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de destino
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
			}catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
	}
}
