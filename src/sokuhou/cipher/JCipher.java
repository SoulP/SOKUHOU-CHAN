package sokuhou.cipher;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class JCipher extends Thread{
	public enum cipher {RSA, AES};
	Cipher rsa;
	Cipher aes;
	private KeyPairGenerator keygenRSA;
	private KeyGenerator keygenAES;
	private SecretKey secretKEY;
	private PrivateKey privateKEY;
	private PublicKey publicKEY;
	private KeyFactory keyfactory;

	public JCipher(){
		 try {
			rsa = Cipher.getInstance("RSA");
			aes = Cipher.getInstance("AES");
			keygenRSA = KeyPairGenerator.getInstance("RSA");
			keygenAES = KeyGenerator.getInstance("AES");
			keyfactory = KeyFactory.getInstance("RSA");;
			keygenRSA.initialize(2048);
			keygenAES.init(256);
			privateKEY = null;
			publicKEY = null;
			secretKEY = null;
			generateRSA_KEY();
			generateAES_KEY();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void setRSA_KEY_SIZE(int size){
		keygenRSA.initialize(size);
	}

	public void setAES_KEY_SIZE(int size){
		keygenAES.init(size);
	}

	public void generateRSA_KEY(){
		KeyPair rsaKey = keygenRSA.generateKeyPair();
		privateKEY = rsaKey.getPrivate();
		publicKEY = rsaKey.getPublic();
	}

	public PrivateKey getPrivateKey(){
		return privateKEY;
	}

	public PublicKey getPublicKey(){
		return publicKEY;
	}

	public void generateAES_KEY(){
		secretKEY = keygenAES.generateKey();
	}

	public SecretKey getSecretKey(){
		return secretKEY;
	}

	public void setPrivateKey(PrivateKey privateKEY){
		this.privateKEY = privateKEY;
	}

	public void setPublicKey(PublicKey publicKEY){
		this.publicKEY = publicKEY;
	}

	public void setSecretKey(SecretKey secretKEY){
		this.secretKEY = secretKEY;
	}

	public byte[] privateKey2bytes(PrivateKey privateKEY){
		return privateKEY.getEncoded();
	}

	public byte[] publicKey2bytes(PublicKey publicKEY){
		return publicKEY.getEncoded();
	}

	public byte[] secretKey2bytes(SecretKey secretKEY){
		return secretKEY.getEncoded();
	}

	public PrivateKey bytes2privateKey(byte[] privateKeyBytes){
		try {
			return keyfactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public PublicKey bytes2publicKey(byte[] publicKeyBytes){
		try {
			return keyfactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public SecretKey bytes2secretKey(byte[] secretKeyBytes){
		return new SecretKeySpec(secretKeyBytes, "AES");
	}

}
