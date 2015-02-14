// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.CButtonUI;
import goryachev.common.ui.theme.CCheckBoxUI;
import goryachev.common.ui.theme.CComboBoxUI;
import goryachev.common.ui.theme.CFieldBorder;
import goryachev.common.ui.theme.CMenuBarUI;
import goryachev.common.ui.theme.CMenuItemUI;
import goryachev.common.ui.theme.CRadioButtonUI;
import goryachev.common.ui.theme.CScrollBarUI;
import goryachev.common.ui.theme.CScrollPaneUI;
import goryachev.common.ui.theme.CSplitPaneUI;
import goryachev.common.ui.theme.CTabbedPaneUI;
import goryachev.common.ui.theme.CTableHeaderUI;
import goryachev.common.ui.theme.CTreeUI;
import goryachev.common.ui.theme.SpinningGearIcon;
import goryachev.common.ui.theme.ThemeOptions;
import goryachev.common.ui.theme.TimePeriodFormatter;
import goryachev.common.util.CFactory;
import goryachev.common.util.CPlatform;
import goryachev.common.util.Log;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.InsetsUIResource;


public class Theme
{
	private static float gradientFactor = 0.84f;
	private static float BORDER_FACTOR = 0.75f;
	
	private static Border lineBorder;
	private static Border normalBorder;
	private static Border raisedBevelBorder;
	private static Border loweredBevelBorder;

	public static final Border BORDER_10 = new CBorder(10);
	public static final Border BORDER_NONE = new CBorder(0);
	public static final Border BORDER_EMPTY = new CBorder(2, 4);
	public static final Border BORDER_FIELD = new CFieldBorder();
	public static final Border BORDER_LINE = new CBorder(lineColor());

	protected static CFactory<? extends CToolBar> toolbarFactory;
	protected static CFactory<? extends JButton> toolbarButtonFactory;
	protected static Dimension preferredToolbarDimensions;


