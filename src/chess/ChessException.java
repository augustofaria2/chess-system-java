package chess;

import boardgame.BoardException;

public class ChessException extends BoardException { //Não é mais RuntimeException, dessa forma ele trata melhor as exceções, já q captura ambos
	private static final long serialVersionUID = 1L; //número de serial padrão
	
	public ChessException(String msg) {
		super(msg);
	}
}
