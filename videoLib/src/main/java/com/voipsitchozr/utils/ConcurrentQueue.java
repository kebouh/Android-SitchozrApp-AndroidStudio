package com.voipsitchozr.utils;

import java.util.ArrayDeque;

public class ConcurrentQueue <T>{

	ArrayDeque<T>		queue;
	
	public ConcurrentQueue()
	{
		queue = new ArrayDeque<T>(100);
	}
	
	public void add(T data)
	{
		queue.add(data);
	}
	
	public	void	clear()
	{
		queue.clear();
	}
	
	public T	poll()
	{
		return queue.poll();
	}
	
	public boolean	isEmpty()
	{
		return queue.isEmpty();
	}
	
	public int	getSize()
	{
		return queue.size();
	}
}
