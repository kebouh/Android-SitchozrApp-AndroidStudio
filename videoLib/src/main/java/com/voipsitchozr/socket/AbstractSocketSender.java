package com.voipsitchozr.socket;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.voipsitchozr.packet.AbstractPacketWraper;

public abstract class AbstractSocketSender {

	Object					socket;
	int					portServer;
	String				addrServer;
	InetAddress			inetAddr;
	
	
	public	AbstractSocketSender(String addr, int port)
	{
		this.addrServer = addr;
		this.portServer = port;
	}
	
	public	void	initInetAddrAndSocket() throws IOException
	{
		try {
			inetAddr = InetAddress.getByName(addrServer);
			if (socket instanceof TcpSocketWraper)
			{
				socket = new Socket(inetAddr, portServer);
			}
			else
				socket = new DatagramSocket(portServer);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public	abstract void	sendPacket(AbstractPacketWraper<?>	packet);
}
