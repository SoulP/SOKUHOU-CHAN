package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKey;

public abstract class JSocket extends Thread{
	final int port;// 接続先のポート番号
	final String host;// 接続先のホスト名/ドメイン名/IPアドレス
	enum ctrl { NULL, READ, WRITE, DELETE };// 接続情報の操作
	enum type { NULL, USER, DATA, OPTION };// 接続情報の種類
	private Socket socket;// 通信ソケット
	private DataOutputStream dos;// 送信用ストリーム
	private DataInputStream dis;// 受信用ストリーム
	private byte[] iData;// 接続情報のバイト列
	private byte[] bData;// データ情報のバイト列
	private byte[] allData;// 送信用のバイト列 (接続情報 + データ情報 + 終了コード 0x00, 0xFF, 0x00, 0xFF)
	protected byte[] bufferData;// 受信用のバイト列
	private byte[] rData;// 受信したデータ情報のバイト列
	protected String sData;// データ情報の文字列
	private SecretKey key;// 共通鍵
	private String user_name, password, email, birth_day, otp;// ユーザ情報の文字列
	private int cKEY, cNO;// 接続情報の接続番号用の鍵と接続番号
	private List<String> info;// 接続情報のリスト

	// コンストラクタ
	public JSocket(){
		// 初期化
		port = 55324;// 接続先のポート番号
		host = "sokuhou.soulp.moe";// 接続先のホスト名/ドメイン名/IPアドレス
		iData = new byte[ 3 ];// 接続情報のバイト列
		bData = null;// データ情報のバイト列
		rData = null;// 受信したデータのバイト列
		allData = null;// 送信用のバイト列
		bufferData = new byte[Byte.MAX_VALUE - 1];// 受信用のバイト列
		clearBytes(iData);// バイト列のバイト値を全て0x00にする
		clearBytes(bufferData);// バイト列のバイト値を全て0x00にする
		sData = null;// データ情報の文字列
		socket = null;// 通信ソケット
		dos = null;// 送信用ストリーム
		dis = null;// 受信用ストリーム
		user_name = null;// ユーザ名の文字列
		password = null;// パスワードの文字列
		email = null;// メールアドレスの文字列
		birth_day = null;// 誕生日の文字列
		otp = null;// ワンタイムパスワードの文字列
		cKEY = 0;// 接続情報の接続番号用の鍵
		cNO = 0;// 接続情報の接続番号
		info = null;// 接続情報のリスト
	}

// 通信

	// ソケット 生成
	protected void createSocket(){
		try {
			socket = new Socket(host, port);// 接続先へのソケットを作成
		} catch (Exception e){
			// エラーが起きた際の処理
			System.out.println(e);// エラー内容表示
			e.printStackTrace();// 原因の追跡を表示
			try {
				socket.close();// ソケットを閉じる
			} catch (IOException e1) {
				// エラーが起きた際の処理
				System.out.println(e1);// エラー内容表示
				socket = null;// ソケットをnull値で消す
			}
		}
	}
	// ソケット 出力
	public Socket getSocket(){
		return socket;
	}

	// 送信用ストリーム 入力
	protected void setDOS(OutputStream os){
		dos = new DataOutputStream(os);
	}

	// 送信用ストリーム 出力
	protected DataOutputStream getDOS(){
		return dos;
	}

	// 受信用ストリーム 入力
	protected void setDIS(InputStream is){
		dis = new DataInputStream(is);
	}

	// 送信用ストリーム 出力
	protected DataInputStream getDIS(){
		return dis;
	}

	// 送信 バイト列
	protected void send(byte[] bytes) throws IOException{
		dos.write(bytes);
	}

	// 送信 byte型
	protected void sendByte(byte b) throws IOException{
		dos.writeByte((int)(b & 0xFF));
	}

	// 送信 short型
	protected void sendShort(short s) throws IOException{
		dos.writeShort(s);
	}

	// 送信 int型
	protected void sendInt(int i) throws IOException{
		dos.writeInt(i);
	}

	// 送信 long型
	protected void sendLong(long l) throws IOException{
		dos.writeLong(l);
	}

	// 送信 float型
	protected void sendFloat(float f) throws IOException{
		dos.writeFloat(f);
	}

	// 送信 double型
	protected void sendDouble(double d) throws IOException{
		dos.writeDouble(d);
	}

	// 送信 boolean型
	protected void sendBoolean (boolean bool) throws IOException{
		dos.writeBoolean(bool);
	}

	// 送信 char型
	protected void sendChar(char c) throws IOException{
		dos.writeChar(c);
	}

	// 送信 文字列(UTF-8)
	protected void sendString(String str) throws IOException{
		dos.writeUTF(str);
	}

	// 受信 バイト列
	protected int recv(byte[] bytes) throws IOException{
		return dis.read(bytes);
	}