	public static void init()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e)
		{ }

		UIDefaults defs = UIManager.getLookAndFeelDefaults();
		
		fixFonts(defs);
		
		try
		{
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		// panel background
		Color c = uiColor(panelBG());
		defs.put("Button.background", c);
		defs.put("Button.light", c);
		defs.put("CheckBox.background", c);
		defs.put("CheckBox.light", c);
		defs.put("ColorChooser.background", c);
		defs.put("ColorChooser.swatchesDefaultRecentColor", c);
		defs.put("ComboBox.buttonBackground", c);
		defs.put("ComboBox.disabledBackground", c);
		defs.put("EditorPane.disabledBackground", c);
		defs.put("FormattedTextField.disabledBackground", c);
		defs.put("FormattedTextField.inactiveBackground", c);
		defs.put("InternalFrame.activeBorderColor", c);
		defs.put("InternalFrame.borderColor", c);
		defs.put("InternalFrame.borderLight", c);
		defs.put("InternalFrame.inactiveBorderColor", c);
		defs.put("InternalFrame.inactiveTitleForeground", c);
		defs.put("InternalFrame.minimizeIconBackground", c);
		defs.put("InternalFrame.resizeIconHighlight", c);
		defs.put("Label.background", c);
		defs.put("Menu.background", c);
		defs.put("MenuBar.background", c);
		defs.put("OptionPane.background", c);
		defs.put("Panel.background", c);
		defs.put("PasswordField.disabledBackground", c);
		defs.put("PasswordField.inactiveBackground", c);
		defs.put("ProgressBar.background", c);
		defs.put("ProgressBar.selectionForeground", c);
		defs.put("RadioButton.background", c);
		defs.put("RadioButton.light", c);
		defs.put("RadioButtonMenuItem.background", c);
		defs.put("ScrollBar.background", c);
		defs.put("ScrollBar.foreground", c);
		defs.put("ScrollBar.thumb", c);
		defs.put("ScrollBar.trackForeground", c);
		defs.put("ScrollPane.background", c);
		defs.put("Slider.background", c);
		defs.put("Slider.foreground", c);
		defs.put("Spinner.background", c);
		defs.put("Spinner.foreground", c);
		defs.put("SplitPane.background", c);
		defs.put("TabbedPane.background", c);
		defs.put("TabbedPane.light", c);
		defs.put("TabbedPane.contentAreaColor", c);
		defs.put("Table.light", c);
		defs.put("TableHeader.background", c);
		defs.put("TextArea.disabledBackground", c);
		defs.put("TextField.disabledBackground", c);
		defs.put("TextField.inactiveBackground", c);
		defs.put("TextField.light", c);
		defs.put("TextPane.disabledBackground", c);
		defs.put("ToggleButton.background", c);
		defs.put("ToggleButton.light", c);
		defs.put("ToolBar.background", c);
		defs.put("ToolBar.dockingBackground", c);
		defs.put("ToolBar.floatingBackground", c);
		defs.put("ToolBar.light", c);
		defs.put("Viewport.background", c);
		defs.put("activeCaptionBorder", c);
		defs.put("control", c);
		defs.put("controlHighlight", c);
		defs.put("inactiveCaptionBorder", c);
		defs.put("inactiveCaptionText", c);
		defs.put("menu", c);
		defs.put("scrollbar", c);
		
		// selection foreground
		c = textFG();
		defs.put("CheckBoxMenuItem.selectionForeground", c);
		defs.put("ComboBox.selectionForeground", c);
		defs.put("EditorPane.selectionForeground", c);
		defs.put("FormattedTextField.selectionForeground", c);
		defs.put("List.selectionForeground", c);
		defs.put("Menu.selectionForeground", c);
		defs.put("MenuBar.selectionForeground", c);
		defs.put("MenuItem.selectionForeground", c);
		defs.put("PasswordField.selectionForeground", c);
		defs.put("PopupMenu.selectionForeground", c);
		defs.put("ProgressBar.selectionForeground", c);
		defs.put("RadioButtonMenuItem.selectionForeground", c);
		defs.put("Table.selectionForeground", c);
		defs.put("TextArea.selectionForeground", c);
		defs.put("TextField.selectionForeground", c);
		defs.put("TextPane.selectionForeground", c);
		defs.put("Tree.selectionForeground", c);
		
		// selection background
		c = selectionColor();
		defs.put("CheckBoxMenuItem.selectionBackground", c);
		defs.put("ComboBox.selectionBackground", c);
		defs.put("EditorPane.selectionBackground", c);
		defs.put("FormattedTextField.selectionBackground", c);
		defs.put("List.selectionBackground", c);
		defs.put("Menu.selectionBackground", c);
		defs.put("MenuBar.selectionBackground", c);
		defs.put("MenuItem.selectionBackground", c);
		defs.put("PasswordField.selectionBackground", c);
		defs.put("PopupMenu.selectionBackground", c);
		defs.put("ProgressBar.selectionBackground", c);
		defs.put("RadioButtonMenuItem.selectionBackground", c);
		defs.put("Table.selectionBackground", c);
		defs.put("TextArea.selectionBackground", c);
		defs.put("TextField.selectionBackground", c);
		defs.put("TextPane.selectionBackground", c);
		defs.put("Tree.selectionBackground", c);
		
		// inactive color
		c = UI.mix(127, textFG(), textBG());
		defs.put("TextField.inactiveForeground", c);
		
		if(CPlatform.isMac())
		{
			// margins
			defs.put("EditorPane.margin", new InsetsUIResource(3, 3, 3, 3));
			defs.put("TextPane.margin", new InsetsUIResource(3, 3, 3, 3));
			
			// ui
			defs.put("MenuBarUI", "javax.swing.plaf.basic.BasicMenuBarUI");
			defs.put("MenuUI", "javax.swing.plaf.basic.BasicMenuUI");
			defs.put("Menu.background", panelBG());
			defs.put("Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE);
			
			// TODO
			defs.put("ComboBox.background", Color.white);
			
			defs.put("Table.foreground", Color.black);
			defs.put("Table.selectionForeground", Color.black);
			
			//defs.put("PopupMenu.background", panelBG());
			defs.put("PopupMenu.foreground", textFG());
			//"ComboBox.foreground", 
			//"ComboBox.font"
			
			// no more aqua buttons
			defs.put("ButtonUI", "javax.swing.plaf.basic.BasicButtonUI");
			
			CMenuItemUI.init(defs);
			CMenuBarUI.init(defs);
		}
		else if(CPlatform.isLinux())
		{
			defs.put("PopupMenu.border", new CBorder(Color.gray));
			
			// buttons
			defs.put("ButtonUI", "javax.swing.plaf.basic.BasicButtonUI");
		}

		defs.put("MenuBar.border", new CBorder());
		
		// table
		defs.put("Table.gridColor", tableGridColor());
		//defs.put("Table.cellNoFocusBorder", new CBorder.UIResource(2,1));
		
		// text fields
		//defs.put("PasswordFieldUI.border", BORDER_FIELD);
		defs.put("TextField.border", BORDER_FIELD);
		defs.put("ComboBox.border", BORDER_FIELD);
		
		CButtonUI.init(defs);
		CCheckBoxUI.init(defs);
		CComboBoxUI.init(defs);
		CRadioButtonUI.init(defs);
		CScrollPaneUI.init(defs);
		CScrollBarUI.init(defs);
		CSplitPaneUI.init(defs);
		CTabbedPaneUI.init(defs);
		CTableHeaderUI.init(defs);
		CTreeUI.init(defs);
		
		// configure tooltips
		ToolTipManager tm = ToolTipManager.sharedInstance();
		tm.setInitialDelay(50);
		tm.setDismissDelay(Integer.MAX_VALUE);
		
		// standard factories
		toolbarFactory = new CFactory<CToolBar>()
		{
			public CToolBar construct()
			{
				if(preferredToolbarDimensions == null)
				{
					CToolBar t = new CToolBar();
					t.add(new CButton("W"));
					t.add(new CComboBox());
					t.add(new JLabel("W"));
					preferredToolbarDimensions = t.getPreferredSize();
					preferredToolbarDimensions.width = -1;
				}
				
				CToolBar t = new CToolBar();
				t.setMinimumSize(preferredToolbarDimensions);
				t.setPreferredSize(preferredToolbarDimensions);
				return t;
			}
		};
		
		toolbarButtonFactory = new CFactory<JButton>()
		{
			public JButton construct()
			{
				CButton t = new CButton();
				t.setFocusable(false);
				return t;
			}
		};
	}
		

	private static void fixFonts(UIDefaults defs)
	{
		Font font = getPanelFont();
		int size = font.getSize();

		// force all fonts to have the same size and no bold attribute
		for(Object key: defs.keySet())
		{
			if(key instanceof String)
			{
				String k = key.toString();
				if(k.endsWith(".font"))
				{
					Font f = defs.getFont(key);
					if(f != null)
					{
						f = f.deriveFont(Font.PLAIN, size);
						defs.put(key, f);
					}
				}
			}
		}
	}


	public static Color panelBG()
	{
		return ThemeOptions.panelBG.get();
	}
	
	
	public static Color toolbarColor()
	{
		return ThemeOptions.toolbarBG.get();
	}


	/** secondary text on panel not as visible as normal FG */ 
	public static Color panelFG()
	{
		return panelBG().darker();
	}


	public static Color textBG()
	{
		return Color.white;
	}


	public static Color textFG()
	{
		return Color.black;
	}
	
	
	public static Color hoverColor()
	{
		return ThemeOptions.hoverColor.get();
	}
	
	
	public static Color focusColor()
	{
		return ThemeOptions.focusColor.get();
	}


	/** darker than panel packground */
	public static Color lineColor()
	{
//		Color c = UI.mix(48, Color.black, panelBG());
//		return c;
		return ThemeOptions.lineColor.get();
	}


	public static Color linkColor()
	{
		return ThemeOptions.linkColor.get();
	}
	
	
	public static float getGradientFactor()
	{
		return gradientFactor;
	}
	
	
	public static Color darker(Color c)
	{
		return ColorTools.darker(c, gradientFactor);
	}
	
	
	public static Color darker(Color c, float factor)
	{
		return ColorTools.darker(c, gradientFactor * factor);
	}
	
	
	public static Color brighter(Color c)
	{
		return ColorTools.brighter(c, gradientFactor);
	}
	
	
	public static Color brighter(Color c, float factor)
	{
		return ColorTools.brighter(c, gradientFactor * factor);
	}
	
	
	public static Border lineBorder()
	{
		if(lineBorder == null)
		{
			lineBorder = new LineBorder(lineColor(), 1); 
		}
		return lineBorder;
	}
	
	
	public static Border normalBorder()
	{
		if(normalBorder == null)
		{
			normalBorder = new CompoundBorder(lineBorder(), BORDER_EMPTY);
		}
		return normalBorder;
	}


	public static Font getPanelFont()
	{
		Font f = UIManager.getLookAndFeelDefaults().getFont("Panel.font");
		// strip UIResource and kill bold attribute
		return f.deriveFont(Font.PLAIN, f.getSize2D());
	}


	public static Font plainFont()
	{
		return ThemeOptions.fontOption.get();
	}


	public static Font monospacedFont()
	{
		int size;
		if(CPlatform.isWindows())
		{
			size = 12;
		}
		else
		{
			size = plainFont().getSize();
		}
		return new Font("Monospaced", Font.PLAIN, size);
	}


	public static Font boldFont()
	{
		return plainFont().deriveFont(Font.BOLD);
	}


	public static Font titleFont()
	{
		Font f = plainFont();
		return f.deriveFont(Font.BOLD, f.getSize() * 1.6f);
	}

	
	public static Font getFont(double scale, boolean bold)
	{
		return UI.deriveFont(plainFont(), bold, (float)scale);
	}
	
	
	public static Color fieldBG()
	{
		return ThemeOptions.fieldBG.get();
	}
	
	
	public static Color fieldFG()
	{
		return ThemeOptions.fieldFG.get();
	}
	
	
	public static Color buttonHighlight()
	{
		return ThemeOptions.buttonHighlight.get();
	}
	
	
	public static Color alternativeButtonHighlight()
	{
		return ThemeOptions.buttonHighlightAlternative.get();
	}
	
	
	public static Color tableGridColor()
	{
		return ThemeOptions.tableGrid.get();
	}
	
	
	public static Color selectionColor()
	{
		return ThemeOptions.selectionColor.get();
	}
	
	
	public static Border raisedBevelBorder()
	{
		if(raisedBevelBorder == null)
		{
			raisedBevelBorder = new CBevelBorder(BORDER_FACTOR, false);
		}
		return raisedBevelBorder;
	}
	
	
	public static Border loweredBevelBorder()
	{
		if(loweredBevelBorder == null)
		{
			loweredBevelBorder = new CBevelBorder(BORDER_FACTOR, true);
		}
		return loweredBevelBorder;
	}
	
	
	public static void setToolbarFactory(CFactory<? extends CToolBar> f)
	{
		if(f == null)
		{
			throw new NullPointerException("null toolbar factory");
		}
		toolbarFactory = f;
	}
	

	public static CToolBar toolbar()
	{
		return toolbarFactory.construct();
	}
	
	
	public static CMenuBar menubar()
	{
		CMenuBar b = new CMenuBar();
		b.setBorder(null);
		return b;
	}
	
	
	public static void setToolbarButtonFactory(CFactory<? extends JButton> f)
	{
		if(f == null)
		{
			throw new NullPointerException("null toolbar factory");
		}
		toolbarButtonFactory = f;
	}
	
	
	public static JButton tbutton()
	{
		return toolbarButtonFactory.construct();
	}


	public static JButton tbutton(String text, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		return b;
	}
	
	
	public static JButton tbutton(String text, Icon icon, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		b.setIcon(icon);
		return b;
	}


	public static JButton tbutton(String text, String tooltip, Action a)
	{
		JButton b = tbutton();
		b.setAction(a);
		b.setText(text);
		b.setToolTipText(tooltip);
		return b;
	}
	
	
	protected static Color uiColor(Color c)
	{
		return new ColorUIResource(c);
	}
	
	
	public static String formatDateTime(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		// FIX ltr?
		return ThemeOptions.dateFormat.get().format(x) + " " + ThemeOptions.timeFormat.get().format(x);
	}
	
	
	public static String formatDate(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		return ThemeOptions.dateFormat.get().format(x);
	}
	
	
	public static String formatTime(Object x)
	{
		if(x == null)
		{
			return "";
		}
		
		return ThemeOptions.timeFormat.get().format(x);
	}
	
	
	public static String formatNumber(Object x)
	{
		if(x instanceof Number)
		{
			return ThemeOptions.numberFormat.get().format(x);
		}
		else
		{
			return "";
		}
	}
	
	
	// FIX use number format!
	public static String formatPercent(Object x)
	{
		if(x instanceof Number)
		{
			float val = ((Number)x).floatValue();
			if(Math.abs(val) < 0.1f)
			{
				return new DecimalFormat("0.##%").format(val);
			}
			else if(Math.abs(val) < 1f)
			{
				return new DecimalFormat("00.#%").format(val);
			}
			else
			{
				return new DecimalFormat("#00%").format(val);
			}
		}
		return null;
	}
	
	
	/** 
	 * Formats time period in days/hours/minutes/seconds/milliseconds.  
	 * Accepts Long or Date argument.
	 * Returns null if time is negative or null.  
	 */
	public static String formatTimePeriod2(Object x)
	{
		// FIX
		return TimePeriodFormatter.format(x);
	}
	
	
	public static String formatTimePeriod2(long t)
	{
		// FIX
		return TimePeriodFormatter.format(t);
	}
	
	
	public static String formatTimePeriodRough(Object x)
	{
		return TimePeriodFormatter.formatRough(x);
	}
	
	
	public static String formatTimePeriodRough(long t)
	{
		return TimePeriodFormatter.formatRough(t);
	}
	
	
	public static Icon waitIcon(int size)
	{
		return new SpinningGearIcon(size);
	}
}
