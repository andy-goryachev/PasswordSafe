// Copyright © 2008-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing;
import javax.swing.Action;


/** @deprecated use CToggleToolBarButton */
public class TToggleButton
	extends TButton
{
	public TToggleButton(String text, boolean hideText, Action a)
	{
		super(text, hideText, a);
		init();
	}


	public TToggleButton(Action a)
	{
		super(a);
		init();
	}
	
	
	private void init()
	{
//		if(getAction() instanceof CAction)
//		{
//			setSelected(((CAction)getAction()).isSelected());
//		}
	}
	
	
//	protected void actionPropertyChanged(Action action, String propertyName)
//	{
//		if(CAction.SELECTED.equals(propertyName))
//		{
//			setSelected(((CAction)action).isSelected());
//		}
//		else
//		{
//			super.actionPropertyChanged(action, propertyName);
//		}
//	}
}
