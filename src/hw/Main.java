package hw;

/**
 * The Class Main.
 */
public class Main {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		BoardView view;
		if(args.length == 4) {
			view = new BoardView(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		}else {
			view = new BoardView(15, 15, 5, 0);
		}
	}

}
