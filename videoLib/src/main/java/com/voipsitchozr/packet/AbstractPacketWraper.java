package com.voipsitchozr.packet;

public abstract class AbstractPacketWraper <T>{

	T	data;
	
	public	AbstractPacketWraper(T data)
	{
		this.data = data;
	}
	
	public	abstract T getPacket();
	
	
	public	abstract void	createPacket();
}
