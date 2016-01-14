package com.voipsitchozr.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import com.voipsitchozr.utils.ConcurrentQueue;

public class TcpSocket {

	String ip;
	int port;
	public SocketChannel channel;
	ConcurrentQueue<String> queueSend; 
	ConcurrentQueue<String> queueReceive;
	ThreadTcp tt = null;
	
	public TcpSocket(String ip, int port, ConcurrentQueue<String> queueSend, ConcurrentQueue<String> queueReceive) {
		this.ip = ip;
		this.port = port;
		this.queueSend = queueSend;
		this.queueReceive = queueReceive;
	}

	private void initTcpSocket() throws IOException {

		System.out.println("tcp: connexion");
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		System.out.println("ip: " + ip);
		channel.connect(new InetSocketAddress(ip, port));

		while (!channel.finishConnect()) {
			System.out.println("still connecting");
		}
		System.out.println("tcp: Connected");

	}

	public void	interrupt()
	{
		disconnect();
		if (tt.isAlive())
			tt.interrupt();
	}
	
	class ThreadTcp extends Thread {
		@Override
		public void run() {
			try {
				initTcpSocket();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			while (!Thread.currentThread().isInterrupted()) {

				ByteBuffer bufferA = ByteBuffer.allocate(256);
				int count = 0;
				String message = "";
				try {
					if ((count = channel.read(bufferA)) > 0) {
						bufferA.flip();
						message += Charset.defaultCharset().decode(bufferA);

					}
				} catch (IOException e) {
					this.interrupt();
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count > 0)
					queueReceive.add(message);
//				System.out.println(message);
				// write some data into the channel
				if (!queueSend.isEmpty()) {
					String txt = queueSend.poll();
					if (txt != null) {
						CharBuffer buffer = CharBuffer.wrap(txt);
						System.out.println("tcp write:" + buffer);

						while (buffer.hasRemaining()) {
							try {
								channel.write(Charset.defaultCharset().encode(buffer));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
				message = "";
			}

		}
	}

	public void start() throws IOException, Exception {
		tt = new ThreadTcp();
		tt.start();
	}

	public void disconnect() {
		try {
			if (channel != null && channel.isConnected())
			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
