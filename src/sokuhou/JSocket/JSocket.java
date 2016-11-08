package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public abstract class JSocket extends Thread{
	final int port;
	final String host;
	enum ctrl { NULL, READ, WRITE, DELETE };
	enum type { NULL, USER, DATA, OPTION };
	enum data { CONNECTION_NO };
	private Socket socket;
	public DataOutputStream dos;
	public DataInputStream dis;
	private byte[] iData;
	private byte[] bData;
	private byte[] allData;
	private byte[] bufferData;
	String sData;

	public JSocket(){
		port = 55324;
		host = "sokuhou.soulp.moe";
		iData = new byte[ 4 ];
		bData = new byte[ 1024 - iData.length ];
		allData = new byte[ iData.length + bData.length ];
		clearBytes(iData);
		clearBytes(bData);
		clearBytes(allData);
		sData = null;
		socket = null;
		dos = null;
		dis = null;
	}

	public void createSocket(){
		try {
			socket = new Socket(host, port);

			socket.close();
		} catch (Exception e){
			System.out.println(e);
			try {
				socket.close();
			} catch (IOException e1) {
				System.out.println(e1);
				socket = null;
			}
		}
	}

	public void createDOS(OutputStream os){
		dos = new DataOutputStream(os);
	}

	public Socket getSocket(){
		return socket;
	}

	public void setSocket(Socket socket){
		this.socket = socket;
	}

	public void clearBytes(byte[] bytes){
		for(int i = 0; i < bytes.length; i++) bytes[i] = 0;
	}

	public void buildBytes(){
		final int length = iData.length + bData.length;
		byte[] z = new byte[ length ];
		clearBytes(z);
		for(int i = 0; i < iData.length; i++) z[i] = iData[i];
		for(int i = iData.length; i < length; i++) z[i] = bData[i];
		allData = z;
	}

	public byte[] createDataBytes(String str){
		try {
			byte[] buffStr = str2byteArray(str);
			byte[] buff = new byte[buffStr.length + 4];
			clearBytes(buff);
			for(int i = 0; i < buffStr.length; i++) buff[i] = buffStr[i];
			for(int i = buffStr.length; i < buffStr.length + 4; i++){
				buff[i] = (byte)0x00;
				buff[++i] = (byte)0xFF;
			}
			return buff;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public byte[] createInfoBytes(String connection_no, String nextConnection, ctrl c, type t){
		byte[] buff = new byte[iData.length];
		clearBytes(buff);
		try{
			if(connection_no.length() != 4)throw new Exception("ERROR: isn't 4 digits of connection_no");
			int tempInt;
			tempInt = Integer.parseInt(connection_no);
			if(tempInt < 0) throw new Exception("ERROR: connection_no can't use minus(-)");

			buff[0] = (byte)(tempInt & 0xFF);
			buff[1] = (byte)(tempInt >>> 8);

			tempInt = Integer.parseInt(nextConnection);
			if(tempInt > 255) throw new Exception("ERROR: nextConnection maximum is 255. value: " + tempInt);
			if(tempInt < 0) throw new Exception("ERROR: nextConnection minimum is 0. value: " + tempInt);

			buff[2] = (byte)tempInt;

			switch(c){
				case DELETE: tempInt = 0x01;
				case WRITE: tempInt = 0x02;
				case READ: tempInt = 0x04;
				default: tempInt = 0x00;
			}

			tempInt = tempInt << 4;

			switch(t){
				case USER: tempInt += 0x01;
				case DATA: tempInt += 0x02;
				case OPTION: tempInt += 0x04;
				default: tempInt += 0x00;
			}

			buff[3] = (byte)tempInt;

			return buff;
		}catch (Exception e){
			System.out.println(e);
			return null;
		}
	}

	public String randomNO(int digits){
		String buffer = "";
		for(int i = 0; i < digits; i++)buffer += (int)(Math.random() * 10.0);
		int x = Integer.parseInt(buffer);
		buffer = "" + x;
		if(buffer.length() != digits) buffer = randomNO(digits);
		return buffer;
	}

	public byte[] getAllBytes(){
		return allData;
	}

	public void setBufferBytes(byte[] bufferData){
		this.bufferData = bufferData;
	}

	public byte[] getBufferBytes(){
		return bufferData;
	}

	public byte[] str2byteArray(String str){
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}

	public String byteArray2str(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}


}
