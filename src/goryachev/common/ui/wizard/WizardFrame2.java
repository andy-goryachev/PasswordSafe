// Copyright (c) 2013-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.wizard;
import goryachev.common.ui.AppFrame;
import goryachev.common.ui.BasePanel;
import goryachev.common.ui.CAction;
import goryachev.common.ui.UI;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;


public class WizardFrame2
	extends AppFrame
{
	public final CAction backAction;
	public final WizardPanel2 panel;
	
	
	public WizardFrame2(String name)
	{
		super(name);
		
		panel = new WizardPanel2();
		backAction = panel.backAction;
		
		setCenter(panel);
	}
	
	
	public void actionBack()
	{
		panel.actionBack();
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
		JComponent c = panel.getCurrentCard();
		UI.whenInFocusedWindow(c, KeyEvent.VK_ESCAPE, closeAction);
	}
	
	
	public BasePanel setErrorCard(Object message, boolean allowBackButton)
	{
		return panel.setErrorCard(message, allowBackButton, closeAction);
	}
	
	
	public BasePanel setErrorCard(Object message)
	{
		return panel.setErrorCard(message, true, closeAction);
	}
}
