import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public class Game {

	final int BOARD_SIZE = 8;

	Piece[][] Board;

	String status = "";

	Piece blank = new Piece("   ");

	Piece[] White_Pawn = new Piece[8];
	Piece[] White_Rook = new Piece[2];
	Piece[] White_Knight = new Piece[2];
	Piece[] White_Bishop = new Piece[2];

	Piece[] Black_Pawn = new Piece[8];
	Piece[] Black_Rook = new Piece[2];
	Piece[] Black_Knight = new Piece[2];
	Piece[] Black_Bishop = new Piece[2];

	Piece White_King = new Piece(false, false, "W-K");
	Piece White_Queen = new Piece(false, "W-Q");

	Piece Black_King = new Piece(false, false, "B-K");
	Piece Black_Queen = new Piece(false, "B-Q");

	int[] selected = new int[2];

	boolean isWTurn = true;
	boolean select = false;

	private static final int width = 115, height = 115;

	public static BufferedImage[] White, Black;

	GameGUI gui = new GameGUI();

	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("res/textures/Pieces/sheet.png"));

		Black = new BufferedImage[6];
		White = new BufferedImage[6];

		White[0] = sheet.crop(width * 0, 0, width, height);
		White[1] = sheet.crop(width * 1, 0, width, height);
		White[2] = sheet.crop(width * 2, 0, width, height);
		White[3] = sheet.crop(width * 3, 0, width, height);
		White[4] = sheet.crop(width * 4, 0, width, height);
		White[5] = sheet.crop(width * 5, 0, width, height);

		Black[0] = sheet.crop(width * 0, height, width, height);
		Black[1] = sheet.crop(width * 1, height, width, height);
		Black[2] = sheet.crop(width * 2, height, width, height);
		Black[3] = sheet.crop(width * 3, height, width, height);
		Black[4] = sheet.crop(width * 4, height, width, height);
		Black[5] = sheet.crop(width * 5, height, width, height);

	}

	public void spriteFill() {
		White_King.setSprite(White[0]);
		White_Queen.setSprite(White[1]);
		for (int i = 0; i < 2; i++) {
			White_Rook[i].setSprite(White[2]);
			White_Knight[i].setSprite(White[3]);
			White_Bishop[i].setSprite(White[4]);
		}

		for (int i = 0; i < 8; i++) {
			White_Pawn[i].setSprite(White[5]);
		}

		Black_King.setSprite(Black[0]);
		Black_Queen.setSprite(Black[1]);
		for (int i = 0; i < 2; i++) {
			Black_Rook[i].setSprite(Black[2]);
			Black_Knight[i].setSprite(Black[3]);
			Black_Bishop[i].setSprite(Black[4]);
		}
		for (int i = 0; i < 8; i++) {
			Black_Pawn[i].setSprite(Black[5]);
		}
	}

	Game() {

		init();
		String status = "inProgress";

		Board = new Piece[BOARD_SIZE][BOARD_SIZE];

		for (int i = 0; i < 2; i++) {
			Black_Rook[i] = new Piece(false, false, "B-R");
			Black_Knight[i] = new Piece(true, "B-N");
			Black_Bishop[i] = new Piece(false, "B-B");
			White_Rook[i] = new Piece(false, false, "W-R");
			White_Knight[i] = new Piece(true, "W-N");
			White_Bishop[i] = new Piece(false, "W-B");
		}

		Board[0][7] = White_Rook[0];
		Board[7][7] = White_Rook[1];
		Board[1][7] = White_Knight[0];
		Board[6][7] = White_Knight[1];
		Board[2][7] = White_Bishop[0];
		Board[5][7] = White_Bishop[1];
		Board[3][7] = White_Queen;
		Board[4][7] = White_King;

		Board[0][0] = Black_Rook[0];
		Board[7][0] = Black_Rook[1];
		Board[1][0] = Black_Knight[0];
		Board[6][0] = Black_Knight[1];
		Board[2][0] = Black_Bishop[0];
		Board[5][0] = Black_Bishop[1];
		Board[4][0] = Black_Queen;
		Board[3][0] = Black_King;

		for (int i = 0; i < BOARD_SIZE; i++) {
			Black_Pawn[i] = new Piece(false, "B-P", -1);
			Board[i][1] = Black_Pawn[i];

			White_Pawn[i] = new Piece(false, "W-P", 1);
			Board[i][6] = White_Pawn[i];
		}

		spriteFill();

		int x;
		int y;

		for (int i = 16; i < 48; i++) {
			x = i % 8;
			y = i / 8;
			Board[x][y] = blank;
		}

		printBoard();

	}

	public String getPieceName(int x, int y) {
		return Board[x][y].getName();
	}

	public void printBoard() {
		if (select) {
			GameGUI.render(Board, selected);
		} else {
			GameGUI.render(Board);
		}
	}

	public GameGUI getGUI() {
		return gui;
	}

	public void move(int x, int y) {
		int[] temp = new int[2];
		temp[0] = x;
		temp[1] = y;

		if (!select) {
			selected = temp;
			select = true;
			printBoard();
		} else {
			if (validMove(selected, temp, isWTurn)) {
				performTurn(selected, temp);
				isWTurn = !isWTurn;
			}
			select = false;
			printBoard();
		}
	}

	public boolean validMove(int[] first, int[] second, boolean isWTurn) {

		boolean check1, check2, check3, check4;
		boolean check5 = true;

		if (first[0] < 8 && first[0] > -1 && first[1] < 8 && first[1] > -1 && second[0] < 8 && second[0] > -1
				&& second[1] < 8 && second[1] > -1) {
			check1 = turnChecker(first, second, isWTurn);
			check2 = nofinalCollision(second, isWTurn);
			check3 = basicCheck(first, second);
			check4 = noCollision(first, second, isWTurn);
			if (Board[first[0]][first[1]].getSymbol().endsWith("K")) {
				check5 = check(second, isWTurn);
			}

			if (check1 && check2 && check3 && check4 && check5) {
				return true;
			}
			jout("Invalid Move");
		}

		return false;
	}

	public void performTurn(int[] start, int[] end) {
		Piece temp;

		temp = Board[start[0]][start[1]];
		Board[start[0]][start[1]] = blank;
		Board[end[0]][end[1]] = temp;
		if(Board[end[0]][end[1]].getSymbol().endsWith("P") && (end[1] == 7 || end[1] == 0)){
			if(Board[end[0]][end[1]].getSymbol().startsWith("W")){
				Piece Queen = new Piece(false, "W-Q");
				Board[end[0]][end[1]] = Queen;
				Queen.setSprite(White[1]);
			} else {
				Piece Queen = new Piece(false, "B-Q");
				Board[end[0]][end[1]] = Queen;
				Queen.setSprite(Black[1]);
			}
		}
	}

	public boolean turnChecker(int[] first, int[] second, boolean isWTurn) {
		int deltaX, deltaY;

		deltaX = second[0] - first[0];
		deltaY = second[1] - first[1];

		boolean check1;

		if (Board[first[0]][first[1]].getSymbol().startsWith("W") && isWTurn) {
			if (Board[first[0]][first[1]].getSymbol().endsWith("P")) {
				jout("pawn");
				check1 = validPawnMove(deltaX, deltaY, first, second, isWTurn);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("R")) {
				check1 = validRookMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("N")) {
				check1 = validKnightMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("B")) {
				check1 = validBishopMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("Q")) {
				check1 = validQueenMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("K")) {

				check1 = validKingMove(deltaX, deltaY, first);
			} else {
				return false;
			}
		} else if (Board[first[0]][first[1]].getSymbol().startsWith("B") && !isWTurn) {
			if (Board[first[0]][first[1]].getSymbol().endsWith("P")) {
				jout("pawn");
				check1 = validPawnMove(deltaX, deltaY, first, second, isWTurn);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("R")) {
				check1 = validRookMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("N")) {
				check1 = validKnightMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("B")) {
				check1 = validBishopMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("Q")) {
				check1 = validQueenMove(deltaX, deltaY);
			} else if (Board[first[0]][first[1]].getSymbol().endsWith("K")) {
				check1 = validKingMove(deltaX, deltaY, first);
			} else {
				return false;
			}
		} else {
			return false;
		}
		return check1;
	}

	public boolean nofinalCollision(int[] end, boolean isWturn) {
		if ((Board[end[0]][end[1]].getSymbol().startsWith("W") && isWturn)
				|| (Board[end[0]][end[1]].getSymbol().startsWith("B") && !isWturn)) {
			return false;
		}
		return true;
	}

	public boolean basicCheck(int[] start, int[] end) {
		if (end[0] > 8 || end[0] < 0 || end[1] > 8 || end[1] < 0) {
			return false;
		}
		return true;
	}

	public boolean noCollision(int[] start, int[] end, boolean isWTurn) {
		final int subX, subY;
		int tempX, tempY;

		if (Board[start[0]][start[1]].getSymbol().endsWith("N")) {
			return true;
		}

		tempX = start[0];
		tempY = start[1];

		if (end[0] - start[0] != 0) {
			subX = (start[0] - end[0]) / (Math.abs(start[0] - end[0]));
		} else {
			subX = 0;
		}

		if (end[1] - start[1] != 0) {
			subY = (start[1] - end[1]) / (Math.abs(start[1] - end[1]));
		} else {
			subY = 0;
		}

		for (int i = 0; i < 8; i++) {
			if (end[0] == tempX && end[1] == tempY) {
				return true;
			}
			if (end[0] != tempX) {
				tempX -= subX;
			}
			if (end[1] != tempY) {
				tempY -= subY;
			}
			if (!Board[tempX][tempY].getSymbol().endsWith(" ")) {
				if (end[0] == tempX && end[1] == tempY) {
					if (Board[tempX][tempY].getSymbol().startsWith("B") && isWTurn) {
						return true;
					} else if ((Board[tempX][tempY].getSymbol().startsWith("W") && !isWTurn)) {
						return true;
					} else {
						return false;
					}
				}
			}
		} // for loop
		return true;
	}

	public boolean check(int[] xyToCheck, boolean isW) {
		String pussy;
		if (isW) {
			pussy = "B";
		} else {
			pussy = "W";
		}

		for (int a = 0; a < 8; a++) {
			if (Board[xyToCheck[0] + a][xyToCheck[1]].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] + a][xyToCheck[1]].getSymbol().endsWith("R")
						|| Board[xyToCheck[0] + a][xyToCheck[1]].getSymbol().endsWith("Q")) {
					return true;
				}
			}
			if (Board[xyToCheck[0] - a][xyToCheck[1]].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] - a][xyToCheck[1]].getSymbol().endsWith("R")
						|| Board[xyToCheck[0] - a][xyToCheck[1]].getSymbol().endsWith("Q")) {
					return true;
				}
			}
			if (Board[xyToCheck[0]][xyToCheck[1] + a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0]][xyToCheck[1] + a].getSymbol().endsWith("R")
						|| Board[xyToCheck[0]][xyToCheck[1] + a].getSymbol().endsWith("Q")) {
					return true;
				}
			}
			if (Board[xyToCheck[0]][xyToCheck[1] - a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0]][xyToCheck[1] - a].getSymbol().endsWith("R")
						|| Board[xyToCheck[0]][xyToCheck[1] - a].getSymbol().endsWith("Q")) {
					return true;
				}
			}
			if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith(pussy)
						|| Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith("Q")) {
					return true;
				}
				if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith("P")
						&& Board[xyToCheck[0] + a][xyToCheck[1] + a].originSide == 1) {
					return true;
				}
			}
			if (Board[xyToCheck[0] - a][xyToCheck[1] - a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] - a][xyToCheck[1] - a].getSymbol().endsWith(pussy)
						|| Board[xyToCheck[0] - a][xyToCheck[1] - a].getSymbol().endsWith("Q")) {
					return true;
				}
				if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith("P")
						&& Board[xyToCheck[0] + a][xyToCheck[1] + a].originSide == 1) {
					return true;
				}
			}
			if (Board[xyToCheck[0] + a][xyToCheck[1] - a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] + a][xyToCheck[1] - a].getSymbol().endsWith(pussy)
						|| Board[xyToCheck[0] + a][xyToCheck[1] - a].getSymbol().endsWith("Q")) {
					return true;
				}
				if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith("P")
						&& Board[xyToCheck[0] + a][xyToCheck[1] + a].originSide == -1) {
					return true;
				}
			}
			if (Board[xyToCheck[0] - a][xyToCheck[1] + a].getSymbol().startsWith(pussy)) {
				if (Board[xyToCheck[0] - a][xyToCheck[1] + a].getSymbol().endsWith(pussy)
						|| Board[xyToCheck[0] - a][xyToCheck[1] + a].getSymbol().endsWith("Q")) {
					return true;
				}
				if (Board[xyToCheck[0] + a][xyToCheck[1] + a].getSymbol().endsWith("P")
						&& Board[xyToCheck[0] + a][xyToCheck[1] + a].originSide == -1) {
					return true;
				}
			}
			if ((Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().endsWith("N")
					&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] + 3][xyToCheck[1] - 1].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] - 3][xyToCheck[1] + 1].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] - 3][xyToCheck[1] - 1].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] + 1][xyToCheck[1] + 3].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] - 1][xyToCheck[1] + 3].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] + 1][xyToCheck[1] - 3].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))
					|| (Board[xyToCheck[0] - 1][xyToCheck[1] - 3].getSymbol().endsWith("N")
							&& Board[xyToCheck[0] + 3][xyToCheck[1] + 1].getSymbol().startsWith(pussy))) {
				return true;
			}
		}

		return false;
	}

	public boolean validPawnMove(int deltaX, int deltaY, int[] start, int[] end, boolean isWTurn) {
		// originSide is 1 or -1 indicating the pawns origin on the board
		// 1 for bottom side and -1 for far side
		deltaX = Math.abs(deltaX);

		if (Board[start[0]][start[1]].originSide == -1) {
			if ((deltaX == 0 && deltaY == 1)
					|| (deltaY == 1 && deltaX == 1
							&& ((Board[end[0]][end[1]].getSymbol().startsWith("W")
									&& Board[start[0]][start[1]].getSymbol().startsWith("B"))
									|| (Board[end[0]][end[1]].getSymbol().startsWith("B")
											&& Board[start[0]][start[1]].getSymbol().startsWith("W"))))
					|| (deltaX == 0 && deltaY == 2 && start[1] == 1)) {
				if (Board[end[0]][end[1]].getSymbol().startsWith("B")
						&& Board[start[0]][start[1]].getSymbol().startsWith("W") && deltaX == 1) {
					return false;
				}
				return true;
			}
		} else if (Board[start[0]][start[1]].originSide == 1) {
			if ((deltaX == 0 && deltaY == -1)
					|| (deltaX == 1 && deltaY == -1
							&& (Board[end[0]][end[1]].getSymbol().startsWith("W")
									&& Board[start[0]][start[1]].getSymbol().startsWith("B")
									|| Board[end[0]][end[1]].getSymbol().startsWith("B")
											&& Board[start[0]][start[1]].getSymbol().startsWith("W")))
					|| (deltaX == 0 && deltaY == -2 && start[1] == 6)) {
				if (Board[end[0]][end[1]].getSymbol().startsWith("W")
						&& Board[start[0]][start[1]].getSymbol().startsWith("B") && deltaX == 1) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	public boolean validRookMove(int deltaX, int deltaY) {
		deltaX = Math.abs(deltaX);
		deltaY = Math.abs(deltaY);
		if ((deltaX == 0 && deltaY < 8 && deltaY > 0) || (deltaY == 0 && deltaX < 8 && deltaX > 0)) {
			return true;
		}
		return false;
	}

	public boolean validKnightMove(int deltaX, int deltaY) {
		deltaX = Math.abs(deltaX);
		deltaY = Math.abs(deltaY);
		jout(deltaX + " " + deltaY);
		if ((deltaX == 1 && deltaY == 2) || (deltaY == 1 && deltaX == 2)) {
			return true;
		}
		return false;
	}

	public boolean validBishopMove(int deltaX, int deltaY) {
		deltaX = Math.abs(deltaX);
		deltaY = Math.abs(deltaY);
		if (deltaX == deltaY) {
			return true;
		}
		return false;
	}

	public boolean validKingMove(int deltaX, int deltaY, int[] start) {
		deltaX = Math.abs(deltaX);
		deltaY = Math.abs(deltaY);
		if ((deltaX == 1 && deltaY == 1) || (deltaY == 0 && deltaX == 1) || (deltaY == 1 && deltaX == 0)
				|| (deltaY == 0 && deltaX == 2 && !Board[start[0]][start[1]].hasBeenMoved)) {
			return true;
		}
		return false;
	}

	public boolean validQueenMove(int deltaX, int deltaY) {
		deltaX = Math.abs(deltaX);
		deltaY = Math.abs(deltaY);
		if ((deltaX == deltaY) || (deltaY == 0 && deltaX < 8) || (deltaX == 0 && deltaY < 8)) {
			return true;
		}
		return false;
	}

	public Piece[][] getBoard() {
		return Board;
	}

	public static void jout(String print) {
		System.out.println(print);
	}

	public static void jout(boolean print) {
		System.out.println(print);
	}

}
