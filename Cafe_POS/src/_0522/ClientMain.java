package _0522;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientMain {
	public static void main(String[] args) {
		Socket socket;
		
		try {
			socket = new Socket("127.0.0.1", 10001);
			SendThread send = new SendThread(socket);
			ReceiveThread rec = new ReceiveThread(socket);
			send.start();
			rec.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
}

class SendThread extends Thread{
	BufferedReader br;
	PrintWriter pw;
	Socket socket;
	
	SendThread(Socket socket){
		this.socket = socket;
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		String line = null;
		while(true) {
			try {
				line = br.readLine();
				pw.println(line);
				pw.flush();
				
			} catch (IOException e) {
				System.out.println("입력 오류 발생");
			}
		}

	}
}

class ReceiveThread extends Thread{
	BufferedReader br;
	Socket socket;
	
	ReceiveThread(Socket socket){
		this.socket = socket;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void run() {
		String line = null;
		while(true) {
			try {
				if((line=br.readLine())!=null) {
					System.out.println(line);
				}
			} catch (IOException e) {
				// 서버와 연결이 끊기면 IOException 이 생깁니다
				System.out.println("서버와의 연결 종료");
				// system.exit(0) : 프로그램 종료 명령입니다
				System.exit(0);
				break;
			}
		}
		
	}
	
}