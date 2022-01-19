// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.text;
import goryachev.common.log.Log;
import javax.swing.text.BoxView;
import javax.swing.text.Element;


public class XBoxView
	extends BoxView
{
	protected static final Log log = Log.get("XBoxView");
	
	
	public XBoxView(Element em, int axis)
	{
		super(em, axis);
	}
	
	
	// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=7141488
	public void setSize(float width, float height)
	{
		try
		{
			super.setSize(width, height);
		}
		catch(Exception e)
		{
			log.error(e);
		}
	}


	// https://netbeans.org/bugzilla/show_bug.cgi?id=200649
	protected int getOffset(int axis, int childIndex)
	{
		try
		{
			return super.getOffset(axis, childIndex);
		}
		catch(Exception e)
		{
			log.error(e);
			return 0;
		}
	}
}
