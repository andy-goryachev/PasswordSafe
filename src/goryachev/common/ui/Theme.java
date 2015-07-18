// Copyright (c) 2008-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui;
import goryachev.common.ui.theme.ATheme;
import goryachev.common.ui.theme.AgButtonUI;
import goryachev.common.ui.theme.AgCheckBoxUI;
import goryachev.common.ui.theme.AgComboBoxUI;
import goryachev.common.ui.theme.AgMenuBarUI;
import goryachev.common.ui.theme.AgMenuItemUI;
import goryachev.common.ui.theme.AgPanelUI;
import goryachev.common.ui.theme.AgPopupMenuUI;
import goryachev.common.ui.theme.AgPopupSeparatorUI;
import goryachev.common.ui.theme.AgRadioButtonUI;
import goryachev.common.ui.theme.AgScrollBarUI;
import goryachev.common.ui.theme.AgScrollPaneUI;
import goryachev.common.ui.theme.AgSeparatorUI;
import goryachev.common.ui.theme.AgSplitPaneUI;
import goryachev.common.ui.theme.AgTabbedPaneUI;
import goryachev.common.ui.theme.AgTableHeaderUI;
import goryachev.common.ui.theme.AgToolBarUI;
import goryachev.common.ui.theme.AgToolTipUI;
import goryachev.common.ui.theme.AgTreeUI;
import goryachev.common.ui.theme.CFieldBorder;
import goryachev.common.ui.theme.SpinningGearIcon;
import goryachev.common.ui.theme.ThemeColor;
import goryachev.common.ui.theme.ThemeOptions;
import goryachev.common.ui.theme.TimePeriodFormatter;
import goryachev.common.util.CList;
import goryachev.common.util.CPlatform;
import goryachev.common.util.Log;
import goryachev.common.util.Rex;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.InsetsUIResource;


/** defines color/font theme and forces a platform-independent look and feel */
public class Theme
{
	public static final ThemeColor COLOR_PANEL_BG = ThemeColor.create(ThemeKey.COLOR_PANEL_BG);
	public static final ThemeColor COLOR_PANEL_FG = ThemeColor.create(ThemeKey.COLOR_PANEL_FG);
	public static final ThemeColor COLOR_TOOLBAR = ThemeColor.create(ThemeKey.COLOR_TOOLBAR);
	public static final ThemeColor COLOR_TEXT_BG = ThemeColor.create(ThemeKey.COLOR_TEXT_BG);
	public static final ThemeColor COLOR_TEXT_FG = ThemeColor.create(ThemeKey.COLOR_TEXT_FG);
	public static final ThemeColor COLOR_TARGET = ThemeColor.create(ThemeKey.COLOR_TARGET);
	public static final ThemeColor COLOR_FOCUS = ThemeColor.create(ThemeKey.COLOR_FOCUS);
	public static final ThemeColor COLOR_LINE = ThemeColor.create(ThemeKey.COLOR_LINE);
	public static final ThemeColor COLOR_LINK = ThemeColor.create(ThemeKey.COLOR_LINK);
	public static final ThemeColor COLOR_FIELD_BG = ThemeColor.create(ThemeKey.COLOR_FIELD_BG);
	public static final ThemeColor COLOR_FIELD_FG = ThemeColor.create(ThemeKey.COLOR_FIELD_FG);
	public static final ThemeColor COLOR_BUTTON_AFFIRM = ThemeColor.create(ThemeKey.COLOR_BUTTON_AFFIRM);
	public static final ThemeColor COLOR_BUTTON_DESTRUCTIVE = ThemeColor.create(ThemeKey.COLOR_BUTTON_DESTRUCTIVE);
	public static final ThemeColor COLOR_GRID = ThemeColor.create(ThemeKey.COLOR_GRID);
	public static final ThemeColor COLOR_TEXT_SELECTION_BG = ThemeColor.create(ThemeKey.COLOR_TEXT_SELECTION_BG);
	public static final ThemeColor COLOR_TEXT_SELECTION_FG = ThemeColor.create(ThemeKey.COLOR_TEXT_SELECTION_FG);
	public static final ThemeColor COLOR_TOOL_TIP_BG = ThemeColor.create(ThemeKey.COLOR_TOOL_TIP_BG);
	
	private static float gradientFactor = 0.84f;
	
