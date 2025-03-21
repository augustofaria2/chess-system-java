package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;
	
	public ChessPosition(char column, int row) {
		if(column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Erro instanciando posições do xadrez, valores válidos a1 até h8");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	/*No xadrez se fala a coluna primeiro e depois a linha (ex: A1)
	 *Posição A8 do tabuleiro, é a posição 0,0 da matriz, por isso 8 - row (chess) = row (matriz), 
	 *já que fica 8 - 8 = 0 (posição 0 na matriz), já para o A, seria column (chess) - 'a' = column (matriz),
	 *'a' (column) - 'a' = 0... gerando a matriz na posição [0][0].
	 */
	
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	
	@Override
	public String toString() {
		return "" + column + row; //"" pro compilador entender que é uma contenação de strings, 
								  //se não ele não aceita
	}
}
