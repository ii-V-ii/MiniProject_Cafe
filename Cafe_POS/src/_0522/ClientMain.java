package _0522;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
	public static void main(String[] args) {
		Socket socket;
		BufferedReader br;
		PrintWriter pw;
		
		
	}
}

class receiveThread extends Thread{
	Socket socket;
	
	receiveThread(Socket socket){
		this.socket = socket;
	}
	
	public void run() {
		
	}
	
}