package hw;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 * The Class BoardModel stores the state and data.
 */
public class BoardModel extends AbstractTableModel{
	
	/** The bombflagged. */
	private int row, col, bomb, flagged, revealed = 0, bombflagged = 0;
	
	/** The status. */
	private State status = State.PLAYING;
	
	/** The generated stores if the board was genereated. */
	boolean generated = false;
	
	/** The fields stores Fields. */
	ArrayList<Field> fields;
	
	/** The sprite. */
	SpriteReader sprite = new SpriteReader();
	
	/**
	 * The Enum State.
	 */
	public static enum State{
		
		/** The win. */
		WIN, 
 /** The lose. */
 LOSE, 
 /** The playing. */
 PLAYING;
	}
	
	/**
	 * Instantiates a new board model.
	 *
	 * @param r the row
	 * @param c the column
	 * @param b the bomb
	 */
	public BoardModel(int r, int c, int b) {
		row = r;
		col = c;
		flagged = bomb = b; 
		fields = new ArrayList<Field>(r*c);
		for (int i = 0; i < r*c; i++) {
			fields.add(new Field(0));
		}
	}
	
	/**
	 * Gets the revealed.
	 *
	 * @return the revealed
	 */
	int getRevealed() {
		return revealed;
	}
	
	/**
	 * Gets the bomb flagged.
	 *
	 * @return the bomb flagged
	 */
	int getBombFlagged() {
		return bombflagged;
	}
	
	/**
	 * Checks if the board is generated.
	 *
	 * @return true, if the board is generated
	 */
	boolean isGenerated() {
		return generated;
	}
	
	/**
	 * Game state.
	 *
	 * @return the game state
	 */
	State gameState() {
		return status;
	}
	
	/**
	 * Increases a field value by one.
	 *
	 * @param r the row
	 * @param c the column
	 */
	void add(int r, int c) {
		if(r < 0 || r >= row) return;
	    if(c < 0 || c >= col) return;
	    Field field = fields.get(r * col + c);
	    if(field.getValue() == 9) return;
	    field.setValue(field.getValue()+1);
	}
	
	/**
	 * Generates the board.
	 *
	 * @param mr the clicked row
	 * @param mc the clicked column
	 */
	void genBoard(int mr, int mc) {
		int n = 0; int r, c;
		Random rand = new Random();
		while (n < bomb) {
			r = rand.nextInt(row);
			c = rand.nextInt(col);
			if(r == mr && c == mc) continue;
			Field field = fields.get(r * col + c);
			if(field.getValue() != 9) {
				field.setValue(9);
				add(r + 1, c);
	            add(r - 1, c);
	            add(r, c + 1);
	            add(r, c - 1);
	            add(r - 1, c + 1);
	            add(r + 1, c + 1);
	            add(r - 1, c - 1);
	            add(r + 1, c - 1);
	            n++;
			}
		}
		generated = true;
	}
	
	/**
	 * Resets the game.
	 */
	void reset() {
		revealed = bombflagged = 0;
		flagged = bomb;
		status = State.PLAYING;
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				Field field = fields.get(r * col + c);
				field.setValue(0);
				field.setHidden(true);
				field.setFlagged(false);
				field.setQuestion(false);
			}
		}
		generated = false;
		fireTableDataChanged();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return col;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return row;
	}
	
	/**
	 * Reveals the click field and the surroundings.
	 *
	 * @param r the row
	 * @param c the col
	 */
	void reveal(int r, int c) {
		if(r < 0 || r >= row) return;
		if(c < 0 || c >= col) return;
		Field field = fields.get(r * col + c);
		if(!field.isHidden() || field.isFlagged()) return;
		if(field.val == 9) {
			field.val = 10;
			end();
		}else {
			revealed++;
			field.reveal();
			if(revealed == row * col - bomb)
				status = State.WIN;
			if(field.getValue() == 0) {
				reveal(r + 1, c);
				reveal(r - 1, c);
				reveal(r, c + 1);
				reveal(r, c - 1);
			}
			fireTableDataChanged();
		}
	}
	
	/**
	 * End is called when the game is over.
	 */
	void end() {
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				Field field = fields.get(r * col + c);
				field.setHidden(false);
			}
		}
		fireTableDataChanged();
		status = State.LOSE;
	}
	
	/**
	 * Marks one Field.
	 *
	 * @param r the r
	 * @param c the c
	 * @param q the q
	 */
	void mark(int r, int c, boolean q) {
		Field field = fields.get(r * col + c);
		if(q)
			field.toggleQuestion();
		else {
			if(!field.isFlagged() && flagged > 0) {
				flagged--;
			}
				
			if(field.isFlagged() && flagged < bomb) {
				flagged++;
			}
			
			if(field.val == 9) {
				if(field.isFlagged()) {
					bombflagged--;
				}else {
					bombflagged++;
				}
			}
			field.toggleFlagged();
		}
		if(bombflagged == bomb)
			status = State.WIN;
		fireTableCellUpdated(r, c);
	}
	
	/**
	 * Gets the flagged.
	 *
	 * @return the flagged
	 */
	int getFlagged() {
		return flagged;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int r, int c) {
		Field f = fields.get(r * col + c);
		fireTableCellUpdated(r, c);
		if(f.isHidden()) {
			if(f.isQuestion())
				return sprite.image(12);
			if(f.isFlagged())
				return sprite.image(13);
			else
				return sprite.image(14);
		}

		return sprite.image(fields.get(r * col + c).getValue());
	}
	
	/**
	 * Gets the field.
	 *
	 * @param r the row
	 * @param c the col
	 * @return the field
	 */
	Field getField(int r, int c) {
		return fields.get(r * col + c);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
	 */
	@Override
	public Class<?> getColumnClass(int arg0) {
		return ImageIcon.class;
	}

}
