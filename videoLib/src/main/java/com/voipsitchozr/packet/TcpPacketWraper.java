package com.voipsitchozr.packet;

public class TcpPacketWraper extends AbstractPacketWraper<String> {


	public TcpPacketWraper(String data)
	{
		super(data);
	}

	@Override
	public String getPacket() {
		// TODO Auto-generated method stub
		return data;
	}

	@Override
	public void createPacket() {
		//create packet MAGIC + SIZE.. header		
	}
	
}
