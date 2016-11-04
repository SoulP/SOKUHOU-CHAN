package sokuhou;

public class JSocket extends Thread{
	final int port;
	final String host;
	enum ctrl { NULL, GET, ADD, DELETE, UPDATE };
	enum type { USER, DATA, OPTION };
	private ctrl mode;
	String data;

	public JSocket(){
		port = 55324;
		host = "sokuhou.soulp.moe";
		mode = ctrl.NULL;
		data = null;
	}

	public void changeMode(ctrl mode){
		this.mode = mode;
	}

	public ctrl getMode(){
		return mode;
	}

	public void GET(type t, String name){
		data = ctrl.GET + "/" + t + "/" + name;
	}

	public void run(){
		switch(mode){
			case GET: break;
			case ADD: break;
			case DELETE: break;
			case UPDATE: break;
			default: break;
		}
	}
}
