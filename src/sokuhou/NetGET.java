package sokuhou;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetGET extends NetWork {
	// URLアドレス先 HTML取得

	// エンコード種類
	final String sjis = "Shift_JIS", utf8 = "UTF-8", jis = "JISAutoDetect";

	// コンストラクタ
	public NetGET(){ super(); }

	// コンストラクタ
	public NetGET(NetWork nw){ super(nw); }

	// 文字列から文字列配列に変換; 入力: 文字列; 出力: 文字列配列
	public List<String> str2strArray(String str)
	{
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
	public boolean isUTF8(byte[] src)
    {
        try {
            byte[] tmp = new String(src, "UTF8").getBytes("UTF8");
            return Arrays.equals(tmp, src);
        }
        catch(UnsupportedEncodingException e) {
            return false;
        }
    }

	// 文字コード確認 Shift_JIS
    public boolean isSJIS(byte[] src)
    {
        try {
            byte[] tmp = new String(src, "Shift_JIS").getBytes("Shift_JIS");
            return Arrays.equals(tmp, src);
        }
        catch(UnsupportedEncodingException e) {
            return false;
        }
    }

    // HTML文のタグの中の値を出力; 入力: HTML文, タグ **<>は不要**; 出力: タグの中にある値
    public String html2string(List<String> html, String str)
    {
    	Pattern p = Pattern.compile("(?i)<*" + str + "*>");
    	for(String Str : html){
    		if(p.matcher(Str).matches()){
    			return html.get(html.indexOf(Str)+1);
    		}
    	}
    	return "";
    }

    /*
     * HTML文の検索一致したmetaタグの中にあるcontentを出力;
     * 入力: HTML文, propertyもしくはname, true = property false = name;
     * 出力: content
     */
    private String meta2string(List<String> html, String str, boolean pn){
    	Pattern p = Pattern.compile("(?i)" +"<meta.*" + ((pn)?"property":"name") + "=\"" + str + "\".*>");
    	for(String Str : html){
    		if(p.matcher(Str).find()){
    			return getContentFromMeta(Str);
    		}
    	}
    	return "";
    }

    // metaタグの中にあるcotentを出力; 入力: meta; 出力: content
    private String getContentFromMeta(String str){
    	try{
    		Matcher m = Pattern.compile("(?i)content=\".+?\"").matcher(str);
    		String temp = "";
    		while(m.find())temp = m.group();
    		temp = temp.substring(0, temp.length() - 1).substring("content=\"".length());
    		return temp;
    	}catch (Exception e){
    		System.out.println(e);
    		return "";
    	}
    }

    // 接続先からHTML読込、HTML文出力
    private List<String> readHTML(){
    	InputStream is = null;
    	InputStreamReader isr = null;
    	try{
    		byte[] src = new byte[Byte.MAX_VALUE-1];
			String encode;
			is = new URL(getURL()).openStream();
			is.read(src);
			if(isUTF8(src))encode = utf8;
			else if(isSJIS(src)) encode = sjis;
			else encode = jis;
			isr = new InputStreamReader(new URL(getURL()).openStream(), encode);
			BufferedReader open = new BufferedReader(isr);
			String str = "";
			String buff = null;
			while((buff = open.readLine()) != null){
				str += buff;
			}
			is.close();
			isr.close();
			return str2strArray(str);
    	}catch (Exception e){
    		System.out.println(e);
    		try{
    			is.close();
    			isr.close();
    		}catch (Exception ex){
    			System.out.println(ex);
    		}
    		return null;
    	}
    }

	// タイトル入手
    private void setTitle(){
    	String temp = html2string(getHTML(), "title");
		temp = (temp.equals(""))? meta2string(getHTML(), "og:title", true) : temp;
		temp = (temp.equals(""))? meta2string(getHTML(), "title", false) : temp;
		setTitle(temp);
    }

	// 実行処理
	public void run()
	{
		try{
			setHTML(readHTML());
			setTitle();
		}catch(Exception e){
			System.out.println(e);
			System.exit(-1);
		}
	}
}
