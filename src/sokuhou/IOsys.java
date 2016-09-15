package sokuhou;

import javax.swing.JFrame;

//Input / Output (入出力)
public class IOsys extends IO{

	private JFrame jFrame;

	public IOsys(){
		jFrame = new JFrame();
		jFrame.setTitle("TEST_テスト");
		jFrame.setBounds(100, 100, 640, 360);
		jFrame.setVisible(true);
	}

}

