package _0522;

import java.net.Socket;
/*
 * 프로그램이 시작 될 준비를 하고, 프로그램이 돌아갑니다
 */
public class Pos_main extends Thread {
	Socket socket;
	Pos_controller posControl;
	Scripts scripts;
	public Pos_main(Socket socket) {
		this.socket = socket;
		scripts = new Scripts(socket);
		posControl = Pos_controller.getPosInstance();
		posControl.setScripts(scripts);
	}
	
	public void run() {
		posControl.start();
	}
	
}
