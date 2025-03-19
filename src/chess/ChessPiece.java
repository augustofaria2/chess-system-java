package chess;

import boardgame.Board;
import boardgame.Piece;

public class ChessPiece extends Piece{
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {//apenas get para n permitir modificar a cor da peça, só acessa-la.
		return color;
	}
}
