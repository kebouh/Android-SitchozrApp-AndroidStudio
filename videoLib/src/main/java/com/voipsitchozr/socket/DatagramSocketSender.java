package com.voipsitchozr.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.voipsitchozr.packet.AbstractPacketWraper;

public class DatagramSocketSender extends AbstractSocketSender {

	public DatagramSocketSender(String addr, int port) throws SocketException
	{
		super(addr, port);
	
	}

	
	public	void	sendPacket(AbstractPacketWraper<?> pck)
	{
		byte[] data = (byte[]) pck.getPacket();
		DatagramPacket packet = new DatagramPacket(data, data.length, inetAddr, portServer);
		try {
			if (socket != null)
			((DatagramSocket)socket).send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DatagramSocket	getDatagram() { return (DatagramSocket)socket;}

}
