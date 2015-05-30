// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.BasePanel;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.UI;
import goryachev.common.ui.dialogs.StandardDialogPanel;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CKit;
import goryachev.common.util.HasPrompts;
import goryachev.common.util.UserException;
import java.awt.Component;
import java.util.Stack;
import javax.swing.Action;
import javax.swing.JComponent;


public class WizardPanel2
	extends CPanel
{
	public final CAction backAction = new CAction() { public void action() { actionBack(); }};
	private Stack<Entry> stack = new Stack();
	protected final WizardTitleBar titleField;

	
	public WizardPanel2()
	{
		titleField = new WizardTitleBar();
	}
	
	
	public void setCardTitle(String s)
	{
		titleField.setText(s);
	}
	
	
	/** 
	 * Replaces current card (if any) with the specified component.
	 */
	public void setCard(JComponent c, String title)
	{
		stack.add(new Entry(c, title));
		setCenter(c);
		
		titleField.setText(title);
		
		if(title == null)
		{
			remove(titleField);
		}
		else
		{
			setNorth(titleField);
		}
		
		validate();
		repaint();
		
		// TODO set focus to an appropriate component (which?)
	}
	
	
	public JComponent getCurrentCard()
	{
		Entry en = stack.peek();
		return en.component;
	}
	
	
	protected void actionBack()
	{
		if(stack.size() >= 2)
		{
			stack.pop();
			
			Entry en = stack.pop();
			setCard(en.component, en.title);
			
			updatePrompts(en.component);
			updatePrompts(UI.getParentWindow(this));
		}
	}
	
	
	protected void updatePrompts(Component c)
	{
		if(c instanceof HasPrompts)
		{
			((HasPrompts)c).updatePrompts();
		}
	}
	
	
	public BasePanel setErrorCard(Object messageOrThrowable, boolean allowBackButton, Action closeAction)
	{
		String s;
		if(messageOrThrowable instanceof Throwable)
		{
			if(messageOrThrowable instanceof UserException)
			{
				s = ((UserException)messageOrThrowable).getMessage();
			}
			else
			{
				s = CKit.stackTrace((Throwable)messageOrThrowable);
			}
		}
		else
		{
			s = messageOrThrowable.toString();
		}
		
		StandardDialogPanel c = new StandardDialogPanel();
		c.setIcon(CIcons.Error96);
		c.setText(s);
		
		BasePanel p = new BasePanel(c);
		if(allowBackButton)
		{
			p.buttonPanel().add(new CButton(Menus.Back, backAction));
		}
		p.buttonPanel().add(new CButton(Menus.Close, closeAction));
		
		setCard(p, Menus.Error);
		return p;
	}
	
	
	//
	
	
	public static class Entry
	{
		public JComponent component;
		public String title;
		
		public Entry(JComponent component, String title)
		{
			this.component = component;
			this.title = title;
		}
	}
}
