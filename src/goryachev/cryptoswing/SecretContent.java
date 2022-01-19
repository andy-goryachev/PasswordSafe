// Copyright © 2012-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import goryachev.common.util.WeakList;
import goryachev.crypto.Crypto;
import goryachev.crypto.OpaqueChars;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;


public class SecretContent
	implements AbstractDocument.Content
{
	private char[] text = { '\n' }; 
	private WeakList<SecretPosition> positions = new WeakList();
	
	
	public SecretContent()
	{
	}
	
	
	public Position createPosition(int offset) throws BadLocationException
	{
		SecretPosition p = new SecretPosition(offset);
		positions.add(p);
		return p;
	}


	public int length()
	{
		return text.length;
	}
	
	
	protected BadLocationException err(int x)
	{
		return new BadLocationException("invalid location", x);
	}


	public UndoableEdit insertString(int pos, String s) throws BadLocationException
	{
		if(pos < 0)
		{
			throw err(pos);
		}
		else if(pos > length())
		{
			throw err(pos);
		}
		
		int len = s.length();
		if(len > 0)
		{
			char[] cs = new char[length() + len];
			
			if(pos > 0)
			{
				System.arraycopy(text, 0, cs, 0, pos);
			}
			
			char[] incoming = s.toCharArray();
			System.arraycopy(incoming, 0, cs, pos, len);
			Crypto.zero(incoming);
			
			int tail = length() - pos;
			if(tail > 0)
			{
				System.arraycopy(text, pos, cs, pos + len, tail);
			}
			
			Crypto.zero(text);
			text = cs;
			
			for(SecretPosition p: positions.asList())
			{
				if(p.getOffset() != 0)
				{
					if(p.getOffset() >= pos)
					{
						p.moveOffset(len);
					}
				}
			}
		}
		
		// no undo
		return null;
	}


	public UndoableEdit remove(int pos, int len) throws BadLocationException
	{
		char[] cs = new char[length() - len];
		if(pos > 0)
		{
			System.arraycopy(text, 0, cs, 0, pos);
		}
		
		int after = pos + len;
		int tail = length() - pos - len;
		if(tail > 0)
		{
			System.arraycopy(text, after, cs, pos, tail);
		}
		
		Crypto.zero(text);
		text = cs;
		
		for(SecretPosition p: positions.asList())
		{
			if(p.getOffset() != 0)
			{
				if(p.getOffset() >= after)
				{
					p.moveOffset(-len);
				}
				else if(p.getOffset() > pos)
				{
					p.setOffset(pos);
				}
			}
		}
		
		// no undo
		return null;
	}


	/** this method leaks secret information via String instances */
	public String getString(int pos, int len) throws BadLocationException
	{
		// TODO change actions (i.e. DefaultEditorKit.DeleteNextCharAction) that require this method
		//throw new RuntimeException("String is not secure enough");
		char[] cs = getChars(pos, len);
		try
		{
			return new String(cs);
		}
		finally
		{
			Crypto.zero(cs);
		}
	}


	public OpaqueChars getOpaqueChars(int pos, int len) throws BadLocationException
	{
		char[] cs = getChars(pos, len);
		try
		{
			return new OpaqueChars(cs);
		}
		finally
		{
			Crypto.zero(cs);
		}
	}


	public void getChars(int pos, int len, Segment seg) throws BadLocationException
	{
		char[] cs = getChars(pos, len);
		seg.array = cs;
		seg.count = len;
		seg.offset = 0;
	}
	
	
	// caller must zero the array
	protected char[] getChars(int pos, int len) throws BadLocationException
	{
		int end = pos + len;
		
		if(pos < 0 || end < 0)
		{
			throw err(-1);
		}
		if(end > length() || pos > length())
		{
			throw err(length() + 1);
		}
		
		char[] cs = new char[len];
		System.arraycopy(text, pos, cs, 0, len);
		return cs;
	}
}
