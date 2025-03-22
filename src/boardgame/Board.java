package boardgame;

public class Board {
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	public Board(int rows, int columns) {
		if(rows < 1 || columns < 1) {
			throw new BoardException("Erro criando tabuleiro: é necessário ter ao menos 1 linha e 1 coluna");
		}
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}
	
	public Piece piece(int row, int column) { //retorna a peça que esta na linha x coluna especificada
		if(!positionExists(row, column)) {
			throw new BoardException("Posição não está no tabuleiro");
		}
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { //retorna a peça que esta na posição[row][column]
		if(!positionExists(position)) {
			throw new BoardException("Posição não está no tabuleiro");
		}
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) { //coloca a peça naquela posição
		if(thereIsAPiece(position)) {
			throw new BoardException("Já existe uma peça na posição " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		if(!positionExists(position)) { //verifica se existe essa posição no tabuleiro
			throw new BoardException("Posição não está no tabuleiro");
		}
		if(piece(position) == null) { //não tem nenhuma peça nessa posição
			return null;
		}
		Piece aux = piece(position); 
		aux.position = null; //posição da peça aux é nula, ou seja, foi retirada do tabuleiro
		pieces[position.getRow()][position.getColumn()] = null; /*na matriz de peças, na posição 
		onde estou removendo, agora é nulo, indicando que não tem mais peça nessa posição da matriz.*/
		return aux;
	}
	
	private boolean positionExists(int row, int column) { //verifica se a posição existe por linha x coluna
		return row >= 0 && row < rows && column >= 0 && column < columns;
	}
	
	//sobrecarga de método
	public boolean positionExists(Position position) { //verifica se a posição existe dado uma posição
		return positionExists(position.getRow(), position.getColumn()); //reaproveita o método de cima
	}
	
	public boolean thereIsAPiece(Position position) {
		if(!positionExists(position)) {
			throw new BoardException("Posição não está no tabuleiro");
		}
		return piece(position) != null; //se for dif de nulo, quer dizer q tem uma peça nessa posição
	}
}
