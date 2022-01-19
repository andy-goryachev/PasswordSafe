// Copyright © 2011-2020 Andy Goryachev <andy@goryachev.com>
package goryachev.cryptoswing;


public enum VCode
{
	Backspace("\u2190"),
	Tab("tab"),
	CapsLock("caps"),
	Enter("\u21b2"),
	Shift("shift"),
	AltGr("altgr"),
	National("\u2609"),
	Empty("");
	
	//
	
	private String text;


	VCode(String text)
	{
		this.text = text;
	}


	public String getText()
	{
		return text;
	}
}