

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

@SuppressWarnings("unused")
public class GameGUI {

	final static int width = 960;
	final static int height = 960;
	
	final static int SPRITE_WIDTH = 115;
	final static int SPRITE_HEIGHT = 115;

	final static int BORDER = 20;

	private static BufferStrategy bs;
	private static Graphics g;

	public static Display display = new Display("Chess Application", width, height);

	private final static BufferedImage Board = ImageLoader.loadImage("res/textures/Board/BOARDWOOD.png");

	private Piece[][] game;

	public GameGUI() {
		render();
	}

	public static void render() {
		if (display.getCanvas().getBufferStrategy() == null) {
			display.getCanvas().createBufferStrategy(1);
			return;
		}
	}

	public static void render(Piece[][] game, int[] xy) {
		render();

		bs = display.getCanvas().getBufferStrategy();

		g = bs.getDrawGraphics();
		// Clear Screen
		g.clearRect(0, 0, width, height);
		// Draw

		g.drawImage(Board, 0, 0, null);

		int x, y;

		for (int i = 0; i < 64; i++) {
			int xx = i/8;
			int yy = i%8;
			if (!game[xx][yy].getSymbol().endsWith(" ")) {
				x = (((xx) * SPRITE_WIDTH) + BORDER);
				y = (((yy) * SPRITE_HEIGHT) + BORDER);
				g.drawImage(game[xx][yy].getSprite(), x, y, null);
			}
		}
		Color color = new Color(0, 0, 1, 0.25f);
		g.setColor(color);
		g.fillRect(xy[0]*115+20, xy[1]*115+20, 115, 115);

		// Draw

		bs.show();
		g.dispose();

	}
	
	public static void render(Piece[][] game) {
		render();

		bs = display.getCanvas().getBufferStrategy();

		g = bs.getDrawGraphics();
		// Clear Screen
		g.clearRect(0, 0, width, height);
		// Draw

		g.drawImage(Board, 0, 0, null);

		int x, y;

		for (int i = 0; i < 64; i++) {
			int xx = i/8;
			int yy = i%8;
			if (!game[xx][yy].getSymbol().endsWith(" ")) {
				x = (((xx) * SPRITE_WIDTH) + BORDER);
				y = (((yy) * SPRITE_HEIGHT) + BORDER);
				g.drawImage(game[xx][yy].getSprite(), x, y, null);
			}
		}

		// Draw

		bs.show();
		g.dispose();

	}
	
	public static void jout(String print){
		System.out.println(print);
	}
	
	public static void jout(int print){
		System.out.println(print);
	}
	
	public static Display getDisplay(){
		return display;
	}
}
