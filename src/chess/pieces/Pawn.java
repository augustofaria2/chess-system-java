package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece { //peão
	
	public Pawn(Board board, Color color) {
		super(board, color);
	}
	
	@Override
	public boolean[][] possibleMoves(){
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		Position p = new Position(0,0);
		
		if(getColor() == Color.WHITE) {//só anda pra cima no tabuleiro
			//verifica se a primeira casa em frente está livre
			p.setValues(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			//verifica se a segunda casa a frente está livre
			p.setValues(position.getRow() - 2, position.getColumn()); 
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if(getBoard().positionExists(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//diagonal esquerda
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//diagonal direita
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}else if(getColor() == Color.BLACK) { //só anda pra baixo no tabuleiro
			//verifica se a primeira casa em frente está livre
			p.setValues(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			//verifica se a segunda casa a frente está livre
			p.setValues(position.getRow() + 2, position.getColumn()); 
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if(getBoard().positionExists(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0){
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//diagonal esquerda
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			
			//diagonal direita
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if(getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		return mat; //como se o peão estivesse preso, já q tudo é falso e n há movimentos possiveis
	}
	
	@Override
	public String toString() {
		return "P";
	}
}
