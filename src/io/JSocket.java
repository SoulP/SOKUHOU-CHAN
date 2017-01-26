package io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKey;

import data.Data;
import data.Option;
import data.Packet;
import data.User;

public abstract class JSocket implements OID{
	// インスタンス変数
	public					enum				ctrl { NULL, READ, WRITE, DELETE };	// 接続情報の操作
	public					enum				type { NULL, USER, DATA, OPTION };	// 接続情報の種類
	public		transient	Socket				socket;								// 通信ソケット
	public		transient	Packet				packet;								// パケット
	public					User				user;								// ユーザー
	public					Data				data;								// データー
	public					Option				option;								// オプション
	private		transient	DataOutputStream	dos;								// 送信用ストリーム
	private		transient	DataInputStream		dis;								// 受信用ストリーム
	private		transient	ObjectOutputStream	oos;								// 送信用ストリーム
	private		transient	ObjectInputStream	ois;								// 受信用ストリーム
	protected	transient	SecretKey			secretKEY;							// 共通鍵
	protected	transient	PrivateKey			privateKEY;							// 秘密鍵
	public					PublicKey			publicKEY;							// 公開鍵
	protected	transient	boolean				check;								// 確認
	protected 				int					id;									// オブジェクトID
	public 					int					year,								// 年
												month,								// 月
												date,								// 日
												day,								// 曜日
												hour,								// 時
												minute,								// 分
												second;								// 秒

	// コンストラクタ
	public JSocket() throws IOException{
		// 初期化
		socket	= createSocket("sokuhou.soulp.moe", 55324);							// 通信ソケット
		open();																		// 接続を開く
		check	= false;															// 結果
		id		= -1;																// オブジェクトID
	}

	// コンストラクタ
	public JSocket(String host, int port) throws IOException{
		socket	= createSocket(host, port);											// 通信ソケット
		open();																		// 接続を開く
		check	= false;															// 結果
		id		= -1;																// オブジェクトID
	}

	// コンストラクタ
	public JSocket(Socket socket) throws IOException{
		this.socket = socket;														// 通信ソケット
		open();																		// 接続を開く
		check		= false;														// 結果
		id			= -1;															// オブジェクトID
	}


	// ソケット 生成
	public Socket createSocket(String host, int port){
		try {
			return new Socket(host, port);										// 接続先へのソケットを作成
		} catch (Exception e){
			// エラー表示
			System.out.println(e);
			e.printStackTrace();
			try {
				socket.close();													// ソケットを閉じる
			} catch (IOException e1) {
				// エラー表示
				System.out.println(e1);
				e1.printStackTrace();
				socket = null;													// null値で初期化
			}
			return null;
		}
	}

	// 送信 バイト列
	protected void send(byte[] bytes) throws IOException{
		dos.write(bytes);
	}

	// 送信 byte型
	protected void send(byte b) throws IOException{
		dos.writeByte((int)(b & 0xFF));
	}

	// 送信 short型
	protected void send(short s) throws IOException{
		dos.writeShort(s);
	}

	// 送信 int型
	protected void send(int i) throws IOException{
		dos.writeInt(i);
	}

	// 送信 long型
	protected void send(long l) throws IOException{
		dos.writeLong(l);
	}

	// 送信 float型
	protected void send(float f) throws IOException{
		dos.writeFloat(f);
	}

	// 送信 double型
	protected void send(double d) throws IOException{
		dos.writeDouble(d);
	}

	// 送信 boolean型
	protected void send (boolean bool) throws IOException{
		dos.writeBoolean(bool);
	}

	// 送信 char型
	protected void send(char c) throws IOException{
		dos.writeChar(c);
	}

	// 送信 文字列(UTF-8)
	protected void send(String str) throws IOException{
		dos.writeUTF(str);
	}

	// 送信 オブジェクト
	protected void send(Object obj) throws IOException{
		oos.writeObject(obj);
	}

	// 受信 バイト列
	protected int recv(byte[] bytes) throws IOException{
		return dis.read(bytes);
	}

	// 受信 byte型
	protected byte recvByte() throws IOException{
		return dis.readByte();
	}