	private static Border border10;
	private static Border fieldBorder;
	private static Border lineBorder;
	private static Border noBorder;
	private static Border raisedBevelBorder;
	private static Border loweredBevelBorder;

	protected static Dimension preferredToolbarDimensions;


	public static void init()
	{
		try
		{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e)
		{ }
		
		try
		{
			Toolkit.getDefaultToolkit().setDynamicLayout(true);
		}
		catch(Exception e)
		{
			Log.err(e);
		}
		
		// init default colors and borders
		theme();
		
		// borders
		fieldBorder = new CFieldBorder();
		border10 = new CBorder(10);
		noBorder = new CBorder(0);
		lineBorder = new CBorder(COLOR_LINE);
		
		UIDefaults d = UIManager.getLookAndFeelDefaults();
		
		AgButtonUI.init(d);
		AgCheckBoxUI.init(d);
		AgComboBoxUI.init(d);
		AgMenuBarUI.init(d);
		AgMenuItemUI.init(d);
		AgPanelUI.init(d);
		AgPopupMenuUI.init(d);
		AgPopupSeparatorUI.init(d);
		AgRadioButtonUI.init(d);
		AgScrollBarUI.init(d);
		AgScrollPaneUI.init(d);
		AgSeparatorUI.init(d);
		AgSplitPaneUI.init(d);
		AgTabbedPaneUI.init(d);
		AgTableHeaderUI.init(d);
		AgToolBarUI.init(d);
		AgToolTipUI.init(d);
		AgTreeUI.init(d);

		if(CPlatform.isMac())
		{
			// margins
			d.put("EditorPane.margin", new InsetsUIResource(3, 3, 3, 3));
			d.put("TextPane.margin", new InsetsUIResource(3, 3, 3, 3));
			
			// ui
			d.put("MenuUI", "javax.swing.plaf.basic.BasicMenuUI");
			d.put("Menu.background", Theme.panelBG());
			d.put("Menu.useMenuBarBackgroundForTopLevel", Boolean.TRUE);
		}
		
		// label
		d.put("Label.background", Theme.panelBG());
		d.put("Label.foreground", Theme.textFG());
		
		// menu
		d.put("Menu.background", Theme.panelBG());
		d.put("Menu.foreground", Theme.textFG());
		
		// table
		d.put("Table.background", Theme.COLOR_TEXT_BG);
		d.put("Table.foreground", Theme.COLOR_TEXT_FG);
		d.put("Table.selectionBackground", Theme.COLOR_TEXT_SELECTION_BG);
		d.put("Table.selectionForeground", Theme.COLOR_TEXT_SELECTION_FG);
		d.put("Table.gridColor", Theme.gridColor());
		//d.put("Table.cellNoFocusBorder", new CBorder.UIResource(2,1));
		
		// text area
		d.put("TextArea.background", Theme.textBG());
		d.put("TextArea.foreground", Theme.textFG());
		
		// text field
		d.put("TextField.border", fieldBorder);
		d.put("TextField.background", Theme.textBG());
		d.put("TextField.foreground", Theme.textFG());
		d.put("TextField.inactiveBackground", ThemeColor.create(ThemeKey.COLOR_TEXT_BG, 0.5, ThemeKey.COLOR_PANEL_BG));
		d.put("TextField.inactiveForeground", ThemeColor.create(ThemeKey.COLOR_TEXT_FG, 0.5, ThemeKey.COLOR_PANEL_BG));
		
		// text pane
		d.put("TextPane.background", Theme.textBG());
		d.put("TextPane.foreground", Theme.textFG());
		
		//d.put("PasswordFieldUI.border", BORDER_FIELD);
	}
		

	public static Color panelBG()
	{
		return COLOR_PANEL_BG;
	}
	

	public static Color panelFG()
	{
		return COLOR_PANEL_FG;
	}

	
	public static Color toolbarColor()
	{
		return COLOR_TOOLBAR;
	}


	public static Color textBG()
	{
		return COLOR_TEXT_BG;
	}


	public static Color textFG()
	{
		return COLOR_TEXT_FG;
	}
	
	
	/** indicates positional target: focused text field (TODO), current table row and column */
	public static Color targetColor()
	{
		return COLOR_TARGET;
	}
	
	
	/** focus dotted border color */
	public static Color focusColor()
	{
		return COLOR_FOCUS;
	}


	/** border line color */
	public static Color lineColor()
	{
		return COLOR_LINE;
	}


