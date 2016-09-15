package sokuhou;

import javax.swing.JFrame;

public class Oframe extends IOsys{
	private JFrame frame;

	//コンストラクタ
	public Oframe(){
		super();
		frame = new JFrame();
	}

	// コンストラクタ(JFrame: フレーム)
	public Oframe(JFrame frame){
		super();
		this.frame = frame;
	}

	// 入力: フレーム
	public void setFrame(JFrame frame){
		this.frame = frame;
	}

	// 出力: フレーム
	public JFrame getFrame(){
		return frame;
	}
}
