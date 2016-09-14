package sokuhou;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NetGET extends NetWork {
	// URLアドレス先 HTML取得

	// エンコード種類
	final String sjis = "Shift_JIS", utf8 = "UTF-8", jis = "JISAutoDetect";

	// コンストラクタ
	public NetGET(){
	}

	// コンストラクタ
	public NetGET(NetWork nw){super(nw);}

	// 文字列から文字列配列に変換; 入力: 文字列; 出力: 文字列配列
	public List<String> str2strArray(String str){
		List<String> strArray = new ArrayList<String>();
		int a = 0;
		for(int i=1; i < str.length(); i++ ){
			if(str.substring(a,a+1).equals("<")){
				if(str.substring(i,i+1).equals(">")){
					strArray.add(str.substring(a, i+1));
					a=i+1;
				}
			}else{
				if(str.substring(i,i+1).equals("<")){
					strArray.add(str.substring(a,i));
					a=i;
				}
			}
		}
		return strArray;
	}

	// 文字コード確認 UTF-8
	public static boolean isUTF8(byte[] src)
    {
        try {
            byte[] tmp = new String(src, "UTF-8").getBytes("UTF-8");
            return Arrays.equals(tmp, src);
        }
        catch(UnsupportedEncodingException e) {
            return false;
        }
    }

	// 文字コード確認 Shift_JIS
    public static boolean isSJIS(byte[] src)
    {
        try {
            byte[] tmp = new String(src, "Shift_JIS").getBytes("Shift_JIS");
            return Arrays.equals(tmp, src);
        }
        catch(UnsupportedEncodingException e) {
            return false;
        }
    }

	// 実行処理
	public void run(){
		try{
			byte[] src = new byte[1024];
			String encode;
			InputStream is = new URL(super.getURL()).openStream();
			is.read(src);
			if(isSJIS(src))encode = sjis;
			else if(isUTF8(src)) encode = utf8;
			else encode = jis;
			System.out.println(isSJIS(src));
			System.out.println(isUTF8(src));
			BufferedReader open = new BufferedReader(new InputStreamReader(is, encode));
			String str = "";
			String buff = null;
			while((buff = open.readLine()) != null){
				str += buff;
			}
			super.setURL("YEAH!");
			super.setHTML(str2strArray(str));
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
	}
}
