package _0522;

import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/*
 * 프로그램이 시작 될 준비를 하고, 프로그램이 돌아갑니다
 */
public class Pos_main extends Thread {
	Socket socket;
	Pos_controller posControl;
	Scripts scripts;
	
	ConnectionPool cp = null;
	Connection con = null;
	String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
	String user = "cafe_pos";
	String password = "cafe_pos";
	
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
		posControl = new Pos_controller();
		posControl.setScripts(scripts);
		scripts.setPosControl(posControl);
		
		// DB와 연결
		cp = ConnectionPool.getInstance(url, user, password, -1, -1);
		try {
			con = cp.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		posControl.setQueryList(con);
		try {
			con = cp.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run() {
		System.out.println("pos_main start");
		posControl.start();
	}
	
}
