package application;

import java.util.InputMismatchException;
import java.util.Scanner;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
	
	public static void clearScreen() { //limpa a tela
		 System.out.print("\033[H\033[2J");
		 System.out.flush();
	} 

	public static ChessPosition readChessPosition(Scanner sc) { //recebe o scanner da main e faz a leitura com ele
		try {
			String s = sc.nextLine();
			char column = s.charAt(0); //pega o primeiro caractere da string a1, b1, c1 (a, b, c)
			int row = Integer.parseInt(s.substring(1)); //recorta o segundo caractere da string a1 b1 c1 (1, 1, 1) e transforma 
			return new ChessPosition(column, row);		//em int, pra isso serve o "parse"
		}catch(RuntimeException e) {
			throw new InputMismatchException("Erro instanciando posições do xadrez, valores válidos a1 até h8");
		}
	}
	
	public static void printMatch(ChessMatch chessMatch) {
		printBoard(chessMatch.getPieces());
		System.out.println();
		System.out.println("Rodada: " + chessMatch.getTurn());
		System.out.println("Esperando jogador: " + chessMatch.getCurrentPlayer());
	}

	public static void printBoard(ChessPiece[][] pieces) { // imprime o tabuleiro na tela
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h ");
	}
	
	public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) { // imprime o tabuleiro na tela
		for (int i = 0; i < pieces.length; i++) {
			System.out.print((8 - i) + " ");
			for (int j = 0; j < pieces.length; j++) {
				printPiece(pieces[i][j], possibleMoves[i][j]); //pinta o fundo dependendo dessa variavel
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h ");
	}
	
	private static void printPiece(ChessPiece piece, boolean backGround) { // caso tenha uma peça naquela posição, ela é colocada
		if(backGround) { //verifica se deve colorir ou não o fundo da peça
			System.out.print(ANSI_BLUE_BACKGROUND);
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getColor() == Color.WHITE) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.print(" ");
	}
}