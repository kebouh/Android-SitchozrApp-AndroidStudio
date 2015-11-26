package com.voipsitchozr.chat;

public class TextItem {

	boolean source = false;
	String message;
	public TextItem(String message, boolean source)
	{
		this.message = message.replace("\n", " ");
		this.source = source;
	}
	
	boolean		isSource()
	{
		return source;
	}
	
	String		getMessage()
	{
		return message;
	}
	
}
