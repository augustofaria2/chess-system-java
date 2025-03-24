package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{ //rei

	public King(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()]; /*cria uma matriz com as dimensões do tabuleiro.
		em que todas posições por padrão começa com falso.*/
		return mat; //como se o rei estivesse preso, já q tudo é falso, não há movimentos possíveis
	}

}
