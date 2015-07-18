// Copyright (c) 2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.dialogs.options;
import goryachev.common.ui.CAction;
import goryachev.common.ui.CButton;
import goryachev.common.ui.CDialog;
import goryachev.common.ui.CPanel;
import goryachev.common.ui.CTabbedPane;
import goryachev.common.ui.CTextField;
import goryachev.common.ui.HorizontalPanel;
import goryachev.common.ui.Menus;
import goryachev.common.ui.ThemeKey;
import goryachev.common.ui.UI;
import goryachev.common.ui.icons.CIcons;
import goryachev.common.util.CUnique;
import goryachev.common.util.Parsers;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class ThemeEditorDialog
	extends CDialog
{
	public final CAction saveAction = new CAction() { public void action() { actionSave(); } };
	public final CTextField nameField;
	public final ThemePreviewPanel preview;
	public final JTextField baseFontField;
	public final JTextField titleFontField;
	public final JTextField monoFontField;
	public final CPanel selectedElementPanel;
	public final CTabbedPane tabbedPane;
	protected final AWTEventListener listener;
	
	
	// TODO attach mouse click listener and handle selected element
	public ThemeEditorDialog(Component parent)
	{
		super(parent, "ThemeEditorDialog", true);
		setMinimumSize(600, 700);
		setTitle("Customize Theme [?]"); // FIX
		borderless();
		
		nameField = new CTextField();
		
		baseFontField = new JTextField();
		
		titleFontField = new JTextField();
		
		monoFontField = new JTextField();
		
		selectedElementPanel = new CPanel();
		
		preview = new ThemePreviewPanel();
		preview.setTopText("Preview: click on an element to see what parameters can be changed.");
		
		listener = new AWTEventListener()
		{
			public void eventDispatched(AWTEvent ev)
			{
				if(ev.getID() == MouseEvent.MOUSE_PRESSED)
				{
					handleMouseClick((MouseEvent)ev);
				}
			}
		};
		Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.MOUSE_EVENT_MASK);
		
		tabbedPane = new CTabbedPane();
		// TODO set tab area background to be the same as preview bg
		// tabbedPane.setBackground(Color.red);
		tabbedPane.setContentBorderInsets(1);
		tabbedPane.addTab("Fonts", createFontsPanel());
		tabbedPane.addTab("Colors", createColorsPanel());
		tabbedPane.addTab("Icons", createIconsPanel());
		tabbedPane.addTab("Selected Element", selectedElementPanel);
		
		CPanel p = new CPanel();
		p.setNorth(preview);
		p.setCenter(tabbedPane);
		
		HorizontalPanel tb = new HorizontalPanel();
		tb.setGap(5);
		tb.setBorder(5);
		tb.add(new JLabel("Name:"));
		tb.fill(nameField);
		tb.add(new CButton(Menus.Save, saveAction, true));
		
		panel().setNorth(tb);
		panel().setCenter(p);
//		buttonPanel().addButton(Menus.Cancel, closeDialogAction);
//		buttonPanel().addButton(Menus.Save, saveAction, true);
	}
	

	public void onWindowClosed()
	{
		Toolkit.getDefaultToolkit().removeAWTEventListener(listener);
	}
	
	
	protected void handleMouseClick(MouseEvent ev)
	{
		Component c = SwingUtilities.getDeepestComponentAt((Component)ev.getSource(), ev.getX(), ev.getY());
		ThemeKey[] keys = ThemePreviewPanel.getKeys(c);
		if(keys == null)
		{
			selectedElementPanel.setCenter(null);
		}
		else
		{
			// TODO name
//			String name = Parsers.parseString(c.get)
			selectedElementPanel.setCenter(createEditors(keys));
			tabbedPane.setSelectedComponent(selectedElementPanel);
		}
		
		UI.validateAndRepaint(selectedElementPanel);
	}
	
	
	protected JComponent createEditors(ThemeKey[] keys)
	{
		CPanel p = new CPanel();
		p.border();
		p.setGaps(10, 5);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED, 
			CPanel.PREFERRED
		);
		
		CUnique<ThemeKey> u = new CUnique(keys);
		
		// fonts
		
		if(u.contains(ThemeKey.FONT_BASE))
		{
			a(p, "Base font:", baseFontField, smaller(), larger());
		}
		
		// colors
		
		if(u.contains(ThemeKey.COLOR_TEXT_FG))
		{
			a(p, "Text foreground:", new CTextField(), new CTextField(10), new CTextField(10));
		}
		if(u.contains(ThemeKey.COLOR_TEXT_BG))
		{
			a(p, "Text backround:", new CTextField(), new CTextField(10), new CTextField(10));
		}
		if(u.contains(ThemeKey.COLOR_TEXT_SELECTION_FG))
		{
			a(p, "Selected text foreground:", new CTextField(), new CTextField(10), new CTextField(10));
		}
		if(u.contains(ThemeKey.COLOR_TEXT_SELECTION_BG))
		{
			a(p, "Selected text backround:", new CTextField(), new CTextField(10), new CTextField(10));
		}
		
		return p;
	}
	

	protected CPanel createFontsPanel()
	{
		CPanel p = new CPanel();
		p.border();
		p.setGaps(10, 5);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED, 
			CPanel.PREFERRED
		);
		
		a(p, "Base font:", baseFontField, smaller(), larger());
		a(p, "Title font:", titleFontField, smaller(), larger());
		a(p, "Monospace font:", monoFontField, smaller(), larger());
		
		return p;
	}
	
	
	protected CPanel createColorsPanel()
	{
		CPanel p = new CPanel();
		p.border();
		p.setGaps(10, 5);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED,
			CPanel.PREFERRED
		);
		
		a(p, null, null, new JLabel("Foreground"), new JLabel("Background"));
		a(p, "Text:", new CTextField(), new CTextField(10), new CTextField(10));
		a(p, "Selected text:", new CTextField(), new CTextField(10), new CTextField(10));
		a(p, "Field:", new CTextField(), new CTextField(10), new CTextField(10));
		a(p, "Panel:", new CTextField(), new CTextField(10), new CTextField(10));
		a(p, "Toolbar:", new CTextField(), null, new CTextField(10));
		a(p, "Affirmative button:", new CTextField(), null, new CTextField(10));
		a(p, "Destructive button:", new CTextField(), null, new CTextField(10));
		
		return p;
	}
	
	
	protected CPanel createIconsPanel()
	{
		CPanel p = new CPanel();
		p.border();
		p.setGaps(10, 10);
		p.addColumns
		(
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL,
			CPanel.FILL
		);
		// sort by name?
		ic(p, 0, CIcons.Success32, "Success");
		ic(p, 1, CIcons.Error32, "Error");
		ic(p, 2, CIcons.Cancelled32, "Cancelled");
		ic(p, 3, CIcons.Info32, "Info");
		ic(p, 4, CIcons.Question32, "Question");
		ic(p, 5, CIcons.Warning32, "Warning");
		return p;
	}
	
	
	protected void ic(CPanel p, int col, Icon ic, String text)
	{
		JLabel t = new JLabel(text, ic, JLabel.CENTER);
		t.setVerticalTextPosition(JLabel.BOTTOM);
		t.setHorizontalTextPosition(JLabel.CENTER);
		p.row(col, t); 
	}
	
	
	protected void a(CPanel p, String text, JComponent a, JComponent b, JComponent c)
	{
		p.nextRow();
		p.row(0, p.label(text));
		if(a != null)
		{
			p.row(1, a);
		}
		if(b != null)
		{
			p.row(2, b);
		}
		if(c != null)
		{
			p.row(3, c);
		}
	}
	
	
	protected CButton smaller()
	{
		return new CButton("- Smaller");
	}
	
	
	protected CButton larger()
	{
		return new CButton("+ Larger");
	}
	
	
	public static CAction openAction(final Component parent)
	{
		return new CAction()
		{
			public void action() throws Exception
			{
				new ThemeEditorDialog(parent).open();
			}
		};
	}
	
	
	protected void actionSave()
	{
		// TODO
		close();
	}
}
