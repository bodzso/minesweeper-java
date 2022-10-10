package hw;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import hw.BoardModel.State;

/**
 * The Class BoardView shows the board and the state of the game.
 */
public class BoardView extends JFrame{
	
	/** The time. */
	private int row, col, bomb, time;
	
	/** The table. */
	private JTable table;
	
	/** The save file. */
	File saveFile = new File("save.dat");
	
	/** The sprite. */
	SpriteReader sprite = new SpriteReader();
	
	/** The smiley. */
	SmileyButton smiley;
	
	/** The bomb counter. */
	CounterPanel bombCounter;
	
	/** The time counter. */
	CounterPanel timeCounter;
	
	/** The data. */
	private BoardModel data;
	
	/**
	 * The Class CounterPanel shows 3 numbers which can be used to show the
	 * number of bombs or the remaining time.
	 */
	class CounterPanel extends JPanel implements Runnable {
		
		/** The time. */
		private int val, time;
		
		/** The thread. */
		Thread t = null;
		
		/** The running is used for the thread. */
		private AtomicBoolean running = new AtomicBoolean(false);
		
		/**
		 * Instantiates a new counter panel.
		 *
		 * @param n the n
		 */
		public CounterPanel(int n) {
			setPreferredSize(new Dimension(39, 23));
			time = val = n;
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			int num = val;
			//1
			g.drawImage(sprite.buffered(15+(num % 10)), 26, 0, null);
			num /= 10;
			
			//10
			g.drawImage(sprite.buffered(15+(num % 10)), 13, 0, null);
			num /= 10;
			
			//100
			g.drawImage(sprite.buffered(15+num), 0, 0, null);
		}
		
		/**
		 * Sets the value.
		 *
		 * @param n the new value
		 */
		void setValue(int n) {
			val = n;
			repaint();
		}
		
		/**
		 * Gets the value.
		 *
		 * @return the value
		 */
		int getValue() {
			return val;
		}
		
		/**
		 * Start the counting.
		 */
		void start() {
			t = new Thread(this);
			t.start();
			val = time;
		}
		
