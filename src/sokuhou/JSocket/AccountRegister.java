package sokuhou.JSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountRegister extends JSocket{
	private String user_name, password, email, birth_day;
	private String connection_no, nextConnect;

	public AccountRegister(){
		super();
		user_name = null;
		password = null;
		email = null;
		birth_day = null;
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

	public void run(){
		try{
			if(getUserName() == null) throw new Exception("ERROR: user_name value is null");
			if(getEmail() == null) throw new Exception("ERROR: email value is null");
			if(getPassword() == null) throw new Exception ("ERROR: password value is null");
			if(getBirthDay() == null) throw new Exception ("ERROR: birth_day value is null");

			createSocket();
			dos = new DataOutputStream(getSocket().getOutputStream());
			dis = new DataInputStream(getSocket().getInputStream());

			createInfoBytes("0000", "0", ctrl.WRITE, type.USER);
			sData = "$REGISTER";
			createDataBytes(sData);
			buildBytes();
			dos.write(getAllBytes());

			dis.read(getBufferBytes());

			//←取得した接続番号を使って計算する

			createInfoBytes(connection_no, nextConnect, ctrl.WRITE, type.USER);

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

			//←取得した鍵を使って、sDataを暗号化する

			createDataBytes(sData);
			sData = null;
			buildBytes();

			dos.write(getAllBytes());


			dis.close();
			dos.close();
			setSocket(null);
		} catch (Exception e){
			System.out.println(e);
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
