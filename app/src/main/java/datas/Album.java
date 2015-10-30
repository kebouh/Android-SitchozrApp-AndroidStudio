package datas;

import java.util.ArrayList;

public class Album {

	private String name;
	private ArrayList<Images>	list;
	
	public Album(String name)
	{
		list = new ArrayList<Images>();
		this.name = name;
	}
	
	public void setList(ArrayList<Images> imgs)
	{
		list = imgs;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void addImagesToList(Images img)
	{
		list.add(img);
	}
	
	public Images	getImagesAtPosition(int pos)
	{
		return list.get(pos);
	}
	
	public ArrayList<Images>	getList()
	{
		return list;
	}
	
	@Override
	public String toString()
	{
		return name;
	}
}
