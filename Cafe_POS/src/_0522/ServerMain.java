package _0522;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
	public static void main(String[] args) {
		ServerSocket serversocket;
		Socket socket;
		
		try {
			serversocket = new ServerSocket(10001);
			socket = serversocket.accept();
			while(true) {
				Pos_main main = new Pos_main();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}