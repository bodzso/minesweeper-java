package hw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The Class SpriteReader reads the tileset from rsc/tileset.png
 * this class can be used to get one tile.
 */
public class SpriteReader {
	
	/** The tile. */
	BufferedImage tile;
	
	/** The sprites. */
	BufferedImage[] sprites;
	
	/**
	 * Instantiates a new sprite reader.
	 */
	public SpriteReader() {
		//TileSet
		try {
			tile = ImageIO.read(new File("rsc/tileset.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		sprites = new BufferedImage[] {
						//   x  y  sx  sy
		    tile.getSubimage(0, 0, 16, 16),  //0 empty
		    tile.getSubimage(16, 0, 16, 16), //1
		    tile.getSubimage(32, 0, 16, 16),
		    tile.getSubimage(48, 0, 16, 16),
		    tile.getSubimage(64, 0, 16, 16),
		    tile.getSubimage(80, 0, 16, 16),
		    tile.getSubimage(96, 0, 16, 16),
		    tile.getSubimage(112, 0, 16, 16),
		    tile.getSubimage(128, 0, 16, 16), //8
		    tile.getSubimage(0, 16, 16, 16),  //9  b
		    tile.getSubimage(16, 16, 16, 16), //10 bx
		    tile.getSubimage(32, 16, 16, 16), //11 ba
		    tile.getSubimage(48, 16, 16, 16), //12 ?
		    tile.getSubimage(64, 16, 16, 16), //13 flag
		    tile.getSubimage(80, 16, 16, 16), //14 filled
		    tile.getSubimage(0, 32, 13, 23),  //15 numbers
		    tile.getSubimage(13, 32, 13, 23), //1
		    tile.getSubimage(26, 32, 13, 23), //2
		    tile.getSubimage(39, 32, 13, 23), //3
		    tile.getSubimage(52, 32, 13, 23), //4
		    tile.getSubimage(65, 32, 13, 23), //5
		    tile.getSubimage(78, 32, 13, 23), //6
		    tile.getSubimage(91, 32, 13, 23), //7
		    tile.getSubimage(104, 32, 13, 23), //8
		    tile.getSubimage(117, 32, 13, 23), //9
		    tile.getSubimage(0, 55, 26, 26), //smiley 25
		    tile.getSubimage(26, 55, 26, 26), //normal 26
		    tile.getSubimage(52, 55, 26, 26), //O.o 27
		    tile.getSubimage(78, 55, 26, 26), //x.x 28
		    tile.getSubimage(104, 55, 26, 26) //cool 29
		};
	}
	
	/**
	 * Image.
	 *
	 * @param i the index of the image
	 * @return the image icon
	 */
	public ImageIcon image(int i) {
		return new ImageIcon(sprites[i]);
	}
	
	/**
	 * Buffered.
	 *
	 * @param i the index of the image
	 * @return the buffered image
	 */
	public BufferedImage buffered(int i) {
		return sprites[i];
	}
}
