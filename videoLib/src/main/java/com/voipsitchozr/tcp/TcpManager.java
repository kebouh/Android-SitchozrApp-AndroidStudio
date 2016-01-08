package com.voipsitchozr.tcp;

import com.voipsitchozr.chat.ChatView;
import com.voipsitchozr.main.VoipManager;
import com.voipsitchozr.options.ConnexionOptions;
import com.voipsitchozr.socket.TcpSocket;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.voipsitchozr.utils.ConcurrentQueue;

import java.net.SocketException;

public class TcpManager {

	Context context;
	TcpSocket tcp;
	public ConcurrentQueue<String> queueSend;
	public ConcurrentQueue<String> queueReceive;
	TcpReceiveThread tcpReceive = null;
	TcpCommand		tcpCommand = null;
	TcpActions		tcpActions = null;
	public	TcpManager(Context context)
	{
		this.context = context;
		queueSend = new ConcurrentQueue<String>();
		queueReceive = new ConcurrentQueue<String>();
		this.tcpCommand = new TcpCommand(queueSend, queueReceive);
		this.tcpActions = new TcpActions(tcpCommand);
	}
	
	public void	init() throws Exception
	{
		System.out.println("server: " + ConnexionOptions.SERVER_IP + " " + ConnexionOptions.SERVER_PORT);
		tcpActions.addReceiveCodeActions();
		tcp = new TcpSocket(ConnexionOptions.SERVER_IP, ConnexionOptions.SERVER_PORT, queueSend, queueReceive);
		tcp.start();
		tcpReceive = new TcpReceiveThread();
		tcpReceive.start();
		Log.d("TCP", "tcp start()");

	}
	
	class TcpReceiveThread extends Thread
	{
		@Override
		public void run()
		{
			this.setPriority(Thread.MIN_PRIORITY);
			while (!Thread.currentThread().isInterrupted())
			{
				if (!queueReceive.isEmpty())
				{
					tcpCommand.getCodeAndPerformAction(queueReceive.poll());
				}
			}
		}
	}
	public void	interrupt()
	{
		tcp.interrupt();
		if (tcpReceive.isAlive())
			tcpReceive.interrupt();
	}

	public void	onDiscnonnectReceive() {
		interrupt();
		//voip.disconnect();
	}

	public void	disconnect()
	{
		interrupt();
		//voip.onStop();
	}

	public TcpCommand getTcpCommand() {return tcpCommand;}
}
