// Copyright © 2012-2017 Andy Goryachev <andy@goryachev.com>
package goryachev.common.io;
import goryachev.common.util.CKit;
import goryachev.common.util.FileTools;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;


public class CWriter
	extends BufferedWriter
{
	public CWriter(File f) throws Exception
	{
		this(f, CKit.CHARSET_UTF8);
	}
	
	
	public CWriter(String filename) throws Exception
	{
		this(filename, CKit.CHARSET_UTF8);
	}
	
	
	public CWriter(File f, Charset cs) throws Exception
	{
		this(new FileOutputStream(ensureParent(f)), cs);
	}
	
	
	public CWriter(String filename, Charset cs) throws Exception
	{
		this(new FileOutputStream(ensureParent(new File(filename))), cs);
	}
	
	
	public CWriter(OutputStream in, Charset cs) throws Exception
	{
		super(new OutputStreamWriter(in, cs));
	}
	
	
	public CWriter(OutputStream in) throws Exception
	{
		super(new OutputStreamWriter(in, CKit.CHARSET_UTF8));
	}
	
	
	public void nl() throws IOException
	{
		write("\n");
	}
	
	
	private static File ensureParent(File f)
	{
		FileTools.ensureParentFolder(f);
		return f;
	}
}
