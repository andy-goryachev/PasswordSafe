// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import java.util.Vector;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;


public class InsertUndo
	extends AbstractUndoableEdit
{
	/** Where string was inserted. */
	protected int offset;
	/** Length of string inserted. */
	protected int length;
	/** The string that was inserted. This will only be valid after an
	 * undo. */
	protected String string;
	/** An array of instances of UndoPosRef for the Positions in the
	 * range that was removed, valid after undo. */
	protected Vector posRefs;
	
	
	protected InsertUndo(int offset, int length)
	{
		this.offset = offset;
		this.length = length;
	}


	public void undo() throws CannotUndoException
	{
		super.undo();
//		try
//		{
//			// Get the Positions in the range being removed.
//			posRefs = getPositionsInRange(null, offset, length);
//			string = getString(offset, length);
//			remove(offset, length);
//		}
//		catch(BadLocationException bl)
//		{
//			throw new CannotUndoException();
//		}
	}


	public void redo() throws CannotRedoException
	{
		super.redo();
//		try
//		{
//			insertString(offset, string);
//			string = null;
//			// Update the Positions that were in the range removed.
//			if(posRefs != null)
//			{
//				updateUndoPositions(posRefs, offset, length);
//				posRefs = null;
//			}
//		}
//		catch(BadLocationException bl)
//		{
//			throw new CannotRedoException();
//		}
	}
}