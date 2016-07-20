// Copyright (c) 2010-2016 Andy Goryachev <andy@goryachev.com>
package goryachev.password;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CMenuItem;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CPopupMenu;
import goryachev.common.ui.CPopupMenuController;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CSplitPane;
import goryachev.common.ui.Dialogs;
import goryachev.common.ui.GlobalSettings;
import goryachev.common.ui.Theme;
import goryachev.common.ui.UI;
import goryachev.common.ui.table.CTableSelector;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZFilterLogic;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.util.CComparator;
import goryachev.common.util.CList;
import goryachev.common.util.Menus;
import goryachev.common.util.TXT;
import goryachev.password.data.DataFile;
import goryachev.password.data.PassEntry;
import java.awt.event.KeyEvent;
import javax.swing.JPopupMenu;


public class ListTab
	extends CPanel
{
	public final CAction addEntryAction = new CAction() { public void action() { actionAddEntry(); } };
	public final CAction deleteEntryAction = new CAction() { public void action() { actionDeleteEntry(); } };
	public final CAction focusFilterAction = new CAction() { public void action() { actionFocusFilter(); } };
	public final CAction focusTableAction = new CAction() { public void action() { actionFocusTable(); } };

	public final ZModel<PassEntry> model;
	public final ZTable table;
	public final ZFilterLogic filter;
	public final PassEditor passEditor;
	public final CTableSelector selector;
	protected CPanel detailPanel;
	protected boolean handleEvents;
	private DataFile dataFile;


	public ListTab()
	{
		setName("ListTab");
		
		model = new ZModel();		
		model.addColumn(TXT.get("ListTab.column.name", "Name"), new ZColumnHandler<PassEntry>()
		{
			public Object getCellValue(PassEntry x) { return x.getName(); }
		});

		table = new ZTable(model);

		selector = new CTableSelector(table, true, false, true)
		{
			public void tableSelectionChangeDetected()
			{
				onSelectionChange();
			}
		};

		filter = new ZFilterLogic(table);
		UI.whenFocused(filter.getField(), KeyEvent.VK_DOWN, focusTableAction);

		CScrollPane scroll = new CScrollPane(table, CScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, CScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getViewport().setBackground(Theme.TEXT_BG);

		new CPopupMenuController(table, scroll)
		{
			public JPopupMenu constructPopupMenu()
			{
				return createPopupMenu();
			}
		};

		// detail
		
		passEditor = new PassEditor();
		
		detailPanel = new CPanel();
		detailPanel.setName("detail");
		detailPanel.setBackground(Theme.TEXT_BG);
		
		// layout
		
		CSplitPane split = new CSplitPane(true, scroll, detailPanel);
		split.setName("split");
		split.setBorder(new CBorder());
		split.setDividerLocation(150);

		setCenter(split);

		updateActions();
		handleEvents = true;
	}
	
	
	public JPopupMenu createPopupMenu()
	{
		CPopupMenu m = new CPopupMenu();
		m.add(new CMenuItem(TXT.get("ListTab.menu.add entry", "Add Entry"), addEntryAction));
		m.addSeparator();
		m.add(new CMenuItem(TXT.get("ListTab.menu.delete entry", "Delete"), deleteEntryAction));
		return m;
	}
	
	
	protected void onSelectionChange()
	{
		selector.ensureSelection();
		setSelection(model.getSelectedEntries(selector));
		updateActions();
	}
	

	public void updateActions()
	{
		deleteEntryAction.setEnabled(selector.isNotEmpty());
	}

	
	public void setSelection(CList<PassEntry> items)
	{
		handleEvents = false;
		
		GlobalSettings.storePreferences(detailPanel);
		detailPanel.removeAll();
		
		if(items == null)
		{
			// no selection
			detailPanel.setCenter(null);
			passEditor.setEntry(null);
		}
		else if(items.size() == 0)
		{
			// no selection
			detailPanel.setCenter(null);
			passEditor.setEntry(null);
			
			if(filter.isFiltering())
			{
				if(table.getRowCount() > 0)
				{
					table.changeSelection(0, 0, false, false);
				}
			}
		}
		else if(items.size() == 1)
		{
			// single selection
			detailPanel.setCenter(passEditor);
			PassEntry en = items.get(0);
			passEditor.setEntry(en);
		}
		else
		{
			// multiple selection
			detailPanel.setCenter(null);
			passEditor.setEntry(null);
		}
		
		GlobalSettings.restorePreferences(detailPanel);
		detailPanel.validate();
		detailPanel.repaint();
		
		handleEvents = true;
	}


	public void setDataFile(DataFile df)
	{
		this.dataFile = df;
		refresh();
	}
	

	public DataFile getDataFile()
	{
		return dataFile;
	}
	
	
	public void purgeSecrets()
	{
		// ?
	}

	
	protected void refresh()
	{
		PassEntry[] es = dataFile.getEntries();
		
		new CComparator<PassEntry>()
		{
			public int compare(PassEntry a, PassEntry b)
			{
				return compareText(a.getName(), b.getName());
			}	
		}.sort(es);
		
		model.replaceAll(es);
		
		if(table.getRowCount() > 0)
		{
			table.changeSelection(0, 0, false, false);
		}
	}
	
	
	protected void modified()
	{
		MainWindow.get(this).setModified(true);
	}
	
	
	public void actionAddEntry()
	{
		filter.clear();
		
		PassEntry en = new AddEntryDialog(this, dataFile).openDialog();
		if(en != null)
		{
			refresh();
			select(en);
			passEditor.focus();
		}
	}
	
	
	public void actionDeleteEntry()
	{
		if(Dialogs.confirm(this, Menus.Delete, TXT.get("ListTab.delete entries", "Delete selected entries?"), Menus.Delete))
		{
			CList<PassEntry> sel = model.getSelectedEntries(selector);
			for(PassEntry en: sel)
			{
				model.removeItem(en);
				dataFile.removeEntry(en);
			}
			
			modified();
				
			filter.clear();
			selector.selectFirstRow();
		}
	}
	
	
	public void select(PassEntry en)
	{
		int ix = indexOf(en);
		if(ix >= 0)
		{
			table.changeSelection(ix, 0, false, false);
		}
	}
	
	
	protected int indexOf(PassEntry en)
	{
		for(int i=0; i<table.getRowCount(); i++)
		{
			int ix = table.convertRowIndexToModel(i);
			if(ix >= 0)
			{
				PassEntry r = model.getItem(ix);
				if(r == en)
				{
					// FIX why is this here?
					//table.changeSelection(i, 0, false, false);
					
					return i;
				}
			}
		}
		return -1;
	}
	
	
	public void actionFocusFilter()
	{
		filter.getField().requestFocusInWindow();
	}


	protected void actionFocusTable()
	{
		if(table.getSelectedRowCount() == 0)
		{
			table.changeSelection(0, 0, false, false);
		}

		table.requestFocusInWindow();
	}


	public void refresh(PassEntry en)
	{
		int ix = indexOf(en);
		if(ix >= 0)
		{
			model.fireTableRowsUpdated(ix, ix);
		}
	}
}
