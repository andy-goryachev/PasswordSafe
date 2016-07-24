// Copyright © 2013-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.wizard;
import goryachev.swing.CAction;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.UI;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;


public class WizardDialog2
	extends CDialog
{
	public final CAction backAction;
	public final WizardPanel2 panel;
	
	
	public WizardDialog2(Component parent, String name, boolean modal)
	{
		super(parent, name, modal);
		borderless();
		
		panel = new WizardPanel2();
		backAction = panel.backAction;
		
		panel().setCenter(panel);
	}
	
	
	public void setCardTitle(String s)
	{
		panel.setCardTitle(s);
	}
	
	
	/** Replaces current card (if any) with the specified component. */
	public void setCard(JComponent c, String title)
	{
		panel.setCard(c, title);
	}
	
	
	/** allows the current card to close the wizard on ESC key */
	public void closeOnEscape()
	{
		closeOnEscape(panel.getCurrentCard());
	}
	
	
	public void closeOnEscape(Container c)
	{
		UI.whenInFocusedWindow(c, KeyEvent.VK_ESCAPE, closeDialogAction);
	}
	
	
	public CPanel setErrorCard(Object message, boolean allowBackButton)
	{
		return panel.setErrorCard(message, allowBackButton, closeDialogAction);
	}
	
	
	public CPanel setErrorCard(Object message)
	{
		return panel.setErrorCard(message, true, closeDialogAction);
	}
}
