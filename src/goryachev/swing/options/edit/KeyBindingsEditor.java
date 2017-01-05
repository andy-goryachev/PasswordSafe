// Copyright © 2008-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.options.edit;
import goryachev.common.i18n.Menus;
import goryachev.common.i18n.TXT;
import goryachev.common.util.CList;
import goryachev.common.util.SB;
import goryachev.swing.Accelerator;
import goryachev.swing.CAction;
import goryachev.swing.CBorder;
import goryachev.swing.CButton;
import goryachev.swing.CMenuItem;
import goryachev.swing.CPanel;
import goryachev.swing.CPopupMenu;
import goryachev.swing.CPopupMenuController;
import goryachev.swing.CScrollPane;
import goryachev.swing.CToolBar;
import goryachev.swing.Theme;
import goryachev.swing.UI;
import goryachev.swing.options.OptionEditorInterface;
import goryachev.swing.table.CTableSelector;
import goryachev.swing.table.ZFilterLogic;
import goryachev.swing.table.ZTable;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;


/** global key bindings editor */
public class KeyBindingsEditor
	extends CPanel
	implements OptionEditorInterface
{
	public final CAction clearAction = new CAction(TXT.get("KeyBindingsEditor.clear key binding", "Clear")) { public void action() { actionClear(); } };
	public final CAction modifyAction = new CAction(TXT.get("KeyBindingsEditor.modify key binding", "Modify")) { public void action() { actionModify(); } };
	public final KeyBindingsTableModel model;
	public final ZTable table;
	public final ZFilterLogic filter;
	public final CTableSelector selector;
	public final JLabel infoLabel;
	
	
	public KeyBindingsEditor()
	{
		model = new KeyBindingsTableModel();
		
		table = new ZTable(model);
		table.setSortable(true);
		
		UI.whenFocused(table, KeyEvent.VK_ENTER, modifyAction);
		
		filter = new ZFilterLogic(table);

		selector = new CTableSelector(table)
		{
			public void tableSelectionChangeDetected()
			{
				onSelectionChange();
			}
		};
		
		CScrollPane scroll = new CScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBorder(new CBorder());
		scroll.setViewportBorder(CBorder.NONE);
		scroll.getViewport().setBackground(Theme.FIELD_BG);

		new CPopupMenuController(table, scroll)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createTablePopupMenu();
			}

			public void onDoubleClick()
			{
				actionModify();
			}
		};		

		infoLabel = new JLabel();
		
		setNorth(createToolbar());
		setCenter(scroll);
		setName("KeyBindingsEditor");
	}
	
	
	public JPopupMenu createTablePopupMenu()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(modifyAction));
		m.addSeparator();
		m.add(new CMenuItem(clearAction));
		return m;
	}
	
	
	protected void onSelectionChange()
	{
		KeyBindingEntry x = getSelectedEntry();
		show(x);
	}
	
	
	protected KeyBindingEntry getSelectedEntry()
	{
		return model.getSelectedEntry(selector);
	}
	
	
	public void init()
	{
		load();
		
		show(null);
	}


	public boolean isFullWidth()
	{
		return true;
	}


	public float getPreferredHeight()
	{
		return HEIGHT_MAX;
	}


	public JComponent getComponent()
	{
		return this;
	}


	private JComponent createToolbar()
	{
		CToolBar t = Theme.toolbar();
		t.add(new CButton(modifyAction));
		t.add(new CButton(clearAction));
		t.space(20);
		t.add(infoLabel);
		t.fill();
		t.add(200, filter.getComponent());
		return t;
	}
	

	public void load()
	{
		CList<KeyBindingEntry> list = new CList();
		for(Accelerator a: Accelerator.getAccelerators())
		{
			list.add(new KeyBindingEntry(a, a.get()));
		}

		model.replaceAll(list);
	}


	protected void show(KeyBindingEntry en)
	{
		if(en == null)
		{
			infoLabel.setText(null);
			modifyAction.setEnabled(false);
		}
		else
		{
			String s = en.accelerator.getFullName() + ": " + en.accelerator.getKeyName();			
			infoLabel.setText(s);
			
			modifyAction.setEnabled(true);
		}
	}
	
	
	protected void actionModify()
	{
		KeyBindingEntry en = getSelectedEntry();
		if(en != null)
		{
			KeyStroke k = KeyEditorDialog.open(this, en);
			if(k != null)
			{
				en.setKey(k);
				model.refreshAll();
			}
		}
	}
	
	
	protected void actionClear()
	{
		KeyBindingEntry en = getSelectedEntry();
		if(en != null)
		{
			en.setKey(null);
			model.refreshAll();
		}
	}


	public boolean isModified()
	{
		for(KeyBindingEntry en: model.asList())
		{
			if(en.isModified())
			{
				return true;
			}
		}
		return false;
	}


	public void commit()
	{
		for(KeyBindingEntry en: model.asList())
		{
			en.commit();
		}
	}
	
	
	public void revert()
	{
	}


	public String getSearchString()
	{
		SB sb = new SB();
		sb.a(Menus.Keys).sp();
		for(KeyBindingEntry en: model)
		{
			sb.a(en.getCommand()).sp();
		}
		return sb.toString();
	}
}
