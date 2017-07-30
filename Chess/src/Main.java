
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Scanner;

public class Main {

	public static void main(String[] arguments) {
		Scanner input = new Scanner(System.in);
		jout("would you like to begin?");
		input.next();
		init();
	}

	public static void init() {
		Game Chess = new Game();
		Chess.printBoard();
		Chess.getGUI();

		GameGUI.getDisplay().getCanvas().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x, y;
				x = e.getX();
				y = e.getY();
				Chess.move(x, y);
			}

			// @Override
			public void mouseEntered(MouseEvent arg0) {
			}

			// @Override
			public void mouseExited(MouseEvent arg0) {
			}

			// @Override
			public void mousePressed(MouseEvent arg0) {
			}

			// @Override
			public void mouseReleased(MouseEvent arg0) {
			}
		});

	}

	public static int[] decodeMouse(int x, int y) {
		int[] xy = new int[2];
		xy[0] = (x - 20) / 115;
		xy[1] = (y - 20) / 115;

		return xy;
	}

	public static void jout(String print) {
		System.out.println(print);
	}

}
