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
import java.util.logging.Logger;

public class TcpManager {

	Context context;
	ChatView chat;
	TcpSocket tcp;
	ConcurrentQueue<String> queueSend;
	ConcurrentQueue<String> queueReceive;
	Handler mHandler = new Handler();
	VoipManager voip;
	TcpCommand tcpCommand = null;
	public	TcpManager(Context context, ChatView chat, VoipManager voip)
	{
		this.context = context;
		this.chat = chat;
		queueSend = new ConcurrentQueue<String>();
		queueReceive = new ConcurrentQueue<String>();
		this.voip = voip;
	}
	
	public void	init() throws Exception
	{
		System.out.println("server: " + ConnexionOptions.SERVER_IP + " " + ConnexionOptions.SERVER_PORT);
		tcp = new TcpSocket(ConnexionOptions.SERVER_IP, ConnexionOptions.SERVER_PORT, queueSend, queueReceive);
		tcp.start();
		tcpCommand = new TcpCommand();
		tcpCommand.start();
		Log.d("TCP", "tcp start()");

	}
	
	class	TcpCommand extends Thread
	{
		@Override
		public void run()
		{
			while (!Thread.currentThread().isInterrupted())
			{
				if (!queueReceive.isEmpty())
				{

					String pack = queueReceive.poll();
					pack = pack.replace("\n", "").replace("\r", "");
					System.out.println("TCP Buffer" + pack);
					if (pack.compareTo("welcome") == 0) {
						System.out.println("TCP send 301");
							queueSend.add("301 " + ConnexionOptions.ID_SELF + " " + ConnexionOptions.ID_CONTACT/* + " " + ConnexionOptions.LISTEN_PORT*/);
					}
					else
					{
						Log.d("TCP", "tcp buffer " + pack);

						System.out.println("TCP buffer" + pack);
						final String[] array = pack.split(" ", 2);
						System.out.println("TCP array[0]" + array[0] + "-" + array[0].compareTo("303"));
						if (array[0].compareTo("201") == 0)
						{
							Log.d("TCP", "start video sender");

							System.out.println("TCP start video sender: " + array[1] +"-");
							try {
								voip.startSendVideo(Integer.valueOf(array[1]));
							} catch (SocketException e) {
								e.printStackTrace();
							}
						}
						else if (array[0].compareTo("303") == 0)
						{
							System.out.println("TCP 303 code received");
							mHandler.post(new Runnable(){
								@Override
								public void run() {
									// TODO Auto-generated method stub
									System.out.println("45678");
									if (array.length == 2)
									chat.addItem(array[1]);
								}
							
							});
						}
						else if (array[0].compareTo("401") == 0) {
							onDiscnonnectReceive();
						}
					}
				}
				else if (!TcpManager.this.chat.isPendingQueueEmpty())
				{
					queueSend.add("303 " + TcpManager.this.chat.getPendingMessage());
				}
			}
		}
	}
	public void	interrupt()
	{
		tcp.interrupt();
		if (tcpCommand.isAlive())
			tcpCommand.interrupt();
	}

	public void	onDiscnonnectReceive() {
		interrupt();
		voip.disconnect();
	}

	public void	disconnect()
	{
		queueSend.add("401 disconnect");
		interrupt();
		voip.onStop();
	}
}
