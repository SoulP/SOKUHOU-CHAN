package data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

public final class Converter {
	// オブジェクトからバイト列に変換
	public static byte[] object2bytes(Object obj){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			byte[] bytes = bos.toByteArray();
			out.close();
	        bos.close();
	        return bytes;
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	// バイト列からオブジェクトに変換
	public static Object bytes2object(byte[] bytes){
		try{
			return new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	// 文字列からバイト列に変換
	public static byte[] string2bytes(String str){
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			e.printStackTrace();
			return str.getBytes();
		}
	}

	// バイト列から文字列に変換
	public static String bytes2string(byte[] bytes){
		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			e.printStackTrace();
			return new String(bytes);
		}
	}


}
