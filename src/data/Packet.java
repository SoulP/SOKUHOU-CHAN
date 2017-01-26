package data;

import java.io.IOException;
import java.io.Serializable;

import io.JCalendar;
import io.JSocket;
import io.OID;

public class Packet extends JSocket implements Serializable{
	// 送受信用
	public byte[]		bytesKEY;			// 共通鍵 (公開鍵で暗号化必須)
	public ctrl			controll;			// 操作
	public type			dataTYPE;			// 操作するデータの種類

	// コンストラクタ
	public Packet() throws IOException{
		// 初期化
		JCalendar.getTime();
		year		= JCalendar.getYEAR();
		month		= JCalendar.getMONTH();
		date		= JCalendar.getDATE();
		day			= JCalendar.getDAY();
		hour		= JCalendar.getHOUR();
		minute		= JCalendar.getMINUTE();
		second		= JCalendar.getSECOND();
		secretKEY	= null;
		privateKEY	= null;
		publicKEY	= null;
		user		= null;
		data		= null;
		option		= null;
		bytesKEY	= null;
		controll	= ctrl.NULL;
		dataTYPE	= type.NULL;
		id			= 0;
	}

	// コンストラクタ
		public Packet(OID obj) throws IOException{
			// 初期化
			JCalendar.getTime();
			year		= JCalendar.getYEAR();
			month		= JCalendar.getMONTH();
			date		= JCalendar.getDATE();
			day			= JCalendar.getDAY();
			hour		= JCalendar.getHOUR();
			minute		= JCalendar.getMINUTE();
			second		= JCalendar.getSECOND();
			secretKEY	= null;
			privateKEY	= null;
			publicKEY	= null;
			user		= null;
			data		= null;
			option		= null;
			bytesKEY	= null;
			controll	= ctrl.NULL;
			dataTYPE	= type.NULL;
			try {
				id		= obj.getID();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				id		= 0;
			}
		}
}
