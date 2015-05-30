// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options.edit;
import goryachev.common.ui.Accelerator;
import goryachev.common.ui.BaseDialog;
import goryachev.common.ui.BasePanel;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.KeyNames;
import goryachev.common.ui.Menus;
import goryachev.common.ui.text.CDocumentFilter;
import goryachev.common.util.TXT;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;


/** Allows to enter almost all key strokes, except for Tab and Modifier keys. */
public class KeyEditorDialog
	extends BaseDialog
{
	public final CAction clearAction = new CAction() { public void action() { actionClear(); } };
	public final CAction cancelAction = new CAction() { public void action() { actionCancel(); } };
	public final CAction okAction = new CAction() { public void action() { actionCommit(); } };
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
		p.setBorder(new CBorder(10));
		p.setLayout
		(
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.FILL
			},
			new double[]
			{
				CPanel.PREFERRED,
				CPanel.PREFERRED,
				CPanel.FILL
			},
			10, 5
		);

		int ix = 0;
		p.add(1, ix, p.info(TXT.get("KeyEditorDialog.info.select new key for FUNCTION", "Type new key for {0}.", ac.getFullName())));
		ix++;
		p.add(0, ix, p.label(TXT.get("KeyEditorDialog.label.new key", "New key:")));
		p.add(1, ix, keyField);
		ix++;
		p.add(1, ix, warningLabel);
		
		BasePanel bp = new BasePanel();
		bp.setCenter(p);
		bp.buttonPanel().add(new CButton(Menus.Cancel, cancelAction));
		bp.buttonPanel().add(new CButton(TXT.get("KeyEditorDialog.clear keystroke", "Clear"), clearAction));
		bp.buttonPanel().add(new CButton(Menus.OK, okAction, true));
		
		setCenter(bp);
		
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
