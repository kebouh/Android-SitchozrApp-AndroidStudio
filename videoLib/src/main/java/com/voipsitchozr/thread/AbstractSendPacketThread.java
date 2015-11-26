package com.voipsitchozr.thread;

import java.io.IOException;

import android.util.Log;

import com.voipsitchozr.packet.DatagramPacketWraper;
import com.voipsitchozr.socket.DatagramSocketSender;
import com.voipsitchozr.utils.ConcurrentQueue;

import com.voipsitchozr.packet.AbstractPacketWraper;
import com.voipsitchozr.socket.AbstractSocketSender;

public abstract class AbstractSendPacketThread <T> extends Thread {
	
	protected ConcurrentQueue<T> queue;
	public AbstractSocketSender	socket;

	
	public AbstractSendPacketThread (ConcurrentQueue<T> queue)
	{
		this.queue = queue;
	}

	public void setDatagramSocket(DatagramSocketSender socket){
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			socket.initInetAddrAndSocket();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("VideoSender", "Run");
		while (!Thread.currentThread().isInterrupted())
		{
			T		data;
			if (!queue.isEmpty())
			{
				data = queue.poll();
				DatagramPacketWraper packet = (DatagramPacketWraper) preparePacket(data);
				if (packet != null)
				socket.sendPacket(packet);
			}
		}
	}
	
	public		abstract	AbstractPacketWraper<T> preparePacket(T data);
	
}
