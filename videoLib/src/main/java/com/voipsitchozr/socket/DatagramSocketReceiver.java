package com.voipsitchozr.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import android.util.Log;
public class DatagramSocketReceiver {

	DatagramSocket datagram;
	DatagramPacket packet;

	byte[] buffer;

	public	DatagramSocketReceiver(int port) throws SocketException
	{
		this.buffer = new byte[65508];
		datagram = new DatagramSocket(port);
		packet = new DatagramPacket(buffer, buffer.length);
		System.out.println("availablePort: " + datagram.getPort());
	}

	public	DatagramSocketReceiver(DatagramSocket socket) throws SocketException
	{
		this.buffer = new byte[65508];
		datagram = socket;

		packet = new DatagramPacket(buffer, buffer.length);
		Log.d("UDP", "coànstructor");

	}
	
	public	byte[]	receivePacket() throws IOException
	{
		System.out.println("UDPRECEIVE");
		datagram.receive(packet);

		/*
		while (queue.getSize() > 10)
			queue.poll();*/
		/*byte[]	extract = new byte[4];
		
		extract[0] = buffer[0];
		extract[1] = buffer[1];
		extract[2] = buffer[2];
		extract[3] = buffer[3];

		//System.out.println("size packet received: " + buffer.length);
		Log.d("UDP", "packet lengh" + String.valueOf(packet.getLength()));
		ByteBuffer wrapped = ByteBuffer.wrap(extract);
		int num = wrapped.getInt();*/
		//Log.d("UDP", "size int" + String.valueOf(num));
		//if (num <= 0)
		//	return null;
		Log.d("UDP", "size int" + String.valueOf(packet.getLength()));
		byte[]	frame = new byte[packet.getLength()];
		System.arraycopy(buffer, 0, frame, 0, packet.getLength());
		return frame;
	}
	public void	close()
	{
		datagram.close();
	}

}
