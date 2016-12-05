package sokuhou.window;

import java.awt.Color;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;

public class WinPanel extends Panel{
	public WinPanel(){
		setName("WinPanel");
		setBounds(0, 0, 640, 360);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);

		Label label = new Label("ラベル");
		label.setBounds(100, 100, 40, 20);
		label.setVisible(true);
		TextField text = new TextField();
		text.setBounds(150, 100, 200, 20);
		text.setVisible(true);

		add(label);
		add(text);

		setVisible(false);
	}
}
