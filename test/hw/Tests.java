package hw;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Tests {
	
	Field f;
	BoardModel b;
	BoardView bv;
	
	@Before
	public void setUp() {
		f = new Field(0);
		b = new BoardModel(10, 10, 5);
		bv = new BoardView(10, 10, 5, 0);
	}
	
	@Test
	public void testFieldCreation() {
		Assert.assertEquals(0, f.getValue());
	}
	
	@Test
	public void testFieldReveal() {
		f.reveal();
		Assert.assertFalse(f.isHidden());
	}
	
	@Test
	public void testsetValue() {
		f.setValue(5);
		Assert.assertEquals(5, f.getValue());
	}
	
	@Test
	public void testToggleFlagged() {
		f.toggleFlagged();
		Assert.assertTrue(f.isFlagged());
		f.toggleFlagged();
		Assert.assertFalse(f.isFlagged());
	}
	
	@Test
	public void testToggleHidden() {
		Assert.assertTrue(f.isHidden());
		f.toggleHidden();
		Assert.assertFalse(f.isHidden());
	}
	
	@Test
	public void testBoardModel() {
		Assert.assertEquals(0, b.getRevealed());
		Assert.assertEquals(0, b.getBombFlagged());
		Assert.assertFalse(b.isGenerated());
	}
	
	@Test
	public void testBoardGenerate() {
		b.genBoard(5, 5);
		Assert.assertTrue(b.generated);
	}
	
	@Test
	public void testBoardReset() {
		b.reset();
		Assert.assertFalse(b.isGenerated());
		Assert.assertEquals(0, b.getField(0, 0).getValue());
	}
	
	@Test
	public void testBombCounter() {
		Assert.assertEquals(5, bv.bombCounter.getValue());
		
	}
	
	@Test
	public void testTimeCounter() {
		Assert.assertEquals(0, bv.timeCounter.getValue());
	}
}
