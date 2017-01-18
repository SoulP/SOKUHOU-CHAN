package data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

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
}
