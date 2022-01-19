// Copyright © 2015-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.dialogs.options;
import goryachev.common.util.CKit;
import goryachev.i18n.TXT;
import goryachev.swing.CButton;
import goryachev.swing.CComboBox;
import goryachev.swing.CPanel;
import goryachev.swing.InputTracker;
import goryachev.swing.Theme;
import goryachev.swing.XAction;
import goryachev.swing.options.OptionEditorInterface;
import goryachev.swing.theme.ATheme;
import javax.swing.JComponent;


public class ThemeOptionEditor
	extends CPanel
	implements OptionEditorInterface
{
	public final XAction editThemeAction = new XAction(this::actionEditTheme);
	public final CComboBox selectorField;
	protected ThemePreviewPanel preview;
	private String old;
	private boolean changed;
	
	
	public ThemeOptionEditor()
	{
		old = Theme.getTheme();
		
		selectorField = new CComboBox(Theme.getAvailableThemes());
		selectorField.setSelectedItem(old);
		new InputTracker(selectorField)
		{
			public void onInputEvent()
			{
				updateTheme();
			}
		};
		
		preview = new ThemePreviewPanel(false);
		
		// FIX
		editThemeAction.setEnabled(System.getProperty("edit.theme") != null);
		
		CPanel p = new CPanel(false);
		p.addColumns
		(
			CPanel.PREFERRED,
			CPanel.FILL,
			CPanel.PREFERRED
		);
		
		p.setBorder(5);
		p.setGaps(5, 5);
		p.row(0, p.label(TXT.get("ThemeOptionEditor.theme name", "Name:")));
		p.row(1, selectorField);
		p.row(2, new CButton("Customize", editThemeAction));
		
		setNorth(p);
		setCenter(preview);
	}
	

	public void init()
	{
	}


	public JComponent getComponent()
	{
		return this;
	}


	public boolean isFullWidth()
	{
		return true;
	}


	public float getPreferredHeight()
	{
		return HEIGHT_MAX;
	}


	public boolean isModified()
	{
		return CKit.notEquals(old, getThemeName());
	}
	
	
	public String getThemeName()
	{
		return (String)selectorField.getSelectedItem();
	}


	public void commit() throws Exception
	{
		String name = getThemeName();
		Theme.setTheme(name);
	}
	
	
	public void revert()
	{
		if(changed)
		{
			Theme.setTheme(old);
		}
	}


	public String getSearchString()
	{
		return null;
	}
	
	
	protected void updateTheme()
	{
		String name = getThemeName();
		ATheme.setTheme(name, false);
		changed = true;
	}
	
	
	protected void actionEditTheme()
	{
		String base = getThemeName();
		new ThemeEditorDialog(this, base).open();
	}
}
