package hw;

import java.io.Serializable;

/**
 * The Class Field represents one field on the board.
 */
public class Field implements Serializable {
	private boolean flagged = false, hidden = true, question = false;
	/** The value of the field. */
	int val;
	
	/**
	 * Instantiates a new field.
	 *
	 * @param v the v
	 */
	public Field(int v) {
		val = v;
	}
	
	/**
	 * Checks if is question.
	 *
	 * @return true, if is question
	 */
	public boolean isQuestion() {
		return question;
	}

	/**
	 * Sets the question.
	 *
	 * @param question the new question
	 */
	public void setQuestion(boolean question) {
		this.question = question;
	}

	/**
	 * Sets the flagged.
	 *
	 * @param flagged the new flagged
	 */
	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	/**
	 * Sets the hidden.
	 *
	 * @param hidden the new hidden
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param v the new value
	 */
	void setValue(int v) {
		val = v;
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
	 * Toggle flagged.
	 */
	void toggleFlagged() {
		flagged = !flagged;
	}
	
	/**
	 * Checks if the field is flagged.
	 *
	 * @return true, if the field is flagged
	 */
	boolean isFlagged() {
		return flagged;
	}
	
	/**
	 * Toggle hidden.
	 */
	void toggleHidden() {
		hidden = !hidden;
	}
	
	/**
	 * Toggle question mark.
	 */
	void toggleQuestion() {
		question = !question;
	}
	
	/**
	 * Checks if the field is hidden.
	 *
	 * @return true, if the field is hidden
	 */
	boolean isHidden() {
		return hidden;
	}
	
	/**
	 * Reveal the field.
	 */
	void reveal() {
		hidden = false;
	}
}
