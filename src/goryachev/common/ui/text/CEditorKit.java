// Copyright (c) 2009-2015 Andy Goryachev <andy@goryachev.com>
package goryachev.common.ui.text;
import goryachev.common.util.CLanguage;
import java.util.Locale;
import javax.swing.text.AbstractDocument;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


public class CEditorKit
	extends StyledEditorKit
{
	public static final CEditorBoldAction boldAction = new CEditorBoldAction();
	public static final CEditorCopyAction copyAction = new CEditorCopyAction();
	public static final CEditorCutAction cutAction = new CEditorCutAction();
	public static final CEditorPasteAction pasteAction = new CEditorPasteAction();
	public static final CEditorItalicAction italicAction = new CEditorItalicAction();
	public static final CEditorSelectAllAction selectAllAction = new CEditorSelectAllAction();
	public static final CEditorSelectLineAction selectLineAction = new CEditorSelectLineAction();
	public static final CEditorStrikethroughAction strikeThroughAction = new CEditorStrikethroughAction();
	public static final CEditorSubscriptAction subscriptAction = new CEditorSubscriptAction();
	public static final CEditorSuperscriptAction superscriptAction = new CEditorSuperscriptAction();
	public static final CEditorToUpperCaseAction toUpperCaseAction = new CEditorToUpperCaseAction();
	public static final CEditorToLowerCaseAction toLowerCaseAction = new CEditorToLowerCaseAction();
	public static final CEditorUnderlineAction underlineAction = new CEditorUnderlineAction();
	private final ViewFactory viewFactory = createViewFactory();
	private boolean noWrapMode;
	protected int tabSize = 4;
	private static final Object KEY_LOCALE = new Object();


	public CEditorKit()
	{
	}
	
	
	private ViewFactory createViewFactory()
	{
		return new ViewFactory()
		{
			public View create(Element elem)
			{
				return createView(elem);
			}
		};
	}


	public ViewFactory getViewFactory()
	{
		return viewFactory;
	}
	
	
	public int getTabSize()
	{
		return tabSize;
	}
	
	
	protected View createView(Element elem)
	{
		String name = elem.getName();
		if(name != null)
		{
			if(name.equals(AbstractDocument.ContentElementName))
			{
				// deals with breaking long words
				return new XLabelView(elem);
			}
			else if(name.equals(AbstractDocument.ParagraphElementName))
			{
				// deals with breaking long words
				return new XParagraphView(this, elem);
			}
			else if(name.equals(AbstractDocument.SectionElementName))
			{
				return new XBoxView(elem, View.Y_AXIS);
			}
			else if(name.equals(StyleConstants.ComponentElementName))
			{
				return new ComponentView(elem);
			}
			else if(name.equals(StyleConstants.IconElementName))
			{
				return new IconView(elem);
			}
		}

		return new XLabelView(elem);
	}


	public Document createDefaultDocument()
	{
		return new CDocument();
	}
	
	
	public void setNoWrapMode(boolean on)
    {
		noWrapMode = on;
    }
	
	
	public boolean isNoWrapMode()
    {
		return noWrapMode;
    }
	
	
	public MutableAttributeSet getInputAttributes()
	{
		// TODO use this to clean up attributes
		return super.getInputAttributes();
	}


	// called on attribute change
	protected void createInputAttributes(Element em, MutableAttributeSet a)
	{
		super.createInputAttributes(em, a);
		
		boldAction.setSelected(StyleConstants.isBold(a));
		italicAction.setSelected(StyleConstants.isItalic(a));
		underlineAction.setSelected(StyleConstants.isUnderline(a));
		strikeThroughAction.setSelected(StyleConstants.isStrikeThrough(a));
		subscriptAction.setSelected(StyleConstants.isSubscript(a));
		superscriptAction.setSelected(StyleConstants.isSuperscript(a));
	}


	public static Locale getLocale(JTextComponent c)
	{
		Locale loc = null;
		if(c != null)
		{
			Object x = c.getClientProperty(KEY_LOCALE);
			if(x instanceof Locale)
			{
				loc = (Locale)x;
			}
		}
		
		if(loc != null)
		{
			return loc;
		}
		
		return Locale.getDefault();
	}


	public static void setLocale(JTextComponent c, CLanguage la)
	{
		setLocale(c, la == null ? null : la.getLocale());
	}
	
	
	public static void setLocale(JTextComponent c, Locale loc)
	{
		if(c != null)
		{
			c.putClientProperty(KEY_LOCALE, loc);
		}
	}
}
