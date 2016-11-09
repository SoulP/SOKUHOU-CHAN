package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

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
	byte[] bufferData;
	String sData;
	SecretKey key;

	public JSocket(){
		port = 55324;
		host = "sokuhou.soulp.moe";
		iData = new byte[ 4 ];
		bData = new byte[ 1024 - iData.length ];
		allData = new byte[ iData.length + bData.length ];
		bufferData = new byte[Byte.MAX_VALUE - 1];
		clearBytes(iData);
		clearBytes(bData);
		clearBytes(allData);
		clearBytes(bufferData);
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

	public void buildBytes(byte[] bytes){
		final int length = iData.length + bytes.length;
		byte[] z = new byte[ length ];
		clearBytes(z);
		for(int i = 0; i < iData.length; i++) z[i] = iData[i];
		for(int i = iData.length; i < length; i++) z[i] = bData[i];
		byte[] buff = new byte[z.length + 4];
		clearBytes(buff);
		for(int i = 0; i < z.length; i++) buff[i] = z[i];
		for(int i = z.length; i < z.length + 4; i++){
			buff[i] = (byte)0x00;
			buff[++i] = (byte)0xFF;
		}
		allData = z;
	}

	public void createDataBytes(String str){
		try {
			byte[] buff = str2bytes(str);
			bData = buff;
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void setDataBytes(byte[] bData){
		this.bData = bData;
	}

	public byte[] getDataBytes(){
		return bData;
	}

	public void createInfoBytes(String connection_no, String nextConnection, ctrl c, type t){
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
				case DELETE: tempInt = 0x01; break;
				case WRITE: tempInt = 0x02; break;
				case READ: tempInt = 0x04; break;
				default: tempInt = 0x00; break;
			}

			tempInt = tempInt << 4;

			switch(t){
				case USER: tempInt += 0x01; break;
				case DATA: tempInt += 0x02; break;
				case OPTION: tempInt += 0x04; break;
				default: tempInt += 0x00; break;
			}

			buff[3] = (byte)tempInt;

			iData = buff;
		}catch (Exception e){
			System.out.println(e);
		}
	}

	public List<String> getInfo(byte[] bytes){
		if(bytes.length < 4) return null;
		List<String> str = new ArrayList<String>();
		byte buff;
		buff = bytes[1];
		int xNum = (buff & 0xFF);
		xNum = xNum << 8;
		xNum += (bytes[0] & 0xFF);
		str.add("" + xNum);

		xNum = (bytes[2] & 0xFF);
		str.add("" + xNum);

		xNum = (bytes[3] >>> 4);
		switch (xNum){
			case 0x01: str.add("" + ctrl.DELETE); break;
			case 0x02: str.add("" + ctrl.WRITE); break;
			case 0x04: str.add("" + ctrl.READ); break;
			default: str.add("" + ctrl.NULL); break;
		}

		xNum = (bytes[3] & 0x0F);
		switch(xNum){
			case 0x01: str.add("" + type.USER); break;
			case 0x02: str.add("" + type.DATA); break;
			case 0x04: str.add("" + type.OPTION); break;
			default: str.add("" + type.NULL); break;
		}

		return str;
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

	public byte[] str2bytes(String str){
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}

	public String bytes2str(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return null;
		}
	}

	public int nextConnect(int cKEY, int cNO){
		String sKEY = "" + cKEY;
		String sNO = "" + cNO;
		String buff = "";
		for(int i = 0; i < sKEY.length(); i++){
			int iStr = Integer.parseInt(sKEY.substring(i, i + 1));
			int iXtr = Integer.parseInt(sNO.substring(i, i + 1));
			buff += ((iStr == iXtr)? (iStr + 1) - iXtr : ((iStr > iXtr)? iStr - iXtr : iXtr - iStr));
		}
		return Integer.parseInt(buff);
	}


}
