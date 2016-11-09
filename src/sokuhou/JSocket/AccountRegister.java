package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sokuhou.cipher.JCipher.cipher;
import sokuhou.cipher.JDecrypt;
import sokuhou.cipher.JEncrypt;

public class AccountRegister extends JSocket{
	private String user_name, password, email, birth_day;
	private int nextConnect;
	private boolean check;

	public AccountRegister(){
		super();
		user_name = null;
		password = null;
		email = null;
		birth_day = null;
		check = false;
	}

	public void setUserName(String user_name){
		this.user_name = user_name;
	}

	private String getUserName(){
		return user_name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	private String getEmail(){
		return email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	private String getPassword(){
		return password;
	}

	public void setBirthDay(String birth_day){
		this.birth_day = birth_day;
	}

	public String getBirthDay(){
		return birth_day;
	}

	public boolean check(){
		return check;
	}

	public void run(){
		try{
			if(getUserName() == null) throw new Exception("ERROR: user_name value is null");
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");
			if(getBirthDay() == null) throw new Exception ("ERROR: birth_day value is null");

			byte[] rData = null;
			nextConnect = 0;
			createSocket();
			dos = new DataOutputStream(getSocket().getOutputStream());
			dis = new DataInputStream(getSocket().getInputStream());

			JEncrypt enc = new JEncrypt();
			enc.generateRSA_KEY();
			JDecrypt dec = new JDecrypt(cipher.RSA, enc.getPrivateKey());
			byte[] publicKEY = enc.publicKey2bytes(enc.getPublicKey());

			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);
			createDataBytes("$REGISTER:USER;");
			buildBytes();
			dos.write(getAllBytes());

			if(!dis.readBoolean()) return;

			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);
			buildBytes(publicKEY);

			dos.write(getAllBytes());

			rData = new byte[dis.read(bufferData)];
			clearBytes(rData);
			List<String> info = getInfo(rData);
			if(!info.get(0).equals("0000") || !info.get(1).equals("" + nextConnect++)) throw new Exception("ERROR: conncetion_no or nextConnection value can't use it. need reconnect");
			for(int i = 0; i < rData.length; i++) rData[i] = bufferData[i];
			clearBytes(bufferData);
			if((rData[rData.length-4] | rData[rData.length-2]) != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");
			if((rData[rData.length-3] | rData[rData.length-1]) != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");

			byte[] buffData = new byte[rData.length-8];
			for(int i = 0; i < buffData.length; i++) buffData[i] = rData[i + 4];

			dec.setBytes(buffData);
			dec.start();
			createInfoBytes("0000", "" + nextConnect++, ctrl.WRITE, type.USER);
			createDataBytes("$BOOL:OK;");
			buildBytes();
			dos.write(getAllBytes());

			rData = new byte[dis.read(bufferData)];
			clearBytes(rData);
			for(int i = 0; i < rData.length; i++) rData[i] = bufferData[i];
			info = getInfo(rData);
			if(!info.get(0).equals("0000") || !info.get(1).equals("" + nextConnect++)) throw new Exception("ERROR: conncetion_no or nextConnection value can't use it. need reconnect");
			for(int i = 0; i < rData.length; i++) rData[i] = bufferData[i];
			clearBytes(bufferData);
			if((rData[rData.length-4] | rData[rData.length-2]) != 0x00) throw new Exception("ERROR: data bytes can't find end-code ");
			if((rData[rData.length-3] | rData[rData.length-1]) != 0xFF) throw new Exception("ERROR: data bytes can't find end-code ");

			buffData = new byte[rData.length-8];
			for(int i = 0; i < buffData.length; i++) buffData[i] = rData[i + 4];

			dec.join();
			key = dec.bytes2secretKey(dec.getBytes());
			dec.setBytes(buffData);
			dec.start();
			dec.join();
			String connectKEY = bytes2str(dec.getBytes());
			int cNO = Integer.parseInt(connectKEY.substring(0,4));
			int cKEY = Integer.parseInt(connectKEY.substring(5, 8));


			enc = new JEncrypt(cipher.AES, key);
			dec = new JDecrypt(cipher.AES, key);

			createInfoBytes("" + nextConnect(cKEY, cNO), "" + nextConnect++, ctrl.WRITE, type.USER);

			Pattern pattern = Pattern.compile("[\\s\\¥p{Punct}]");
			Matcher matcher = pattern.matcher(getUserName());
			if(matcher.find()) throw new Exception("ERROR: user_name can't use symbol and space");

			sData = "$USER_NAME:";
			sData += getUserName() + ";";
			sData += "$PASSWORD:";

			pattern = Pattern.compile("[\\s\\p{Punct}&&[@_!%]]");
			matcher = pattern.matcher(getPassword());
			if(matcher.find()) throw new Exception("ERROR: password can't use \"$&'()*+,./:;<=>?[\\]^`{|}~ and space");
			pattern = Pattern.compile("^[^ -~｡-ﾟ]+$");
			matcher = pattern.matcher(getPassword());
			if(matcher.find()) throw new Exception("ERROR: password can't use double-byte character");

			sData += getPassword() + ";";
			setPassword(null);
			sData += "$EMAIL:";

			pattern = Pattern.compile(".+@.+\\..++?");
			matcher = pattern.matcher(getEmail());
			if(!matcher.find()) throw new Exception("ERROR: can't use email, value: " + getEmail());

			sData += getEmail() + ";";
			sData += "$BIRTH_DAY:";

			pattern = Pattern.compile("\\d\\{4}\\-\\d\\{2}\\-\\d\\{2}+?");
			matcher = pattern.matcher(getBirthDay());
			if(!matcher.find()) throw new Exception("ERROR: birth_day need input like YYYY-MM-DD, value: " + getBirthDay());

			sData += getBirthDay() + ";";

			createDataBytes(sData);
			sData = null;

			enc.setBytes(getDataBytes());
			enc.start();
			enc.join();

			buildBytes();

			dos.write(getAllBytes());

			check = dis.readBoolean();

			dis.close();
			dos.close();
			setSocket(null);
		} catch (Exception e){
			System.out.println(e);
			e.getStackTrace();
			setSocket(null);
			try {
				dos.close();
				dis.close();
			} catch (Exception e1) {
				System.out.println(e1);
				dos = null;
				dis = null;
			}
		}
	}

}
