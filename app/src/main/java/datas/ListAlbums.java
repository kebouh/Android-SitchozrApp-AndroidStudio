package datas;

import java.util.ArrayList;

public class ListAlbums {

	public	ArrayList<Album>	list;
	
	public ListAlbums()
	{
		list = new ArrayList<Album>();
	}
	
	public void	addAlbumToList(Album a)
	{
		list.add(a);
	}
	
	public Album	getAlbumAtPosition(int pos)
	{
		if (list.size() > pos)
			return list.get(pos);
		return null;
	}
	
	public ArrayList<Album> getAlbumlist()
	{
		return list;
	}
	
	public Album	getAlbumByName(String name)
	{
		for (int i = 0; i != list.size(); i++)
		{
			if (name.compareTo(list.get(i).getName()) == 0)
				return list.get(i);
		}
		return null;
	}
}
