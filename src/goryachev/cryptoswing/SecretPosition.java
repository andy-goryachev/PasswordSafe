// Copyright © 2012-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;
import javax.swing.text.Position;


public final class SecretPosition
	implements Position
{
	private int offset;
	
	
	public SecretPosition(int offset)
	{
		this.offset = offset;
	}


	public int getOffset()
	{
		return offset;
	}
	
	
	public void setOffset(int x)
	{
		offset = x;
	}
	

	public void moveOffset(int len)
	{
		if(offset != 0)
		{
			offset += len;
		}
	}
}
