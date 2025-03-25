package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {// coração do sistema, regras.
	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check; //por padrão inicia como falso
	
	private List <Piece> piecesOnTheBoard; //aceita todo tipo de peça
	private List <Piece> capturedPieces;

	public ChessMatch() {
		board = new Board(8, 8); // dimensões do tabuleiro de xadrez
		turn = 1;
		currentPlayer = Color.WHITE;
		piecesOnTheBoard = new ArrayList<>();
		capturedPieces = new ArrayList<>();
		inicialSetup(); // coloca as peças
	}
	
	public int getTurn() {
		return turn;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
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
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){ //para imprimir as posições possíveis a partir de uma de origem
		Position position = sourcePosition.toPosition(); //converte a posição de xadrez, para uma posição de matriz normal
		validateSourcePosition(position); //valida assim q o usuário entra com ela
		return board.piece(position).possibleMoves();
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
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target); //operação por realizar o movimento da peça (formato de matriz os parametros)
		
		if(testCheck(currentPlayer)) { //verifica se o jogador atual se colocou em xeque
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em xeque");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false; //verifica se o oponente n ficou em xeque com a jogada
		
		nextTurn();
		return (ChessPiece) capturedPiece; //downcasting pra ChessPiece porque a peça capturada era de tipo Piece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source); //retiro a peça da posição de origem
		Piece capturedPiece = board.removePiece(target); //removo a possível peça da posição de destino e por padrão ela é a peça capturada
		board.placePiece(p, target); //movo a peça retirada na origem pra posição de destino
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece); //retira da lista de peças no tabuleiro
			capturedPieces.add(capturedPiece); //acrescenta na lista de peças capturadas
		}
		return capturedPiece; //retorno a peça capturada
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p = board.removePiece(target); //retiro a peça que movi ao destino
		board.placePiece(p, source);
		
		if(capturedPiece != null) { //retorno a peça capturada pra posição q ela estava
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	
	private void validateSourcePosition(Position position) {
		if(!board.thereIsAPiece(position)) {
			throw new ChessException("Não existe peça no posição de origem");
		}
		if(currentPlayer != ((ChessPiece) board.piece(position)).getColor()) { //faço downcast para ChessPiece e depois testo a cor
			throw new ChessException("A peça escolhida não é sua");
		}
		if(!board.piece(position).isThereAnyPossibleMove()) { //acessa o tabuleiro, acessa a peça na posição de origem e chama o teste de movimento possivel
			throw new ChessException("Não existe movimentos possíveis para a peça escolhida");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) { //usa o possibleMove para verificar se ela pode se mover da posição source -> target
			throw new ChessException("A peça escolhida não pode se mover para a posição de destino");
		}
	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK: Color.WHITE; //expressão condicional ternária
	}
	
	private Color opponent(Color color) { 
		return (color == Color.WHITE ? Color.BLACK : Color.WHITE);
	}
	
	private ChessPiece findKing(Color color) { //filtro usando lista
		List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		//downcast pq Piece n tem cor, só ChessPiece
		for(Piece p : list) {
			if(p instanceof King) {
				return (ChessPiece) p; //downcast dnv
			}
		}
		throw new IllegalStateException("Não existe um rei da cor " + color + " no tabuleiro"); /*nem trato no programa principal, pq nem deveria
		acontecer. Se acontecer, o tabuleiro está com algum problema.*/
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = findKing(color).getChessPosition().toPosition(); //pego a posição no formato de matriz
		List <Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for(Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); //matriz de movimentos possiveis da peça adversária p
			if(mat[kingPosition.getRow()][kingPosition.getColumn()]) { //se o elemento da matriz for verdadeiro, o rei esta em xeque
				return true;
			}
		}
		return false;
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
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
