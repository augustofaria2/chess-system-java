package boardgame;

public class Piece {
	
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
	
	
}
