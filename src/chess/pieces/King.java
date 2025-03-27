package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece{ //rei
	
	private ChessMatch chessMatch; //acrescento uma dependência para a partida

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch; //chamo no construtor pra associar a partida ao rei
		}
	
	@Override
	public String toString() {
		return "R";
	}

	private boolean canMove(Position position) { //método para dizer se o rei pode mover pra x posição
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p == null || p.getColor() != getColor(); //posição vazia ou com peça diferente da cor do rei
	}
	
	private boolean testRookCastling(Position position) {//verifica se a torre está apta pra jogada roque
		ChessPiece p = (ChessPiece) getBoard().piece(position);
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
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
		
		//special move castling
		if(getMoveCount() == 0 && !chessMatch.getCheck()) {
			//roque pequeno
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3); //sempre 3 colunas à direita do rei
			if(testRookCastling(posT1)) {
				Position p1 = new Position(position.getRow(), position.getColumn() + 1); //primeira casa à direita do rei
				Position p2 = new Position(position.getRow(), position.getColumn() + 2); //segunda casa à direita do rei
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null) { //se n houver peças nessas posições pode fazer o roque
					mat[position.getRow()][position.getColumn() + 2] = true;
					//pega a posição do rei + 2 colunas, onde ele fica após o roque
				}
			}
			//roque grande
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4); //sempre 4 colunas à esquerda do rei
			if(testRookCastling(posT2)) {
				Position p1 = new Position(position.getRow(), position.getColumn() - 1); //primeira casa à esquerda do rei
				Position p2 = new Position(position.getRow(), position.getColumn() - 2); //segunda casa à esquerda do rei
				Position p3 = new Position(position.getRow(), position.getColumn() - 3); //terceira casa à esquerda do rei
				if(getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) { //se n houver peças nessas posições pode fazer o roque
					mat[position.getRow()][position.getColumn() - 2] = true;
					//pega a posição do rei - 2 colunas, onde ele fica após o roque
				}
			}
		}
		
		return mat; //como se o rei estivesse preso, já q tudo é falso, não há movimentos possíveis
	}

}
