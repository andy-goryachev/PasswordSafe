// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CBorder;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CCheckBox;
import goryachev.common.ui.CComboBox;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CRadioButton;
import goryachev.common.ui.CScrollPane;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.CTextPane;
import goryachev.common.ui.CToolBar;
import goryachev.common.ui.Theme;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.table.ZColumnHandler;
import goryachev.common.ui.table.ZModel;
import goryachev.common.ui.table.ZTable;
import goryachev.common.ui.text.CDocumentBuilder;
import goryachev.common.util.TXT;
import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.CompoundBorder;
import javax.swing.table.TableModel;


public class ThemePreviewPanel 
	extends CPanel
{
	public final CAction dummyAction = new CAction() { public void action() { }};
	private static final Object PROPERTY_KEYS = new Object();
	private static final Object PROPERTY_NAME = new Object();
	private static final int GAP = 5;
	public final CTextField textField;
	public final CComboBox comboBox;
	public final CTextPane textPane;
	public final CCheckBox checkBox;
	public final CCheckBox checkBoxDisabled;
	public final CRadioButton radioButton;
	public final CRadioButton radioButtonDisabled;
	public final ZTable table;
	
	
	public ThemePreviewPanel()
	{
		setPreferredSize(-1, 300);
		
		// text field
		textField = new CTextField("text");
		keys
		(
			textField,
			"Text Field",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		// combo box
		comboBox = new CComboBox(new Object[] 
		{
			"One", 
			"Two", 
			"Three", 
			"Four", 
			"Five", 
			"Six", 
			"Seven", 
			"Eight", 
			"Nine", 
			"Ten", 
			"Eleven", 
			"Twelve", 
			"Thirteen", 
			"Fourteen", 
			"Fifteen", 
			"Sixteen" 
		});
		keys
		(
			comboBox,
			"Combo Box",
			ThemeKey.TEXT_BG,
			ThemeKey.TEXT_FG,
			ThemeKey.TEXT_SELECTION_BG,
			ThemeKey.TEXT_SELECTION_FG
		);
		
		// checkbox
		checkBox = new CCheckBox("Check box");
		
		checkBoxDisabled = new CCheckBox("Check box (disabled)");		
		checkBoxDisabled.setEnabled(false);
		
		radioButton = new CRadioButton("Radio button");
		
		radioButtonDisabled = new CRadioButton("Radio button (disabled)");
		radioButtonDisabled.setEnabled(false);
		
		String sel = "Selected text.";
		
		CDocumentBuilder b = new CDocumentBuilder();
		b.setFont(Theme.titleFont());
		b.a("Title");
		b.setFont(Theme.plainFont());
		b.a("\nNormal font: ").a(Theme.plainFont()).nl();
		b.bold("Bold typeface").nl();
		b.a(sel).nl();
		b.setFont(Theme.monospacedFont());
		b.a("Monospaced font\n    line 1.\n    line 2.").nl();
		
		textPane = new CTextPane()
		{
			protected void processMouseEvent(MouseEvent ev)
			{
				ev.consume();
			}
		};
		textPane.setScrollableTracksViewportWidth(true);
		textPane.setEditable(false);
		textPane.setDocument(b.getDocument());
		textPane.select(10, 30);
		CScrollPane sc1 = new CScrollPane(textPane);
		sc1.setBorder(Theme.fieldBorder());

		table = new ZTable(createTableModel());
		
		CScrollPane sc2 = new CScrollPane(table);
		sc2.setBorder(Theme.lineBorder());

		CToolBar tb = Theme.toolbar();
		tb.space(5);
		tb.add("Toolbar");
		tb.fill();
		tb.add("Buttons:");
		tb.space(5);
		tb.add(new CButton("Affirmative", dummyAction, true));
		tb.add(new CButton("Destructive", dummyAction, Theme.DESTRUCTIVE_BUTTON_COLOR));
		tb.add(new CButton("Regular", dummyAction));

		CPanel p = new CPanel();
		p.setGaps(5, 2);
		p.setBorder(5);

		p.addColumns
		(
			CPanel.PREFERRED, 
			CPanel.FILL, 
			CPanel.PREFERRED, 
			CPanel.FILL
		);

		p.row(0, p.label("Text field:"));
		p.row(1, textField);
		p.row(2, p.label("Combo box:"));
		p.row(3, comboBox);
		p.nextRow();
		p.row(0, 2, checkBox);
		p.row(2, 2, radioButton);
		p.nextRow();
		p.row(0, 2, checkBoxDisabled);
		p.row(2, 2, radioButtonDisabled);		
		p.nextFillRow();
		p.row(0, 2, sc1);
		p.row(2, 2, sc2);

		CPanel pp = new CPanel(false);
		pp.setCenter(p);
		pp.setBorder(new CompoundBorder(new CBorder(GAP), new CBorder(1, Theme.TEXT_FG)));
		pp.setNorth(tb);
		
		setBackground(Theme.FIELD_BG);
		setCenter(pp);

		setTopText(TXT.get("ThemePreviewPanel.title", "Preview"));
	}
	
	
	protected void keys(JComponent c, String name, ThemeKey ... keys)
	{
		c.putClientProperty(PROPERTY_KEYS, keys);
		c.putClientProperty(PROPERTY_NAME, name);
	}
	

	public void setTopText(String s)
	{
		JLabel t = new JLabel(s);
		t.setFont(Theme.boldFont());
		t.setBorder(new CBorder(GAP, GAP, 0, GAP));
		setNorth(t);
	}
	
	
	protected TableModel createTableModel()
	{
		ZModel<Entry> m = new ZModel();
		m.addColumn("Key", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x) { return x.key; }
		});
		m.addColumn("Value", new ZColumnHandler<Entry>()
		{
			public Object getCellValue(Entry x) { return x.value; }
		});
		m.setRightAlignment();
		
		for(int i=1; i<20; i++)
		{
			Entry en = new Entry();
			en.key = word(i);
			en.value = i;
			m.addItem(en);
		}
		
		return m;
	}
	
	
	protected String word(int n)
	{
		return String.valueOf(n);
	}
	
	
	public static ThemeKey[] getKeys(Component c)
    {
		if(c instanceof JComponent)
		{
			Object v = ((JComponent)c).getClientProperty(PROPERTY_KEYS);
			if(v instanceof ThemeKey[])
			{
				return (ThemeKey[])v;
			}
		}
	    return null;
    }
	
	
	//
	
	
	protected static class Entry
	{
		public String key;
		public Object value;
	}
}