	// 受信 バイト列
	protected byte[] recv(String connectionNO) throws Exception{
		int buffLength = recv(bufferData);
		rData = new byte[buffLength];// 受信したバイト列をbufferDataに保存し、受信したバイト列の配列数を使ってrDataに値なしのバイト列を作成する
		clearBytes(rData);// バイト列を初期化する
		for(int i = 0; i < rData.length; i++)rData[i] = bufferData[i];// 全ての情報とデータをコピーする
		clearBytes(bufferData);// バイト列を初期化する
		info = getInfo(rData);// 接続情報用の文字列のリストを作成し、接続情報のバイト列から各情報をリストに追加する
		// 不正な接続番号の場合は、エラーとして発生させる
		if(!info.get(0).equals(connectionNO)) throw new Exception("ERROR: conncetion_no or nextConnection value can't use it. need reconnect");
		// データ情報の最後の部分に終了コードが無い、もしくは違う値である場合は、エラーとして発生させる
		if(rData[rData.length-1] != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");
		if(rData[rData.length-2] != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");
		if(rData[rData.length-3] != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");
		if(rData[rData.length-4] != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");

		// バイト列からデータを取る
		byte[] buffData = new byte[rData.length-7];// データ用バイト列を作成する
		for(int i = 0; i < buffData.length; i++) buffData[i] = rData[i + 3];// 接続情報と終了コードを含めずに、データだけコピーする
		return buffData;
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

	// ソケット 入力
	public void setSocket(Socket socket){
		this.socket = socket;
	}

	// バイト列初期化
	public void clearBytes(byte[] bytes){
		for(int i = 0; i < bytes.length; i++) bytes[i] = 0;// 全ての配列に0で埋める
	}

	// バイト列構築 接続情報 + データ情報 + 終了コード(0x00, 0xFF, 0x00, 0xFF)
	public void buildBytes(){
		final int length = iData.length + bData.length;
		byte[] z = new byte[length];
		clearBytes(z);
		for(int i = 0; i < iData.length; i++) z[i] = iData[i];
		for(int i = iData.length; i < length; i++) z[i] = bData[i];
		byte[] buff = new byte[z.length + 4];
		clearBytes(buff);
		for(int i = 0; i < z.length; i++) buff[i] = z[i];
		for(int i = z.length; i < buff.length; i++){
			buff[i] = (byte)0x00;
			buff[++i] = (byte)0xFF;
		}
		allData = buff;
	}

	// バイト列構築 接続情報 + データ情報 + 終了コード(0x00, 0xFF, 0x00, 0xFF)
	public void buildBytes(byte[] bytes){// bytes = データ情報
		final int length = iData.length + bytes.length;
		byte[] z = new byte[length];
		clearBytes(z);
		for(int i = 0; i < iData.length; i++) z[i] = iData[i];
		for(int i = iData.length; i < length; i++) z[i] = bytes[i];
		byte[] buff = new byte[z.length + 4];
		clearBytes(buff);
		for(int i = 0; i < z.length; i++) buff[i] = z[i];
		for(int i = z.length; i < buff.length; i++){
			buff[i] = (byte)0x00;
			buff[++i] = (byte)0xFF;
		}
		allData = buff;
		bData = bytes;
	}

	// データ情報のバイト列 入力
	public void setDataBytes(byte[] bData){
		this.bData = bData;
	}

	// データ情報のバイト列 出力
	public byte[] getDataBytes(){
		return bData;
	}

	// 接続情報のバイト列生成 (接続番号 + 接続順 + 操作 + 種類)
	public void createInfoBytes(String connection_no, ctrl c, type t){
		byte[] buff = new byte[iData.length];
		clearBytes(buff);
		try{
			if(connection_no.length() != 4)throw new Exception("ERROR: isn't 4 digits of connection_no");
			int tempInt;
			tempInt = Integer.parseInt(connection_no);
			if(tempInt < 0) throw new Exception("ERROR: connection_no can't use minus(-)");

			buff[0] = (byte)(tempInt & 0xFF);
			buff[1] = (byte)(tempInt >>> 8);

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

			buff[2] = (byte)tempInt;

			iData = buff;
		}catch (Exception e){
			System.out.println(e);
		}
	}

	// 接続情報のリスト 出力
	public List<String> getInfo(byte[] bytes){
		// 接続情報のバイト列からList<String>に変換
		if(bytes.length < 4) return null;
		List<String> str = new ArrayList<String>();
		byte buff;
		buff = bytes[1];
		int xNum = (buff & 0xFF);
		xNum = xNum << 8;
		xNum += (bytes[0] & 0xFF);
		str.add("" + xNum);

		xNum = (bytes[2] >>> 4);
		switch (xNum){
			case 0x01: str.add("" + ctrl.DELETE); break;
			case 0x02: str.add("" + ctrl.WRITE); break;
			case 0x04: str.add("" + ctrl.READ); break;
			default: str.add("" + ctrl.NULL); break;
		}

		xNum = (bytes[2] & 0x0F);
		switch(xNum){
			case 0x01: str.add("" + type.USER); break;
			case 0x02: str.add("" + type.DATA); break;
			case 0x04: str.add("" + type.OPTION); break;
			default: str.add("" + type.NULL); break;
		}

		return str;
	}

	// 乱数生成 一番上の数字が0が出ない出力
	public int randomNO(int digits){
		String buffer = "";// 初期化
		for(int i = 0; i < digits; i++)buffer += (int)(Math.random() * 10.0);// 桁数で各桁に乱数(0～9)を入れる
		int x = Integer.parseInt(buffer);// 文字列をint型に変換
		buffer = "" + x;// int型を文字列に変換
		if(buffer.length() != digits) x = randomNO(digits);// 同じ桁数ではない場合は、乱数を生成する
		x = x == 0 ? randomNO(digits) : x == 9999 ? randomNO(digits) : x;// 数字が0もしくは9999の場合は、乱数を生成する
		x = Integer.parseInt(buffer);
		return x;
	}

	// 構築したバイト列 出力
	public byte[] getAllBytes(){
		return allData;
	}

	// 文字列から配列 変換
	public byte[] str2bytes(String str) throws UnsupportedEncodingException{
		return str.getBytes("UTF-8");// 文字列をバイト列(UTF-8)に変換
	}

	// バイト列から文字列 変換
	public String bytes2str(byte[] bytes) throws UnsupportedEncodingException{
		return new String(bytes, "UTF-8");// バイト列(UTF-8)を文字列に変換
	}


	// 共通鍵 入力
	protected void setSecretKey(SecretKey key){
		this.key = key;
	}

	// 共通鍵 出力
	protected SecretKey getSecretKey(){
		return key;
	}

	// 接続 開く
	protected void open() throws IOException{
		createSocket();// ソケット生成
		setDOS(socket.getOutputStream());// 送信用ストリームを設定
		setDIS(socket.getInputStream());// 受信用ストリームを設定
	}

	// 接続 閉じる
	protected void close() throws IOException{
		dis.close();// 受信用ストリームを閉じる
		dos.close();// 送信用ストリームを閉じる
		socket.close();// ソケットを閉じる
	}

// 接続情報

	// 次の接続番号 出力
	protected int nextConnect(){
		return nextConnect(cKEY, cNO);
	}

	// 次の接続番号 出力
	protected int nextConnect(int cKEY, int cNO){
		// (cNO ^ cKEY) % 10000
		// (cNO pow cKEY) mod 10000
		BigInteger bigINT = BigInteger.valueOf(cNO);
		bigINT = bigINT.pow(cKEY);
		bigINT = bigINT.mod(BigInteger.valueOf(10000));
		return bigINT.intValue();
	}

	// 次の接続番号 設定
	protected void setNextConnection(String connectKEY){
		setConnectionNO(Integer.parseInt(connectKEY.substring(0,4)));// 最初の4桁を接続番号としてcNOに保存する
		setConnectionKEY(Integer.parseInt(connectKEY.substring(5, 8)));// 次の4桁を接続番号用の乱数としてcKEYに保存する
	}

	// 接続番号 入力
	protected void setConnectionNO(int cNO){
		this.cNO = cNO;
	}

	// 接続番号 出力
	protected int getConnectionNO(){
		return cNO;
	}

	// 接続番号用の鍵 入力
	protected void setConnectionKEY(int cKEY){
		this.cKEY = cKEY;
	}

	// 接続番号用の鍵 出力
	protected int getConnectionKEY(){
		return cKEY;
	}

	// 接続情報のリスト 入力
	protected void setInfoList(List<String> info){
		this.info = info;
	}

	// 接続情報のリスト 出力
	protected List<String> getInfoList(){
		return info;
	}

// ユーザ情報

	// ユーザ名 入力
	public void setUserName(String user_name){
		this.user_name = user_name;
	}

	// ユーザ名 出力
	public String getUserName(){
		return user_name;
	}

	// メールアドレス 入力
	public void setEmail(String email){
		this.email = email;
	}

	// メールアドレス 出力
	protected String getEmail(){
		return email;
	}

	// パスワード 入力
	public void setPassword(String password){
		this.password = password;
	}

	// パスワード 出力
	protected String getPassword(){
		return password;
	}

	// 誕生日 入力
	public void setBirthDay(String birth_day){
		this.birth_day = birth_day;
	}

	// 誕生日 出力
	public String getBirthDay(){
		return birth_day;
	}

	// ワンタイムパスワード 入力
	public void setOTP(String otp){
		this.otp = otp;
	}

	// ワンタイムパスワード 出力
	protected String getOTP(){
		return otp;
	}

	// ユーザ情報クリア
	protected void clearUser(){
		// 初期化
		setUserName(null);// null値で初期化
		setPassword(null);// null値で初期化
		setEmail(null);// null値で初期化
		setBirthDay(null);// null値で初期化
		setOTP(null);// null値で初期化
	}
}
