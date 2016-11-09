package sokuhou.cipher;

import java.security.Key;

import javax.crypto.Cipher;

public class JDecrypt extends JCipher{
	private cipher type;
	private Key key;
	private byte[] bytes;

	public JDecrypt(){
		super();
		type = null;
		key = null;
		bytes = null;
	}

	public JDecrypt(cipher type){
		super();
		this.type = type;
		key = null;
		bytes = null;
	}

	public JDecrypt(cipher type, Key key){
		super();
		this.type = type;
		this.key = key;
		bytes = null;
	}

	public JDecrypt(cipher type, Key key, byte[] bytes){
		super();
		this.type = type;
		this.key = key;
		this.bytes = bytes;
	}

	public void setType(cipher type){
		this.type = type;
	}

	public cipher getType(){
		return type;
	}

	public void setKey(Key key){
		this.key = key;
	}

	public Key getKey(){
		return key;
	}

	public void setBytes(byte[] bytes){
		this.bytes = bytes;
	}

	public byte[] getBytes(){
		return bytes;
	}

	private void RSA(Key key, byte[] bytes){
		try {
			rsa.init(Cipher.DECRYPT_MODE, key);
			this.bytes = rsa.doFinal(bytes);
		} catch (Exception e) {
			System.out.println(e);
			this.bytes = null;
		}
	}

	private void AES(Key key, byte[] bytes){
		try {
			aes.init(Cipher.DECRYPT_MODE, key);
			this.bytes = aes.doFinal(bytes);
		} catch (Exception e) {
			System.out.println(e);
			this.bytes = null;
		}
	}

	public void run(){
		if(type != null)if(type == cipher.RSA) RSA(key, bytes); else if(type == cipher.AES) AES(key, bytes);
	}
}
