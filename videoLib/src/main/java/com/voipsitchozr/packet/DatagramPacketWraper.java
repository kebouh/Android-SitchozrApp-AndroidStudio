package com.voipsitchozr.packet;

import java.nio.ByteBuffer;

public class DatagramPacketWraper extends AbstractPacketWraper<byte[]> {

	
	public DatagramPacketWraper(byte[] data)
	{
		super(data);
	}


	@Override
	public byte[] getPacket() {
		// TODO Auto-generated method stub
		byte[] toSend = ByteBuffer.allocate(4 + data.length).putInt(data.length).array();		
		System.arraycopy(data, 0, toSend, 4, data.length);
	//	return toSend;
	return data;
	}


	@Override
	public void createPacket() {
//CREATE HEADER		
	}
	
}