		/**
		 * Stops the counting.
		 */
		void stop() {
			running.set(false);
		}

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		@Override
		synchronized public void run() {
			boolean up = (val == 0) ? true : false;
			running.set(true);
				
			while((val < 999 || val > 0) && running.get()) {
				if(up)
					val++;
				else
					val--;
				repaint();
				try {
					t.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if(val == 999 || val == 0) {
				smiley.setState(28);
				data.end();
			}
		}
	}
	
	/**
	 * The Class SmileyButton represents the game state
	 * when clicked it resets the game.
	 */
	class SmileyButton extends JButton implements MouseListener{
		
		/** The icon. */
		ImageIcon icon;
		
		/**
		 * Instantiates a new smiley button.
		 *
		 * @param img the img
		 */
		public SmileyButton(BufferedImage img) {
			setPreferredSize(new Dimension(26, 26));
			setOpaque(false);
			setBorderPainted(false);
			icon = new ImageIcon(img);
			setIcon(icon);
			addMouseListener(this);
		}
		
		/**
		 * Sets the state.
		 *
		 * @param id the new state
		 */
		void setState(int id) {
			icon.setImage(sprite.buffered(id));
			setIcon(icon);
			repaint();
		}
		
		/**
		 * Win.
		 */
		public void win() {
			setState(29);
			repaint();
		}
		
		/**
		 * Lose.
		 */
		public void lose() {
			setState(28);
			repaint();
		}

		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
		 */
		public void mouseReleased(MouseEvent e) {
			setState(26);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
		 */
		public void mousePressed(MouseEvent e) {
			setState(25);
		}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
		 */
		public void mouseExited(MouseEvent e) {}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
		 */
		public void mouseEntered(MouseEvent e) {}
		
		/* (non-Javadoc)
		 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
		 */
		public void mouseClicked(MouseEvent e) {
			data.reset();
			bombCounter.setValue(bomb);
			timeCounter.stop();
		}
	}
	
	/**
	 * Load.
	 */
	@SuppressWarnings("unchecked")
	void load() {
		try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
            data.fields = (ArrayList<Field>)ois.readObject();
            ois.close();
        } catch(Exception ex) {
            ex.printStackTrace();
            data.fields = null;
        }
	}
	
	/**
	 * Save.
	 */
	void save() {
		try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
	        oos.writeObject(data.fields);
	        oos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
    	}
	}
	
	
	/**
	 * Instantiates a new board view.
	 *
	 * @param r the row
	 * @param c the column
	 * @param b the bomb
	 * @param t the time
	 */
	public BoardView(int r, int c, int b, int t) {
		row = r;
		col = c;
		bomb = b;
		time = t;
		data = new BoardModel(row, col, bomb);
		
		//set window
		//setResizable(false);
		setTitle("MineSweeper");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//create menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);
		
		// create menu items
		JMenuItem open = new JMenuItem("Open"),
		save = new JMenuItem("Save"),
		exit = new JMenuItem("Exit");
		
		menu.add(open);
		menu.add(save);
		menu.add(exit);
		
		exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1)
				System.exit(0);
			}
		});
		
		//save data
		addWindowListener(new WindowAdapter() {
		        @Override
		        public void windowClosing(WindowEvent e) {
		        	save();
		    }
		});
		
		save.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					JFileChooser file = new JFileChooser(System.getProperty("user.dir"));
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"save file", "dat");
					file.setFileFilter(filter);
					int val = file.showSaveDialog(BoardView.this);
					if(val == JFileChooser.APPROVE_OPTION) {
						saveFile = file.getSelectedFile();
						save();
					}
				}
			}
		});
		
		
		//handle opening a file
		open.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					JFileChooser file = new JFileChooser(System.getProperty("user.dir"));
					FileNameExtensionFilter filter = new FileNameExtensionFilter(
							"save file", "dat");
					file.setFileFilter(filter);
					int val = file.showOpenDialog(BoardView.this);
					if(val == JFileChooser.APPROVE_OPTION) {
						System.out.println(file.getSelectedFile());
						saveFile = file.getSelectedFile();
						load();
					}
				}	
			}
		});
		
		//createTable
		table = new JTable(data);
        table.setFillsViewportHeight(true);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setCellSelectionEnabled(false);
        TableColumn column = null;
        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setMinWidth(16);
            column.setMaxWidth(16);
        }
        
        //status
  		JPanel state = new JPanel();
  		add(state, BorderLayout.NORTH);
  		smiley = new SmileyButton(sprite.buffered(26));
  		bombCounter = new CounterPanel(bomb);
  		timeCounter = new CounterPanel(time);
  		state.add(bombCounter);
  		state.add(smiley);
  		state.add(timeCounter);
        
        //handle mouse clicks
        table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(data.gameState() == State.PLAYING) {
					JTable target = (JTable)e.getSource();
					int row = target.rowAtPoint(e.getPoint());
				    int col = target.columnAtPoint(e.getPoint());
					if(e.getButton() == e.BUTTON1) {
						if(!data.isGenerated()) {
							data.genBoard(row, col);
							save();
							timeCounter.start();
						}
							
					    data.reveal(row, col);
					}
					//question
					if(e.getButton() == e.BUTTON2) {
						data.mark(row, col, true);
					}
					
					//flag
					if(e.getButton() == e.BUTTON3) {
						data.mark(row, col, false);
						bombCounter.setValue(data.getFlagged());
					}
					
					//win
					if(data.gameState() == State.WIN) {
						smiley.win();
						timeCounter.stop();
					}
					
					if(data.gameState() == State.LOSE) {
						smiley.lose();
						timeCounter.stop();
					}
				}
			}
		}); 
        
        add(table, BorderLayout.CENTER);
		
        setJMenuBar(menuBar);
        setSize(table.getSize());
		pack();
		setVisible(true);
		setResizable(false);
	}
}
