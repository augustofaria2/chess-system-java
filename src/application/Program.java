package application;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import chess.ChessException;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;

public class Program {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ChessMatch chessMatch = new ChessMatch(); //gera o tabuleiro
		List <ChessPiece> captured = new ArrayList<>();
		
		while(!chessMatch.getCheckMate()) { //trata as exceções e permite digitar novamente após o nextLine
			try {
				UI.clearScreen();
				UI.printMatch(chessMatch, captured); //coloca as peças no tabuleiro
				System.out.println();
				System.out.print("Origem: ");
				ChessPosition source = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de origem
				
				boolean[][] possibleMoves = chessMatch.possibleMoves(source);
				UI.clearScreen();
				UI.printBoard(chessMatch.getPieces(), possibleMoves); /*sobrecarga do printBoard, q imprime o tabuleiro e também 
				todas as posições possíveis de movimento da peça (coloridos)*/
				System.out.println();
				System.out.print("Destino: ");
				ChessPosition target = UI.readChessPosition(sc); //passa o scanner pro método, contendo a posição de destino
				
				ChessPiece capturedPiece = chessMatch.performChessMove(source, target);
				
				if(capturedPiece != null){
					captured.add(capturedPiece);
				}
				
				if(chessMatch.getPromoted() != null) {
					System.out.println("Digite a peça para promoção (B/C/T/D): ");
					String type = sc.nextLine().toUpperCase();
				while(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("D")) {
					System.out.println("Tipo inválido. Digite a peça para promoção (B/C/T/D): ");
					type = sc.nextLine().toUpperCase();
				}
					chessMatch.replacePromotedPiece(type);
				}
				
			}catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}
}
