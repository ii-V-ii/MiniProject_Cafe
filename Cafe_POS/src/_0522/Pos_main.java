package _0522;

import java.net.Socket;
/*
 * 프로그램이 시작 될 준비를 하고, 프로그램이 돌아갑니다
 */
public class Pos_main extends Thread {
	Socket socket;
	Pos_controller posControl;
	Scripts scripts;
	private static boolean clientAccess = true;
	
	public static boolean isClientAccess() {
		return clientAccess;
	}

	public static void setClientAccess(boolean clientAccess) {
		Pos_main.clientAccess = clientAccess;
	}

	public Pos_main(Socket socket) {
		this.socket = socket;
		scripts = new Scripts(socket);
		posControl = Pos_controller.getPosInstance();
		posControl.setScripts(scripts);
	}
	
	public void run() {
		System.out.println("pos_main start");
		posControl.start();
	}
	
}
