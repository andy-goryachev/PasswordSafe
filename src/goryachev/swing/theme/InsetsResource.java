// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.swing.theme;

import java.awt.Insets;
import javax.swing.plaf.UIResource;


public class InsetsResource extends Insets implements UIResource
{
	public InsetsResource(int top, int left, int bottom, int right)
	{
		super(top, left, bottom, right);
	}
}