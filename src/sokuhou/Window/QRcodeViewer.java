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
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

public class QRcodeViewer extends Frame implements WindowListener, ActionListener{

	Panel panel;
	QRimage image;
	Label label;
	TextField text;
	Button button;

	public QRcodeViewer(){

		panel = new Panel();
		panel.setLocation(0,  0);
		panel.setLayout(null);
		panel.setVisible(true);

		image = new QRimage();
		image.setVisible(true);

		label = new Label("6桁のコード: ");
		label.setVisible(true);

		text = new TextField();
		text.setVisible(true);

		button = new Button("確認");
		button.addActionListener(this);
		button.setVisible(true);

		panel.add(image);
		panel.add(label);
		panel.add(text);
		panel.add(button);

		setTitle("2要素認証(ワンタイムパスワード) - QRコード");
		setSize(400, 500);
		setPreferredSize(new Dimension(400, 500));
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(false);
		addWindowListener(this);

		add(panel);

		panel.setSize(getSize());

		image.setSize((int)(panel.getWidth() * 0.9), (int)(panel.getWidth() * 0.9));
		image.setLocation((int)(panel.getWidth() * 0.5) - (int)(image.getWidth() * 0.5) - 4, (int)(panel.getWidth() * 0.05));

		label.setLocation(image.getX(), image.getY() + image.getHeight() + 10);
		label.setSize(80, 20);
		text.setLocation(label.getX() + label.getWidth() + 10, image.getY() + image.getHeight() + 10);
		text.setSize(image.getX() + image.getWidth() - text.getX(), 20);
		button.setSize(50, 30);
		button.setLocation((int)(panel.getSize().width * 0.5) - (int)(button.getSize().width * 0.5), text.getLocation().y + text.getSize().height + 10);

		pack();
	}

	public synchronized void setImage(String url){
		try{
			setImage(ImageIO.read(new URL(url).openStream()));
		}catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public synchronized void setImage(BufferedImage image){
		this.image.setImage(image);
	}

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
		// TODO 自動生成されたメソッド・スタブ

	}


}

class QRimage extends Panel{
	BufferedImage image;

	QRimage(){
		image = null;
	}

	QRimage(BufferedImage image){
		this.image = image;
	}

	public synchronized void setImage(BufferedImage image){
		this.image = image;
	}

	public synchronized Image getImage(){
		return image;
	}

	@Override
	public void paint(Graphics g){
		if(image != null) g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
	}
}
