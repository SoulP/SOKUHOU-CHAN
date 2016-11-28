package sokuhou.Window;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import sokuhou.JSocket.JSocket;
import sokuhou.JSocket.Send;

// QRコード表示
public class QRcodeViewer extends Frame implements WindowListener, ActionListener, MouseListener{

	// インスタンス変数
	Panel panel;
	QRimage image;
	Label label;
	TextField text;
	Button button;
	final String textInputCode = "認証コード入力して下さい。";

	// コンストラクタ
	public QRcodeViewer(){

		// メインパネル
		panel = new Panel();// オブジェクト生成
		panel.setLocation(0, 0);// 0, 0の座標位置に設定
		panel.setLayout(null);// レイアウト無効化
		panel.setVisible(true);// 可視化

		// 画像用のパネル
		image = new QRimage();// オブジェクト生成
		image.setVisible(true);// 可視化

		// ラベル
		label = new Label("認証コード: ");// オブジェクト生成
		label.setVisible(true);// 可視化

		// テキストフィールド
		text = new TextField();// オブジェクト生成
		text.addMouseListener(this);// マウスリスナー追加
		text.setVisible(true);// 可視化

		// ボタン
		button = new Button("確認");// オブジェクト生成
		button.addActionListener(this);// アクションリスナー追加
		button.setVisible(true);// 可視化

		// 各コンポネントをメインパネルに追加
		panel.add(image);
		panel.add(label);
		panel.add(text);
		panel.add(button);

		// メインフレーム
		setTitle("2要素認証(ワンタイムパスワード) - QRコード");// タイトル設定
		setSize(400, 500);// サイズ設定
		setPreferredSize(new Dimension(400, 500));// サイズ設定
		setLocationRelativeTo(null);// デスクトップからの座標初期化(フレームを中央に表示)
		setResizable(false);// フレームサイズ変更不可
		setVisible(false);// 不可視化
		addWindowListener(this);// ウィンドウリスナー追加
		add(panel);// メインパネルをメインフレームに追加

		// 各コンポネントのサイズと座標位置を調整
		panel.setSize(getSize());

		image.setSize((int)(panel.getWidth() * 0.9), (int)(panel.getWidth() * 0.9));
		image.setLocation((int)(panel.getWidth() * 0.5) - (int)(image.getWidth() * 0.5) - 4, (int)(panel.getWidth() * 0.05));

		label.setLocation(image.getX(), image.getY() + image.getHeight() + 10);
		label.setSize(80, 20);
		text.setLocation(label.getX() + label.getWidth() + 10, image.getY() + image.getHeight() + 10);
		text.setSize(image.getX() + image.getWidth() - text.getX(), 20);
		button.setSize(50, 30);
		button.setLocation((int)(panel.getSize().width * 0.5) - (int)(button.getSize().width * 0.5), text.getLocation().y + text.getSize().height + 10);

		// フレーム全体調整
		pack();

		// 初期値設定
		text.setText(textInputCode);
	}

	// 画像 入力: 文字列 (URLアドレス)
	public synchronized void setImage(String url){
		try{
			setImage(ImageIO.read(new URL(url).openStream()));
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}

	// 画像 入力: イメージ
	public synchronized void setImage(BufferedImage image){
		this.image.setImage(image);
	}

	// 画像 出力: イメージ
	public synchronized Image getImage(){
		return image.getImage();
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		setVisible(false);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// ボタンのイベント
		if(e.getSource() == button){
			if(!text.getText().isEmpty()){// テキストフィールド確認
				final Pattern pattern = Pattern.compile("^\\d{6}\\d*$");// パターン設定
				Matcher matcher = pattern.matcher(text.getText());// パターン設定
				if(matcher.find()){// 文字列に問題ない場合
					// 送信
					Send send = (Send)sokuhou.MainSYS.socket;
					send.setSend("$OTP:" + text.getText() + ";");
					send.setDataType(JSocket.type.USER);
					send.start();
					try {
						send.join();
						if(send.check()) setVisible(false);// 送信成功した場合
						else text.setText("認証確認失敗、正しい認証コード入力して下さい。");// 送信失敗した場合
					} catch (InterruptedException e1) {
						System.out.println(e1);// エラー内容表示
						e1.printStackTrace();// 原因追跡表示
					}
				}else{// 文字列に問題ある場合
					text.setText(textInputCode);
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// テキストフィールド
		if(e.getSource() == text) text.setText("");// テキストフィールド内にクリックした時に文字列をクリアにする
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}


}

// 画像用のパネル
class QRimage extends Panel{
	// インスタンス変数
	BufferedImage image;// 画像

	// コンストラクタ
	QRimage(){
		image = null;
	}

	// コンストラクタ
	QRimage(BufferedImage image){
		this.image = image;
	}

	// 画像 入力: イメージ
	public synchronized void setImage(BufferedImage image){
		this.image = image;
	}

	// 画像 出力: イメージ
	public synchronized Image getImage(){
		return image;
	}

	// 描画
	@Override
	public void paint(Graphics g){
		if(image != null) g.drawImage(image, 0, 0, getWidth(), getHeight(), null);// 画像がある場合、画像をパネルに描画する
	}

}
