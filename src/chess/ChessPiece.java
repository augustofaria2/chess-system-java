package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {//apenas get para n permitir modificar a cor da peça, só acessa-la.
		return color;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position); //converte a posição de matriz para posição de xadrez
	}
	
	/*Está na classe genérica ChessPiece porque será reaproveitada em todas as outras peças, ela é protected porque deve ser acessada
	 *somente pelo mesmo pacote e pelas subclasses peças. 
	 */
	protected boolean isThereOpponentPiece(Position position) { //Pq mesmo tendo uma peça, se for adversária, nossa peça ainda pode se mover pra lá
		ChessPiece p = (ChessPiece) getBoard().piece(position); //Downcasting de Piece pra ChessPiece
		return p != null && p.getColor() != color; //retorna se as cores da peça p (target) é diferente da cor da peça onde estou(source)
	}
}
