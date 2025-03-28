package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {// coração do sistema, regras.
	private Board board;
	private int turn;
	private Color currentPlayer;
	private boolean check; //por padrão inicia como falso
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>(); //aceita todo tipo de peça
	private List<Piece> capturedPieces = new ArrayList<>();

	public ChessMatch() { 
		board = new Board(8, 8);  // dimensões do tabuleiro de xadrez
		turn = 1;
		currentPlayer = Color.WHITE;
		inicialSetup(); // coloca as peças
	}
	
	public int getTurn() {
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return promoted;
	}
	
	public ChessPiece[][] getPieces() { // retorna uma matriz de peças de xadrez para essa partida
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows(); i++) {
			for (int j=0; j<board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j); // interpreta como ChessPiece e n Piece. (downcasting)
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) { //para imprimir as posições possíveis a partir de uma de origem
		Position position = sourcePosition.toPosition(); //converte a posição de xadrez, para uma posição de matriz normal
		validateSourcePosition(position);//valida assim q o usuário entra com ela
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
		
		if (testCheck(currentPlayer)) { //verifica se o jogador atual se colocou em xeque
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em xeque");
		}
		
		ChessPiece movedPiece = (ChessPiece) board.piece(target); //peça movida é referênciada
		
		//promoção do peão
		promoted = null;//garantir q é sempre uma nova peça
		if (movedPiece instanceof Pawn) { 
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target); //jogo o peão como promoted
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false; //verifica se o oponente n ficou em xeque com a jogada

		if (testCheckMate(opponent(currentPlayer))) { //testa se a jogada foi xeque-mate
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		//en passant
		if(movedPiece instanceof Pawn && (target.getRow() == source.getRow() + 2 || target.getRow() == source.getRow() - 2)){ //duas pra cima ou pra baixo
			enPassantVulnerable = movedPiece;
		}else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece) capturedPiece; //downcasting pra ChessPiece porque a peça capturada era de tipo Piece;
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if(promoted == null) {
			throw new IllegalStateException("Não há peça para ser promovida");
		}
		if(!type.equals("B") && !type.equals("C") && !type.equals("T") && !type.equals("D")) {
			return promoted;
		}
		Position pos = promoted.getChessPosition().toPosition(); ;//posição da peça promovida
		Piece p = board.removePiece(pos); //removo a peça q estava naquela posição e guardei no p
		piecesOnTheBoard.remove(p); //removi ela da lista de peças no tabuleiro
		
		ChessPiece newPiece = newPiece(type, promoted.getColor()); //instancio o método
		board.placePiece(newPiece, pos); //coloco a nova peça na posição da antigo peão
		piecesOnTheBoard.add(newPiece); //coloco a nova peça na lista de peças no tabuleiro
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) { //pra testar qual peça o usuário quer
		if(type.equals("B")) {
			return new Bishop(board, color);
		}else if(type.equals("C")) {
			return new Knight(board, color);
		}else if(type.equals("Q")) {
			return new Queen(board, color);
		}else {
			return new Rook(board, color); //falhou todos, é torre
		}
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.removePiece(source); //retiro a peça da posição de origem
		p.increaseMoveCount(); //fiz downcast pra facilitar, n altera em nd o código pq acontece o upcast de p no placePiece
		Piece capturedPiece = board.removePiece(target); //removo a possível peça da posição de destino e por padrão ela é a peça capturada
		board.placePiece(p, target); //movo a peça retirada na origem pra posição de destino
		
		if(capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece); //retira da lista de peças no tabuleiro
			capturedPieces.add(capturedPiece); //acrescenta na lista de peças capturadas
		}
		
		//roque pequeno
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) { //mover a torre no roque
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT); //removo ela daquela posição
			board.placePiece(rook, targetT); //coloco ela na posição após o roque
			rook.increaseMoveCount(); //incremento o número de movimentos da torre
		}
		//roque grande
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { 
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceT); 
			board.placePiece(rook, targetT); 
			rook.increaseMoveCount(); 
		}
		
		//en passant
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == null) { //diagonal mas n capturou peça = en passant
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn()); //está abaixo
				}else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn()); //está acima
				}
				capturedPiece = board.removePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece; //retorno a peça capturada
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.removePiece(target); //retiro a peça que movi ao destino
		p.decreaseMoveCount();
		board.placePiece(p, source);
		
		if(capturedPiece != null) { //retorno a peça capturada pra posição q ela estava
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		//roque pequeno
		if(p instanceof King && target.getColumn() == source.getColumn() + 2) { 
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT); 
			board.placePiece(rook, sourceT); 
			rook.decreaseMoveCount(); 
		}
		//roque grande
		if(p instanceof King && target.getColumn() == source.getColumn() - 2) { 
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetT); 
			board.placePiece(rook, sourceT); 
			rook.decreaseMoveCount(); 
		}
		
		//en passant
		if(p instanceof Pawn) {
			if(source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) { //peça capturada foi a vulneravel
				ChessPiece pawn = (ChessPiece) board.removePiece(target);
				Position pawnPosition;
				if(p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn()); //posição de inicio
				}else {
					pawnPosition = new Position(4, target.getColumn()); //posição de inicio
				}
				board.placePiece(pawn, pawnPosition);
				//n precisa da troca de listas pq o algoritmo generico ja trocou de listas a peça capturada
			}
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
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		//downcast pq Piece n tem cor, só ChessPiece
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece) p; //downcast dnv
			}
		}
		throw new IllegalStateException("Não existe um rei da cor " + color + " no tabuleiro"); /*nem trato no programa principal, pq nem deveria
		acontecer. Se acontecer, o tabuleiro está com algum problema.*/
	}
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();//pego a posição no formato de matriz
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves(); //matriz de movimentos possiveis da peça adversária p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) { 
				return true; //se o elemento da matriz for verdadeiro, o rei esta em xeque
			}
		}
		return false;
	}
		
	private boolean testCheckMate(Color color) { 
		if (!testCheck(color)) { //possibilidade dele n estar em xeque, se n esta em xeque, n esta em xeque-mate
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows(); i++) {
				for (int j=0; j<board.getColumns(); j++) {
					if (mat[i][j]) { //testa se a posição tira do xeque
						Position source = ((ChessPiece)p).getChessPosition().toPosition(); /*pega a posição de origem com downcast para
						ChessPiece pq ChessMatch n pode usar o atributo protected position e converte em matriz com toPosition */
						Position target = new Position(i, j); //posição i,j de destino (movimento possivel)
						Piece capturedPiece = makeMove(source, target); //faço a peça p (q agora é capturedPiece) mover da origem pro destino
						boolean testCheck = testCheck(color); //testo se ainda está em xeque
						undoMove(source, target, capturedPiece); //desfaço o movimento, pq ele foi feito somente pra testar
						if (!testCheck) { //quer dizer q tirou do xeque, portanto, retorna falso
							return false;
						}
					}
				}
			}
		}
		return true;
	}	
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void inicialSetup() { // agora usamos as coordenadas de xadrez e não mais a de matriz
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this)); //autoreferência a própria partida que estou
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));
	}
}