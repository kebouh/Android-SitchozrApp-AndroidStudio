package com.voipsitchozr.message;

import com.voipsitchozr.utils.ConcurrentQueue;

import com.voipsitchozr.packet.TcpPacketWraper;
import com.voipsitchozr.socket.AbstractSocketSender;
import com.voipsitchozr.thread.AbstractSendPacketThread;

public class MessageSender <T> extends AbstractSendPacketThread <String>{

	
	
	public MessageSender(ConcurrentQueue<String> queue) {
		super(queue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TcpPacketWraper preparePacket(String data) {
		// TODO Auto-generated method stub
		return new TcpPacketWraper(data);
	}

	

}
