package sokuhou;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public abstract class NetWork extends Net{
	// ネットワーク

	// コンストラクタ
	public NetWork(){
		super();
	}

	// コンストラクタ(文字列: URLアドレス)
	public NetWork(String URL){
		super(URL);
	}

	// コンストラクタ(文字列の配列: 各URLアドレス)
	public NetWork(ArrayList<String> listURL){
		super(listURL);
	}

	// 実行処理
	class EchoServer2Thread extends Thread {
	    protected Socket sock;
	    public EchoServer2Thread(Socket s) {
		sock = s;
	    }
	public void run(){
		try {
		    System.out.println("Connected");
		    BufferedReader in = new BufferedReader(
		        new InputStreamReader(sock.getInputStream()));
		    PrintWriter out = new PrintWriter(sock.getOutputStream());
		    String s;
		    while((s = in.readLine()) != null) {
			out.print(s + "\r\n");
			out.flush();
			System.out.println(s);
		    }
		    sock.close();
		    System.out.println("Connection Closed");
		} catch (IOException e) {
		    System.err.println(e);
		}
	    }
	}	

	public class EchoServer2 {
	    static final int port = 10007;
		private ServerSocket socket;
	    
	    public void main(String[] args) {
		Socket conn = null;
		System.out.println("Server Ready");
		while(true) {
		try {
		    socket = new ServerSocket(port);
			conn = socket.accept();
		    EchoServer2Thread t = new EchoServer2Thread(conn);
		    t.start();
		} catch (IOException e) {
		    System.err.println(e);
		}
		}
	    }
	}
	public class NetRead{
		private int sample;
		public void main(String args[]){
			try{
				String adress = "http://www.geocities.jp/inu_poti/java/sample/Hellow.java";
					       URL url=new URL(adress);//URLを設定

		         // URL接続
		        HttpURLConnection connect = (HttpURLConnection)url.openConnection();//サイトに接続
		          connect.setRequestMethod("GET");//プロトコルの設定
		          InputStream in=connect.getInputStream();//ファイルを開く
		          
		          // ネットからデータの読み込み
		          String str;//ネットから読んだデータを保管する変数を宣言
		          str=readString(in);//1行読み取り
		          while (str!=null) {//読み取りが成功していれば
		            System.out.println(str);//コンソールに出力
		            str=readString(in);//次を読み込む
		          }
		      
		          // URL切断
		          in.close();//InputStreamを閉じる
		          connect.disconnect();//サイトの接続を切断

		    }catch(Exception e){
		      //例外処理が発生したら、表示する
		      System.out.println("Err ="+e);
		    }
		  }

		  //InputStreamより１行だけ読む（読めなければnullを返す）
		  String readString(InputStream in){
		    try{
		      int l;//呼んだ長さを記録
		      int a;//読んだ一文字の記録に使う
		      byte b[]=new byte[2048];//呼んだデータを格納
		      a=in.read();//１文字読む
		      if (a<0) return null;//ファイルを読みっていたら、nullを返す
		      l=0;
		      while(a>10){//行の終わりまで読む
		        if (a>=' '){//何かの文字であれば、バイトに追加
		          b[l]=(byte)a;
		          l++;
		        }
		a=in.read();//次を読む
		      }
		      return new String(b,0,l);//文字列に変換
		    }catch(IOException e){
		      //Errが出たら、表示してnull値を返す
		      System.out.println("Err="+e);
		      return null;
		    }
		  }

		public int getSample() {
			return sample;
		}

		public void setSample(int sample) {
			this.sample = sample;
		}
		}
		

		// Javaのサンプルプログラムです。 

		public class Hellow {

		public int java;

		public void main(String args[]){


		//この下から、内容が順次実行されます


		System.out.println("Hellow Java");//Hellow Javaと出力


		System.out.println("こんにちは");//こんにちはと出力


		System.out.println("ニーハオ");//ニーハオと出力

		}
		}
			
		
	}

