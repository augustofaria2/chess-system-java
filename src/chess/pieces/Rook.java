package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Rook extends ChessPiece { //torre

	public Rook(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public String toString() {
		return "T";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0,0);
		
		//acima (linha na mesma coluna só que pra cima
		p.setValues(position.getRow() - 1, position.getColumn()); //position para acessar a posição da própria peça
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {//verifica se a posição existe e se não tem peça lá já
			mat[p.getRow()][p.getColumn()] = true; //altera pra verdadeiro a posição acima da nossa na matriz, indicando q a peça pode ser movida pra lá
			p.setRow(p.getRow()-1); //repete enquanto existir caminho livre pra peça naquela coluna
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {//verifica se a posição existe e se a peça q esta lá é inimiga
			mat[p.getRow()][p.getColumn()] = true; 
		}
		
		//esquerda
		p.setValues(position.getRow(), position.getColumn() - 1); //o que muda agora é a coluna pra esquerda 
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() - 1); 
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; 
		}
		
		//direita
		p.setValues(position.getRow(), position.getColumn() + 1); //o que muda agora é a coluna pra direita 
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() + 1); 
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; 
		}
		
		//abaixo
		p.setValues(position.getRow() + 1, position.getColumn()); //o que muda agora é a linha na mesma coluna só que pra baixo 
		while(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() + 1); 
		}
		if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true; 
		}
		
		return mat; //como se a torre estivesse presa, já q tudo é falso, não há movimentos possíveis
	}
}
