package _0522;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import _0522.DTO.IdVO;
/*
 * 서버 오픈 및 프로그램이 시작되기 전 준비작업을 하는 클래스입니다
 */
public class ServerMain {
	static HashSet<IdVO> list = new HashSet<>();
	public static void main(String[] args) {
		ConnectionPool cp = null;
		String url = "jdbc:oracle:thin:@127.0.0.1:1521:xe";
		String user = "cafe_pos";
		String password = "cafe_pos";
		int initCons = 5;
		int maxCons = 10;
		cp = ConnectionPool.getInstance(url, user, password, initCons, maxCons);
		
		
		ServerSocket serversocket;
		Socket socket;
		
		try {
			serversocket = new ServerSocket(10001);
			System.out.println("Server Start");
			while(true) {
			// 유저가 접속하면, 해당 유저 전담의 pos 프로그램의 메인 객체를 생성합니다
			socket = serversocket.accept();
			System.out.println("Client accept");
			Pos_main main = new Pos_main(socket);
			main.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}