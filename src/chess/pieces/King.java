package chess.pieces;

import boardgame.Board;
import boardgame.Position;
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

	private boolean canMove(Position position) { //método para dizer se o rei pode mover pra x posição
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor(); //posição vazia ou com peça diferente da cor do rei
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()]; /*cria uma matriz com as dimensões do tabuleiro.
		em que todas posições por padrão começa com falso.*/
		Position p = new Position(0,0);
		
		//acima
		p.setValues(position.getRow() - 1, position.getColumn()); //mesma coluna só que uma linha pra cima
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn() - 1); //mesma linha só que uma coluna pra esquerda
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1); //mesma linha só que uma coluna pra direita
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//abaixo
		p.setValues(position.getRow() + 1, position.getColumn()); //mesma coluna só que uma linha pra baixo
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal noroeste (norte, oeste)
		p.setValues(position.getRow() - 1, position.getColumn() - 1); //uma coluna pra esquerda e uma linha pra cima
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal nordeste (note, leste)
		p.setValues(position.getRow() - 1, position.getColumn() + 1); //uma coluna pra direita e uma linha pra cima
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal sudoeste (sul, oeste)
		p.setValues(position.getRow() + 1, position.getColumn() - 1); //uma coluna pra esquerda e uma linha pra baixo
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//diagonal sudeste (sul, leste)
		p.setValues(position.getRow() + 1, position.getColumn() + 1); //uma coluna pra direita e uma linha pra baixo
		if(getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		return mat; //como se o rei estivesse preso, já q tudo é falso, não há movimentos possíveis
	}

}