	public static Color linkColor()
	{
		return COLOR_LINK;
	}
	
	
	public static Color fieldBG()
	{
		return COLOR_FIELD_BG;
	}
	
	
	public static Color fieldFG()
	{
		return COLOR_FIELD_FG;
	}
	
	
	public static Color buttonHighlight()
	{
		return COLOR_BUTTON_AFFIRM;
	}
	
	
	public static Color alternativeButtonHighlight()
	{
		return COLOR_BUTTON_DESTRUCTIVE;
	}
	
	
	public static Color gridColor()
	{
		return COLOR_GRID;
	}


	public static Color textSelectionBG()
	{
		return COLOR_TEXT_SELECTION_BG;
	}


	public static Color textSelectionFG()
	{
		return COLOR_TEXT_SELECTION_FG;
	}
	
	
	public static Color toolTipBG()
	{
		return COLOR_TOOL_TIP_BG;
	}
	
	
	public static boolean isDark()
	{
		return theme().isDark();
	}
	
	
	// FIX use ThemeColor-derived colors
	@Deprecated
	public static Color darker(Color c)
	{
		return ColorTools.darker(c, gradientFactor);
	}
	@Deprecated
	public static Color darker(Color c, float factor)
	{
		return ColorTools.darker(c, gradientFactor * factor);
	}
	@Deprecated
	public static Color brighter(Color c)
	{
		return ColorTools.brighter(c, gradientFactor);
	}
	@Deprecated
	public static Color brighter(Color c, float factor)
	{
		return ColorTools.brighter(c, gradientFactor * factor);
	}
	

	public static float getGradientFactor()
	{
		return gradientFactor;
	}
	
	
	public static Border lineBorder()
	{
		return lineBorder;
	}
	
	
	public static Border fieldBorder()
	{
		return fieldBorder;
	}
	
	
	public static Border border10()
	{
		return border10;
	}
	
	
	public static Border noBorder()
	{
		return noBorder;
	}


	public static Font plainFont()
	{
		return theme().getFont(ThemeKey.FONT_BASE);
	}


	public static Font monospacedFont()
	{
		return theme().getFont(ThemeKey.FONT_MONOSPACED);
	}


	public static Font boldFont()
	{
		return theme().getFont(ThemeKey.FONT_BOLD);
	}


	public static Font titleFont()
	{
		return theme().getFont(ThemeKey.FONT_TITLE);
	}

	
	public static Font getFont(double scale, boolean bold)
	{
		return UI.deriveFont(plainFont(), bold, (float)scale);
	}
	
	
//	public static Border raisedBevelBorder()
//	{
//		if(raisedBevelBorder == null)
//		{
//			raisedBevelBorder = new CBevelBorder(BORDER_FACTOR, false);
//		}
//		return raisedBevelBorder;
//	}
//	
//	
//	public static Border loweredBevelBorder()
//	{
//		if(loweredBevelBorder == null)
//		{
//			loweredBevelBorder = new CBevelBorder(BORDER_FACTOR, true);
//		}
//		return loweredBevelBorder;
//	}
	

	public static CToolBar toolbar()
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


	public static CMenuBar menubar()
	{
		CMenuBar b = new CMenuBar();
		b.setBorder(null);
		return b;
	}
	
	
	public static JButton tbutton()
	{
		CButton t = new CButton();
		t.setFocusable(false);
		return t;
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
	
	
	/** minimum button width in button panels */
	public static int minimumButtonWidth()
	{
		// TODO depends on font size
		return 70;
	}
	
	
	private static ATheme theme()
	{
		return ATheme.getTheme();
	}


	/** returns theme color specified either by a ThemeKey or a Color */
	public static Color getColor(Object x)
	{
		if(x instanceof ThemeKey)
		{
			return theme().getColor((ThemeKey)x);
		}
		else if(x instanceof Color)
		{
			return (Color)x;
		}
		else
		{
			throw new Rex("must be ThemeKey or Color");
		}
	}


	/** returns theme font specified by the key */
	public static Font getFont(ThemeKey key)
	{
		return theme().getFont(key);
	}
	
	
	public static void setTheme(String name)
	{
		ATheme.setTheme(name, true);
	}
	
	
	public static String getTheme()
	{
		return ATheme.getTheme().getName();
	}
	
	
	public static CList<String> getAvailableThemes()
	{
		return ATheme.getAvailableThemeNames();
	}
}
