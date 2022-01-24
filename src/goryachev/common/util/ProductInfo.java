// Copyright © 2013-2022 Andy Goryachev <andy@goryachev.com>
package goryachev.common.util;


/** application product information */
public class ProductInfo
{
	// web site url
	private static Object webSite = new Obj("http://goryachev.com");
	public static String getWebSite() { return webSite.toString(); }
	public static void setWebSite(String s) { webSite = s; }
}
