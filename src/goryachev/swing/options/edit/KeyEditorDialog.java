// Copyright © 2008-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.swing.Accelerator;
import goryachev.swing.CButton;
import goryachev.swing.CDialog;
import goryachev.swing.CPanel;
import goryachev.swing.KeyNames;
import goryachev.swing.Theme;
import goryachev.swing.XAction;
import goryachev.swing.text.CDocumentFilter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


/** Allows to enter almost all key strokes, except for Tab and Modifier keys. */
public class KeyEditorDialog
	extends CDialog
{
	public final XAction clearAction = new XAction(this::actionClear);
	public final XAction cancelAction = new XAction(this::actionCancel);
	public final XAction okAction = new XAction(this::actionCommit);
	public final JTextField keyField;
	public final JLabel warningLabel;
	protected final Accelerator ac;
	protected final KeyBindingEntry entry;
	protected final KeyBindingsTableModel model;
	protected CDocumentFilter documentFilter;
	protected KeyStroke current;
	protected KeyStroke selected;
	

	protected KeyEditorDialog(KeyBindingsEditor parent, KeyBindingEntry en)
	{
		super(parent, "KeyEditorDialog", true);
		setTitle(TXT.get("KeyEditorDialog.title", "Modify Key"));
		setMinimumSize(350, 180);
		setSize(350, 180);

		this.model = parent.model;
		this.entry = en;
		this.ac = en.accelerator;
		
		keyField = new JTextField()
		{
			protected void processKeyEvent(KeyEvent ev)
			{
				super.processKeyEvent(ev);
			}
		};
		
		documentFilter = new CDocumentFilter(false);
		documentFilter.attachTo(keyField);
		keyField.addKeyListener(new KeyAdapter()
		{
			public void keyPressed(KeyEvent ev)
			{
				KeyStroke k = getKeyStroke(ev.getKeyCode(), ev.getModifiersEx());
				if(k != null)
				{
					setKeyStroke(k);
					ev.consume();
				}
			}
		});
		
		warningLabel = new JLabel();
		
		CPanel p = new CPanel();
		p.setBorder(10);
		p.setGaps(10, 5);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL
		);

		p.row(1,  p.info(TXT.get("KeyEditorDialog.info.select new key for FUNCTION", "Type new key for {0}.", ac.getFullName())));
		p.nextRow();
		p.row(0, p.label(TXT.get("KeyEditorDialog.label.new key", "New key:")));
		p.row(1, keyField);
		p.nextFillRow();
		p.row(1, warningLabel);
		
		p.buttonPanel().add(new CButton(Menus.Cancel, cancelAction));
		p.buttonPanel().add(new CButton(TXT.get("KeyEditorDialog.clear keystroke", "Clear"), clearAction, Theme.DESTRUCTIVE_BUTTON_COLOR));
		p.buttonPanel().add(new CButton(Menus.OK, okAction, true));
		
		setCenter(p);
		
		setKeyStroke(en.getKey());
	}
	
	
	protected KeyStroke getKeyStroke(int code, int mods)
	{
		// filter keys that should not be there
		switch(code)
		{
		case 0:
		case KeyEvent.VK_CONTROL:
		case KeyEvent.VK_SHIFT:
		case KeyEvent.VK_ALT:
		case KeyEvent.VK_NUM_LOCK:
		case KeyEvent.VK_WINDOWS:
		case KeyEvent.VK_CONTEXT_MENU:
		case KeyEvent.VK_ALT_GRAPH:
			return null;
		}
		
		return KeyStroke.getKeyStroke(code, mods);
	}


	protected void onKeyEvent(KeyEvent ev)
	{
		KeyStroke k = KeyStroke.getKeyStroke(ev.getKeyCode(), ev.getModifiersEx());
		setKeyStroke(k);
		
		ev.consume();
	}
	

	public static KeyStroke open(KeyBindingsEditor parent, KeyBindingEntry en)
	{
		KeyEditorDialog d = new KeyEditorDialog(parent, en);		
		d.open();
		return d.selected;
	}
	
	
	protected void setKeyStroke(KeyStroke k)
	{
		documentFilter.setAllowChanges(true);
		
		if(k == null)
		{
			keyField.setText(null);
		}
		else
		{
			keyField.setText(KeyNames.getKeyName(k));
		}
		
		documentFilter.setAllowChanges(false);
		
		current = k;
		
		updateWarning();
	}
	
	
	protected void updateWarning()
	{
		// TODO think about it
//		KeyBindingEntry en = model.findByKeyStroke(current);
//		if(en == entry)
//		{
//			en = null;
//		}
//		warningLabel.setText(en == null ? null : en.accelerator.getFullName()); 
	}
	
	
	protected void actionClear()
	{
		setKeyStroke(null);
	}
	
	
	protected void actionCommit()
	{
		selected = current;
		close();
	}
	
	
	protected void actionCancel()
	{
		close();
	}
}
