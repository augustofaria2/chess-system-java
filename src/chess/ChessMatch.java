package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {// coração do sistema, regras.
	private Board board;

	public ChessMatch() {
		board = new Board(8, 8); // dimensões do tabuleiro de xadrez
		inicialSetup(); // coloca as peças
	}

	public ChessPiece[][] getPieces() { // retorna uma matriz de peças de xadrez para essa partida
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j); // interpreta como ChessPiece e n Piece. (downcasting)
			}
		}
		return mat;
	}
	
	/*
	 * para instanciar (colocar) as peças de xadrez informando as coordenadas no
	 * sistema de xadrez (coluna x linha) ao invés do sistema de matriz que fica
	 * "confuso"
	 */
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		
		validateSourcePosition(source);
		Piece capturedPiece = makeMove(source, target); //operação por realizar o movimento da peça (formato de matriz os parametros)
		return (ChessPiece) capturedPiece; //downcasting pra ChessPiece porque a peça capturada era de tipo Piece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //retiro a peça da posição de origem
		Piece capturedPiece = board.removePiece(target); //removo a possível peça da posição de destino e por padrão ela é a peça capturada
		board.placePiece(p, target); //movo a peça retirada na origem pra posição de destino
		return capturedPiece;
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça no posição de origem");
		}
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}

	private void inicialSetup() { // agora usamos as coordenadas de xadrez e não mais a de matriz
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));

		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
	}
}
