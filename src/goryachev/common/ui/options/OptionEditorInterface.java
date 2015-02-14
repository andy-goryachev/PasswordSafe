// Copyright (c) 2010-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.options;
import javax.swing.JComponent;


public interface OptionEditorInterface
{
	public static final float HEIGHT_MIN = -1;
	public static final float HEIGHT_MAX = -2;
	
	//
	
	public void init();
	
	public JComponent getComponent();
	
	public boolean isFullWidth();
	
	/** returns preferred height or HEIGHT_MIN, HEIGHT_MAX.  returns float because it's passed to TableLayout */
	public float getPreferredHeight();
	
	public boolean isModified();
	
	public void commit() throws Exception;

	public String getSearchString();
}