	// 受信 short型
	protected short recvShort() throws IOException{
		return dis.readShort();
	}

	// 受信 int型
	protected int recvInt() throws IOException{
		return dis.readInt();
	}

	// 受信 long型
	protected long recvLong() throws IOException{
		return dis.readLong();
	}

	// 受信 float型
	protected float recvFloat() throws IOException{
		return dis.readFloat();
	}

	// 受信 double型
	protected double recvDouble() throws IOException{
		return dis.readDouble();
	}

	// 受信 boolean型
	protected boolean recvBoolean() throws IOException{
		return dis.readBoolean();
	}

	// 受信 char型
	protected char recvChar() throws IOException{
		return dis.readChar();
	}

	// 受信 文字列(UTF-8)
	protected String recvString() throws IOException{
		return dis.readUTF();
	}

	// 送信 オブジェクト
	protected Object recvObject() throws IOException, ClassNotFoundException{
		return ois.readObject();
	}

	// 送信元のIPアドレス 出力
	protected String getSenderIPaddress(){
		return socket.getInetAddress().getHostAddress();
	}

	// 送信元のホスト名 出力
	protected String getSenderHostName(){
		return socket.getInetAddress().getHostName();
	}

	// 送信元のポート番号 出力
	protected int getSenderPort(){
		return socket.getPort();
	}

	// 接続を開く
	protected void open() throws IOException{
		dos = new DataOutputStream(socket.getOutputStream());	// 送信用ストリーム
		dis = new DataInputStream(socket.getInputStream());		// 受信用ストリーム
		oos = new ObjectOutputStream(socket.getOutputStream());	// 送信用ストリーム
		ois = new ObjectInputStream(socket.getInputStream());	// 受信用ストリーム
	}

	// 接続を閉じる
	protected void close() throws IOException{
		dis.close();											// 受信用ストリームを閉じる
		dos.close();											// 送信用ストリームを閉じる
		ois.close();											// 受信用ストリームを閉じる
		oos.close();											// 送信用ストリームを閉じる
		if(!socket.isClosed()) socket.close();					// ソケットを閉じる
	}

	// 結果
	public boolean check(){
		return check;
	}

	// オブジェクトID 出力
	public int getID() throws Exception{
		if(id == -1) throw new Exception("不明なIDです");
		return id;
	}

	// オブジェクトID 入力
	public void setID(int id) throws Exception{
		if(id < 0) throw new Exception("不正な値です");
		this.id = id;
	}

	// 名前確認
	public boolean checkName(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("^(\\p{Alnum}|\\p{InHiragana}|\\p{InKatakana}|\\p{InCJKUnifiedIdeographs}|[\\u0020\\u3000\\uFF10-\\uFF19\\uFF21-\\uFF3A\\uFF41-\\uFF5A])+$");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	// メールアドレス確認
	public boolean checkEmail(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("^\\p{Alnum}+@\\p{Alnum}*\\.?\\p{Alnum}+\\.\\p{Alnum}+$");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	// パスワード確認
	public boolean checkPassword(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("\\s|[^ -~｡-ﾟ]");
		Matcher matcher = pattern.matcher(str);
		return !matcher.find();
	}

	// 誕生日確認
	public boolean checkBirthday(String str){
		if(str == null) return false;
		Pattern pattern = Pattern.compile("^\\d{4}-(1[0-2]|0[1-9])-(3[01]|2\\d|1\\d|0[1-9])+$");
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			JCalendar.getTime();
			int yearMAX = JCalendar.getYEAR();
			int yearMIN = yearMAX - 128;
			int month = JCalendar.getMONTH();
			int date = JCalendar.getDATE();
			int birthYEAR = Integer.parseInt(str.substring(0, 4));
			int birthMONTH = Integer.parseInt(str.substring(5, 7));
			int birthDATE = Integer.parseInt(str.substring(8, 10));
			if(birthYEAR >= yearMIN && birthYEAR <= yearMAX){
				if(birthYEAR != yearMAX) return true;
				if(birthMONTH <= month){
					if(birthDATE <= date) return true;
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}
}
