package com.voipsitchozr.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.voipsitchozr.packet.AbstractPacketWraper;
import com.voipsitchozr.packet.TcpPacketWraper;

public class TcpSocketWraper extends AbstractSocketSender {
/*
	Socket				socket;
	int					portServer;
	String				addrServer;
	InetAddress			inetAddr;*/
    PrintWriter 		mBufferOut;
    BufferedReader		mBufferIn;
	
	public	TcpSocketWraper(String addr, int port)
	{
		super(addr, port);/*
		this.addrServer = addr;
		this.portServer = port;*/
	}
	
	public	void	initInetAddrAndSocket() throws IOException
	{
		super.initInetAddrAndSocket();
		mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(((Socket) socket).getOutputStream())), true);
        mBufferIn = new BufferedReader(new InputStreamReader(((Socket) socket).getInputStream()));

		/*try {
			inetAddr = InetAddress.getByName(addrServer);
			socket = new Socket(inetAddr, portServer);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}
	
	@Override
	public	void	sendPacket(AbstractPacketWraper<?>	packet)
	{		
		mBufferOut.println(packet.toString());
		mBufferOut.flush();
	}
	
	public TcpPacketWraper	receivePacket() throws IOException
	{
		String d = mBufferIn.readLine();
		return new TcpPacketWraper(d);
	}
}
