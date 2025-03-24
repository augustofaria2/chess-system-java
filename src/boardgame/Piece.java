package boardgame;

public abstract class Piece {
	
	protected Position position; /*protected pq n é a posição do xadrez, é uma posição simples 
								  de matriz. N quero q ela seja visível na camada de xadrez.*/	
	private Board board; /*cada peça deve ser associada ao tabuleiro*/
	
	public Piece(Board board) {
		this.board = board;
		position = null; //n preciso passar de parâmetro para o construtor pq inicialmente ela será nula.
	}

	protected Board getBoard() { /*apenas o get, pq meu tabuleiro n deve ser alterado. Protected para 
								  ser acessado apenas pelas classes do msm pacote/subclasses.*/
		return board;
	}
	
	public abstract boolean[][] possibleMoves(); //retorna true ou false, n tem implementação e será definido nas subclasses (método abstrato)
	
	public boolean possibleMove(Position position) { //hook methods, basicamente um método que depende de um método abstrato para funcionar
		return possibleMoves()[position.getRow()][position.getColumn()];
		/*Esse método usa possibleMoves() para verificar se o movimento para uma posição é permitido*/
	}
	
	public boolean isThereAnyPossibleMove() {
		boolean[][] mat = possibleMoves();
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat.length; j++) {
				if(mat[i][j]) { //for verdade, retorna true porque existe então um movimento possível
					return true;
				}
			}
		}
		return false; //caso depois de percorrer a matriz, não exista um movimento possível
	}
}
